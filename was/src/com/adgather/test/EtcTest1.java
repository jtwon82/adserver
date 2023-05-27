package com.adgather.test;

import java.io.UnsupportedEncodingException;
import com.adgather.util.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.StringTokenizer;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import com.adgather.reportmodel.AdConfigData;
import com.adgather.reportmodel.ShopData;
import com.adgather.reportmodel.SiteCodeData;
import com.adgather.resource.db.DBManager;
import com.adgather.util.DateUtils;
import com.adgather.util.StringUtils;

public class EtcTest1 {
	public static void main(String []ar) throws Exception {
		//String rtn=getNewChk_rate("BNUM_nurimom.com=19#","BNUM_aaaa",20);
		//System.out.println(rtn);
		//arrTest();
		//hexTest("가");
		//FileUtil.reWriteFile("C:/Downloads/daum_일본자유여행검색.txt","abcd");
		StringTokenizer st=new StringTokenizer("fasdfasdfas234324^aaabbb","^",false);
		System.out.println(st.countTokens());
		if(st.countTokens()>0){
			while(st.hasMoreTokens()){
				System.out.println(st.nextToken());
			}
		}
	}
	private static void hexTest(String word){
		 try {
			byte[] hex=org.bouncycastle.util.encoders.Hex.encode(word.getBytes("euc-kr"));
			System.out.println(new String(hex,"euc-kr"));
			hex="eb9dbcecb9b4ec9db4ec838ceb939ced8c,8cec9db8eba6aceca1b0ed8ab8".getBytes();
			byte[] hex2=org.bouncycastle.util.encoders.Hex.decode(hex);
			System.out.println(new String(hex2,"utf-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	private static void arrTest(){
		try {
			
			String word = "무궁화 꽃이 피었습니다.";
			System.out.println("utf-8 -> euc-kr        : " + new String(word.getBytes("utf-8"), "euc-kr"));
			System.out.println("utf-8 -> ksc5601       : " + new String(word.getBytes("utf-8"), "ksc5601"));
			System.out.println("utf-8 -> x-windows-949 : " + new String(word.getBytes("utf-8"), "x-windows-949"));
			System.out.println("utf-8 -> iso-8859-1    : " + new String(word.getBytes("utf-8"), "iso-8859-1"));

			System.out.println("iso-8859-1 -> euc-kr        : " + new String(word.getBytes("iso-8859-1"), "euc-kr"));
			System.out.println("iso-8859-1 -> ksc5601       : " + new String(word.getBytes("iso-8859-1"), "ksc5601"));
			System.out.println("iso-8859-1 -> x-windows-949 : " + new String(word.getBytes("iso-8859-1"), "x-windows-949"));
			System.out.println("iso-8859-1 -> utf-8         : " + new String(word.getBytes("iso-8859-1"), "utf-8"));

			System.out.println("euc-kr -> utf-8         : " + new String(word.getBytes("euc-kr"), "utf-8"));
			System.out.println("euc-kr -> ksc5601       : " + new String(word.getBytes("euc-kr"), "ksc5601"));
			System.out.println("euc-kr -> x-windows-949 : " + new String(word.getBytes("euc-kr"), "x-windows-949"));
			System.out.println("euc-kr -> iso-8859-1    : " + new String(word.getBytes("euc-kr"), "iso-8859-1"));

			System.out.println("ksc5601 -> euc-kr        : " + new String(word.getBytes("ksc5601"), "euc-kr"));
			System.out.println("ksc5601 -> utf-8         : " + new String(word.getBytes("ksc5601"), "utf-8"));
			System.out.println("ksc5601 -> x-windows-949 : " + new String(word.getBytes("ksc5601"), "x-windows-949"));
			System.out.println("ksc5601 -> iso-8859-1    : " + new String(word.getBytes("ksc5601"), "iso-8859-1"));

			System.out.println("x-windows-949 -> euc-kr     : " + new String(word.getBytes("x-windows-949"), "euc-kr"));
			System.out.println("x-windows-949 -> utf-8      : " + new String(word.getBytes("x-windows-949"), "utf-8"));
			System.out.println("x-windows-949 -> ksc5601    : " + new String(word.getBytes("x-windows-949"), "ksc5601"));
			System.out.println("x-windows-949 -> iso-8859-1 : " + new String(word.getBytes("x-windows-949"), "iso-8859-1"));

			//System.out.println(convertHexToString(b));

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String getNewChk_rate(String chk_ck,String dm,int limitCnt){
		//String chk_ck="BNKL_abc.com=15#BNUM_abc.com=20#BRKL_abc.com=1#BRUM_abc.com=1#ICUM_abc.com=1#ICKL_abc.com=1";
		StringBuffer rtn=new StringBuffer();
		try{
			String[] item=chk_ck.split("#");
			//System.out.println(item.length+","+item[0]);
			if(item!=null && item.length>0){
				for(int i=0;i<item.length;i++){
					String[] cell=item[i].split("=");
					if(cell!=null && cell.length==2){
						//System.out.println(cell[0]);
						if(cell[0].equals(dm)){
							try{
								int rCnt=Integer.parseInt(cell[1]);
								if(rCnt<limitCnt){
									rtn.append(dm+"="+(rCnt+1)+"#");
								}
							}catch(Exception e){
							}
						}else{
							rtn.append(item[i]+"#");
							System.out.println(222);
						}
						System.out.println(111);
					}
				}
				if(rtn.length()==0){
					rtn.append(dm+"=0#");
				}
				//System.out.println(item.length+","+item[0]);
			}else{
				rtn.append(dm+"=0#");
			}
		}catch(Exception e){
			System.out.println(e);
		}
		return rtn.toString();
	}
	public void test4(){
		CacheManager cacheManager=CacheManager.create();
		Cache normalBaseAdConfigCache= cacheManager.getCache("NormalBaseAdConfigCache");
		
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT a.no, a.media_no,f.ad_dcodeno,a.site_code, b.userid,b.site_url,b.url_style,a.script_no,f.adtxt                   \n");
		sql.append(" , b.dispo_sex, b.dispo_age,usemoney,usedmoney,ad_rhour                   										\n");
		sql.append(" FROM adlink a, adsite b, admember c, media_site e, adtype_list f,media_script g	\n");
		sql.append(" WHERE a.adyn='Y' AND a.site_code=b.site_code AND b.userid=c.userid             	\n");
		sql.append(" AND a.site_code = f.site_code                                                  	\n");
		sql.append(" AND e.no=a.media_no                                                            	\n");
		sql.append(" AND b.state='Y' AND b.gubun='AD'                                               	\n");
		sql.append(" AND a.script_no=g.no AND g.ad_type=f.ad_dcodeno									\n");
		sql.append(" AND c.point >= 1000                                                            	\n");
		sql.append(" AND (b.usemoney > (b.usedmoney+b.usemoney/10) OR b.usemoney=0)						\n");
		sql.append(" ORDER BY concat(concat(a.media_no,'_'),f.ad_dcodeno)                               \n");
		
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try{
			conn=DBManager.getConnection("dreamdb");
			stmt=conn.createStatement();
			rs=stmt.executeQuery(sql.toString());
			String tmpS="";
			normalBaseAdConfigCache.removeAll();
			while(rs.next()){
				if( DateUtils.getAdTimeZone(rs.getInt("usemoney"), rs.getInt("usedmoney"), rs.getString("ad_rhour"))==0 ){
					continue;
				}
				
				ArrayList list=null;
				AdConfigData s=new AdConfigData();
				String keyStr=rs.getString("media_no")+"_"+rs.getString("ad_dcodeno");
				s.setKno(rs.getString("no"));
				s.setSite_code(rs.getString("site_code"));
				s.setUserid(rs.getString("userid"));
				s.setSite_url(rs.getString("site_url"));
				s.setAdtxt(rs.getString("adtxt"));
				s.setUrl_style(rs.getString("url_style"));
				s.setDispo_age(rs.getString("dispo_age"));
				s.setDispo_sex(rs.getString("dispo_sex"));
				//System.out.println( keyStr );
				if(tmpS.equals(keyStr)){
					list=(ArrayList)normalBaseAdConfigCache.get(keyStr).getObjectValue();
					list.add(s);
				}else{
					list=new ArrayList();
					list.add(s);
				}
				Element element=new Element(keyStr,list);
				normalBaseAdConfigCache.put(element);
				tmpS=keyStr;
			}
		}catch(Exception ex){
			System.out.println( ex );
		}finally{
			DBManager.close(rs);
			DBManager.close(stmt);
			DBManager.close(conn);
		}
		
		System.out.println( normalBaseAdConfigCache.getSize() );
		
		String dispo="W=4#M=4#b=4#c=4#d=4#";
		//302_i250_250
		
		ArrayList list=(ArrayList) normalBaseAdConfigCache.get("302_i250_250").getObjectValue();
		ArrayList<Integer> rndList=StringUtils.getRandList(list.size());
		
		AdConfigData c=null;
		
		for(int i=0;i<list.size();i++){
			if( c!=null ) break;
			AdConfigData tmpc=(AdConfigData) list.get(rndList.get(i));
			//if(!site_code.containsKey("AD_"+tmpc.getSite_code())){
			
			if( dispo.equals("") ){
				c=tmpc;
				break;
			}else{
				String []tmp_dispo= dispo.split("#");
				if( tmp_dispo.length>0 ){
					String sex= "";
					for( int j=0; j<tmp_dispo.length; j++ ){
						if( tmp_dispo[j].indexOf("M")>-1 || tmp_dispo[j].indexOf("W")>-1 ){
							try{
								sex= tmp_dispo[j].substring(0, tmp_dispo[j].indexOf("=") );
							}catch(Exception e){
								sex="W";
							}
							break;
						}
					}
					if( tmpc.getDispo_sex().indexOf( sex )>-1 ){
						for( int j=0; j<tmp_dispo.length; j++ ) {// chk age
							try{
								String aa= tmp_dispo[j].substring(0, tmp_dispo[j].indexOf("=") );
								if( tmpc.getDispo_age().indexOf( aa )>-1 ){
									c=tmpc;
									break;
								}
							}catch(Exception e){
							}
						}
					}
				}
			}
		}
		if( c!=null )System.out.println( c.getInfo("") );
	}
	
	
	public void test2(){
		String dispo="a=1#M=1#";
		
		SiteCodeData ad = new SiteCodeData();
		ad.setDispo_age("c");
		ad.setDispo_sex("M");
		
		if( ad.getDispo_age()!=null && dispo.indexOf( ad.getDispo_age() )==-1 ){
			dispo += ad.getDispo_age() +"=0#"; 
		}
		if( ad.getDispo_sex()!=null && dispo.indexOf( ad.getDispo_sex() )==-1 ){
			dispo += ad.getDispo_sex() +"=0#"; 
		}
		
		String rebuild= "";
		StringTokenizer st=new StringTokenizer(dispo,"#",false);
		while(st.hasMoreElements()){
			String oneSell=st.nextElement().toString();
			StringTokenizer st2=new StringTokenizer(oneSell,"=",false);
			if(st2.countTokens()==2){
				String sCode= st2.nextElement().toString();
				String sValue= st2.nextElement().toString();
				if( StringUtils.gAtData("1,1,1,1,1,1,1,1,1,1", "a,b,c,d,e,f,g,h,M,W", sCode, ",").equals("1") && 
						(ad.getDispo_age().equals( sCode ) || ad.getDispo_sex().equals( sCode )) ){
					int i= Integer.parseInt(sValue);
					i++;
					rebuild+=sCode+"="+i+"#";
				}else{
					rebuild+=oneSell+"#";
				}
			}
		}
		System.out.println( rebuild );
	}
	public void test1(){
		CacheManager cacheManager= CacheManager.create();;
		Cache shopLogCache= null;
		shopLogCache= cacheManager.getCache("ShopLogCache");

		String ip= "211.36.33.211.873219";
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		StringBuffer sql=new StringBuffer();
		HashMap<String,ShopData> sh=new HashMap<String,ShopData>();
		try {
			sql.append(" select no,site_code,pcode, pnm,url,sc_txt,price,gb,imgpath,purl,TARGETGUBUN,mcgb \n");
			sql.append(" from SHOP_LOG \n");
			sql.append(" where ip='"+ip+"' \n");
			sql.append(" and partid in('"+StringUtils.getPartIdKey(ip)+"','"+StringUtils.getPrevPartIdKey(ip)+"') \n");
			//sql.append(" and length(TARGETGUBUN)< 3 \n");
			//if(product.equals("normal")){
			//	sql.append(" and INSTR(MCGB,'A')=0 \n");
			//}else if(product.equals("sky")){
			//	sql.append(" and INSTR(MCGB,'B')=0 \n");
			//}else if(product.equals("icover")){
			//	sql.append(" and INSTR(MCGB,'C')=0 \n");
			//}
			sql.append(" order by mcgb asc, no desc \n");
			//if(limit>0){
			//	sql.append(" limit "+ limit);
			//}
			conn=DBManager.getConnection("221", "dreamsearch");
			stmt=conn.createStatement();
			rs=stmt.executeQuery(sql.toString());
			int id=0;
			while(rs.next()){
				ShopData sd=new ShopData();
				sd.setNO(rs.getLong("no"));
				sd.setSITE_CODE(rs.getString("site_code"));
				sd.setPCODE(rs.getString("pcode"));
				sd.setCATE1(rs.getString("sc_txt"));
				sd.setPNM(rs.getString("pnm"));
				sd.setURL(rs.getString("url"));
				sd.setPRICE(rs.getString("price"));
				sd.setGB(rs.getString("gb"));
				sd.setIMGPATH(rs.getString("imgpath"));
				sd.setPURL(rs.getString("purl"));
				sd.setMCGB(rs.getString("mcgb"));
				sd.setTARGETGUBUN(rs.getString("TARGETGUBUN"));
				String key1= "r"+ (id++);
				sh.put(key1,sd);
				System.out.println( sd.getInfo("") );
			}
			if(sh.size()>0){
				Element in=new Element(ip,sh);
				shopLogCache.put(in);
			}
		} catch(Exception e) {
			System.out.println( e );
		} finally {
			try{if(rs !=null) rs.close();} catch(Exception e){};
			try{if(stmt !=null) stmt.close();} catch(Exception e){};
			try{if(conn !=null) conn.close();} catch(Exception e){};
		}
		
	}
}
