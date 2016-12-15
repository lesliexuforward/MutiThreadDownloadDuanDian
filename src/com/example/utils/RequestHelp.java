package com.example.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.security.auth.login.FailedLoginException;

public class RequestHelp {
	
	
	//建一个方法，进行得到下载文件的大小
	public static int  getAllSize(final String path)
	{
		int length=0;
		
		
		try {
			//进行请求
			URL url=new URL(path);
			HttpURLConnection conn=(HttpURLConnection) url.openConnection();
			
			conn.setConnectTimeout(5000);
			conn.setRequestMethod("GET");
			
			int type=conn.getResponseCode();
			if(type==200)
			{
				//成功
				System.out.println("猪猪猪");
				//直接拿到文件大小
				length=conn.getContentLength();
				System.out.println("length"+length);
				
				
			}else
			{
				//失败
				System.out.println("请求失败");
				length=-1;
			}
			
			
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();			
		}
			
		return length;
	}
		

}
