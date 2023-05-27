package com.adgather.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.log4j.Logger;
public class PrintFile{
	static  PrintWriter log;
	static Logger logger = Logger.getLogger(PrintFile.class);
	/*
	public static void write(String destFile,String msg) {
		try {
			log = new PrintWriter(new FileWriter(destFile, true), true);
			log.println(msg);
		}catch (IOException e) {
			System.err.println("Can't open the log file: error.log");
			log = new PrintWriter(System.err);
		}
	}
	*/
	public static void write(String destFile,String msg){
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
	public static void main(String[] args){
		write("C:/Downloads/cc.txt","ÇÏ³ªµÑ.");
	}
}
