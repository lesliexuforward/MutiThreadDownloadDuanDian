package com.example.download;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

public class MyThread extends Thread {
	//�������صı���
	private String path;
	private int startIndex;
	private int endIndex;
	private int threadId;
	//����һ���ϵ������ı���
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
		//�˴���������
		//ÿ���̵߳���ʼ�����ֹ���Ѿ�֪��
		System.out.println("�߳�"+threadId+"���صķ�Χ:"+startIndex+"��"+endIndex);
		try {
			//��������
			URL url=new URL(path);
			HttpURLConnection conn=(HttpURLConnection) url.openConnection();
			
			conn.setConnectTimeout(5000);
			conn.setRequestMethod("GET");
			//�ҳ�Ҫ���ص��ļ�
			File file=new File(MainFile.filename);
			//��λ�õ��ļ�
			File filepos=new File(threadId+".position");
			
			RandomAccessFile raf=null;
			if(filepos.exists()){
				//��ȡ���ļ���������
				System.out.println("�߳�"+threadId+"����position��ȡλ��");
				BufferedReader reader=new BufferedReader(new FileReader(filepos));
				String number=reader.readLine();
				alreadyPosition=Integer.parseInt(number);
			//����Ҫ�����ļ��ķ�Χ
			conn.setRequestProperty("range", "bytes="+alreadyPosition+"-"+endIndex);
			//�˿̵õ��˿������ָ��λ�õ��ļ���������
			raf=new RandomAccessFile(file, "rw");
			//��λ
			raf.seek(alreadyPosition);
			}else
			{
				//��û���½�position���ļ�ʱ
				//����Ҫ�����ļ��ķ�Χ
				conn.setRequestProperty("range", "bytes="+alreadyPosition+"-"+endIndex);
				//�˿̵õ��˿������ָ��λ�õ��ļ���������
				raf=new RandomAccessFile(file, "rw");
				//��λ
				raf.seek(alreadyPosition);
			}
			
			
			 
			
			//��λ����ʼ��
			
			
			int type=conn.getResponseCode();
			//�����ļ�������ȷ��Ϊ206
			if(type==206)
			{
				//��ȷ
				//д���ļ�
				InputStream in=conn.getInputStream();
				byte[] buf=new byte[10];
				int len;
				while((len=in.read(buf))>0)
				{
					//ͬ����
					synchronized (MyThread.class) {
						
					
					raf.write(buf,0,len);
					//�ڴ˴���ʱ��д����len��С���ֽ�
					//����������д��ļ�position
					File filepos2=new File(threadId+".position");
					RandomAccessFile rr=new RandomAccessFile(filepos2, "rw");
					alreadyPosition+=len;
					rr.write((alreadyPosition+"").getBytes());
					rr.close();
					}
				}
				
			}else
			{
				//����
				System.out.println("�߳�"+threadId+"���ش���");
				System.out.println("�߳�"+threadId+",type:"+type);
				
			}
			
			raf.close();
			
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
		System.out.println("�߳�"+threadId+"���ؽ���");
	}
	

}
