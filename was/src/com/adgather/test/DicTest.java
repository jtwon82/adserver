package com.adgather.test;

import java.util.StringTokenizer;

import com.adgather.util.StringUtils;

public class DicTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		String url = "http://www.halfclub.com/Detail?PrstCd=KH1OP155A&ColorCd=ZZ9&Category=&joins/withpang";
		String PrstCd = ( StringUtils.getReg("PrstCd=(([A-Z]|[0-9])*)", url).replace("PrstCd=", "") );
		String ColorCd = ( StringUtils.getReg("ColorCd=(([A-Z]|[0-9])*)", url).replace("ColorCd=", "") );
		
		String real_url = "http://www.halfclub.com/joins/withpang.asp?/Detail?PrstCd={PrstCd}&ColorCd={ColorCd}";
		
		real_url = real_url.replace("{PrstCd}", PrstCd);
		real_url = real_url.replace("{ColorCd}", ColorCd);
		
		System.out.println( real_url );
	}
}
