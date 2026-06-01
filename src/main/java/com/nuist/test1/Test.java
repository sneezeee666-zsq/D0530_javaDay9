package com.nuist.test1;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

public class Test {
    //I/O 流
    //InputStream  从别的数据源将数据读取到java的内存中
    //OutputStream 从java内存中将数据写入到其他数据源
    //涉及到IO流和本地磁盘的传输时，和java的file流有联动
    //File
    public static void main(String[] args) {
        /*//File对象的创建方式
        //1.基于文件的绝对路径创建
        File f1 = new File("C:\\Users\\asus\\Desktop\\file");
        //2.基于已存在的File对象（作为父目录）完成文件对象的创建
        File f2 = new File(f1,"dyx.txt");
        //3.基于当前项目的路径创建
        File f3 = new File("kzt.txt");
*//*        System.out.println(f1.getAbsoluteFile());
        System.out.println(f2.getAbsoluteFile());
        System.out.println(f3.getAbsoluteFile());
        //exists()判断文件或目录是否存在
        System.out.println(f1.exists());
        System.out.println(f2.exists());
        System.out.println(f3.exists());
        //isFile()判断是否是文件
        //isDirectory()判断是否是目录
        System.out.println(f1.isFile());
        System.out.println(f1.isDirectory());
        System.out.println(f2.isFile());
        System.out.println(f2.isDirectory());
        System.out.println(f3.isFile());
        System.out.println(f3.isDirectory());*//*
        //length()获取文件大小
        System.out.println(f2.length());
        //NTFS 单个文件大小最大2T
        //所以length返回的值一定是long型

        //renameTo()修改文件名称
//        File f4 = new File("C:\\Users\\asus\\Desktop\\file\\fjn.txt");
//        System.out.println(f2.renameTo(f4));
        //delete()删除文件
        //限制：如果删除的文件，则直接删除
        //如果删除的是目录，则目录必须为空才能删除
        //deleteOnExit()删除文件，jvm虚拟机退出时执行
        //setLastModified()设置文件的最后修改时间，参数是时间毫秒值（距离时间1970年1月1日0时0分0秒的毫秒数）

        //针对于目录的方法
        //list()返回目录下的所有文件名称
        String[] names = f1.list();
        //获取指定目录下所有子文件和子目录的文件名数组
        System.out.println(Arrays.asList(names));//Arrays.asList()将数组转换成List集合  但是list并非之前学的ArrayList，仅用于快速遍历数组
        File f4 = new File("C:\\Users\\asus\\Desktop\\file\\testdir");
        String[] names1 = f4.list();
        System.out.println(Arrays.asList(names1));
        File[] files = f4.listFiles();
        //获取当前目下的子文件和子目录的文件对象数组,可以直接访问文件对象
        for (File file : files){
            System.out.println(file);
        }
        //在遍历目录后，可以用于直接获取子文件的对象进行方法操作

        File f5 = new File("C:\\Users\\asus\\Desktop\\file\\testdir\\c.txt");
        //创建文件
        //createNewFile()
        if(!f5.getParentFile().exists()){
            //mkdir() 等价于Linux方法mkdir 必须保证父目录存在
            //mkdirs() 等价于Linux方法的 mkdir -p 先创建缺失的父目录，在创建
            //如果父目录不存在，则递归创建父目录
            f5.getParentFile().mkdirs();
        }
        //这是一个检查时异常，try-catch或者main方法上throws
        try{
            f5.createNewFile();
        }catch (Exception e){
            e.printStackTrace();
        }*/


        //请你输入一个文件路径 找到这个目录下的最大文件和最小文件（不需要考虑子文件）

        //测试：
/*
        File targetFile = new File("D:\\software\\idea_workspace\\D0530_javaDay9");
        findMaxAndMinFile(targetFile);
        System.out.println("最小文件是："+fileMap.firstEntry().getValue());
        System.out.println("最小文件大小是："+fileMap.firstEntry().getKey());
        System.out.println("最大文件是："+fileMap.lastEntry().getValue());
        System.out.println("最大文件大小是："+fileMap.lastEntry().getKey());
*/

        //IO流
        //文件字节输入流和字节输出流 FileInputStream FileOutputStream
        //文件字符输入流和字符输出流 FileReader FileWriter
        //缓存输入输出流 BufferedReader PrintWriter
        //数据流 DataInputStream DataOutputStream
        //java的对象序列化 对象流 ObjectInputStream ObjectOutputStream   //保存本地建立的索引。

        //InputStream
//        InputStream is = new FileInputStream("D:\\software\\idea_workspace\\D0530_javaDay9\\kzt.txt");
        //流不关闭就会一直占用内存
        //正确的做法是在try-catch块中关闭流
        //OutputStream
//        OutputStream os = new FileOutputStream("D:\\software\\idea_workspace\\D0530_javaDay9\\kzt.txt");







    }


    //静态才能被整个类共享
    private static TreeMap<Long, List<File>> fileMap = new TreeMap<>();
    //利用TreeMap的自动排序的特点
    //思路：遍历指定的目录 如果发现目录或者大小为0的文本 则跳过
    //如果发现是非0的长度文本 则将其放入TreeMap中




    public static void findMaxAndMinFile(File targetDir){
        //如果发现这个目录不存在或者不是一个目录就直接返回
        if(!targetDir.exists() || !targetDir.isDirectory()){
            System.out.println("目录不存在或者不是目录");
            return;
        }
        //获取目录下的所有文件对象
        File[] files = targetDir.listFiles();
        for (File file : files){
            if(file.isFile() && file.length() != 0){
                //如果发现子文件是目录或者大小为0就跳过
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
