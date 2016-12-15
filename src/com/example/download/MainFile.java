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
		// TODO �Զ����ɵķ������
		//��������õ�Ҫ�����ļ��Ĵ�С
		int allsize=RequestHelp.getAllSize(path);
		int blocksize=allsize/3;
		
		 filename=path.substring(path.lastIndexOf("/")+1);
		//�õ��ļ���С�󣬴���һ��ͬ���Ŀ��ļ�
		File file=new File(filename);
		//ʹ������洢�࣬�趨�ļ��Ĵ�С
		try {
			RandomAccessFile raf=new RandomAccessFile(file, "rw");
			raf.setLength(allsize);
			raf.close();		
			
		} catch (Exception e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
		//�˴�����ѭ��3���߳�����
		for (int threadId = 0; threadId < THREADCOUNT; threadId++) {
			//ѭ���������������߳�
			System.out.println("��"+threadId+"���߳̿�ʼ������");
			new MyThread(threadId,blocksize,allsize,path).start();
		}
		

	}

}
