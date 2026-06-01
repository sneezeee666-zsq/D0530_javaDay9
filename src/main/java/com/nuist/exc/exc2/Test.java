package com.nuist.exc.exc2;


import java.io.File;
import java.util.Random;

//2.在目录下生成100个具有随机名字的文件 名字范围为a-z (两位文件名 没有后缀名)
//找到所有 c开头的文件 删除他们 找到所有d开头的文件 记录文件名和文件的数量
public class Test {
    public static void main(String[] args) {
        File newFile = new File("D:\\software\\idea_workspace\\D0530_javaDay9\\testDirs");
        if(!newFile.exists()){
            newFile.mkdirs();
        }
        createFile(newFile);
        findFile(newFile);
    }
    public static void createFile(File targetDir){
        Random random = new Random();
        for (int i = 0; i < 100; i++){
            File file = new File(targetDir,(char)('a'+random.nextInt(26)) + "" + ( char)('a'+random.nextInt(26)));
            try{
                file.createNewFile();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    public static void findFile(File targetDir){
        int count = 0;
        File[] files = targetDir.listFiles();
        for (File file : files){
            if(file.isFile() && file.getName().startsWith("c")){
                file.delete();
            }
            if(file.isFile() && file.getName().startsWith("d")){
                System.out.println(file.getName());
                count++;
            }
        }
        System.out.println("d开头的文件数量是："+count);
    }
}
