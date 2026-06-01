package com.nuist.exc.exc1;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

//1.在课堂练习的基础上,统计指定目录下的所有子文件和子目录 找到
//最大文件和最小文件对应的文件路径和字节大小
public class Test {
    public static void main(String[] args) {
        File targetFile = new File("D:\\software\\idea_workspace\\D0530_javaDay9");
        findMaxAndMinFile(targetFile);
        System.out.println("最小文件是："+fileMap.firstEntry().getValue());
        System.out.println("最小文件大小是："+fileMap.firstEntry().getKey());
        System.out.println("最大文件是："+fileMap.lastEntry().getValue());
        System.out.println("最大文件大小是："+fileMap.lastEntry().getKey());
    }
    //静态,被共享的变量
    private static TreeMap<Long, List<File>> fileMap = new TreeMap<>();
    public static void findMaxAndMinFile(File targetDir){
        //如果发现这个目录不存在或者不是一个目录就直接返回
        if(!targetDir.exists() || !targetDir.isDirectory()){
            System.out.println("目录不存在或者不是目录");
            return;
        }
        //获取目录下的所有文件对象
        File[] files = targetDir.listFiles();
        for (File file : files){
            if(file.length() != 0){
                //如果发现子文件是目录就递归然后查找
                if(file.isDirectory()){
                    findMaxAndMinFile(file);
                }
                //如果发现子文件大小为0就跳过
                if(!file.isFile() || file.length() == 0){
                    continue;
                }
                //如果是文本
                //将文件对象放入TreeMap中
                //先判断key是否存在
                if(!fileMap.containsKey(file.length())){
                    //不存在就动手自己创建
                    fileMap.put(file.length(),new ArrayList<>());
                }
                //如果存在就获取，然后添加
                fileMap.get(file.length()).add(file);
            }
        }
    }
}


