package com.nuist.exc.exc6;

import lombok.Data;

import java.io.*;
import java.util.*;

/**
 * 文件索引树，使用二叉搜索树（BST）对指定目录下的所有文件和子目录建立索引。
 * <p>
 * 索引按文件名（忽略大小写）排序，支持按文件名关键字或文件大小范围搜索，
 * 搜索结果支持批量删除。索引可通过序列化持久化到磁盘文件，以便后续复用。
 * </p>
 */
@Data
public class FileIndexTree implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 二叉搜索树的根节点 */
    private FileIndexNode root;
    /** 树中节点总数 */
    private int nodeCount;

    /**
     * 从指定目录构建文件索引（二叉树）
     * 目录遍历保留递归（深度 ≈ 目录嵌套层数，通常很小）
     * 节点插入使用迭代（避免BST深度大时栈溢出）
     *
     * @param dir 待索引的根目录
     */
    public void buildIndex(File dir) {
        if (!dir.exists() || !dir.isDirectory()) {
            System.out.println("目录不存在或不是一个目录: " + dir.getAbsolutePath());
            return;
        }
        nodeCount = 0;
        root = null;
        traverseAndInsert(dir);
        System.out.println("索引构建完成，共索引 " + nodeCount + " 个文件/目录");
    }

    /**
     * 递归遍历目录树，每遇到一个文件/目录就迭代插入BST
     *
     * @param dir 当前遍历的目录
     */
    private void traverseAndInsert(File dir) {
        File[] files = dir.listFiles();
        if (files == null) return;

        for (File file : files) {
            FileIndexNode node = new FileIndexNode(file);
            insertNode(node);
            nodeCount++;

            if (file.isDirectory()) {
                traverseAndInsert(file);
            }
        }
    }

    /**
     * 迭代式插入，避免递归导致栈溢出
     * <p>
     * 按文件名比较结果决定插入方向：更小则走左子树，更大则走右子树。
     * 文件名相同时依据文件路径区分：若路径不同则插入右子树，路径完全相同则视为重复节点跳过。
     * </p>
     *
     * @param node 待插入的节点
     */
    private void insertNode(FileIndexNode node) {
        if (root == null) {
            root = node;
            return;
        }

        FileIndexNode current = root;
        // 沿BST向下查找插入位置，直到找到空位
        while (true) {
            int cmp = node.compareTo(current);
            if (cmp < 0) {
                if (current.getLeft() == null) {
                    current.setLeft(node);
                    return;
                }
                current = current.getLeft();
            } else if (cmp > 0) {
                if (current.getRight() == null) {
                    current.setRight(node);
                    return;
                }
                current = current.getRight();
            } else {
                // 文件名相同，按路径区分
                if (!node.getFilePath().equals(current.getFilePath())) {
                    if (current.getRight() == null) {
                        current.setRight(node);
                        return;
                    }
                    current = current.getRight();
                } else {
                    // 完全重复的节点（相同路径相同文件），跳过
                    return;
                }
            }
        }
    }

    /**
     * 按文件名搜索（包含关键字）— 迭代式前序遍历
     * 文件名以列表的形式返回
     *
     * @param keyword 搜索关键字（不区分大小写），匹配文件名中包含该关键字的节点
     * @return 包含关键字的所有文件/目录节点列表
     */
    public List<FileIndexNode> searchByName(String keyword) {
        List<FileIndexNode> results = new ArrayList<>();
        String lowerKeyword = keyword.toLowerCase();

        // 迭代式中序遍历整棵BST，匹配文件名包含关键字的节点
        Deque<FileIndexNode> stack = new ArrayDeque<>();
        FileIndexNode current = root;
        while (current != null || !stack.isEmpty()) {
            while (current != null) {
                stack.push(current);
                current = current.getLeft();
            }
            current = stack.pop();
            if (current.getFileName().toLowerCase().contains(lowerKeyword)) {
                results.add(current);
            }
            current = current.getRight();
        }
        return results;
    }

    /**
     * 按文件大小范围搜索（单位：字节）— 迭代式前序遍历
     *
     * @param minSize 文件大小下限（含），单位字节
     * @param maxSize 文件大小上限（含），单位字节
     * @return 大小在 [minSize, maxSize] 范围内的所有文件节点列表（不含目录）
     */
    public List<FileIndexNode> searchBySize(long minSize, long maxSize) {
        List<FileIndexNode> results = new ArrayList<>();

        // 迭代式中序遍历整棵BST，筛选大小在指定范围内的文件
        Deque<FileIndexNode> stack = new ArrayDeque<>();
        FileIndexNode current = root;
        while (current != null || !stack.isEmpty()) {
            while (current != null) {
                stack.push(current);
                current = current.getLeft();
            }
            current = stack.pop();
            long size = current.getFileSize();
            if (size >= minSize && size <= maxSize && !current.isDirectory()) {
                results.add(current);
            }
            current = current.getRight();
        }
        return results;
    }

    /**
     * 删除搜索结果中的文件
     *
     * @param files 待删除的文件节点列表（仅删除实际存在的文件）
     */
    public void deleteFiles(List<FileIndexNode> files) {
        int successCount = 0;
        int failCount = 0;
        for (FileIndexNode node : files) {
            File file = new File(node.getFilePath());
            if (file.exists()) {
                if (file.delete()) {
                    successCount++;
                } else {
                    failCount++;
                }
            }
        }
        System.out.println("删除完成：成功 " + successCount + " 个，失败 " + failCount + " 个");
    }

    /**
     * 持久化：保存索引到文件
     *
     * @param path 目标文件路径
     * @throws IOException 若文件写入失败则抛出
     */
    public void saveIndex(String path) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path))) {
            oos.writeObject(this);
        }
        System.out.println("索引已保存到: " + path);
    }
    /**
     * 持久化：从文件加载索引
     *
     * @param path 索引文件路径
     * @return 反序列化后的文件索引树对象
     * @throws IOException            若文件读取失败则抛出
     * @throws ClassNotFoundException 若序列化版本不匹配则抛出
     */
    public static FileIndexTree loadIndex(String path) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path))) {
            FileIndexTree tree = (FileIndexTree) ois.readObject();
            System.out.println("索引已从 " + path + " 加载，共 " + tree.nodeCount + " 个节点");
            return tree;
        }
    }

}
