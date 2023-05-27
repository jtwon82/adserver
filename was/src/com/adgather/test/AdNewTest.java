package com.adgather.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.jsoup.nodes.Document;

import com.adgather.servlet.RFServlet;
import com.adgather.util.DateUtils;
import com.adgather.util.StringUtils;

public class AdNewTest {
	public void test() throws IOException{
//		Document doc = FileUtil.readFile("/home/dreamsearch/public_html/ad/adbn_adnewhtml.html");
//		
//		Document doc = Jsonp.(new File("/home/dreamsearch/public_html/ad/adbn_adnewhtml.html"));
		
		//RFServlet.instance.loadXmlData();
		
		System.out.print( DateUtils.getDate("yyyyMMdd") );
		
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			new AdNewTest().test();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
