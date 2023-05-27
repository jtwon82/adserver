package com.adgather.test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

public class CmntSend {
	public CmntSend(){}
	public static void main(String[]ar){

		String line = "";
		InputStream input=null;	
		BufferedReader reader=null;
		String keyword=java.net.URLEncoder.encode("");
		String massUrl="http://map.naver.com/comments/set_comment.nhn?ticket=map1&object_id=21701350&page_size=10&object_url=http%3A%2F%2Fmap.naver.com%2Flocal%2Fsiteview.nhn%3Fcode%3D21701350&contents=%EC%A0%9C%ED%92%88%EC%82%AC%EC%9A%A9%ED%95%9C%EC%A7%80%20%ED%95%9C%EB%8B%AC%EB%84%98%EC%97%88%EB%8A%94%EB%8D%B0%20%EC%9A%94%EA%B8%88%20%EC%A0%95%EB%A7%90%20%EC%8B%B8%EA%B2%8C%EB%82%98%EC%98%A4%EB%8D%98%EB%8D%B0%EC%9A%94%5E%5E%3B%20%EA%B0%95%EC%B6%94!&parent_comment_no=0&object_score=5&reply_page_size=3";		
			
		try {
		    URL url = new URL(massUrl);
		    HttpURLConnection con= (HttpURLConnection)url.openConnection();
		    con.setRequestMethod("POST");
		    Random rdm=new Random();
			String crlNum1=rdm.nextInt(10)+"."+rdm.nextInt(10)+"."+(rdm.nextInt(10000)+10000);
			String crlNum2=rdm.nextInt(10)+"."+rdm.nextInt(10)+"."+(rdm.nextInt(10000)+10000);
			String crlNum3=rdm.nextInt(10)+"."+rdm.nextInt(10)+"."+(rdm.nextInt(10000)+10000)+"."+(rdm.nextInt(10000)+10000);
			String crlNum4=rdm.nextInt(10)+"."+rdm.nextInt(10)+"."+(rdm.nextInt(10000)+10000);
		    String agentValue="Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.2; Trident/4.0; Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1) ; .NET CLR "+crlNum1+"; .NET CLR "+crlNum2+"; .NET CLR "+crlNum3+"; .NET CLR "+crlNum4+"; InfoPath.1)";
		    con.setRequestProperty("User-Agent", agentValue);

		    con.setRequestProperty("Host","map.naver.com");
		    con.setRequestProperty("Connection","keep-alive");
		    con.setRequestProperty("Content-Length","435");
    		con.setRequestProperty("charset","utf-8");
			con.setRequestProperty("Origin","http://map.naver.com");
			con.setRequestProperty("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");
			con.setRequestProperty("Accept","*/*");
			con.setRequestProperty("Referer","http://map.naver.com/local/siteview.nhn?code=21701350");
			con.setRequestProperty("Accept-Encoding","gzip,deflate,sdch");
			con.setRequestProperty("Accept-Language","ko-KR,ko;q=0.8,en-US;q=0.6,en;q=0.4");
			con.setRequestProperty("Accept-Charset","windows-949,utf-8;q=0.7,*;q=0.3");
			con.setRequestProperty("Cookie","NNB=NE354KCBBJSVC; NB=GY4DMOJVGAZDOMRX; npic=RZSOOidGE5uUyv3B0hWk5n9OqrJeS7o5uxalGsYjsC5731xI/Crai5YV9DWp9LvpCA==; _naver_usersession_=UYdF43JvLCsAAArCU4M; BMR=; nid_inf=1727281925; NID_MATCH_M=1; NID_AUT=52tIQUKTBB3YxZPT0pl1Kznx+lmjKdwrJfzki3Yjxy+1ecFrti+CqgIcnaY2Tj1Y4hXtiDAeg8GcTCfAQfZDnTuNPPEm2DxPlGlTifR33PM5TX7ISgKCGVwrd80qpSM/FGjXv6TW1uUjmp8kpLWLgA==; JSESSIONID=8B784DC586809A9A12DBCE8AC9A71514; page_uid=RhOa/WHIw6VsssljhBsssssssvh-372226; _naver_usersession_=UYdF43JvLCsAAArCU4M; NID_SES=AAABTmq6dnEUTOtKP8SrL2vqe773UjPFgmWdjTZHfCSSAxWm7uew67WIbYy0f2puYOGxQKfsb+jK7lzVXzRQIBu49ypFXFXujmtdNMGqCNQOj7TPjjBVk12MXJtQPBzh6saO3tz8f0QBN15G9YaTSJYRHBcFSbRX9Tu0XdwCKBLkkjI9zaL8Wss756Mkj/AowedcF66aMGmiNoiLnWlqXjT3axn/isd9jVy99pHXPc0mAQ9USfZvEc+HHIn3t05p5FSF6PU4ef3oqnjdPppeRvSBwsa9r5xAO9q6qvwXb31If+hNKQi+bQnx79FmaguqzObU4UPEygJtv/9K8g0vnTP06lcidRcJH5pxfLPX7vEIhmX2GdDJMHP5fjwMyjYSusv9x6zTGpB5TcmcRgaVeZV96OKBeicGJViLg2+WMvjptoFUUL9711kpK0qg/Ar9Kv7TzA==");
		    

		    con.connect();
		    
		    Object content = con.getContent();
		    input = (InputStream)content;
		    reader = new BufferedReader(new InputStreamReader(input));
		    
			StringBuffer fullBuf =new StringBuffer();
			while((line = reader.readLine()) != null){
				fullBuf.append(line+"\r\n");
			}
			String fullText=fullBuf.toString();
			System.out.println( fullText);
			
		}catch(Exception e){
		}finally{
			try{
				if(input !=null) input.close();
			}catch(Exception e){}
			try{
				if(reader !=null) reader.close();
			}catch(Exception e){}
		}
	}
}
