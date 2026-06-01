package com.nuist.exc.exc6;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

//6.拓展  实现文件搜索功能 可以根据文件的大小或者包含名称进行搜索
//(在java中创建合适的数据结构 （二叉树） 来作为文件的索引 可以考虑做索引持久化)
//将搜索记录和搜索结果存入到 search.log文本中
//并且支持搜索之后是否删除功能？
public class Test {
    private static final String INDEX_FILE = "D:\\software\\idea_workspace\\D0530_javaDay9\\index.dat";
    private static final String LOG_FILE = "D:\\software\\idea_workspace\\D0530_javaDay9\\search.log";
    private static FileIndexTree indexTree;
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("===== 文件搜索系统 =====");
        System.out.println("======================");
        System.out.println("欢迎使用文件搜索系统");
        System.out.println("======================");
        System.out.println("提示：首次使用请先构建索引 (选项1)");

        while (true) {
            System.out.println("""
                    请选择功能：
                    1. 构建/刷新文件索引
                    2. 按文件名搜索
                    3. 按文件大小搜索
                    4. 保存索引到文件
                    5. 从文件加载索引
                    6. 查看搜索日志
                    7. 清空搜索日志
                    8. 生成测试用目录
                    0. 退出系统
                    """);

            System.out.print("请输入选项: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> buildIndex();
                case "2" -> searchByName();
                case "3" -> searchBySize();
                case "4" -> saveIndex();
                case "5" -> loadIndex();
                case "6" -> viewLog();
                case "7" -> deleteLog();
                case "8" -> buildTestDirs();

                case "0" -> {
                    System.out.println("感谢使用文件搜索系统！");
                    return;
                }
                default -> System.out.println("无效选项，请重新输入");
            }
        }
    }

    private static void buildTestDirs() {
        System.out.println("请输入测试用目录的生成目录：");
        String dirPath = scanner.nextLine().trim();
        File dir = new File(dirPath);
        if (!dir.isDirectory()||!dir.exists()) {
            System.out.println("目录不存在或不是一个目录: " + dir.getAbsolutePath());
            return;
        }
        System.out.println("正在生成测试用目录...");
        for (int i = 0; i < 3; i++){
            File subDir = new File(dir, "testDir" + i);
            subDir.mkdirs();
        }
        File[] dirs = dir.listFiles(File::isDirectory);
        for (File f : dirs) {
            for (int j = 0; j < 5; j++) {
                File subDir = new File(f, "subDir" + j);
                subDir.mkdirs();
                for (int k = 0; k < 2; k++) {
                    File file = new File(subDir, "file" + k + ".txt");
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        System.out.println("无法创建文件: " + file.getAbsolutePath());
                    }
                }
            }
        }
        System.out.println("测试用目录已生成");
    }

    private static void deleteLog() {
        System.out.println("正在清空搜索日志...");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE))) {
            writer.write("");
        } catch (IOException e) {
            System.out.println("无法清空搜索日志");
            e.printStackTrace();
            return;
        }
        System.out.println("搜索日志已清空");
    }

    private static void buildIndex() {
        System.out.print("请输入要索引的目录路径: ");
        String dirPath = scanner.nextLine().trim();
        if (dirPath.isEmpty()) {
            System.out.println("目录路径不能为空");
            return;
        }
        File dir = new File(dirPath);
        indexTree = new FileIndexTree();
        indexTree.buildIndex(dir);
    }

    private static void searchByName() {
        if (!ensureIndexLoaded()) return;

        System.out.print("请输入要搜索的文件名关键字: ");
        String keyword = scanner.nextLine().trim();
        if (keyword.isEmpty()) {
            System.out.println("关键字不能为空");
            return;
        }

        System.out.println("正在搜索文件名包含 \"" + keyword + "\" 的文件...");
        List<FileIndexNode> results = indexTree.searchByName(keyword);

        displayResults(results);

        // 记录搜索日志
        logSearch("按文件名搜索", "关键字: " + keyword, results);

        // 询问是否删除
        promptDelete(results);
    }

    private static void searchBySize() {
        if (!ensureIndexLoaded()) return;

        try {
            System.out.print("请输入最小文件大小（字节，直接回车默认为0）: ");
            String minInput = scanner.nextLine().trim();
            long minSize = minInput.isEmpty() ? 0 : Long.parseLong(minInput);

            System.out.print("请输入最大文件大小（字节，直接回车默认为Long.MAX_VALUE）: ");
            String maxInput = scanner.nextLine().trim();
            long maxSize = maxInput.isEmpty() ? Long.MAX_VALUE : Long.parseLong(maxInput);

            if (minSize > maxSize) {
                System.out.println("最小大小不能大于最大大小");
                return;
            }

            System.out.println("正在搜索大小在 " + FileIndexNode.formatSize(minSize)
                    + " ~ " + FileIndexNode.formatSize(maxSize) + " 之间的文件...");
            List<FileIndexNode> results = indexTree.searchBySize(minSize, maxSize);

            displayResults(results);

            // 记录搜索日志
            logSearch("按文件大小搜索",
                    "大小范围: " + FileIndexNode.formatSize(minSize) + " ~ " + FileIndexNode.formatSize(maxSize),
                    results);

            // 询问是否删除
            promptDelete(results);

        } catch (NumberFormatException e) {
            System.out.println("请输入有效的数字");
        }
    }

    private static void displayResults(List<FileIndexNode> results) {
        if (results.isEmpty()) {
            System.out.println("未找到匹配的文件");
            return;
        }

        System.out.println("找到 " + results.size() + " 个匹配结果：");
        System.out.println("------------------------------------------------------");
        for (int i = 0; i < results.size(); i++) {
            System.out.printf("%d. %s%n", i + 1, results.get(i));
        }
        System.out.println("------------------------------------------------------");
    }

    private static void promptDelete(List<FileIndexNode> results) {
        if (results.isEmpty()) return;

        System.out.print("是否删除以上找到的文件？(y/n): ");
        String choice = scanner.nextLine().trim().toLowerCase();
        if ("y".equals(choice) || "yes".equals(choice)) {
            System.out.println("!!!WARNING!!!再次确认:是否删除以上找到的文件？");
            System.out.print("请再次输入y/yes以确认删除: ");
            String choice2 = scanner.nextLine().trim().toLowerCase();
            if("y".equals(choice2) || "yes".equals(choice2)){
                System.out.println("正在删除文件...");
                indexTree.deleteFiles(results);
                // 追加删除记录到日志
                appendDeleteToLog(results.size());
            }else {
                System.out.println("取消删除");
            }
        }
    }

    private static void saveIndex() {
        if (!ensureIndexLoaded()) return;
        try {
            indexTree.saveIndex(INDEX_FILE);
        } catch (IOException e) {
            System.out.println("保存索引失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void loadIndex() {
        try {
            indexTree = FileIndexTree.loadIndex(INDEX_FILE);
        } catch (FileNotFoundException e) {
            System.out.println("索引文件不存在，请先构建索引并保存");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("加载索引失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void viewLog() {
        File logFile = new File(LOG_FILE);
        if (!logFile.exists()) {
            System.out.println("日志文件不存在");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(logFile))) {
            String line;
            System.out.println("========== 搜索日志 ==========");
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
            System.out.println("=============================");
        } catch (IOException e) {
            System.out.println("读取日志失败: " + e.getMessage());
        }
    }

    /**
     * 记录搜索日志到 search.log
     */
    private static void logSearch(String searchType, String criteria, List<FileIndexNode> results) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(LOG_FILE, true), true)) {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            pw.println("========================================");
            pw.println("时间: " + timestamp);
            pw.println("搜索类型: " + searchType);
            pw.println("搜索条件: " + criteria);
            pw.println("结果数量: " + results.size());
            pw.println("搜索结果:");
            for (FileIndexNode node : results) {
                pw.println("  " + node);
            }
            pw.println("========================================");
            pw.println();
        } catch (IOException e) {
            System.out.println("写入日志失败: " + e.getMessage());
        }
    }

    /**
     * 追加删除记录到日志
     */
    private static void appendDeleteToLog(int fileCount) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(LOG_FILE, true), true)) {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            pw.println("【删除操作】时间: " + timestamp + "，删除了 " + fileCount + " 个文件");
            pw.println();
        } catch (IOException e) {
            System.out.println("写入删除日志失败: " + e.getMessage());
        }
    }

    /**
     * 检查索引是否已加载/构建
     */
    private static boolean ensureIndexLoaded() {
        if (indexTree == null) {
            System.out.println("索引未构建，请先构建索引 (选项1)");
            return false;
        }
        return true;
    }
}
