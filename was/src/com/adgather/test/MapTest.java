package com.adgather.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.adgather.reportmodel.ShopData;
import com.adgather.servlet.RFServlet;

public class MapTest {
	public static void main(String[] args){
		MapTest t=new MapTest();
		t.mapTest();
	}
	private void mapTest(){
		HashMap<String,String> shhm=new HashMap();
		shhm.put("103","4");
		shhm.put("104","5");
		shhm.put("100","1");
		shhm.put("101","2");
		shhm.put("102","3");
		
		List<String> it = new ArrayList<String>(shhm.keySet());
		Collections.sort(it);
		for(String me:it){
	    	String a= shhm.get(me);
	    	System.out.println(a);
		}
	}
}
