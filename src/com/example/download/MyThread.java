package com.example.download;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

public class MyThread extends Thread {
	//定义下载的变量
	private String path;
	private int startIndex;
	private int endIndex;
	private int threadId;
	//定义一个断点续传的变量
	private int alreadyPosition;
	public  MyThread(int threadId,int blocksize,int allsize,String path) {
		
		this.threadId=threadId;
		this.path=path;
		startIndex=threadId*blocksize;
		endIndex=(threadId+1)*blocksize-1;
		alreadyPosition=startIndex;
		if(threadId==MainFile.THREADCOUNT-1)
		{
			endIndex=allsize-1;
		}
	}
	//
	@Override
	public void run() {
		//此处开启下载
		//每个线程的起始点和终止点已经知道
		System.out.println("线程"+threadId+"下载的范围:"+startIndex+"到"+endIndex);
		try {
			//进行请求
			URL url=new URL(path);
			HttpURLConnection conn=(HttpURLConnection) url.openConnection();
			
			conn.setConnectTimeout(5000);
			conn.setRequestMethod("GET");
			//找出要下载的文件
			File file=new File(MainFile.filename);
			//存位置的文件
			File filepos=new File(threadId+".position");
			
			RandomAccessFile raf=null;
			if(filepos.exists()){
				//读取出文件的内容来
				System.out.println("线程"+threadId+"进入position读取位置");
				BufferedReader reader=new BufferedReader(new FileReader(filepos));
				String number=reader.readLine();
				alreadyPosition=Integer.parseInt(number);
			//设置要接受文件的范围
			conn.setRequestProperty("range", "bytes="+alreadyPosition+"-"+endIndex);
			//此刻得到了可以随机指定位置的文件操作工具
			raf=new RandomAccessFile(file, "rw");
			//定位
			raf.seek(alreadyPosition);
			}else
			{
				//还没有新建position的文件时
				//设置要接受文件的范围
				conn.setRequestProperty("range", "bytes="+alreadyPosition+"-"+endIndex);
				//此刻得到了可以随机指定位置的文件操作工具
				raf=new RandomAccessFile(file, "rw");
				//定位
				raf.seek(alreadyPosition);
			}
			
			
			 
			
			//定位到初始化
			
			
			int type=conn.getResponseCode();
			//部分文件请求正确码为206
			if(type==206)
			{
				//正确
				//写入文件
				InputStream in=conn.getInputStream();
				byte[] buf=new byte[10];
				int len;
				while((len=in.read(buf))>0)
				{
					//同步块
					synchronized (MyThread.class) {
						
					
					raf.write(buf,0,len);
					//在此处的时候写入了len大小的字节
					//所以在这进行存文件position
					File filepos2=new File(threadId+".position");
					RandomAccessFile rr=new RandomAccessFile(filepos2, "rw");
					alreadyPosition+=len;
					rr.write((alreadyPosition+"").getBytes());
					rr.close();
					}
				}
				
			}else
			{
				//错误
				System.out.println("线程"+threadId+"下载错误");
				System.out.println("线程"+threadId+",type:"+type);
				
			}
			
			raf.close();
			
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
		System.out.println("线程"+threadId+"下载结束");
	}
	

}
