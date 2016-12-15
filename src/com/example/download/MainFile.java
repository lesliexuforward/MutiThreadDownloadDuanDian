package com.example.download;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

import com.example.utils.RequestHelp;

public class MainFile {
	 static final int THREADCOUNT = 3;
//	 static  String path="http://10.190.5.19/Chrome.exe";
	 static  String path="http://star.yule.com.cn/uploadfile/2014/cng/jingtian/yule0029.jpg";

	 static String filename;
//	private static  String path="http://www.baidu.com";
	public static void main(String[] args) {
		// TODO 自动生成的方法存根
		//首先请求得到要下载文件的大小
		int allsize=RequestHelp.getAllSize(path);
		int blocksize=allsize/3;
		
		 filename=path.substring(path.lastIndexOf("/")+1);
		//得到文件大小后，创建一个同名的空文件
		File file=new File(filename);
		//使用随机存储类，设定文件的大小
		try {
			RandomAccessFile raf=new RandomAccessFile(file, "rw");
			raf.setLength(allsize);
			raf.close();		
			
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		//此处进行循环3个线程下载
		for (int threadId = 0; threadId < THREADCOUNT; threadId++) {
			//循环启动三个下载线程
			System.out.println("第"+threadId+"个线程开始下载了");
			new MyThread(threadId,blocksize,allsize,path).start();
		}
		

	}

}
