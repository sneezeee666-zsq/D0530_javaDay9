package com.nuist.test3;


import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

//实现文件的下载
public class DownloadUtil {
    public static void download(String urlPath, String downloadPath){
        File downloadDir =  new File(downloadPath);
        if (!downloadDir.isDirectory()
                ||!urlPath.startsWith("http")||!urlPath.startsWith("https")){
            System.out.println("参数错误");
            return;
        }
        //http连接对象
        HttpURLConnection connection = null;
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            //创建url对象
            URL url = new URL(urlPath);
            //根据url对象获取http连接对象
            connection = (HttpURLConnection) url.openConnection();
            //根据连接对象获取输入流
            is = connection.getInputStream();
            //创建文件保存路径 所对应的文件对象
            //先获取文件名
            String fileName = urlPath.substring(urlPath.lastIndexOf("/")+1);
            File downloadFile = new File(downloadDir,fileName);
            //基于文件对象创建文件输出流
            fos = new FileOutputStream(downloadFile);
            //定义缓存数组 1024*10
            byte[] buffer = new byte[1024*10];
            int len;
            //len = is.read(buffer)
            //将下载数据从输入流中 读取到buffer缓存中
            //每次读取的时候，都会返回读取的字节数的值
            //当输入流读完的时候 则返回-1 依次作为循环读取是否继续的判断条件
            System.out.println("开始下载");
            System.out.println("下载进度：");
            int count = 0;
            while ((len = is.read(buffer)) != -1){
                count++;
                //将数据写入到文件中
                fos.write(buffer,0,len);
                if(count % 500 == 0){
                    System.out.print("#");
                }
            }
            System.out.println("下载完成");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
