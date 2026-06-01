package com.nuist.exc.exc6;

import lombok.Data;

import java.io.File;
import java.io.Serializable;

/**
 * 二叉树索引节点，封装文件/目录信息。
 * 作为二叉搜索树的节点，按文件名（忽略大小写）排序，
 * 支持序列化以实现索引持久化。
 */
@Data
public class FileIndexNode implements Serializable, Comparable<FileIndexNode> {
    private static final long serialVersionUID = 1L;
    //设置唯一序列号

    /** 文件名（不含路径） */
    private String fileName;
    /** 文件绝对路径 */
    private String filePath;
    /** 文件大小（字节） */
    private long fileSize;
    /** 最后修改时间戳 */
    private long lastModified;
    /** 是否为目录 */
    private boolean isDirectory;

    /** 左子节点（文件名更小） */
    private FileIndexNode left;
    /** 右子节点（文件名更大或相同） */
    private FileIndexNode right;



    //节点处存储的是文件的信息
    //名称，路径，大小，最后修改时间，是否是目录
    /**
     * 根据 File 对象构造索引节点，提取文件元信息。
     *
     * @param file 待索引的文件或目录对象
     */
    public FileIndexNode(File file) {
        this.fileName = file.getName();
        this.filePath = file.getAbsolutePath();
        this.fileSize = file.length();
        this.lastModified = file.lastModified();
        this.isDirectory = file.isDirectory();
    }

    //自己设置比较器方法
    // 比较两个节点的文件名
    /**
     * 按文件名（忽略大小写）比较两个节点，用于二叉搜索树排序。
     *
     * @param other 待比较的另一个节点
     * @return 负值、零或正值（当前文件名小于、等于或大于参数字符串）
     */
    @Override
    public int compareTo(FileIndexNode other) {
        return this.fileName.compareToIgnoreCase(other.fileName);
    }

    public String getFileName() { return fileName; }
    public String getFilePath() { return filePath; }
    public long getFileSize() { return fileSize; }
    public long getLastModified() { return lastModified; }
    public boolean isDirectory() { return isDirectory; }
    public FileIndexNode getLeft() { return left; }
    public FileIndexNode getRight() { return right; }

    /**
     * 返回格式化的节点信息字符串，包含类型标识、文件名、大小和路径。
     *
     * @return 例如 "[文件] readme.txt (1.2 KB) -> D:/path/readme.txt"
     */
    @Override
    public String toString() {
        String type = isDirectory ? "[目录]" : "[文件]";
        return type + " " + fileName + " (" + formatSize(fileSize) + ") -> " + filePath;
    }

    /**
     * 将字节数格式化为可读的大小字符串（B / KB / MB / GB）。
     *
     * @param size 文件大小（字节）
     * @return 格式化后的大小字符串，例如 "1.5 MB"
     */
    public static String formatSize(long size) {
        if (size < 1024) return size + " B";
        if (size < 1024 * 1024) return String.format("%.1f KB", size / 1024.0);
        if (size < 1024 * 1024 * 1024) return String.format("%.1f MB", size / (1024.0 * 1024));
        return String.format("%.1f GB", size / (1024.0 * 1024 * 1024));
    }
}
