package com.nuist.exc.exc6;

import java.io.File;
import java.io.Serializable;

public class FileIndexNode implements Serializable, Comparable<FileIndexNode> {
    private static final long serialVersionUID = 1L;

    private String fileName;
    private String filePath;
    private long fileSize;
    private long lastModified;
    private boolean isDirectory;

    private FileIndexNode left;
    private FileIndexNode right;

    public FileIndexNode(File file) {
        this.fileName = file.getName();
        this.filePath = file.getAbsolutePath();
        this.fileSize = file.length();
        this.lastModified = file.lastModified();
        this.isDirectory = file.isDirectory();
    }

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
    public void setLeft(FileIndexNode left) { this.left = left; }
    public void setRight(FileIndexNode right) { this.right = right; }

    @Override
    public String toString() {
        String type = isDirectory ? "[目录]" : "[文件]";
        return type + " " + fileName + " (" + formatSize(fileSize) + ") -> " + filePath;
    }

    public static String formatSize(long size) {
        if (size < 1024) return size + " B";
        if (size < 1024 * 1024) return String.format("%.1f KB", size / 1024.0);
        if (size < 1024 * 1024 * 1024) return String.format("%.1f MB", size / (1024.0 * 1024));
        return String.format("%.1f GB", size / (1024.0 * 1024 * 1024));
    }
}
