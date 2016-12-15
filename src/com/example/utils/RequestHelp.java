package com.example.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.security.auth.login.FailedLoginException;

public class RequestHelp {
	
	
	//��һ�����������еõ������ļ��Ĵ�С
	public static int  getAllSize(final String path)
	{
		int length=0;
		
		
		try {
			//��������
			URL url=new URL(path);
			HttpURLConnection conn=(HttpURLConnection) url.openConnection();
			
			conn.setConnectTimeout(5000);
			conn.setRequestMethod("GET");
			
			int type=conn.getResponseCode();
			if(type==200)
			{
				//�ɹ�
				System.out.println("������");
				//ֱ���õ��ļ���С
				length=conn.getContentLength();
				System.out.println("length"+length);
				
				
			}else
			{
				//ʧ��
				System.out.println("����ʧ��");
				length=-1;
			}
			
			
		} catch (Exception e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();			
		}
			
		return length;
	}
		

}
