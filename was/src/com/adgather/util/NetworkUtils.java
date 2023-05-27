package com.adgather.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.servlet.http.HttpServletRequest;

public class NetworkUtils {
	
	public static final void main(String []ar){
		System.out.println( NetworkUtils.getServerType() );
	}
	
	public static String getClientIP(HttpServletRequest request) {

		String ip = request.getHeader("X-FORWARDED-FOR"); 

		try{
			if (ip == null || ip.length() == 0) {
				ip = request.getHeader("Proxy-Client-IP");
			}

			if (ip == null || ip.length() == 0) {
				ip = request.getHeader("WL-Proxy-Client-IP");
			}

			if (ip == null || ip.length() == 0) {
				ip = request.getRemoteAddr() ;
			}
		}catch(Exception e){}

		return ip;
	}
	public static String getServerType1(){
		InetAddress[] local = null;
		String returnValue="test";
		try {
			local = InetAddress.getAllByName(InetAddress.getLocalHost().getHostName());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if( local!=null ){
	        for(int i=0; i<local.length; i++) {
	        	if( local[i].getHostAddress().indexOf("183.111.148.")>-1 ){
	        		returnValue=local[i].getHostAddress().toString();
	        		break;
	        	}
			}
		}
		return returnValue;
	}
	public static String getServerType(){
		InetAddress[] local = null;
		String returnValue="test";
		try {
			local = InetAddress.getAllByName(InetAddress.getLocalHost().getHostName());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if( local!=null ){
	        for(int i=0; i<local.length; i++) {
	        	if( local[i].getHostAddress().indexOf("183.111.148.")>-1 ){
	        		returnValue="real";
	        		break;
	        	}
			}
		}
		return returnValue;
	}

}
