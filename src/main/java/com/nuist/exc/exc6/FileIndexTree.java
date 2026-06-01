package com.nuist.exc.exc6;

import java.io.*;
import java.util.*;

public class FileIndexTree implements Serializable {
    private static final long serialVersionUID = 1L;

    private FileIndexNode root;
    private int nodeCount;

    /**
     * 从指定目录构建文件索引（二叉树）
     * 目录遍历保留递归（深度 ≈ 目录嵌套层数，通常很小）
     * 节点插入使用迭代（避免BST深度大时栈溢出）
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
     */
    private void insertNode(FileIndexNode node) {
        if (root == null) {
            root = node;
            return;
        }

        FileIndexNode current = root;
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
     */
    public List<FileIndexNode> searchByName(String keyword) {
        List<FileIndexNode> results = new ArrayList<>();
        String lowerKeyword = keyword.toLowerCase();

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
     */
    public List<FileIndexNode> searchBySize(long minSize, long maxSize) {
        List<FileIndexNode> results = new ArrayList<>();

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
     */
    public void saveIndex(String path) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path))) {
            oos.writeObject(this);
        }
        System.out.println("索引已保存到: " + path);
    }

    /**
     * 持久化：从文件加载索引
     */
    public static FileIndexTree loadIndex(String path) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path))) {
            FileIndexTree tree = (FileIndexTree) ois.readObject();
            System.out.println("索引已从 " + path + " 加载，共 " + tree.nodeCount + " 个节点");
            return tree;
        }
    }

    public int getNodeCount() { return nodeCount; }
}
