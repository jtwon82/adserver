package com.adgather.test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;

import org.apache.commons.collections.CollectionUtils;

import com.adgather.reportmodel.ShopData;
import com.adgather.resource.db.DBManager;
import com.adgather.util.MassInformation;
import com.adgather.util.StringUtils;

public class ErrTest {

	public static void main(String[] args) throws Exception{
		
		HashMap hm = new HashMap();
		HashMap item_list = new HashMap();
		hm.put("1", "aaa");
		item_list.put("13", "bbb");
		
		System.out.println( item_list.keySet());

		ArrayList chk_result= new ArrayList();
		ArrayList item_list_arr= new ArrayList();
		chk_result.addAll(hm.keySet());
		item_list_arr.addAll(item_list.keySet());
		Collection<String> hap = CollectionUtils.intersection(chk_result, item_list_arr);
		String a = hap.toString();
		System.out.println( hap.size() );
		
	}
	public void checkKorEnc(String word){ 
		String[] arr = {"euc-kr","utf-8","iso-8859-1","ksc5601","x-windows-949"}; 
		try{ 
			for( int i =0 ; i < arr.length; i++){ 
				for(int z=0; z < arr.length ; z++){ 
				if( i != z) 
					System.out.println( arr[i]+"=>"+ arr[z]+ " \r\n ==> " +new String(word.getBytes(arr[i]),arr[z])); 
				} 
			} 
		}catch(Exception e){ 
			System.out.println(e); 
		} 
	} 

	public void exceptionTest() {
		
		try{
			HashMap hm= new HashMap();
			Connection conn= DBManager.getConnection("5", "dreamsearch");
			
			Statement stmt = null;
			PreparedStatement pstmt= null;
			ResultSet rs = null;
			StringBuffer sql=new StringBuffer();
			try {
				sql.append("SELECT CONCAT(url,'-',cate1) k1 \n");
				sql.append(", url, pcode, pnm, price, imgpath, purl, cate1, lastupdate \n");
				sql.append("FROM SHOP_DATA WHERE IMGPATH!='' \n");
				sql.append("and status=? and cate1!='' and userid='g1ncom' ; \n");
				pstmt=conn.prepareStatement(sql.toString());
				pstmt.setString(1, "Y");
				rs= pstmt.executeQuery();
				
				while(rs.next()){
					ShopData a = new ShopData();
					a.setLASTUPDATE(rs.getTimestamp("lastupdate"));
					System.out.println( a.getLASTUPDATE().getTime() );
				}
			} catch(Exception e) {
				System.out.println(e);
			} finally {
				try{if(rs !=null) rs.close();} catch(Exception e){};
				try{if(stmt !=null) stmt.close();} catch(Exception e){};
				try{if(conn !=null) conn.close();} catch(Exception e){};
			}
		}catch(Exception e){}
	}
	public void test2(){
		try{
			Hashtable domainTable = new Hashtable<String, String>();
			domainTable.put("kr.search.yahoo.com", "p");
			domainTable.put("cyworld.search.empas.com", "q");
			domainTable.put("search.nate.com", "q");
			domainTable.put("search.daum.net", "q");
			domainTable.put("search.d.paran.com", "q");
			domainTable.put("search.chol.com", "q");
			domainTable.put("www.google.co.kr", "q");
			domainTable.put("kr.hanafos.search.yahoo.com", "p");
			domainTable.put("kr.altavista.com", "q");
			domainTable.put("search.naver.com", "query");
			domainTable.put("news.search.naver.com", "query");
			domainTable.put("nate.search.empas.com", "q");
			domainTable.put("kr.dist.search.yahoo.com", "p");
			domainTable.put("www.gaemi.net", "query");
			domainTable.put("www.dajaba.net", "query");
			domainTable.put("www.82rg.com", "q");
			domainTable.put("www.korealist.com", "keyword");
			domainTable.put("asia.pe.kr", "kword");
			domainTable.put("search.xpointer.com", "query");
			domainTable.put("rank.user100.com", "key");
			domainTable.put("search.imsos.com", "s_key");
			domainTable.put("www.jungboland.co.kr", "key");
			domainTable.put("search.zum.com", "query");
			/* test mess */
			domainTable.put("weeset.co.kr", "query");
			//MassInformation mf = new MassInformation(domainTable,"https://www.google.co.kr/#newwindow=1&q=%EA%BD%83%EB%B0%B0%EB%8B%AC");
			MassInformation mf = new MassInformation(domainTable,"http://search.naver.com/search.naver?sm=tab_sug.top&where=nexearch&acq=%EC%95%88%EC%A0%A4%EB%9D%BC%EB%B2%A0%EC%9D%B4%EB%B9%84&acr=4&qdt=0&ie=utf8&query=%EC%95%88%EC%A0%A4%EB%9D%BC%EB%B2%A0%EC%9D%B4%EB");
			
			String scTxt=mf.GetDecodingKeyword();
			System.out.println(scTxt);
		}catch(Exception e){
			System.out.println(e);
		}
	}
	private void test1(){
		ArrayList a1= new ArrayList();
		a1.add("1111111");
		a1.add("222222");
		a1.add("333333");
		a1.add("444444123");
		a1.add("55555aaaaa");
		a1.add("444444bbbbbbb");
		a1.add("55555ddddd");
		a1.add("444444eeeeee");
		a1.add("55555aaaaaaaaeqef");
		
		ArrayList a2= StringUtils.getRandList(a1.size());
		for(int i=0; i<a2.size(); i++){
			int r= Integer.parseInt(a2.get(i).toString());
			//d.d( a1.get(r) );
		}
	}
}
