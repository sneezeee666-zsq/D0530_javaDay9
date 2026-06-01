package com.nuist.exc.exc4;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

//4.文件复制 要求支持复制目录 复制的时候要复制目录和所有子文件和子目录下的文件
//拆开做 先做文件(非文本)复制
//public static void copyFile(File sourceFile,File pasteFile){}
public class Test {
    public static void main(String[] args) {
        copyDir(new File("D:\\software\\idea_workspace\\D0530_javaDay9"), new File("C:\\Users\\asus\\Desktop\\1"));
    }

    //文件复制
    public static void copyFile(File sourceFile, File pasteFile) {
        try (//创建输入流
             FileInputStream fis = new FileInputStream(sourceFile);
             //创建输出流
             FileOutputStream fos = new FileOutputStream(pasteFile)) {
            //创建缓冲区
            byte[] buffer = new byte[1024];
            int len = fis.read(buffer);
            //将下载数据从输入流中 读取到buffer缓存中
            //每次读取的时候，都会返回读取的字节数的值
            //当输入流读完的时候 则返回-1 依次作为循环读取是否继续的判断条件
            System.out.println("开始复制文件"+sourceFile.getName());
            System.out.print("复制进度：");
            int count = 0;
            while (len != -1) {
                fos.write(buffer, 0, len);
                len = fis.read(buffer);
                if (count % 50 == 0) {
                    System.out.print("#");
                }
            }
            System.out.println( "");
            System.out.println("复制完成");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //目录复制
    public static void copyDir(File sourceDir, File pasteDir) {
        try {
            if(sourceDir==pasteDir){
                System.out.println("源目录和粘贴目录不能相同");
            }
            if(!sourceDir.exists()){
                System.out.println("源目录不存在");
            }
            if (!pasteDir.exists()) {
                pasteDir.mkdirs();
            }
            File[] files = sourceDir.listFiles();
            for (File file : files) {
                if (file.isFile()) {
                    copyFile(file, new File(pasteDir, file.getName()));
                } else {
                    copyDir(file, new File(pasteDir, file.getName()));
                }
            }
            System.out.println("复制完成");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
