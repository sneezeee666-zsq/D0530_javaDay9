package com.nuist.test2;

import java.io.*;

public class TestFileStream {
    //FileInputStream测试
    public static void main(String[] args) {
/*        File f = new File("C:\\Users\\asus\\Desktop\\file\\dyx.txt");
        //基于文件对象创建文件输入流
        try {
            FileInputStream fis = new FileInputStream(f);
            byte[] buffer = new byte[(int)f.length()];
            //如果读取的是文本，实际读取的是每一个字符所对应的字符集编码
            fis.read(buffer);
            System.out.println(new String(buffer));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/

        testFileOutStream();


    }


    //FileOutputStream测试
    public static void testFileOutStream(){
        //如果输出流所对应的文件不存在，则会自动创建
        File f = new File("C:\\Users\\asus\\Desktop\\file\\snz.txt");
//        FileOutputStream fos = null;
        /*try {
//            FileOutputStream fos = new FileOutputStream(f);
            //做的是覆盖，不是追加
            fos = new FileOutputStream(f,true);
            //这样就是追加，后面+true

            String str = "你干~111";
            fos.write(str.getBytes());
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(fos!=null){
                //相对安全 但是写法过于麻烦
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }*/

        //jdk7引入了AutoCloseable接口 实现该接口的类 都可以通过try-with-resources语句实现自动关闭
        try(FileOutputStream fos = new FileOutputStream(f)) {

            String str = "你干~111";
            fos.write(str.getBytes());
        }catch (Exception e){
            e.printStackTrace();
        }
    }



}
