package com.nuist.exc.exc3;

import java.io.File;

//3.输入一个文件路径 删除该文件(如果是目录 要求能够删除这个目录下的所有子文件和子目录)
//需要注意的是 File类的delete方法是无法直接删除非空目录的
public class Test {
    public static void main(String[] args) {
        File file = new File("C:\\Users\\asus\\Desktop\\file");
        deleteFile(file);
    }
    public static void deleteFile(File file){
        if(file.isFile()){
            file.delete();
        }else{
            File[] files = file.listFiles();
            for (File f : files){
                deleteFile(f);
            }
            file.delete();
        }
    }
}
