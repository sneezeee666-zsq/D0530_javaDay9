package com.nuist.test4;

import java.io.*;


public class TestFileStream {
    public static void main(String[] args) {
//        testFileReader();
//        testFileWriter();
//        testBufferedReader();
//        testPrintWriter();
//        testDataOutputStream();
//        testDataInputStream();
        testObjectOutputStream();
        testObjectInputStream();
        //两次的学生对象地址 不一样
    }


    //测试文件字符输入流
//FileReader
    public static void testFileReader(){
        File f = new File("C:\\Users\\asus\\Desktop\\file\\dyx.txt");
        try(FileReader fr = new FileReader(f)){
            char[] buffer = new char[(int)f.length()];
            int len = fr.read(buffer);
            System.out.println(new String(buffer,0,len));
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    //测试文件字节输出流
    //FileWriter
    public static void testFileWriter(){
        File f = new File("C:\\Users\\asus\\Desktop\\file\\dyx.txt");
        try(FileWriter fw = new FileWriter(f)){
            fw.write("hello world");
            //底层是调用FileOutputStream的write方法
            //还是覆盖
            //不覆盖就加true
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //缓存流  字节/字符流都存在读写频率过快，I/O操作下降问题
    //解决方法：使用缓存流
    //缓存流不会每次都读取数据，而是将数据先缓存到缓冲区，当缓冲区满了，才会将数据写入到文件中
    //通过降低读写频率，来提高I/O操作效率

    //缓存输入流
    //BufferedReader
    public static void testBufferedReader(){
        File f = new File("C:\\Users\\asus\\Desktop\\file\\dyx.txt");
        try(BufferedReader br = new BufferedReader(new FileReader(f))){
            String line;
            while ((line = br.readLine()) != null){
                System.out.println(line);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //缓存输出流
    //PrintWriter（BufferedWriter本质有点问题，所以先弃用）
    public static void testPrintWriter(){
        File f = new File("C:\\Users\\asus\\Desktop\\file\\dyx.txt");
        try (FileWriter fw = new FileWriter(f,true);
             PrintWriter pw = new PrintWriter(fw, true)   ){
            pw.println("bye world");
            pw.println("hello world");
        }catch (Exception e){
            e.printStackTrace();
        }
    }



    //数据流
    //DataInputStream
    //能够在读写数据的时候，保留数据的格式数据类型
    public static void testDataInputStream(){
        File f = new File("C:\\Users\\asus\\Desktop\\file\\dyx.txt");
        try(DataInputStream dis = new DataInputStream(new FileInputStream(f))){
            //可以直接读取通过数据输出流输出的数据（数据输出流在输出数据的时候会标记数据类型）
            System.out.println(dis.readUTF());
            System.out.println(dis.readInt());
            System.out.println(dis.readBoolean());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //DataOutputStream
    public static void testDataOutputStream(){
        File f = new File("C:\\Users\\asus\\Desktop\\file\\dyx.txt");
        try(DataOutputStream dos = new DataOutputStream(new FileOutputStream(f))){
            dos.writeUTF("hello world");
            dos.writeInt(123);
            dos.writeBoolean(true);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //序列化 将对象转化为特定的字节码或字符串
    //反序列化 将字节码或字符串转化为对象
    //补充：
    //在后续前后端开发中，json格式是比较标准的java对象序列化格式
    //java对象-》 json 的字符串：序列化
    //json的字符串-》java对象：反序列化
    //ObjectOutputStream
    //ObjectInputStream
    //基于字节流FileOutputStream和FileInputStream实现

    //ObjectOutputStream
    public static void testObjectOutputStream(){
        File f = new File("C:\\Users\\asus\\Desktop\\file\\object.txt");
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f))){
            Student s = new Student();
            s.setName("张三");
            s.setAge(18);
            oos.writeObject(s);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //ObjectInputStream
    public static void testObjectInputStream(){
        File f = new File("C:\\Users\\asus\\Desktop\\file\\object.txt");
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))){
            Student s = (Student) ois.readObject();
            //读取的时候会做校验，保证读取的uid和当前使用的uid一致   所以要做强转
            System.out.println(s);
            System.out.println(s.getName());
            System.out.println(s.getAge());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //json 格式
    //javascript object notation
    //var a = 1;
    //var a = function(){}
    //return 1;
    //var student = {
    // "name":"张三",
    // "age":21,
    //showName:function(){}
    // }
    //{"name":张三,age:18}

}



















