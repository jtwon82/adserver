package com.adgather.test;

import java.net.URLDecoder;

public class UrlTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String url="http://www.loveloveme.com/?NaPm=ct%3Dhqyhy95s%7Cci%3D0x40000G2Irgow8900ly%7Ctr%3Dsa%7Chk%3D22d9b97498a3dd26d7ab8909973a0255d1c396e6&NVKWD=%EA%B3%A0%EA%B3%A0%EC%8B%B1&NVADKWD=%EA%B3%A0%EA%B3%A0%EC%8B%B";
		
		try {
			url = URLDecoder.decode(url,"utf-8");
		} catch (Exception e) {
		}
		
		System.out.println( url );
	}

}
