package com.adgather.util;

import java.awt.Image;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;

public class FileUtil {
	static Logger logger = Logger.getLogger(FileUtil.class);
	
	public static void fileCopy(String inFileName, String outFileName) {
		FileInputStream fis=null;
		FileOutputStream fos=null;
		try {
			fis = new FileInputStream(inFileName);
			fos = new FileOutputStream(outFileName);
			
			byte [] buffer=new byte[512];
			int data = 0;
			while( ( data=fis.read(buffer, 0, 512) )!=-1 ) {
				fos.write(buffer,0,data);
			}
		} catch (IOException e) {
			logger.error("fileMove[41] err "+ e);
		}finally{
			try{	fis.close();	}catch(Exception e){	logger.error("fileCopy[42] err");	}
			try{	fos.close();	}catch(Exception e){	logger.error("fileCopy[43] err");	}
		}
	}
	public static void fileDelete(String deleteFileName) {
		try{
			File I = new File(deleteFileName);
			System.out.println( deleteFileName );
			I.delete();
		}catch(Exception e){
			logger.error("fileDelete[48] err "+ deleteFileName);
		}
	}
	public static void writeFile(String destFile,String msg){
		BufferedWriter fw=null;
		try{
			File f=new File(destFile);
			fw = new BufferedWriter(new FileWriter(f,true)); 
			fw.write(msg); 
			fw.flush();  
		}catch(Exception e){
			logger.error(e.getMessage());
		}finally{
			try {
				if(fw!=null)fw.close();
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
		}
	}
	public static void reWriteFile(String destFile,String msg){
		DataOutputStream outs=null;
		try{
			File file=new File(destFile);
			outs = new DataOutputStream(new FileOutputStream(file,false));
			outs.write(msg.getBytes());
			outs.close(); 
		}catch(Exception e){
			logger.error(e.getMessage());
		}finally{
			try {
				if(outs!=null)outs.close();
			} catch (IOException e) {
				logger.error(e);
			}
		}
	}
	public static int saveUrlData(String saveDest,String downloadUrl){
		URL u=null;
		URLConnection conn=null;
		BufferedInputStream reader=null;
		BufferedOutputStream mywriter=null;
		int rt_int= 0;
		try{
			u=new URL(downloadUrl);
		    conn = u.openConnection();
		    conn.connect();
		    
		    String filename = saveDest;
		    File myfile = new File(filename);
		    reader = new BufferedInputStream(conn.getInputStream());
		    mywriter = new BufferedOutputStream(new FileOutputStream(myfile)); 
		    byte[]buf=new byte[4096];
		    int n = 0;
		    while ((n=reader.read(buf,0,buf.length))!=-1) {
		    	mywriter.write(buf,0,n);
		    }
		    reader.close();
		    mywriter.close();
		    
		}catch(Exception e){
			rt_int= 1;
			System.out.print(e);
		}finally{
			if(reader !=null){
				try {
					reader.close();
				} catch (IOException e) {}
			}
			if(mywriter !=null){
				try {
					mywriter.close();
				} catch (IOException e) {}
			}
		}
		return rt_int;
	}
	public static boolean imgURLCheck(String imgUrl){
		try{
			URL url=new URL(imgUrl);
	        Image image=ImageIO.read(url);
	        image.getWidth(null);
			return true;
		}catch(Exception e){
			return false;
		}
	}
	public static String readFile(String destFile){
		BufferedReader fr=null;
		try{
			File f=new File(destFile);
			fr = new BufferedReader(new FileReader(f));
			StringBuffer sb=new StringBuffer();
			String line;
			while((line=fr.readLine())!=null){
				sb.append(line +"\n");
			}
			return sb.toString(); 
		}catch(Exception e){
			logger.error(e.getMessage());
			return "";
		}finally{
			try {
				if(fr!=null)fr.close();
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
		}
	}
	public static void main(String[] args){
		writeFile("C:/Downloads/daum_일본자유여행검색.txt","abcd");
	}
}
