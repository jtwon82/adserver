package com.adgather.beans;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.stream.JsonParsingException;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.log4j.Logger;

import com.adgather.reportmodel.AdConfigData;
import com.adgather.reportmodel.MediaScriptData;
import com.adgather.reportmodel.PointData;
import com.adgather.reportmodel.RFData;
import com.adgather.reportmodel.ShopData;
import com.adgather.reportmodel.SiteCodeData;
import com.adgather.resource.db.DBManager;
import com.adgather.servlet.RFServlet;
import com.adgather.util.DateUtils;
import com.adgather.util.StringUtils;

public class AdInfoCache {
	private CacheManager cacheManager;
	private Cache mediaCodeCache;
	private Cache adSiteCache;
	private Cache iAdSiteCache;
	private Cache mediaSiteCache;
	
	private Cache shopAdConfigCache;
	private Cache urlMatchConfigCache;
	private Cache iKeywordMatchConfigCache;
	private Cache iKeywordDataCache;
	private Cache baseAdConfigCache;
	private Cache adCashCache;
	private Cache mediaScriptCache;
	
	public Cache normalBaseAdConfigCache;
	private Cache normalShopAdConfigCache;
	private Cache normalKeywordMatchConfigCache;
	private Cache normalKeywordDataCache;
	private Cache normalUrlMatchConfigCache;
	
	public Cache shopLogCache;
	public Cache adLinkCache;
	public Cache rfKeywordLogCache;
	public Cache shopCategoryDataCache;
	public Cache shopPCodeDataCache;
	
	private Cache noMatchKeywordCache;
	
	static Logger logger = Logger.getLogger(AdInfoCache.class);
	public void init(){
		cacheManager=CacheManager.create();
		mediaCodeCache=cacheManager.getCache("MediaCodeCache");
		adSiteCache=cacheManager.getCache("AdSiteCache");
		iAdSiteCache=cacheManager.getCache("IadSiteCache");
		mediaSiteCache=cacheManager.getCache("MediaSiteCache");
		shopAdConfigCache=cacheManager.getCache("ShopAdConfigCache");
		urlMatchConfigCache=cacheManager.getCache("UrlMatchConfigCache");
		iKeywordMatchConfigCache=cacheManager.getCache("IkeywordMatchConfigCache");
		iKeywordDataCache=cacheManager.getCache("IkeywordDataCache");
		baseAdConfigCache=cacheManager.getCache("BaseAdConfigCache");
		adCashCache=cacheManager.getCache("AdCashCache");
		mediaScriptCache=cacheManager.getCache("MediaScriptCache");
		shopCategoryDataCache=cacheManager.getCache("ShopCategoryDataCache");
		shopPCodeDataCache=cacheManager.getCache("ShopPCodeDataCache");
		
		normalBaseAdConfigCache=cacheManager.getCache("NormalBaseAdConfigCache");
		normalShopAdConfigCache=cacheManager.getCache("NormalShopAdConfigCache");
		normalKeywordMatchConfigCache=cacheManager.getCache("NormalkeywordMatchConfigCache");
		normalKeywordDataCache=cacheManager.getCache("NormalKeywordDataCache");
		normalUrlMatchConfigCache=cacheManager.getCache("NormalUrlMatchConfigCache");
		
		shopLogCache=cacheManager.getCache("ShopLogCache");
		adLinkCache=cacheManager.getCache("AdLinkCache");
		rfKeywordLogCache=cacheManager.getCache("RfKeywordLogCache");
		
		noMatchKeywordCache=cacheManager.getCache("NoMatchKeywordCache");
		
	}
	public void cacheClear(String keyIp){
		shopLogCache.remove(keyIp);
	}
	public void loadAdLinkData(String key){
		try {
			logger.info("adLinkCache reload start.");
			if(adLinkCache.isKeyInCache(key)){
				adLinkCache.remove(key);
				logger.info("loadAdLinkData: remove key - "+key);
			} 
			logger.debug("adLinkCache : succ");
		} catch(Exception e) {
			logger.error("adLinkCache:"+e);
		} finally {
		}
	}
	public void loadShopCategoryData(){
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		StringBuffer sql=new StringBuffer();
		try {
			logger.info("shopCategoryDataCache reload start.");
			//shopCategoryDataCache.removeAll();
			/*
			sql.append("SELECT CONCAT(userid,'_',cate1) k1 \n");
			sql.append(", url, pcode, pnm, price, imgpath, purl, cate1 \n");
			sql.append("FROM SHOP_DATA WHERE IMGPATH!='' \n");
			sql.append("and status='Y' and cate1!='' ; \n");
			logger.debug(sql.toString());
			conn=DBManager.getConnection("dreamdb");
			stmt=conn.createStatement();
			rs=stmt.executeQuery(sql.toString());
			shopCategoryDataCache.removeAll();
			while(rs.next()){
				ArrayList list1=null;
				ShopData sd= new ShopData();
				String keyStr=rs.getString("k1");
				sd.setURL(rs.getString("url"));
				sd.setPCODE(rs.getString("pcode"));
				sd.setPNM(rs.getString("pnm"));
				sd.setPRICE(rs.getString("price"));
				sd.setIMGPATH(rs.getString("imgpath"));
				sd.setPURL(rs.getString("purl"));
				sd.setCATE1(rs.getString("cate1"));

				if( shopCategoryDataCache.isKeyInCache(keyStr)){
					list1=(ArrayList)shopCategoryDataCache.get(keyStr).getObjectValue();
					list1.add(sd);
				}else{
					list1=new ArrayList();
					list1.add(sd);
				}

				Element element=new Element(keyStr,list1);
				shopCategoryDataCache.put(element);

			}
			*/
			logger.debug("loadShopCategoryData : succ");
		} catch(Exception e) {
			logger.error("loadShopCategoryData:"+e);
			RFServlet.instance.dbStatus=false;
		} finally {
			try{if(rs !=null) rs.close();} catch(Exception e){};
			try{if(stmt !=null) stmt.close();} catch(Exception e){};
			try{if(conn !=null) conn.close();} catch(Exception e){};
		}
	}/*
	public ArrayList getShopCategoryData(String userid,String cate){	
		ArrayList result=null;
		String key=userid+"_"+cate;
		try{
			if( key==null || key.equals("")) return result;
			if(shopCategoryDataCache.isKeyInCache(key)){
				Element element=shopCategoryDataCache.get(key);
				if(element !=null){
					result= (ArrayList) element.getObjectValue();
				}
			}
			if(result==null){
				result=RFServlet.instance.dataMapper.loadShopCategoryDataFromDB(userid,cate);
			}
			if(result.size()<10){
				ArrayList result2=RFServlet.instance.dataMapper.loadShopCategoryDataFromDB(userid,"");
				if(result2.size()>0){
					for(int i=0;i<result2.size();i++){
						result.add(result2.get(i));
					}
				}
				logger.debug("getShopCategoryData no="+ key );
			}
			logger.debug("load shopCategoryDataCache From Cache: key="+key+", data.size="+ result.size() );
		}catch(Exception e){
			logger.error("getShopCategoryData():"+key+":"+e);
		}
		return result;
	}*/
	public LinkedHashMap getShopStatsData(String userid,String cate){	
		LinkedHashMap<String,String> result=null;
		if( cate==null ) cate="";
		String key=userid+"#_#"+cate;
		String debug="0,";
		try{
			boolean repFlag=false;
			if( key==null || key.equals("")) return result;
			if(shopCategoryDataCache.isKeyInCache(key)){
				Element element=shopCategoryDataCache.get(key);
				if(element !=null){
					result= (LinkedHashMap) element.getObjectValue();
					debug+="0-1,";
					if(result.size()>0){
						long now=Calendar.getInstance().getTimeInMillis();
						long mlong=0l;
						Iterator it = result.keySet().iterator();
						while(it.hasNext()){
							mlong=Long.parseLong(result.get(it.next().toString()));
							break;
						}
						debug+="0-2,";
						if(cate.length()>0){	//Ä«Å×°í¸®¸é 4ÀÏ
							if(mlong+1000*60*60*24*7<now) repFlag=true;
						}else{	// ÀÏ ÃßÃµ ÂÊÀÌ¸é ÇÏ·ç
							if(mlong+1000*60*60*24*1<now) repFlag=true;
						}
						debug+="0-3,";
					}
				}
			}
			debug+="1";
			if(result==null){
				result=RFServlet.instance.dataMapper.loadShopStatsDataFromDB(key,"2nd");
				if(result==null || result.size()==0 || repFlag==true){
					Connection conn = null;
					Statement stmt4 = null;
					try{
						conn=DBManager.getConnection("dreamdb");
						stmt4=conn.createStatement();
						stmt4.executeUpdate("CALL proc_CacheUpdateExec('shopCategoryDataCache','"+key+"') ");
						logger.info("CALL proc_CacheUpdateExec('shopCategoryDataCache'):"+key);
					}catch(Exception e){
						logger.error(e);
						RFServlet.instance.dbStatus=false;
					}finally{
						try{if(stmt4 !=null) stmt4.close();} catch(Exception e){};
						try{if(conn !=null) conn.close();} catch(Exception e){};
					}
				}
			}
			debug+="2";
			if(cate.length()>0 && (result!=null && result.size()<20)){
				debug+="2-1,";
				LinkedHashMap<String,String> result2=getShopStatsData(userid,"");
				debug+="2-2,";
				if(result2!=null && result2.size()>0){
					Iterator iData=result2.keySet().iterator();
					if(result==null) result=new LinkedHashMap();
					while(iData.hasNext()){
						debug+="2-3,";
						String ivData=iData.next().toString();
						result.put(ivData,ivData);
						if(result.size()>=20){
							Element element=new Element(userid+"#_#"+cate,result);
							RFServlet.instance.adInfoCache.shopCategoryDataCache.put(element);
							break;
						}
					}
					debug+="2-4,";
				}
				logger.debug("getShopStatsData no="+ key );
			}
			debug+="3";
			logger.debug("load shopCategoryDataCache From Cache: key="+key+" size="+result.size());
		}catch(Exception e){
			logger.error("getShopStatsData():"+key+":"+e+","+debug);
		}
		return result;
	}
	public void loadShopPCodeData(){
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		StringBuffer sql=new StringBuffer();
		try {
			logger.info("shopPCodeDataCache reload start.");
			shopPCodeDataCache.removeAll();
			/*
			sql.append("SELECT CONCAT(userid,'_',pcode) k1 \n");
			sql.append(", url, pcode, pnm, price, imgpath, purl, cate1 \n");
			sql.append("FROM SHOP_DATA WHERE IMGPATH!='' \n");
			sql.append("and status='Y'; \n");
			logger.debug(sql.toString());
			conn=DBManager.getConnection("dreamdb");
			stmt=conn.createStatement();
			rs=stmt.executeQuery(sql.toString());
			shopPCodeDataCache.removeAll();
			while(rs.next()){
				ShopData sd= new ShopData();
				String keyStr=rs.getString("k1");
				sd.setURL(rs.getString("url"));
				sd.setPCODE(rs.getString("pcode"));
				sd.setPNM(rs.getString("pnm"));
				sd.setPRICE(rs.getString("price"));
				sd.setIMGPATH(rs.getString("imgpath"));
				sd.setPURL(rs.getString("purl"));
				sd.setCATE1(rs.getString("cate1"));
				Element element=new Element(keyStr,sd);
				shopPCodeDataCache.put(element);
			}
			*/
			logger.debug("shopPCodeDataCache : succ");
		} catch(Exception e) {
			logger.error("shopPCodeDataCache:"+e);
			RFServlet.instance.dbStatus=false;
		} finally {
			try{if(rs !=null) rs.close();} catch(Exception e){};
			try{if(stmt !=null) stmt.close();} catch(Exception e){};
			try{if(conn !=null) conn.close();} catch(Exception e){};
		}
	}
	public ShopData getShopPCodeData(String userid,String pcode){	
		ShopData result=null;
		String key="";
		try{
			if( pcode==null || pcode.equals("")) return result;
			key=userid+"_"+pcode;
			if(shopPCodeDataCache.isKeyInCache(key) ){
				Element element=shopPCodeDataCache.get(key);
				if(element !=null){
					result= (ShopData) element.getObjectValue();
				}
			}else{
				result=RFServlet.instance.dataMapper.loadShopPCodeDataFromDB(userid,pcode);
				logger.debug("getShopPCodeData no="+ key );
			}
			logger.debug("load shopPCodeDataCache From Cache: key="+key);
		}catch(Exception e){
			logger.error("shopPCodeDataCache:"+key+":"+e);
		}
		return result;
	}
	public HashMap getShopLogData1(String shopLog, String ip,String product){
		HashMap result=null;
		try{
			if(shopLogCache.isKeyInCache(ip)){
				result=(HashMap) shopLogCache.get(ip).getObjectValue();
				logger.debug("getShopLogData : "+product+" : "+ip + " Cache hit.");
			}else{
				result=RFServlet.instance.dataMapper.getShopLogDataFromIp(ip,product);
				logger.debug("getShopLog ip="+ ip +" result.size="+ result.size());
			}
		}catch(Exception e){
			logger.error("Adbn==>getShopLogData:"+ip+":"+e);
		}
		return result;
		
	}
	public HashMap getShopLogData(String logStr,String ip,String product){
		HashMap result=null;
		InputStream is=null;
		String debug="";
		try{
			debug="1";
			if(logStr!=null && logStr.length()>0){
				int ord=100;
				debug="2";
				logger.debug("getShopLogData 304 "+logStr);
				
				if( logStr.indexOf("\"data\"")<0 ){
					logger.debug("getShopLogData 304 1 "+logStr);
				}else{
					try{
						is = new ByteArrayInputStream(logStr.getBytes());
						JsonReader rdr = Json.createReader(is);
						JsonArray obj=rdr.readObject().getJsonArray("data");
						if(obj!=null && obj.size()>0){
							result=new HashMap();
							String site_codes="";
							for (JsonObject jobj : obj.getValuesAs(JsonObject.class)) {
								ShopData sd=new ShopData();
								sd.setSITE_CODE(jobj.getString("sc",""));
								sd.setPCODE(jobj.getString("pcd", ""));
								sd.setPURL(jobj.getString("purl",""));
								sd.setSITE_URL(jobj.getString("purl",""));
								sd.setMCGB(jobj.getString("mcgb",""));
								sd.setGB(jobj.getString("gb",""));
								sd.setTARGETGUBUN(jobj.getString("tg",""));
								if( site_codes.indexOf(jobj.getString("sc",""))==-1 && jobj.getString("mcgb","").equals("") ){
									sd.setFlag("1");
								}else{
									sd.setFlag("0");
								}
								if( jobj.getString("mcgb","").equals("") ){
									site_codes += jobj.getString("sc","") +",";
								}
								//logger.debug(ReflectionToStringBuilder.toString(sd));
								logger.debug(sd);
								result.put(ord+"",sd);
								ord++;
							}
							logger.debug("getShopLogData : "+product+" : "+ip + ","+result.size()+" cookie hit.");
						}
						debug="3";
					}catch(JsonParsingException e1){
						logger.debug("getShopLogData have no json");
					}catch(Exception e){
						logger.error("getShopLogData err 1 "+e);
					}
				}
				debug="4";
				if((result!=null && result.size()>=5) || (logStr.length()>0 && result==null)){
					if( result==null ){
						result=new HashMap();
					}else{
						result.clear();
					}
					HashMap<String,ShopData> dbdata=null;
					if(shopLogCache.isKeyInCache(ip)){
						debug="5";
						dbdata=(HashMap) shopLogCache.get(ip).getObjectValue();
						logger.debug("getShopLogData : "+product+" : "+ip + ","+dbdata.size()+" Cache hit.");
					}else{
						debug="6";
						dbdata=RFServlet.instance.dataMapper.getShopLogDataFromIp(ip,product);
						logger.debug("getShopLogData : "+product+" : "+ip + ","+dbdata.size()+" db hit.");
					}
					debug="7";
					if(dbdata!=null && dbdata.size()>0){
						debug="8";
						for(String ss : dbdata.keySet()){
							result.put(ord+"",dbdata.get(ss));
							ord++;
						}
					}
				}
			}
		}catch(Exception e){
			logger.error("Adbn==>getShopLogData:"+ip+":"+e+" debug="+debug);
		}finally{
			try{ if(is!=null){
				is.close();
			}
			}catch(Exception e){}
		}
		return result;
	}
	public String getAdLinkData(String site_code,String adgubun,String media_no,String script_no,String tbname,String product){
		String result="D";
		String keyStr="";
		try{
			keyStr=site_code+"_"+media_no+"_"+script_no+"_"+tbname;
			if(adLinkCache.isKeyInCache(keyStr)){
				result=(String) adLinkCache.get(keyStr).getObjectValue();
			}else{
				result=RFServlet.instance.dataMapper.loadAdLinkDataFromDB(site_code,adgubun,media_no,script_no,tbname,product);
			}
			if( result!=null ){
				logger.debug("AdinfoCache[277] load succ keyStr= "+ keyStr);
			}else{
				logger.debug("AdinfoCache[277] load fail keyStr= "+ keyStr);
			}
		}catch(Exception e){
			logger.error("getAdLinkData:"+keyStr+":"+e);
		}
		return result;
		
	}
	public HashMap getRfKeywordLogData(String ip){
		HashMap result=null;
		try{
			if(rfKeywordLogCache.isKeyInCache(ip)){
				result=(HashMap) rfKeywordLogCache.get(ip).getObjectValue();
				//logger.info("getRfKeywordLogData : "+ip + " Cache hit.");
			}else{
				result=RFServlet.instance.dataMapper.getRF_KeywordDataFromIp(ip);
			}
		}catch(Exception e){
			logger.error("Adbn==>getShopLogData:"+ip+":"+e);
		}
		return result;
		
	}
	public MediaScriptData getMediaScriptData(String no){
		MediaScriptData result=null;
		try{
			Element element=mediaScriptCache.get(no);
			if(element !=null){
				result=(MediaScriptData) element.getObjectValue();
				logger.debug("load getMedisScriptData From Cache: key="+no+",userId="+result.getUSERID());
			}
		}catch(Exception e){
			logger.error("MediaScriptData:"+no+":"+e);
		}
		return result;
		
	}
	public void loadMediaScriptData(){
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		StringBuffer sql=new StringBuffer();
		ArrayList list=new ArrayList();
		try {
			logger.info("mediaScriptCache reload start.");
			//sql.append(" select userid,no,ad_type,h_type,h_banner, accept_sr, accept_um, accept_kl, accept_ad  \n");
			//sql.append(" , (SELECT COUNT(seq) c FROM media_icover_limit b WHERE a.no=b.script_no and state='Y' ) limit_pop  \n");
			//sql.append(" from media_script a \n");

			sql.append(" SELECT a.userid,a.no,ad_type,h_type,h_banner, accept_sr, accept_um, accept_kl, accept_ad, accept_st, accept_sp  \n");
			sql.append(" , (SELECT COUNT(seq) c FROM media_icover_limit b WHERE a.no=b.script_no AND state='Y' ) limit_pop  \n");
			sql.append(" , weight_pct, a.mediasite_no \n");
			sql.append(" FROM media_script a, media_site b \n");
			sql.append(" WHERE a.mediasite_no=b.no AND b.state='Y' AND a.state='Y' \n");
			
			logger.debug(sql.toString());
			conn=DBManager.getConnection("dreamdb");
			stmt=conn.createStatement();
			rs=stmt.executeQuery(sql.toString());
			mediaScriptCache.removeAll();
			while(rs.next()){
				MediaScriptData ms=new MediaScriptData();
				ms.setMediasite_no(rs.getString("mediasite_no"));
				ms.setAD_TYPE(rs.getString("ad_type"));
				ms.setUSERID(rs.getString("userid"));
				ms.setH_TYPE(rs.getString("h_type"));
				ms.setH_BANNER(rs.getString("h_banner"));
				ms.setAccept_sr(rs.getString("accept_sr"));
				ms.setAccept_um(rs.getString("accept_um"));
				ms.setAccept_kl(rs.getString("accept_kl"));
				ms.setAccept_ad(rs.getString("accept_ad"));
				ms.setAccept_sp(rs.getString("accept_sp"));
				ms.setAccept_st(rs.getString("accept_st"));
				ms.setWeight_pct(rs.getInt("weight_pct"));
				if( !rs.getString("limit_pop").equals("0") ){
					Statement stmt1=null;
					ResultSet rs1=null;
					try{
						StringBuffer sqls=new StringBuffer();
						StringBuffer domains=new StringBuffer();
						sqls.append("select domains from media_icover_limit a, media_icover_domain b where a.state='Y' and a.domain_no=b.seq and a.script_no='"+ rs.getString("no") +"' \n");
						stmt1= conn.createStatement();
						rs1= stmt1.executeQuery( sqls.toString() );
						logger.debug( "loadMediaScriptData sql ="+ sqls.toString() );
						
						while( rs1.next() ){
							domains.append( ","+ rs1.getString("domains") +"," );
						}
						ms.setLimit_domains( domains.toString() );
						
					}catch(Exception ex){
						logger.debug("loadMediaScriptData[356 ex="+ ex);
					}finally {
						try{if(rs1 !=null) rs1.close();} catch(Exception e){}
						try{if(stmt1 !=null) stmt1.close();} catch(Exception e){}
					}
				}
				
				Element element=new Element(rs.getString("no"),ms);
				mediaScriptCache.put(element);
				//logger.debug("loadMediaScriptData : "+rs.getString("userid")+" Cached.");
			}
			logger.info("mediaScriptCache reload end.");
		} catch(Exception e) {
			logger.error("loadMediaScriptData"+e);
			RFServlet.instance.dbStatus=false;
		} finally {
			try{if(rs !=null) rs.close();} catch(Exception e){};
			try{if(stmt !=null) stmt.close();} catch(Exception e){};
			try{if(conn !=null) conn.close();} catch(Exception e){};
		}
	}
	public PointData getAdCacheData(String userId){
		PointData result=null;
		try{
			Element element=adCashCache.get(userId);
			if(element !=null){
				result=(PointData) element.getObjectValue();
				logger.debug("load getAdCacheData From Cache: key="+userId+",userId="+result.getUSERID());
			}
		}catch(Exception e){
			logger.error("getAdCacheData:"+userId+":"+e);
		}
		return result;
	}
	public void loadAdCashData(){
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		StringBuffer sql=new StringBuffer();
		ArrayList list=new ArrayList();
		try {
			logger.info("adCashCache reload start.");
			sql.append(" select a.userid, b.iad, b.ikl, b.isr, b.ium,b.ad, b.kl, b.sr, b.um, b.sad, b.ssr, b.sum, b.skl, b.st, b.sp, b.ist, b.isp, b.sst, b.ssp \n");
			sql.append(" , a.cookieday, a.cookie_dirsales, a.cookie_indirsales,a.point \n");
			sql.append(" FROM admember a, dbcate b \n");
			sql.append(" WHERE a.cate=b.seq \n");
			logger.debug(sql.toString());
			conn=DBManager.getConnection("dreamdb");
			stmt=conn.createStatement();
			rs=stmt.executeQuery(sql.toString());
			adCashCache.removeAll();
			while(rs.next()){
				PointData acd=new PointData();
				acd.setUSERID(rs.getString("userid"));
				acd.setIAD(rs.getInt("iad"));
				acd.setIKL(rs.getInt("ikl"));
				acd.setISR(rs.getInt("isr"));
				acd.setIUM(rs.getInt("ium"));
				acd.setAD(rs.getInt("ad"));
				acd.setKL(rs.getInt("kl"));
				acd.setSR(rs.getInt("sr"));
				acd.setUM(rs.getInt("um"));
				acd.setSAD(rs.getInt("sad"));
				acd.setSSR(rs.getInt("ssr"));
				acd.setSKL(rs.getInt("skl"));
				acd.setSUM(rs.getInt("sum"));
				acd.setST(rs.getInt("st"));
				acd.setIST(rs.getInt("ist"));
				acd.setSST(rs.getInt("sst"));
				acd.setSP(rs.getInt("sp"));
				acd.setISP(rs.getInt("isp"));
				acd.setSSP(rs.getInt("ssp"));
				acd.setCookieDay(rs.getInt("cookieday"));
				acd.setCookie_dirsales(rs.getInt("cookie_dirsales"));
				acd.setCookie_indirsales(rs.getInt("cookie_indirsales"));
				acd.setPoint(rs.getInt("point"));
				Element element=new Element(rs.getString("userid"),acd);
				adCashCache.put(element);
				//logger.debug("loadAdCashData : "+rs.getString("userid")+" Cached.");
			}
			logger.info("adCashCache reload end.");
			logger.debug("loadAdCashData : succ");
		} catch(Exception e) {
			logger.error("loadAdCashData:"+e);
			RFServlet.instance.dbStatus=false;
		} finally {
			try{if(rs !=null) rs.close();} catch(Exception e){};
			try{if(stmt !=null) stmt.close();} catch(Exception e){};
			try{if(conn !=null) conn.close();} catch(Exception e){};
		}
	}
	private String getWSql(){
		Calendar cal=Calendar.getInstance();
		int w=cal.get(Calendar.DAY_OF_WEEK);
		String wSql="";
		switch(w){
		case 1:
			wSql=" and b.sun = 'y' ";
			break;
		case 2:
			wSql=" and b.mon = 'y' ";
			break;
		case 3:
			wSql=" and b.tue = 'y' ";
			break;
		case 4:
			wSql=" and b.wed = 'y' ";
			break;
		case 5:
			wSql=" and b.thu = 'y' ";
			break;
		case 6:
			wSql=" and b.fri = 'y' ";
			break;
		case 7:
			wSql=" and b.sat = 'y' ";
			break;
		}
		return wSql;
	}
	public void loadNormalAdConfig(String adGubun){
		StringBuffer sql=new StringBuffer();
		try {
			SimpleDateFormat yyyymmdd = new SimpleDateFormat("yyyyMMdd");
			java.util.Date date=new java.util.Date();
			String ymd=yyyymmdd.format(date);
			String wSql=getWSql();
			if(adGubun.equals("AD")){	
				logger.info("normalBaseAdConfigCache reload start.");
				/*sql.append(" SELECT a.no, a.media_no,f.ad_dcodeno,a.site_code, b.userid,b.site_url,b.url_style,a.script_no,f.adtxt                   \n");
				sql.append(" , b.dispo_sex, b.dispo_age,usemoney,usedmoney,ad_rhour								\n");
				sql.append(" , c.imgname_logo, c.site_title, c.site_name, c.site_desc							\n");
				sql.append(" FROM adlink a, adsite b, admember c, adtype_list f,media_script g					\n");
				sql.append(" WHERE a.adyn='Y' AND a.site_code=b.site_code AND b.userid=c.userid             	\n");
				sql.append(" AND a.site_code = f.site_code                                                  	\n");
				sql.append(" AND b.state='Y' AND b.gubun='AD'                                               	\n");
				sql.append(" AND ((b.use_f_day='' AND b.use_t_day = '') OR (b.use_f_day <= '"+ymd+"' AND b.use_t_day >= '"+ymd+"')) \n");
				sql.append(" "+wSql+"                                                                       	\n");
				sql.append(" AND a.script_no=g.no AND g.ad_type=f.ad_dcodeno									\n");
				sql.append(" AND c.point >= 1000                                                            	\n");
				sql.append(" AND (b.usemoney > (b.usedmoney+b.usemoney/10) OR b.usemoney=0)						\n");
				sql.append(" ORDER BY concat(concat(a.media_no,'_'),f.ad_dcodeno)                               \n");
				*/
				sql.append("SELECT IFNULL(b.state,'') stateW, IFNULL(m.state,'') stateM, a.no, a.media_no, a.site_code, a.script_no								\n");
				sql.append(", b.userid, b.site_url, b.url_style, f.adtxt, f.ad_dcodeno                      \n");
				sql.append(", b.dispo_sex, b.dispo_age, usemoney, usedmoney, ad_rhour                       \n");
				sql.append(", c.imgname_logo, c.site_title, c.site_name, c.site_desc,b.adweight                        \n");
				sql.append(", m.banner_path1, m.site_desc site_descm, m.site_url site_urlm	,c.imgname_schonlogo	  \n");
				sql.append("FROM adsite b JOIN adtype_list f                                                \n");
				sql.append("ON b.site_code=f.site_code JOIN media_script g                                  \n");
				sql.append("ON g.ad_type=f.ad_dcodeno JOIN admember c										\n");
				sql.append("ON b.userid=c.userid JOIN adlink a USE INDEX(site_media)												\n");
				sql.append("ON a.script_no=g.no AND a.site_code=b.site_code LEFT JOIN adsite_mobile m       \n");
				sql.append("ON b.site_code=m.site_code 														\n");
				sql.append("WHERE a.adyn='Y' AND b.gubun='AD'												\n");
				sql.append("AND (m.state='Y' OR b.state='Y')                                                \n");
				sql.append("AND c.point >= 1000                                                             \n");
				sql.append("AND (b.usemoney > (b.usedmoney+b.usemoney/10) OR b.usemoney=0)                  \n");
				sql.append("AND ((b.use_f_day='' AND b.use_t_day = '') OR (b.use_f_day <= '"+ymd+"' AND b.use_t_day >= '"+ymd+"')) \n");
				sql.append(""+wSql+"																		\n");
				
				logger.debug(sql.toString());
				Connection conn = null;
				Statement stmt = null;
				ResultSet rs = null;
				float weightSum =0;
				try{
					conn=DBManager.getConnection("dreamdb");
					stmt=conn.createStatement();
					rs=stmt.executeQuery(sql.toString());
					Calendar now=Calendar.getInstance();
					normalBaseAdConfigCache.removeAll();
					HashMap<String,PointData> adWeightMap=new HashMap<String,PointData>();
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
						s.setImgname_logo(rs.getString("imgname_logo"));
						s.setSite_title(rs.getString("site_title"));
						s.setSite_name(rs.getString("site_name"));
						s.setSite_desc(rs.getString("site_desc"));
						s.setBanner_path1(rs.getString("banner_path1"));
						s.setSite_descm(rs.getString("site_descm"));
						s.setSite_urlm(rs.getString("site_urlm"));
						s.setStateM(rs.getString("stateM"));
						s.setStateW(rs.getString("stateW"));
						s.setSchonlogo(rs.getString("imgname_schonlogo"));
						
						if(normalBaseAdConfigCache.isKeyInCache(keyStr)){
							list=(ArrayList)normalBaseAdConfigCache.get(keyStr).getObjectValue();
							list.add(s);
							//logger.debug("normalBaseAdConfigCache.get(keyStr).getObjectValue():site_code="+((AdConfigData)list.get(0)).getSite_code());
						}else{
							list=new ArrayList();
							list.add(s);
						}
						Element element=new Element(keyStr,list);
						normalBaseAdConfigCache.put(element);
						//logger.debug("baseAdWeightList baseaaa user add="+rs.getString("userid"));
						//logger.debug(adGubun+" : "+keyStr+" Cached.");
						String mKey=rs.getString("userid")+"_"+rs.getString("site_code");
						if(!adWeightMap.containsKey(mKey)){
							PointData p=new PointData();
							p.setUSERID(mKey);
							p.setAdWeight(rs.getFloat("adweight"));
							adWeightMap.put(mKey, p);
							weightSum+=rs.getFloat("adweight");
							//logger.debug("baseAdWeightList base user add="+mKey+","+rs.getFloat("adweight"));
						}
					}
					Set keySet=adWeightMap.keySet();
					Iterator itor = keySet.iterator(); 
					ArrayList<PointData> baseAdWeightList=new ArrayList<PointData>();
					while(itor.hasNext()) {
						String sKey=itor.next().toString();
						PointData p=adWeightMap.get(sKey);
						int rate=Math.round((p.getAdWeight()/weightSum)*1000);
						if(rate==0)rate=1;
						p.setAdWeightRate(rate);
						//adWeightMap.put(sKey,p);
						for(int i=0;i<rate;i++){
							baseAdWeightList.add(p);
						}
						//logger.info("baseAdWeightList base rate add="+sKey+","+p.getAdWeight()+","+p.getAdWeightRate());
					}
					Element element=new Element("baseAdWeightList",baseAdWeightList);
					normalBaseAdConfigCache.put(element);
					logger.info("baseAdWeightList="+adWeightMap.size()+","+baseAdWeightList.size());
					logger.info("normalBaseAdConfigCache reload end.");
					logger.debug(adGubun+" : succ");
				}catch(Exception ex){
					logger.error("loadNormalAdConfig:"+adGubun+":"+ex);
				}finally{
					DBManager.close(rs);
					DBManager.close(stmt);
					DBManager.close(conn);
				}
			}else if(adGubun.equals("SR")){
				logger.info("normalShopAdConfigCache reload start.");
				/*
				sql.append(" select a.media_no,a.script_no,b.site_code,b.userid, b.site_etc                                            \n");
				sql.append(" from adlink a,adsite b, admember c                                                                      \n");
				sql.append(" where b.userid=c.userid                                                                                      \n");
				sql.append(" and a.adyn='Y' AND a.site_code=b.site_code \n");
				sql.append(" and b.state='Y' and b.gubun='SR'                            \n");
				sql.append(" and ((b.use_f_day='' and b.use_t_day = '') or (b.use_f_day <= '"+ymd+"' and b.use_t_day >= '"+ymd+"')) \n");
				sql.append(" "+wSql+"                                                                                                \n");
				sql.append(" and c.point >= 1000                                                                                        \n");				
				sql.append(" and (b.usemoney > (b.usedmoney+b.usemoney/10) OR b.usemoney=0)                                                           \n");
				*/
				sql.append(" SELECT b.gubun, IFNULL(b.state,'') stateW, IFNULL(m.state,'') stateM, b.site_code,b.userid, b.site_etc, b.recom_ad,usemoney,usedmoney,ad_rhour	\n");
				sql.append(" , c.imgname_logo, c.site_title, c.site_name, c.site_desc							\n");
				sql.append(" , m.banner_path1, m.site_desc site_descm, m.site_url site_urlm	 ,c.imgname_schonlogo	\n");
				sql.append(" , m.site_etc site_etcm,b.adweight, 'banner' svc_type		\n");
				sql.append(" FROM adsite b JOIN admember c														\n");
				sql.append(" ON b.userid=c.userid LEFT JOIN adsite_mobile m										\n");
				sql.append(" ON b.site_code=m.site_code 														\n");
				sql.append(" WHERE b.userid=c.userid															\n");
				sql.append(" AND (m.state='Y' OR b.state='Y')                            						\n");
				sql.append(" AND b.gubun='SR'                				            						\n");
				sql.append(" AND c.point >= 1000																\n");				
				sql.append(" AND (b.usemoney > (b.usedmoney+b.usemoney/10) OR b.usemoney=0)						\n");
				sql.append(" AND ((b.use_f_day='' AND b.use_t_day = '') OR (b.use_f_day <= '"+ymd+"' AND b.use_t_day >= '"+ymd+"')) \n");
				sql.append(" UNION																			\n");
				sql.append(" SELECT b.gubun, IFNULL(b.state,'') stateW, IFNULL(m.state,'') stateM, b.site_code,b.userid, b.site_etc, b.recom_ad,usemoney,usedmoney,ad_rhour	\n");
				sql.append(" , c.imgname_logo, c.site_title, c.site_name, c.site_desc							\n");
				sql.append(" , m.banner_path1, m.site_desc site_descm, m.site_url site_urlm	 ,c.imgname_schonlogo	\n");
				sql.append(" , m.site_etc site_etcm,b.adweight, 'banner' svc_type		\n");
				sql.append(" FROM adsite b JOIN admember c														\n");
				sql.append(" ON b.userid=c.userid LEFT JOIN adsite_mobile m										\n");
				sql.append(" ON b.site_code=m.site_code 														\n");
				sql.append(" WHERE b.userid=c.userid															\n");
				sql.append(" AND (m.state='Y' OR b.state='Y')                            						\n");
				sql.append(" AND b.gubun='SP'                				            						\n");
				sql.append(" AND c.point >= 1000																\n");				
				sql.append(" AND (b.usemoney > (b.usedmoney+b.usemoney/10) OR b.usemoney=0)						\n");
				sql.append(" AND ((b.use_f_day='' AND b.use_t_day = '') OR (b.use_f_day <= '"+ymd+"' AND b.use_t_day >= '"+ymd+"')) \n");
				sql.append(" "+wSql+"																			\n");
				logger.debug(sql.toString());
				Connection conn = null;
				Statement stmt = null;
				ResultSet rs = null;
				try{
					conn=DBManager.getConnection("dreamdb");
					stmt=conn.createStatement();
					rs=stmt.executeQuery(sql.toString());
					normalShopAdConfigCache.removeAll();
					while(rs.next()){
						if( DateUtils.getAdTimeZone(rs.getInt("usemoney"), rs.getInt("usedmoney"), rs.getString("ad_rhour"))==0 ){
							logger.debug("normalShopAdConfigCache exp "+rs.getString("site_code") );
							continue;
						}
						
						AdConfigData s=new AdConfigData();
						s.setSite_code(rs.getString("site_code"));
						s.setUserid(rs.getString("userid"));
						s.setSite_etc(rs.getString("site_etc"));
						s.setRecom_ad(rs.getString("recom_ad"));
						s.setImgname_logo(rs.getString("imgname_logo"));
						s.setSite_title(rs.getString("site_title"));
						s.setSite_name(rs.getString("site_name"));
						s.setSite_desc(rs.getString("site_desc"));
						s.setBanner_path1(rs.getString("banner_path1"));
						s.setSite_descm(rs.getString("site_descm"));
						s.setSite_urlm(rs.getString("site_urlm"));
						s.setSite_etcm(rs.getString("site_etcm"));
						s.setStateM(rs.getString("stateM"));
						s.setStateW(rs.getString("stateW"));
						s.setGubun(rs.getString("gubun"));
						s.setSvc_type(rs.getString("svc_type"));
						s.setSchonlogo(rs.getString("imgname_schonlogo"));
						
						//s.setMediano(rs.getString("media_no"));
						//s.setScriptno(rs.getString("script_no"));
						//String hKey=rs.getString("media_no")+"_"+rs.getString("script_no")+"_"+rs.getString("site_code");
						
						String hKey=rs.getString("site_code");
						Element element=new Element(hKey,s);
						normalShopAdConfigCache.put(element);
						//logger.debug(adGubun+" : "+rs.getString("site_code")+" Cached.");
					}
					logger.debug(adGubun+" : succ");
					logger.info("normalShopAdConfigCache reload end.");
				}catch(Exception ex){
					logger.error("loadNormalSRAdConfig:"+adGubun+":"+ex);
				}finally{
					DBManager.close(rs);
					DBManager.close(stmt);
					DBManager.close(conn);
				}
			}else if(adGubun.equals("KL")){
				logger.info("normalKeywordMatchConfigCache reload start.");
				/*
				sql.append(" select a.media_no,a.script_no,b.site_code, b.userid,e.site_title, e.site_desc,IFNULL(e.imgname,'') as imgname,IFNULL(e.imgname2,'') as imgname2,IFNULL(e.imgname3,'') as imgname3,IFNULL(e.imgname4,'') as imgname4,IFNULL(e.imgname5,'') as imgname5           \n");
				sql.append(" from adlink a,adsite b, admember c,klsite e                                \n");
				sql.append(" WHERE b.userid=c.userid and b.upseq = e.dscode and b.userid=e.userid \n");
				sql.append(" and a.adyn='Y' AND a.site_code=b.site_code \n");
				sql.append(" AND b.state='Y' and e.state='Y' and b.gubun='KL' \n");
				sql.append(" AND ((b.use_f_day='' AND b.use_t_day = '') OR (b.use_f_day <= '"+ymd+"' AND b.use_t_day >= '"+ymd+"')) \n");
				sql.append(" "+wSql+"                                                                                         \n");
				sql.append(" AND c.point >= 1000                                                                                      \n");
				sql.append(" AND (b.usemoney > (b.usedmoney+b.usemoney/10) OR b.usemoney=0)                                                           \n");
				*/
				sql.append(" SELECT IFNULL(b.state,'') stateW, IFNULL(m.state,'') stateM, b.site_code, b.userid,e.site_title, e.site_desc								\n");
				sql.append(" , IFNULL(e.imgname,'') as imgname, IFNULL(e.imgname6,'') as imgname6				\n");
				sql.append(" , IFNULL(e.imgname2,'') as imgname2, IFNULL(e.imgname3,'') as imgname3				\n");
				sql.append(" , IFNULL(e.imgname4,'') as imgname4, IFNULL(e.imgname5,'') as imgname5           	\n");
				sql.append(" , c.imgname_logo,b.adweight,c.homepi ,c.imgname_schonlogo                                									\n");
				sql.append(" , usemoney,usedmoney,ad_rhour, c.site_title, c.site_name, c.site_desc				\n");
				sql.append(" , km.imgname banner_path1, km.site_desc site_descm, km.site_url site_urlm						\n");
				sql.append(" FROM adsite b JOIN admember c														\n");
				sql.append(" ON b.userid=c.userid JOIN klsite e													\n");
				sql.append(" ON b.upseq = e.dscode AND b.userid=e.userid LEFT JOIN adsite_mobile m				\n");
				sql.append(" ON b.site_code=m.site_code	LEFT JOIN klsite_m km ON m.upseq=km.dscode				\n");
				sql.append(" WHERE b.gubun='KL'																	\n");
				sql.append(" AND (b.state='Y' and e.state='Y' OR m.state='Y')									\n");
				sql.append(" AND c.point >= 1000                                                                \n");
				sql.append(" AND (b.usemoney > (b.usedmoney+b.usemoney/10) OR b.usemoney=0)                     \n");
				sql.append(" AND ((b.use_f_day='' AND b.use_t_day = '') OR (b.use_f_day <= '"+ymd+"' AND b.use_t_day >= '"+ymd+"')) \n");
				sql.append(" "+wSql+"                                                                           \n");
				logger.debug(sql.toString());
				Connection conn = null;
				Statement stmt = null;
				ResultSet rs = null;
				float weightSum =0;
				try{
					conn=DBManager.getConnection("dreamdb");
					stmt=conn.createStatement();
					rs=stmt.executeQuery(sql.toString());
					normalKeywordMatchConfigCache.removeAll();
					HashMap<String,PointData> adWeightMap=new HashMap<String,PointData>();
					while(rs.next()){
						if( DateUtils.getAdTimeZone(rs.getInt("usemoney"), rs.getInt("usedmoney"), rs.getString("ad_rhour"))==0 ){
							continue;
						}
						AdConfigData s=new AdConfigData();
						s.setSite_code(rs.getString("site_code"));
						s.setUserid(rs.getString("userid"));
						//s.setMediano(rs.getString("media_no"));
						//s.setScriptno(rs.getString("script_no"));
						s.setSite_title(rs.getString("site_title"));
						s.setSite_desc(rs.getString("site_desc"));
						s.setHomepi(rs.getString("homepi"));
						s.setImgname(rs.getString("imgname"));
						s.setImgname2(rs.getString("imgname2"));
						s.setImgname3(rs.getString("imgname3"));
						s.setImgname4(rs.getString("imgname4"));
						s.setImgname5(rs.getString("imgname5"));
						s.setImgname6(rs.getString("imgname6"));
						s.setImgname_logo(rs.getString("imgname_logo"));
						s.setSite_title(rs.getString("site_title"));
						s.setSite_name(rs.getString("site_name"));
						s.setSite_desc(rs.getString("site_desc"));
						s.setBanner_path1(rs.getString("banner_path1"));
						s.setSite_descm(rs.getString("site_descm"));
						s.setSite_urlm(rs.getString("site_urlm"));
						s.setStateM(rs.getString("stateM"));
						s.setStateW(rs.getString("stateW"));
						s.setSchonlogo(rs.getString("imgname_schonlogo"));
						
						//String hKey=rs.getString("media_no")+"_"+rs.getString("script_no")+"_"+rs.getString("site_code");
						String hKey=rs.getString("site_code");
						Element element=new Element(hKey,s);
						normalKeywordMatchConfigCache.put(element);
						//logger.debug(adGubun+" : "+rs.getString("site_code")+" Cached.");
						String mKey=rs.getString("userid")+"_"+rs.getString("site_code");
						if(!adWeightMap.containsKey(mKey)){
							PointData p=new PointData();
							p.setUSERID(mKey);
							p.setAdWeight(rs.getFloat("adweight"));
							adWeightMap.put(mKey, p);
							weightSum+=rs.getFloat("adweight");
							//logger.debug("kwAdWeightList base user add="+mKey+","+rs.getFloat("adweight"));
						}
					}
					Set keySet=adWeightMap.keySet();
					Iterator itor = keySet.iterator(); 
					ArrayList<PointData> kwAdWeightList=new ArrayList<PointData>();
					while(itor.hasNext()) {
						String sKey=itor.next().toString();
						PointData p=adWeightMap.get(sKey);
						int rate=Math.round((p.getAdWeight()/weightSum)*1000);
						if(rate==0)rate=1;
						p.setAdWeightRate(rate);
						//adWeightMap.put(sKey,p);
						for(int i=0;i<rate;i++){
							kwAdWeightList.add(p);
						}
						//logger.info("kwAdWeightList base rate add="+sKey+","+p.getAdWeight()+","+p.getAdWeightRate());
					}
					Element element=new Element("kwAdWeightList",kwAdWeightList);
					normalKeywordMatchConfigCache.put(element);
					logger.info("kwAdWeightList="+adWeightMap.size()+","+kwAdWeightList.size());
					logger.debug(adGubun+" : succ");
					logger.info("normalKeywordMatchConfigCache reload end.");
				}catch(Exception ex){
					logger.error("loadNormalKwAdConfig:"+adGubun+":"+ex);
				}finally{
					DBManager.close(rs);
					DBManager.close(stmt);
					DBManager.close(conn);
				}
			}else if(adGubun.equals("UM")){ 
				logger.info("normalUrlMatchConfigCache reload start.");
				/*
				sql.append(" select a0.media_no,a0.script_no,a.site_code, b.userid, b.site_url,a.media_code,IFNULL(b.imgname,'') as imgname \n");
				sql.append(" , IFNULL(b.imgname2,'') as imgname2 ,IFNULL(b.imgname3,'') as imgname3,IFNULL(b.imgname4,'') as imgname4 \n");
				sql.append(" , IFNULL(b.imgname5,'') as imgname5, b.url_style, a.no          \n");
				sql.append(" from adlink a0,urlmatch a, adsite b, admember c                                                                  \n");
				sql.append(" WHERE a.site_code=b.site_code and b.userid=c.userid                                                      \n");
				sql.append(" and a0.adyn='Y' AND a0.site_code=b.site_code \n");
				sql.append(" AND b.state='Y' AND b.gubun='UM'                                                                         \n");
				sql.append(" AND a.del_fg='N'                                                                          \n");
				sql.append(" AND ((b.use_f_day='' AND b.use_t_day = '') OR (b.use_f_day <= '"+ymd+"' AND b.use_t_day >= '"+ymd+"')) \n");
				sql.append(" "+wSql+"                                                                                          \n");
				sql.append(" AND c.point >= 1000                                                                                      \n");
				sql.append(" AND (b.usemoney > (b.usedmoney+b.usemoney/10) OR b.usemoney=0)                                                           \n");
				sql.append(" order by a.media_code                                                           \n");
				*/
				sql.append(" SELECT IFNULL(b.state,'') stateW, IFNULL(m.state,'') stateM, b.site_code, b.userid, b.gubun, a.media_code	\n");
				sql.append(" , b.url_style, a.no, INSTR(b.site_url,a.media_code)ismain                          \n");
				sql.append(" , usemoney, usedmoney, ad_rhour, c.site_title, c.site_name, c.site_desc \n");
				sql.append(" , c.imgname_logo, b.site_url, 'banner' svc_type, b.site_etc,b.adweight							\n");
				sql.append(" , m.banner_path1, m.site_desc site_descm, m.site_url site_urlm, m.state statem		\n");
				sql.append(" , IFNULL(b.imgname,'') AS imgname, IFNULL(b.imgname6,'') AS imgname6				\n");
				sql.append(" , IFNULL(b.imgname2,'') AS imgname2, IFNULL(b.imgname3,'') AS imgname3             \n");
				sql.append(" , IFNULL(b.imgname4,'') AS imgname4, IFNULL(b.imgname5,'') AS imgname5  , c.imgname_schonlogo		           \n");
				sql.append(" FROM urlmatch a JOIN adsite b														\n");
				sql.append(" ON a.site_code=b.site_code JOIN admember c											\n");
				sql.append(" ON b.userid=c.userid LEFT JOIN adsite_mobile m										\n");
				sql.append(" ON b.site_code=m.site_code 														\n");
				sql.append(" WHERE b.gubun='UM'  				                                                 \n");
				sql.append(" AND (m.state='Y' OR b.state='Y')                                                   \n");
				sql.append(" AND a.del_fg='N'                                                                   \n");
				sql.append(" AND c.point >= 1000                                                                \n");
				sql.append(" AND (b.usemoney > (b.usedmoney+b.usemoney/10) OR b.usemoney=0)                     \n");
				sql.append(" AND ((b.use_f_day='' AND b.use_t_day = '') OR (b.use_f_day <= '"+ymd+"' AND b.use_t_day >= '"+ymd+"')) \n");
				sql.append(" "+wSql+"                                                                           \n");
				sql.append(" union																				\n");
				sql.append(" SELECT IFNULL(b.state,'') stateW, IFNULL(m.state,'') stateM, b.site_code, b.userid, b.gubun, homepi media_code	\n");
				sql.append(" , b.url_style, 0 NO, 0 ismain                                                      \n");
				sql.append(" , usemoney, usedmoney, ad_rhour, c.site_title, c.site_name, c.site_desc \n");
				sql.append(" , c.imgname_logo, b.site_url, 'banner' svc_type, b.site_etc,b.adweight							\n");
				sql.append(" , m.banner_path1, m.site_desc site_descm, m.site_url site_urlm, m.state statem		\n");
				sql.append(" , IFNULL(b.imgname,'') AS imgname, IFNULL(b.imgname6,'') AS imgname6				\n");
				sql.append(" , IFNULL(b.imgname2,'') AS imgname2 ,IFNULL(b.imgname3,'') AS imgname3             \n");
				sql.append(" , IFNULL(b.imgname4,'') AS imgname4, IFNULL(b.imgname5,'') AS imgname5  , c.imgname_schonlogo		           \n");
				sql.append(" FROM adsite b LEFT JOIN admember c													\n");
				sql.append(" ON b.userid=c.userid LEFT JOIN adsite_mobile m										\n");
				sql.append(" ON b.site_code=m.site_code 														\n");
				sql.append(" WHERE b.gubun='ST'  				                                               \n");
				sql.append(" AND (m.state='Y' OR b.state='Y')                                                   \n");
				sql.append(" AND c.point >= 1000                                                                \n");
				sql.append(" AND (b.usemoney > (b.usedmoney+b.usemoney/10) OR b.usemoney=0)                     \n");
				sql.append(" AND ((b.use_f_day='' AND b.use_t_day = '') OR (b.use_f_day <= '"+ymd+"' AND b.use_t_day >= '"+ymd+"')) \n");
				sql.append(" "+wSql+"                                                                           \n");
				
				logger.debug(sql.toString());
				Connection conn = null;
				Statement stmt = null;
				ResultSet rs = null;
				float weightSum =0;
				try{
					conn=DBManager.getConnection("dreamdb");
					stmt=conn.createStatement();
					rs=stmt.executeQuery(sql.toString());
					//String tmpS="";
					normalUrlMatchConfigCache.removeAll();
					HashMap<String,PointData> adWeightMap=new HashMap<String,PointData>();
					while(rs.next()){
						if( DateUtils.getAdTimeZone(rs.getInt("usemoney"), rs.getInt("usedmoney"), rs.getString("ad_rhour"))==0 ){
							continue;
						}
						
						ArrayList list=null;
						AdConfigData s=new AdConfigData();
						s.setSite_code(rs.getString("site_code"));
						s.setUserid(rs.getString("userid"));
						s.setGubun(rs.getString("gubun"));
						s.setMedia_code(rs.getString("media_code"));
						s.setUrl_style(rs.getInt("url_style")+"");
						s.setKno(rs.getString("no"));
						s.setIsmain(rs.getString("ismain"));
						s.setSite_title(rs.getString("site_title"));
						s.setSite_name(rs.getString("site_name"));
						s.setSite_desc(rs.getString("site_desc"));
						s.setImgname_logo(rs.getString("imgname_logo"));
						s.setSite_url(rs.getString("site_url"));
						s.setHomepi(rs.getString("site_url"));
						s.setSvc_type(rs.getString("svc_type"));
						s.setImgname(rs.getString("imgname"));
						s.setImgname2(rs.getString("imgname2"));
						s.setImgname3(rs.getString("imgname3"));
						s.setImgname4(rs.getString("imgname4"));
						s.setImgname5(rs.getString("imgname5"));
						s.setImgname6(rs.getString("imgname6"));
						s.setBanner_path1(rs.getString("banner_path1"));
						s.setSite_descm(rs.getString("site_descm"));
						s.setSite_urlm(rs.getString("site_urlm"));
						s.setStateM(rs.getString("stateM"));
						s.setStateW(rs.getString("stateW"));
						s.setSchonlogo(rs.getString("imgname_schonlogo"));
						
						//String hKey=rs.getString("media_no")+"_"+rs.getString("script_no")+"_"+rs.getString("media_code");
						String hKey=rs.getString("media_code");
						
						if( normalUrlMatchConfigCache.isKeyInCache(hKey) ){
							list=(ArrayList) normalUrlMatchConfigCache.get(hKey).getObjectValue();
							//logger.debug("((AdConfigData)list.get(0)).getMedia_code()="+((AdConfigData)list.get(0)).getMedia_code());
							list.add(s);
						}else{
							list=new ArrayList();
							list.add(s);
						}
						Element element=new Element(hKey,list);
						normalUrlMatchConfigCache.put(element);
						//logger.debug(adGubun+" : "+rs.getString("media_code")+" Cached.");
						String mKey=rs.getString("userid")+"_"+rs.getString("site_code");
						if(!adWeightMap.containsKey(mKey)){
							PointData p=new PointData();
							p.setUSERID(mKey);
							p.setAdWeight(rs.getFloat("adweight"));
							adWeightMap.put(mKey, p);
							weightSum+=rs.getFloat("adweight");
							//logger.debug("urlAdWeightList base user add="+mKey+","+rs.getFloat("adweight"));
						}
					}
					Set keySet=adWeightMap.keySet();
					Iterator itor = keySet.iterator(); 
					ArrayList<PointData> urlAdWeightList=new ArrayList<PointData>();
					while(itor.hasNext()) {
						String sKey=itor.next().toString();
						PointData p=adWeightMap.get(sKey);
						int rate=Math.round((p.getAdWeight()/weightSum)*1000);
						if(rate==0)rate=1;
						p.setAdWeightRate(rate);
						//adWeightMap.put(sKey,p);
						for(int i=0;i<rate;i++){
							urlAdWeightList.add(p);
						}
						//logger.info("urlAdWeightList base rate add="+sKey+","+p.getAdWeight()+","+p.getAdWeightRate());
					}
					Element element=new Element("urlAdWeightList",urlAdWeightList);
					normalUrlMatchConfigCache.put(element);
					logger.info("urlAdWeightList="+adWeightMap.size()+","+urlAdWeightList.size());
					
					logger.debug("AdInfoCache[680] end");
					logger.debug(adGubun+" : succ");
					logger.info("normalUrlMatchConfigCache reload end.");
				}catch(Exception ex){
					logger.error("loadNormalAdConfig:"+adGubun+":"+ex);
				}finally{
					DBManager.close(rs);
					DBManager.close(stmt);
					DBManager.close(conn);
				}
			}
		}catch(Exception e){
			logger.error(e);
		}
	}
	public ArrayList getNormalBaceAdList(String keyStr,HashMap site_code, String kno, String scriptno,String dispo){
		AdConfigData c=null;
		ArrayList rt_list= null;

		if(normalBaseAdConfigCache.isKeyInCache(keyStr)){
			rt_list= new ArrayList();
			
			ArrayList list=(ArrayList) normalBaseAdConfigCache.get(keyStr).getObjectValue();
			ArrayList<Integer> rndList=StringUtils.getRandList(list.size());
			for(int i=0;i<list.size();i++){
				
				AdConfigData tmpc=(AdConfigData) list.get(rndList.get(i));
				logger.debug(tmpc.getInfo("getNormalBaceAdList[722] tmpc.info size="+ list.size()));
				
				if( kno.equals(tmpc.getKno()) ){
					continue;
				}
				
				if( dispo.equals("") ){
					rt_list.add(tmpc);
				}else{
					try{
						String []tmp_dispo= dispo.split("#");
						if( tmp_dispo.length>0 ){
	
							String sex= "";
							for( int j=0; j<tmp_dispo.length; j++ ){
								if( tmp_dispo[j].indexOf("M")>-1 || tmp_dispo[j].indexOf("W")>-1 ){
									try{
										sex= tmp_dispo[j].substring(0, 1 );
									}catch(Exception e){
										sex="W";
										logger.error("getNormalBaceAdList[798] base target 1");
									}
									break;
								}
							}
	
							if( tmpc.getDispo_sex().indexOf( sex )>-1 ){
								String age="";
								for( int j=0; j<tmp_dispo.length; j++ ) {// chk age
									try{
										age= tmp_dispo[j].substring(0, 1 );
										
										if( tmpc.getDispo_age().indexOf( age )>-1 ){
											rt_list.add(tmpc);
											break;
										}
									}catch(Exception e){
										age="a";
									}
								}
							}
						}
					}catch(Exception e){}
				}
			}
		}
		return rt_list;
	}
	public ArrayList getNormalBaceAdConfig(String keyStr,int nCnt){
		ArrayList rtn=new ArrayList();
		try{
			int limitCnt=0;
			if(normalBaseAdConfigCache.isKeyInCache(keyStr)){
				ArrayList list=(ArrayList) ((ArrayList) normalBaseAdConfigCache.get(keyStr).getObjectValue()).clone();
				if(list!=null && list.size()>0){
					HashMap<String,String> overlapUID=new HashMap<String,String>();
					ArrayList baseAdWeightList=(ArrayList) normalBaseAdConfigCache.get("baseAdWeightList").getObjectValue();
					while(list.size()>0 && rtn.size()<nCnt && limitCnt<baseAdWeightList.size()){
						limitCnt++;
						if(list.size()==0){
							logger.debug("aaaaaaabbbb");
							break;
						}
						int rInt=new Random().nextInt(baseAdWeightList.size());
						PointData p=(PointData) baseAdWeightList.get(rInt);
						if(overlapUID.containsKey(p.getUSERID())){ 
							//baseAdWeightList.remove(rInt);
							continue;
						}
						for(int i=0;i<list.size();i++){
							try{
								AdConfigData tmpc=((AdConfigData) list.get(i)).clone();
								logger.debug(tmpc.getInfo("getNormalBaceAdConfig[795].php tmpc.info size="+ list.size()));
								//if(!site_code.containsKey("AD_"+tmpc.getSite_code())){
								String key1=tmpc.getUserid()+"_"+tmpc.getSite_code();
								if(overlapUID.containsKey(key1)){
									logger.debug("getNormalBaceAdConfig.php overlapUID:"+key1+" continued.");
									continue;
								}
								if(p.getUSERID().equals(key1)){
								}else{
									logger.debug("getNormalBaceAdConfig.php userid diff:"+","+limitCnt+","+list.size()+","+p.getUSERID()+","+key1);
									continue;
								}
								list.remove(i);
								i--;
								rtn.add(tmpc.clone());
								
							}catch(Exception e){
								logger.error("getNormalBaceAdConfig.php loop:"+e);
							}
						}// for end
						overlapUID.put(p.getUSERID(),"");
					}// while end
				}
			}
			//logger.info("getNormalBaceAdConfig.php:limitCnt="+limitCnt);
		}catch(Exception e){
			logger.error("getNormalBaceAdConfig.php:"+keyStr+":"+e);
		}
		return rtn;
	}
	public AdConfigData getNormalBaceAdConfig(String keyStr,HashMap site_code,String scriptno,String dispo,String cho_sitecode){
		AdConfigData c=null;
		String debug="1,";
		try{
//			if(site_code==null || site_code.size()==0){
//				if(normalBaseAdConfigCache.isKeyInCache(keyStr)){
//					ArrayList list=(ArrayList) normalBaseAdConfigCache.get(keyStr).getObjectValue();
//					if(list!=null && list.size()>0){
//						ArrayList<Integer> rndList=StringUtils.getRandList(list.size());
//						
//						for(int i=0;i<list.size();i++){
//							//logger.debug("getNormalBaceAdConfig "+list.size()+" count  "+ rndList.get(i) +" count");
//							if(scriptno!=null && scriptno.length()>0){
//								AdConfigData tmp=(AdConfigData)list.get(rndList.get(i));
//								if(tmp.getScriptno().equals(scriptno)){
//									c=tmp;
//									if(c!=null) break;
//								}
//							}else{
//								c=(AdConfigData)list.get(rndList.get(i));
//								if(c!=null) break;
//							}
//						}
//					}
//				}
//			}else{
				int limitCnt=0;
				if(normalBaseAdConfigCache.isKeyInCache(keyStr)){
					ArrayList list=(ArrayList) ((ArrayList) normalBaseAdConfigCache.get(keyStr).getObjectValue()).clone();
					debug="1-1";
					if(list!=null && list.size()>0){
						//ArrayList<Integer> rndList=StringUtils.getRandList(list.size());
						debug="1-2";
						HashMap<String,String> overlapUID=new HashMap<String,String>();
						ArrayList baseAdWeightList=(ArrayList) normalBaseAdConfigCache.get("baseAdWeightList").getObjectValue();
						debug="2";
						while(c==null && list.size()>0 && limitCnt<baseAdWeightList.size()){
							limitCnt++;
							int rInt=new Random().nextInt(baseAdWeightList.size());
							PointData p=(PointData) baseAdWeightList.get(rInt);
							if(overlapUID.containsKey(p.getUSERID())){ 
								//baseAdWeightList.remove(rInt);
								continue;
							}
							debug="3";
							for(int i=0;i<list.size();i++){
								try{
									if( c!=null ) break;
									AdConfigData tmpc=((AdConfigData) list.get(i)).clone();
									logger.debug(tmpc.getInfo("getNormalBaceAdConfig[795] tmpc.info size="+ list.size()));
									//if(!site_code.containsKey("AD_"+tmpc.getSite_code())){
									String key1=tmpc.getUserid()+"_"+tmpc.getSite_code();
									if(overlapUID.containsKey(key1)){
										logger.debug("getNormalBaceAdConfig overlapUID:"+key1+" continued.");
										continue;
									}
									if(p.getUSERID().equals(key1)){
									}else{
										logger.debug("getNormalBaceAdConfig userid diff:"+","+limitCnt+","+list.size()+","+p.getUSERID()+","+key1);
										continue;
									}
									debug="4";
									list.remove(i);
									i--;
									if(!cho_sitecode.equals("")){
										if( tmpc.getSite_code().equals(cho_sitecode)){
											c=tmpc.clone();
											logger.debug("getNormalBaceAdConfig rate1:"+limitCnt+","+p.getUSERID()+"_"+tmpc.getSite_code()+","+p.getAdWeight()+","+p.getAdWeightRate());
											break;
										}
									}else{
										if( dispo.equals("") ){
											debug="6";
											c=tmpc.clone();
											logger.debug("getNormalBaceAdConfig rate2:"+limitCnt+","+p.getUSERID()+"_"+tmpc.getSite_code()+","+p.getAdWeight()+","+p.getAdWeightRate());
											break;
										}else{
											String []tmp_dispo= dispo.split("#");
											if( tmp_dispo.length>0 ){
												String sex= "";
												for( int j=0; j<tmp_dispo.length; j++ ){
													if( tmp_dispo[j].indexOf("M")>-1 || tmp_dispo[j].indexOf("W")>-1 ){
														try{
															sex= tmp_dispo[j].substring(0, 1 );
														}catch(Exception e){
															logger.error("getNormalBaceAdConfig[866] base target 1");
														}
														debug="7";
														break;
													}
												}
												if( tmpc.getDispo_sex().indexOf( sex )>-1 ){
													for( int j=0; j<tmp_dispo.length; j++ ) {// chk age
														try{
															String aa= tmp_dispo[j].substring(0, 1 );
															logger.debug("getNormalBaceAdConfig[810] chking "+ tmp_dispo[j] );
															if( tmpc.getDispo_age().indexOf( aa )>-1 ){
																logger.debug("getNormalBaceAdConfig[810 chking age is "+ aa);
																c=tmpc.clone();
																logger.debug("getNormalBaceAdConfig rate3:"+limitCnt+","+p.getUSERID()+","+p.getAdWeight()+","+p.getAdWeightRate());
																debug="8";
																break;
															}
														}catch(Exception e){
															logger.error("getNormalBaceAdConfig[866] base target 2");
														}
													}
												}
											}
										}
									}
								}catch(Exception e){
									logger.error("getNormalBaceAdConfig loop:"+e+","+debug);
								}
							}// for end
							overlapUID.put(p.getUSERID(),"");
						}// while end
					}
				}
//			}
			if(c!=null){
				logger.debug("load normalBaseAdConfigCache Status From Cache: key="+keyStr+",status="+c.getMedia_code());
			}
			//logger.info("getNormalBaceAdConfig:limitCnt="+limitCnt);
		}catch(Exception e){
			logger.error("getNormalBaceAdConfig:"+keyStr+":"+e+","+debug);
		}
		return c;
	}
	public AdConfigData getNormalShopAdConfig(String site_code,String us,String s,String dbname){
		AdConfigData c=null;
		try{
			if(site_code!=null && site_code.length()>0){
				if(normalShopAdConfigCache.isKeyInCache(site_code)){
					String adLinkStatus=RFServlet.instance.adInfoCache.getAdLinkData(site_code,"",us,s,dbname,"");
					if(adLinkStatus!=null && adLinkStatus.equals("Y")){
						c=(AdConfigData) normalShopAdConfigCache.get(site_code).getObjectValue();
						logger.debug( "load normalShopAdConfigCache for c="+ c.toString() );
					}else{
						logger.debug("±¤°í ½ÂÀÎ Á¤º¸ ½ÇÆÐ! : "+site_code+"_"+us+"_"+s+"_"+"adlink");
					}
				}else{
					logger.debug("getNormalShopAdConfig[758] site_code="+ site_code +" none in cache " );					
				}
			}
		}catch(Exception e){
			logger.error("getNormalShopAdConfig:"+site_code+":"+e);
		}
		return c;
	}
	
	public AdConfigData getRecomAdConfig(String userid,String us,String s,String dbname, String svc_type){
		AdConfigData c=null;
		try{
			ArrayList list = null;
			if( svc_type.equals("banner") ){
				list = (ArrayList) normalShopAdConfigCache.getKeys();
			}else{
				list = (ArrayList) shopAdConfigCache.getKeys();
			}
			logger.debug("getRecomAdConfig list="+list.toString());
			
			if( list!=null && list.size()>0 ){
				Iterator it = list.iterator();
				while( it.hasNext() ){
					String key = (String) it.next();
					
					//logger.debug("getRecomAdConfig key="+key);
					
					AdConfigData c1=null;
					if( svc_type.equals("banner")) {
						c1= (AdConfigData) normalShopAdConfigCache.get(key).getObjectValue();
					}else{
						//key= StringUtils.gAt1(key, 0, "_");
						c1= (AdConfigData) shopAdConfigCache.get(key).getObjectValue();
					}
					//logger.debug("getRecomadConfig c1 "+c1);
					if( c1!=null && userid.equals(c1.getUserid()) && c1.getGubun().equals("SP") && c1.getSvc_type().equals(svc_type) ){
						logger.debug("getRecomAdConfig c1="+c1);
						String adLinkStatus=RFServlet.instance.adInfoCache.getAdLinkData(c1.getSite_code(),"SP",us,s,dbname,svc_type);
						if(adLinkStatus!=null && adLinkStatus.equals("Y")){
							c=c1.clone();
							logger.debug( "load getRecomAdConfig for c="+ c.toString() );
							break;
						}else{
							logger.debug("±¤°í ½ÂÀÎ Á¤º¸ ½ÇÆÐ! : "+c1.getSite_code()+"_"+us+"_"+s+"_"+"adlink");
						}
					}
				}
			}
		}catch(Exception e){
			logger.error("getRecomAdConfig:"+userid+":"+e);
		}
		return c;
	}
	
	public void loadshopAdConfig(String adGubun){
		StringBuffer sql=new StringBuffer();
		try {
			SimpleDateFormat yyyymmdd = new SimpleDateFormat("yyyyMMdd");
			java.util.Date date=new java.util.Date();
			String ymd=yyyymmdd.format(date);
			String wSql=getWSql();
			if(adGubun.equals("SR")){
				logger.info("shopAdConfigCache reload start.");
				/*
				sql.append(" SELECT a.media_no,a.script_no,b.site_code_s as site_code,b.userid,b.site_etc AS site_etc, b.svc_type, b.imgname, c.mallnm                \n");
				sql.append(" FROM iadlink a,iadsite b, admember c                                                                               \n");
				sql.append(" WHERE b.userid=c.userid                                                                                  \n");
				sql.append(" and a.adyn='Y' AND a.site_code=b.site_code \n");
				sql.append(" AND b.state='Y' AND b.gubun='SR'                                                                         \n");
				sql.append(" AND ((b.use_f_day='' AND b.use_t_day = '') OR (b.use_f_day <= '"+ymd+"' AND b.use_t_day >= '"+ymd+"')) \n");
				sql.append(" "+wSql+"                                                                                          \n");
				sql.append(" AND c.point >= 1000                                                                                      \n");
				sql.append(" AND (b.usemoney > (b.usedmoney+b.usemoney/10) OR b.usemoney=0)                                                           \n");
				*/
				sql.append(" SELECT b.gubun, b.site_code_s as site_code,b.userid,b.site_etc AS site_etc, b.svc_type		\n");
				sql.append(" , b.imgname, c.mallnm, b.recom_ad, usemoney,usedmoney,ad_rhour, site_code site_code_tmp                \n");
				sql.append(" FROM iadsite b, admember c                                                         \n");
				sql.append(" WHERE b.userid=c.userid                                                            \n");
				sql.append(" AND b.state='Y' AND b.gubun='SR'                                                   \n");
				sql.append(" AND c.point >= 1000                                                                \n");
				sql.append(" AND (b.usemoney > (b.usedmoney+b.usemoney/10) OR b.usemoney=0)                     \n");
				sql.append(" AND ((b.use_f_day='' AND b.use_t_day = '') OR (b.use_f_day <= '"+ymd+"' AND b.use_t_day >= '"+ymd+"')) \n");
				sql.append(" "+wSql+"                                                                           \n");
				sql.append(" union                                                                           \n");
				sql.append(" SELECT b.gubun, b.site_code as site_code,b.userid,b.site_etc AS site_etc, b.svc_type		\n");
				sql.append(" , b.imgname, c.mallnm, b.recom_ad, usemoney,usedmoney,ad_rhour, '' site_code_tmp               \n");
				sql.append(" FROM iadsite b, admember c                                                         \n");
				sql.append(" WHERE b.userid=c.userid                                                            \n");
				sql.append(" AND b.state='Y' AND b.gubun='SP'                                                   \n");
				sql.append(" AND c.point >= 1000                                                                \n");
				sql.append(" AND (b.usemoney > (b.usedmoney+b.usemoney/10) OR b.usemoney=0)                     \n");
				sql.append(" AND ((b.use_f_day='' AND b.use_t_day = '') OR (b.use_f_day <= '"+ymd+"' AND b.use_t_day >= '"+ymd+"')) \n");
				sql.append(" "+wSql+"                                                                           \n");
				logger.debug(sql.toString());
				Connection conn = null;
				Statement stmt = null;
				ResultSet rs = null;
				try{
					conn=DBManager.getConnection("dreamdb");
					stmt=conn.createStatement();
					rs=stmt.executeQuery(sql.toString());
					shopAdConfigCache.removeAll();
					while(rs.next()){
						if( DateUtils.getAdTimeZone(rs.getInt("usemoney"), rs.getInt("usedmoney"), rs.getString("ad_rhour"))==0 ){
							continue;
						}
						
						AdConfigData s=new AdConfigData();
						s.setSite_code(rs.getString("site_code"));
						s.setSite_code_tmp(rs.getString("site_code_tmp"));
						s.setUserid(rs.getString("userid"));
						//s.setMediano(rs.getString("media_no"));
						//s.setScriptno(rs.getString("script_no"));
						s.setSite_etc(rs.getString("site_etc"));
						s.setSvc_type(rs.getString("svc_type"));
						s.setImgname(rs.getString("imgname"));
						s.setMallnm(rs.getString("mallnm"));
						s.setRecom_ad(rs.getString("recom_ad"));
						s.setGubun(rs.getString("gubun"));
						
						//String skey=rs.getString("media_no") +"_"+rs.getString("script_no") +"_"+rs.getString("site_code") +"_"+ rs.getString("svc_type");
						String skey=rs.getString("site_code") +"_"+ rs.getString("svc_type") +"_"+ rs.getString("gubun");
						
						Element element=new Element(skey ,s);
						shopAdConfigCache.put(element);
						//logger.debug(adGubun+" : "+ skey +" Cached.");
					}
					logger.debug(adGubun+" : succ");
					logger.info("shopAdConfigCache reload end.");
				}catch(Exception ex){
					logger.error("loadshopAdConfig:"+adGubun+":"+ex);
				}finally{
					DBManager.close(rs);
					DBManager.close(stmt);
					DBManager.close(conn);
				}
			}else if(adGubun.equals("UM")){ 
				logger.info("urlMatchConfigCache reload start.");
				/*
				sql.append(" select a0.media_no,a0.script_no,a.site_code, b.userid, b.site_url,a.media_code, a.no,b.pop_style as pop,b.url_style \n");
				sql.append(" , b.svc_type, b.imgname   \n");
				sql.append(" from iadlink a0,iurlmatch a, iadsite b, admember c                                                                  \n");
				sql.append(" WHERE a.site_code=b.site_code and b.userid=c.userid                                                      \n");
				sql.append(" and a0.adyn='Y' AND a0.site_code=b.site_code \n");
				sql.append(" AND b.state='Y' AND b.gubun='UM'                                                                         \n");
				sql.append(" AND a.del_fg='N'                                                                          \n");
				sql.append(" AND ((b.use_f_day='' AND b.use_t_day = '') OR (b.use_f_day <= '"+ymd+"' AND b.use_t_day >= '"+ymd+"')) \n");
				sql.append(" "+wSql+"                                                                                          \n");
				sql.append(" AND c.point >= 1000                                                                                      \n");
				sql.append(" AND (b.usemoney > (b.usedmoney+b.usemoney/10) OR b.usemoney=0)                                                           \n");
				sql.append(" order by a.media_code                                                           \n");
				*/
				sql.append(" SELECT b.site_code, b.userid, b.gubun, a.media_code, b.url_style, a.no, INSTR(b.site_url,a.media_code)ismain	\n");
				sql.append(" , usemoney, usedmoney, ad_rhour, c.site_title, c.site_name, c.site_desc, c.imgname_logo,b.adweight	\n");
				sql.append(" , b.site_url, b.svc_type, site_etc, b.imgname, '' imgname2, '' imgname3, '' imgname4, '' imgname5                            \n");
				sql.append(" from iurlmatch a, iadsite b, admember c                                                                  \n");
				sql.append(" WHERE a.site_code=b.site_code and b.userid=c.userid                                                      \n");
				sql.append(" AND b.state='Y' AND b.gubun='UM'                                                                         \n");
				sql.append(" AND a.del_fg='N'																							\n");
				sql.append(" AND ((b.use_f_day='' AND b.use_t_day = '') OR (b.use_f_day <= '"+ymd+"' AND b.use_t_day >= '"+ymd+"'))		\n");
				sql.append(" "+wSql+"																									\n");
				sql.append(" AND c.point >= 1000																						\n");
				sql.append(" AND (b.usemoney > (b.usedmoney+b.usemoney/10) OR b.usemoney=0)												\n");
				sql.append(" UNION                                                                                                      \n");
				sql.append(" SELECT b.site_code, b.userid, b.gubun, homepi media_code, b.url_style, 0 NO, 0 ismain                                  \n");
				sql.append(" , usemoney, usedmoney, ad_rhour, c.site_title, c.site_name, c.site_desc, c.imgname_logo,b.adweight                            \n");
				sql.append(" , b.site_url, b.svc_type, site_etc, b.imgname, '' imgname2, '' imgname3, '' imgname4, '' imgname5                            \n");
				sql.append(" FROM iadsite b, admember c                                                                                 \n");
				sql.append(" WHERE b.userid=c.userid                                                                                    \n");
				sql.append(" AND b.state='Y'                                                                                            \n");
				sql.append(" AND b.gubun='ST'                                                                                           \n");
				sql.append(" AND c.point >= 1000                                                                                        \n");
				sql.append(" AND ((b.use_f_day='' AND b.use_t_day = '') OR (b.use_f_day <= '"+ymd+"' AND b.use_t_day >= '"+ymd+"'))		\n");
				sql.append(" "+wSql+"																									\n");
				sql.append(" AND (b.usemoney > (b.usedmoney+b.usemoney/10) OR b.usemoney=0)                                             \n");

				//sql.append(" order by rand()                                                           \n");
				logger.debug(sql.toString());
				Connection conn = null;
				Statement stmt = null;
				ResultSet rs = null;
				float icWeightSum =0;
				float brWeightSum =0;
				try{
					conn=DBManager.getConnection("dreamdb");
					stmt=conn.createStatement();
					rs=stmt.executeQuery(sql.toString());
					urlMatchConfigCache.removeAll();
					HashMap<String,PointData> adWeightMap=new HashMap<String,PointData>();
					while(rs.next()){
						if( DateUtils.getAdTimeZone(rs.getInt("usemoney"), rs.getInt("usedmoney"), rs.getString("ad_rhour"))==0 ){
							continue;
						}
						ArrayList list=null;
						AdConfigData s=new AdConfigData();
						s.setSite_code(rs.getString("site_code"));
						s.setUserid(rs.getString("userid"));
						s.setGubun(rs.getString("gubun"));
						s.setMedia_code(rs.getString("media_code"));
						s.setUrl_style(rs.getInt("url_style")+"");
						s.setKno(rs.getString("no"));
						s.setIsmain(rs.getString("ismain"));
						s.setSite_title(rs.getString("site_title"));
						s.setSite_name(rs.getString("site_name"));
						s.setSite_desc(rs.getString("site_desc"));
						s.setImgname_logo(rs.getString("imgname_logo"));
						s.setSite_url(rs.getString("site_url"));
						s.setSvc_type(rs.getString("svc_type"));
						s.setImgname(rs.getString("imgname"));
						s.setImgname2(rs.getString("imgname2"));
						s.setImgname3(rs.getString("imgname3"));
						s.setImgname4(rs.getString("imgname4"));
						s.setImgname5(rs.getString("imgname5"));
						//String skey=rs.getString("media_no") +"_"+rs.getString("script_no")+"_"+rs.getString("media_code");
						String skey=rs.getString("media_code");
						if( urlMatchConfigCache.isKeyInCache(skey) ){
							list=(ArrayList) urlMatchConfigCache.get(skey).getObjectValue();
							//logger.debug("((AdConfigData)list.get(0)).getMedia_code()="+((AdConfigData)list.get(0)).getMedia_code());
							list.add(s);
						}else{
							list=new ArrayList();
							list.add(s);
						}
						Element element=new Element(skey,list);
						urlMatchConfigCache.put(element);
						//logger.debug(adGubun+" : "+rs.getString("media_code")+" Cached.");
						String mKey=rs.getString("userid")+"_"+rs.getString("site_code");
						if(!adWeightMap.containsKey(mKey)){
							PointData p=new PointData();
							p.setUSERID(mKey);
							p.setSvcType(rs.getString("svc_type"));
							if(rs.getString("svc_type").equals("")){
								p.setAdWeight(rs.getFloat("adweight"));
								icWeightSum+=rs.getFloat("adweight");
							}else if(rs.getString("svc_type").equals("sky")){
								p.setAdWeight(rs.getFloat("adweight"));
								brWeightSum+=rs.getFloat("adweight");
							}
							adWeightMap.put(mKey, p);
							
							//logger.debug("icurlAdWeightList base user add="+mKey);
						}
					}
					Set keySet=adWeightMap.keySet();
					Iterator itor = keySet.iterator(); 
					ArrayList<PointData> icUrlAdWeightList=new ArrayList<PointData>();
					ArrayList<PointData> brUrlAdWeightList=new ArrayList<PointData>();
					while(itor.hasNext()) {
						String sKey=itor.next().toString();
						PointData p=adWeightMap.get(sKey);
						if(p.getSvcType().equals("")){
							int rate=Math.round((p.getAdWeight()/icWeightSum)*100);
							if(rate==0)rate=1;
							p.setAdWeightRate(rate);
							for(int i=0;i<rate;i++){
								icUrlAdWeightList.add(p);
							}
						}else if(p.getSvcType().equals("sky")){
							int rate=Math.round((p.getAdWeight()/brWeightSum)*1000);
							if(rate==0)rate=1;
							p.setAdWeightRate(rate);
							for(int i=0;i<rate;i++){
								brUrlAdWeightList.add(p);
							}
						}
						//logger.info("ic.br.urlAdWeightList base rate add="+sKey+","+p.getAdWeight()+","+p.getAdWeightRate());
					}
					Element icElement=new Element("icUrlAdWeightList",icUrlAdWeightList);
					Element brElement=new Element("brUrlAdWeightList",brUrlAdWeightList);
					urlMatchConfigCache.put(icElement);
					urlMatchConfigCache.put(brElement);
					logger.info("icUrlAdWeightList="+adWeightMap.size()+","+icUrlAdWeightList.size());
					logger.info("brUrlAdWeightList="+adWeightMap.size()+","+brUrlAdWeightList.size());
					logger.debug(adGubun+" : succ");
					logger.info("urlMatchConfigCache reload end.");

				}catch(Exception ex){
					logger.error("loadshopAdConfig:"+adGubun+":"+ex);
				}finally{
					DBManager.close(rs);
					DBManager.close(stmt);
					DBManager.close(conn);
				}
			}else if(adGubun.equals("KL")){
				logger.info("iKeywordMatchConfigCache reload start.");
				/*
				sql.append(" select a.media_no,a.script_no,b.site_code, b.userid, b.site_url,b.pop_style as pop, b.svc_type,e.imgname           \n");
				sql.append(" from iadlink a,iadsite b, admember c,iklsite e                                \n");
				sql.append(" WHERE  b.userid=c.userid and b.upseq = e.dscode and b.userid=e.userid \n");
				sql.append(" and a.adyn='Y' AND a.site_code=b.site_code \n");
				sql.append(" AND b.state='Y' and e.state='Y' and b.gubun='KL' \n");
				sql.append(" AND ((b.use_f_day='' AND b.use_t_day = '') OR (b.use_f_day <= '"+ymd+"' AND b.use_t_day >= '"+ymd+"')) \n");
				sql.append(" "+wSql+"                                                                                         \n");
				sql.append(" AND c.point >= 1000                                                                                      \n");
				sql.append(" AND (b.usemoney > (b.usedmoney+b.usemoney/10) OR b.usemoney=0)                                                           \n");
				*/
				sql.append(" select b.site_code, b.userid, b.site_url,b.pop_style as pop, b.svc_type,e.imgname,b.adweight           \n");
				sql.append(" ,usemoney,usedmoney,ad_rhour           \n");
				sql.append(" from iadsite b, admember c,iklsite e                                \n");
				sql.append(" WHERE b.userid=c.userid and b.upseq = e.dscode and b.userid=e.userid \n");
				sql.append(" AND b.state='Y' and e.state='Y' and b.gubun='KL' \n");
				sql.append(" AND ((b.use_f_day='' AND b.use_t_day = '') OR (b.use_f_day <= '"+ymd+"' AND b.use_t_day >= '"+ymd+"')) \n");
				sql.append(" "+wSql+"                                                                                         \n");
				sql.append(" AND c.point >= 1000                                                                                      \n");
				sql.append(" AND (b.usemoney > (b.usedmoney+b.usemoney/10) OR b.usemoney=0)                                                           \n");
				logger.debug(sql.toString());
				Connection conn = null;
				Statement stmt = null;
				ResultSet rs = null;
				float icWeightSum =0;
				float brWeightSum =0;
				try{
					conn=DBManager.getConnection("dreamdb");
					stmt=conn.createStatement();
					rs=stmt.executeQuery(sql.toString());
					iKeywordMatchConfigCache.removeAll();
					HashMap<String,PointData> adWeightMap=new HashMap<String,PointData>();
					while(rs.next()){
						if( DateUtils.getAdTimeZone(rs.getInt("usemoney"), rs.getInt("usedmoney"), rs.getString("ad_rhour"))==0 ){
							continue;
						}
						
						AdConfigData s=new AdConfigData();
						s.setSite_code(rs.getString("site_code"));
						s.setUserid(rs.getString("userid"));
						//s.setMediano(rs.getString("media_no"));
						//s.setScriptno(rs.getString("script_no"));
						s.setSite_url(rs.getString("site_url"));
						s.setPop(rs.getString("pop"));
						s.setSvc_type(rs.getString("svc_type"));
						s.setImgname( rs.getString("imgname"));
						//String skey=rs.getString("media_no") +"_"+rs.getString("script_no")+"_"+rs.getString("site_code");
						String skey=rs.getString("site_code");
						Element element=new Element(skey,s);
						iKeywordMatchConfigCache.put(element);
						//logger.debug(adGubun+" : "+rs.getString("site_code")+" Cached.");
						String mKey=rs.getString("userid")+"_"+rs.getString("site_code");
						if(!adWeightMap.containsKey(mKey)){
							PointData p=new PointData();
							p.setUSERID(mKey);
							p.setSvcType(rs.getString("svc_type"));
							if(rs.getString("svc_type").equals("")){
								p.setAdWeight(rs.getFloat("adweight"));
								icWeightSum+=rs.getFloat("adweight");
							}else if(rs.getString("svc_type").equals("sky")){
								p.setAdWeight(rs.getFloat("adweight"));
								brWeightSum+=rs.getFloat("adweight");
							}
							adWeightMap.put(mKey, p);
							
							//logger.debug("icbaseAdWeightList base user add="+mKey);
						}
					}
					Set keySet=adWeightMap.keySet();
					Iterator itor = keySet.iterator(); 
					ArrayList<PointData> icKwAdWeightList=new ArrayList<PointData>();
					ArrayList<PointData> brKwAdWeightList=new ArrayList<PointData>();
					while(itor.hasNext()) {
						String sKey=itor.next().toString();
						PointData p=adWeightMap.get(sKey);
						if(p.getSvcType().equals("")){
							int rate=Math.round((p.getAdWeight()/icWeightSum)*1000);
							if(rate==0)rate=1;
							p.setAdWeightRate(rate);
							for(int i=0;i<rate;i++){
								icKwAdWeightList.add(p);
							}
							if(rate==0)logger.debug("icWeightSum="+icWeightSum+", rate=0 "+p.toString());
						}else if(p.getSvcType().equals("sky")){
							int rate=Math.round((p.getAdWeight()/brWeightSum)*1000);
							if(rate==0)rate=1;
							p.setAdWeightRate(rate);
							for(int i=0;i<rate;i++){
								brKwAdWeightList.add(p);
							}
							if(rate==0)logger.debug("icWeightSum="+icWeightSum+", rate=0 "+p.toString());
						}
						//logger.info("ic.br.kwAdWeightList base rate add="+sKey+","+p.getAdWeight()+","+p.getAdWeightRate());
					}
					Element icElement=new Element("icKwAdWeightList",icKwAdWeightList);
					Element brElement=new Element("brKwAdWeightList",brKwAdWeightList);
					iKeywordMatchConfigCache.put(icElement);
					iKeywordMatchConfigCache.put(brElement);
					logger.info("icKwAdWeightList="+adWeightMap.size()+","+icKwAdWeightList.size());
					logger.info("brKwAdWeightList="+adWeightMap.size()+","+brKwAdWeightList.size());
					logger.debug(adGubun+" : succ");
					logger.info("iKeywordMatchConfigCache reload end.");
				}catch(Exception ex){
					logger.error("loadshopAdConfig:"+adGubun+":"+ex);
				}finally{
					DBManager.close(rs);
					DBManager.close(stmt);
					DBManager.close(conn);
				}
			}else if(adGubun.equals("AD")){	
				logger.info("baseAdConfigCache reload start.");
				sql.append(" select a.site_code,a.media_no,a.script_no,b.userid, b.site_url,b.pop_style as pop,b.url_style,b.adweight           \n");
				sql.append(" , concat(concat(a.media_no,'_'),a.script_no) as media_code, b.svc_type, b.imgname, b.dispo_age, b.dispo_sex \n");
				sql.append(" , usemoney,usedmoney,ad_rhour \n");
				sql.append(" from iadlink a, iadsite b, admember c                                \n");
				sql.append(" where a.adyn='Y' and a.site_code=b.site_code and b.userid=c.userid                         \n");
				sql.append(" AND b.state='Y' and b.gubun='AD' \n");
				sql.append(" AND ((b.use_f_day='' AND b.use_t_day = '') OR (b.use_f_day <= '"+ymd+"' AND b.use_t_day >= '"+ymd+"')) \n");
				sql.append(" "+wSql+"                                                                                     \n");
				sql.append(" AND c.point >= 1000                                                                                      \n");
				sql.append(" AND (b.usemoney > (b.usedmoney+b.usemoney/10) OR b.usemoney=0)                                                           \n");
				sql.append(" order by concat(concat(a.media_no,'_'),a.script_no)                                                           \n");
				logger.debug(sql.toString());
				Connection conn = null;
				Statement stmt = null;
				ResultSet rs = null;
				float icWeightSum =0;
				float brWeightSum =0;
				try{
					conn=DBManager.getConnection("dreamdb");
					stmt=conn.createStatement();
					rs=stmt.executeQuery(sql.toString());
					String tmpS="";
					baseAdConfigCache.removeAll();
					HashMap<String,PointData> adWeightMap=new HashMap<String,PointData>();
					while(rs.next()){
						if( DateUtils.getAdTimeZone(rs.getInt("usemoney"), rs.getInt("usedmoney"), rs.getString("ad_rhour"))==0 ){
							continue;
						}
						
						ArrayList list=null;
						AdConfigData s=new AdConfigData();
						s.setSite_code(rs.getString("site_code"));
						s.setUserid(rs.getString("userid"));
						s.setSite_url(rs.getString("site_url"));
						s.setPop(rs.getString("pop"));
						s.setUrl_style(rs.getString("url_style"));
						s.setSvc_type(rs.getString("svc_type"));
						s.setImgname(rs.getString("imgname"));
						s.setDispo_age(rs.getString("dispo_age"));
						s.setDispo_sex(rs.getString("dispo_sex"));
						if(tmpS.equals(rs.getString("media_code"))){
							list=(ArrayList) baseAdConfigCache.get(rs.getString("media_code")).getObjectValue();
							list.add(s);
						}else{
							list=new ArrayList();
							list.add(s);
						}
						Element element=new Element(rs.getString("media_code"),list);
						baseAdConfigCache.put(element);
						//logger.debug(adGubun+" : "+rs.getString("media_code")+" Cached.");
						tmpS=rs.getString("media_code");
						String mKey=rs.getString("userid")+"_"+rs.getString("site_code");
						if(!adWeightMap.containsKey(mKey)){
							PointData p=new PointData();
							p.setUSERID(mKey);
							p.setSvcType(rs.getString("svc_type"));
							if(rs.getString("svc_type").equals("")){
								p.setAdWeight(rs.getFloat("adweight"));
								icWeightSum+=rs.getFloat("adweight");
							}else if(rs.getString("svc_type").equals("sky")){
								p.setAdWeight(rs.getFloat("adweight"));
								brWeightSum+=rs.getFloat("adweight");
							}
							adWeightMap.put(mKey, p);
							
							//logger.debug("icbaseAdWeightList base user add="+mKey);
						}
					}
					Set keySet=adWeightMap.keySet();
					Iterator itor = keySet.iterator(); 
					ArrayList<PointData> icBaseAdWeightList=new ArrayList<PointData>();
					ArrayList<PointData> brBaseAdWeightList=new ArrayList<PointData>();
					while(itor.hasNext()) {
						String sKey=itor.next().toString();
						PointData p=adWeightMap.get(sKey);
						if(p.getSvcType().equals("")){
							int rate=Math.round((p.getAdWeight()/icWeightSum)*1000);
							if(rate==0)rate=1;
							p.setAdWeightRate(rate);
							for(int i=0;i<rate;i++){
								icBaseAdWeightList.add(p);
							}
						}else if(p.getSvcType().equals("sky")){
							int rate=Math.round((p.getAdWeight()/brWeightSum)*1000);
							if(rate==0)rate=1;
							p.setAdWeightRate(rate);
							for(int i=0;i<rate;i++){
								brBaseAdWeightList.add(p);
							}
						}
						//logger.info("ic.br.baseAdWeightList base rate add="+sKey+","+p.getAdWeight()+","+p.getAdWeightRate());
					}
					Element icElement=new Element("icBaseAdWeightList",icBaseAdWeightList);
					Element brElement=new Element("brBaseAdWeightList",brBaseAdWeightList);
					baseAdConfigCache.put(icElement);
					baseAdConfigCache.put(brElement);
					logger.info("icbaseAdWeightList="+adWeightMap.size()+","+icBaseAdWeightList.size());
					logger.info("brbaseAdWeightList="+adWeightMap.size()+","+brBaseAdWeightList.size());
					logger.debug(adGubun+" : succ");
					logger.info("icbaseAdConfigCache reload end.");
				}catch(Exception ex){
					logger.error("loadshopAdConfig:"+adGubun+":"+ex);
				}finally{
					DBManager.close(rs);
					DBManager.close(stmt);
					DBManager.close(conn);
				}
			}
			
		} catch(Exception e) {
			logger.error(e);
			RFServlet.instance.dbStatus=false;
		}
	}
	
	public void loadMediaSite(){
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		StringBuffer sql=new StringBuffer();
		ArrayList list=new ArrayList();
		try {
			logger.info("mediaSiteCache reload start.");
			sql.append(" select no,state from media_site \n");
			logger.debug(sql.toString());
			conn=DBManager.getConnection("dreamdb");
			stmt=conn.createStatement();
			rs=stmt.executeQuery(sql.toString());
			mediaSiteCache.removeAll();
			while(rs.next()){
				Element element=new Element(rs.getString("no"),rs.getString("state"));
				mediaSiteCache.put(element);
				//logger.debug("loadMediaSite : "+rs.getString("no")+" Cached.");
			}
			logger.debug("loadMediaSite : succ");
			logger.info("mediaSiteCache reload end.");
		} catch(Exception e) {
			logger.error("loadMediaSite:"+e);
			RFServlet.instance.dbStatus=false;
		} finally {
			try{if(rs !=null) rs.close();} catch(Exception e){};
			try{if(stmt !=null) stmt.close();} catch(Exception e){};
			try{if(conn !=null) conn.close();} catch(Exception e){};
		}
	}
	public String getMediaSiteStatus(String mediaSiteNo){
		String result="";
		try{
			Element element=mediaSiteCache.get(mediaSiteNo);
			if(element !=null){
				result=element.getObjectValue().toString();
				logger.debug("load mediaSite Status From Cache: key="+mediaSiteNo+",status="+result);
			}
		}catch(Exception e){
			logger.error("getMediaSiteStatus:"+mediaSiteNo+":"+e.getMessage());
			result="N";
		}
		return result;
	}
	public String getMediaCode(String mediaCode){
		String result="";
		try{
			Element element=mediaCodeCache.get(mediaCode);
			if(element !=null){
				result=element.getObjectValue().toString();
				logger.debug("load getMediaCode From Cache: key="+mediaCode+",mediaCode="+result);
			}
		}catch(Exception e){
			logger.error("getMediaCode:"+mediaCode+":"+e.getMessage());
		}
		return result;
	}
	public void loadMediaCode(){
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		StringBuffer sql=new StringBuffer();
		ArrayList list=new ArrayList();
		try {
//			sql.append(" select a.media_code                     \n");
//			sql.append(" from iurlmatch a, iadsite b, admember c                                         \n");
//			sql.append(" where a.site_code=b.site_code and b.userid=c.userid \n");
//			sql.append(" and b.state='Y' and b.gubun='UM'                                                \n");
			logger.info("mediaCodeCache reload start.");
			sql.append("SELECT DISTINCT LOWER(media_code)media_code FROM ( \n");
			sql.append("	SELECT a.media_code \n");
			sql.append("	FROM urlmatch a, adsite b \n"); 
			sql.append("	WHERE a.site_code=b.site_code \n");
			sql.append("	AND b.state='Y' AND b.gubun='UM'  \n");
			sql.append("    AND a.del_fg='N'                  \n");
			sql.append("	UNION \n");
			sql.append("	SELECT a.media_code \n");
			sql.append("	FROM iurlmatch a, iadsite b \n"); 
			sql.append("	WHERE a.site_code=b.site_code \n");
			sql.append("	AND b.state='Y' AND b.gubun='UM'  \n");
			sql.append("    AND a.del_fg='N'                  \n");
			sql.append("	union select 'weeset.co.kr' media_code  \n");	
			sql.append("	union select 'enliple.com' media_code  \n");	 
			sql.append(")t; \n");
			logger.debug(sql.toString());
			conn=DBManager.getConnection("dreamdb");
			stmt=conn.createStatement();
			rs=stmt.executeQuery(sql.toString());
			mediaCodeCache.removeAll();
			while(rs.next()){
				Element element=new Element(rs.getString("media_code"),rs.getString("media_code"));
				mediaCodeCache.put(element);
				//logger.debug("loadMediaCode : "+rs.getString("media_code")+" Cached.");
			}
			logger.info("mediaCodeCache reload end.");
			logger.debug("loadMediaCode : succ");
		} catch(Exception e) {
			logger.error("loadMediaCode:"+e);
			RFServlet.instance.dbStatus=false;
		} finally {
			try{if(rs !=null) rs.close();} catch(Exception e){};
			try{if(stmt !=null) stmt.close();} catch(Exception e){};
			try{if(conn !=null) conn.close();} catch(Exception e){};
		}
	}
	public SiteCodeData getIadSiteStatus(String siteCode){
		SiteCodeData result=null;
		try{
			Element element=iAdSiteCache.get(siteCode);
			if(element !=null){
				result=(SiteCodeData) element.getObjectValue();
				logger.debug("load getIadSiteStatus Status From Cache: key="+siteCode+",status="+result.getSite_code());
			}
		}catch(Exception e){
			logger.error("getIadSiteStatus:"+siteCode+":"+e.getMessage());
		}
		return result;
	}
	public void loadIadSiteCode(){
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		StringBuffer sql=new StringBuffer();
		try {
			logger.info("iAdSiteCache reload start.");
			sql.append(" select site_code, site_code_s, b.userid, state, sales_url, sales_prdno, site_etc ");
			sql.append(" , b.dispo_age, b.dispo_sex \n");
			sql.append(" from iadsite a left join admember b \n");
			sql.append(" on a.userid=b.userid \n");
			logger.debug(sql.toString());
			conn=DBManager.getConnection("dreamdb");
			stmt=conn.createStatement();
			rs=stmt.executeQuery(sql.toString());
			iAdSiteCache.removeAll();
			while(rs.next()){
				SiteCodeData sd=new SiteCodeData();
				sd.setSite_code(rs.getString("site_code"));
				sd.setSite_code_s(rs.getString("site_code_s"));
				sd.setUserid(rs.getString("userid"));
				sd.setState(rs.getString("state"));
				sd.setSales_url(rs.getString("sales_url"));
				sd.setSales_prdno(rs.getString("sales_prdno"));
				sd.setSite_etc( rs.getString("site_etc") );
				sd.setDispo_age(rs.getString("dispo_age"));
				sd.setDispo_sex(rs.getString("dispo_sex"));
				
				Element element=new Element(rs.getString("site_code"),sd);
				iAdSiteCache.put(element);
				//logger.debug("loadIadSiteCode : "+rs.getString("site_code")+" Cached.");
			}
			logger.info("iAdSiteCache reload end.");
			logger.debug("loadIadSiteCode : succ");
		} catch(Exception e) {
			logger.error("loadIadSiteCode:"+e);
			RFServlet.instance.dbStatus=false;
		} finally {
			try{if(rs !=null) rs.close();} catch(Exception e){};
			try{if(stmt !=null) stmt.close();} catch(Exception e){};
			try{if(conn !=null) conn.close();} catch(Exception e){};
		}
	}
	public void loadAdSiteCode(){
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		StringBuffer sql=new StringBuffer();
		try {
			logger.info("adSiteCache reload start.");
			sql.append(" SELECT a.site_code,a.userid, a.usemoney, a.usedmoney, a.ad_rhour, c.point, a.state, IFNULL(b.site_code,'') site_code_s \n");
			sql.append(" , a.site_etc, a.sales_url, a.sales_prdno, c.dispo_sex, c.dispo_age \n");
			sql.append(" FROM adsite a LEFT JOIN iadsite b \n");
			sql.append(" ON a.site_code= b.site_code_s LEFT JOIN admember c \n");
			sql.append(" ON a.userid=c.userid \n");
			logger.debug(sql.toString());
			conn=DBManager.getConnection("dreamdb");
			stmt=conn.createStatement();
			rs=stmt.executeQuery(sql.toString());
			adSiteCache.removeAll();
			while(rs.next()){
				SiteCodeData sd= new SiteCodeData();
				sd.setSite_code(rs.getString("site_code"));
				sd.setState(rs.getString("state"));
				sd.setSite_code_s(rs.getString("site_code_s"));
				sd.setUserid(rs.getString("userid"));
				sd.setSales_url(rs.getString("sales_url"));
				sd.setSales_prdno(rs.getString("sales_prdno"));
				sd.setSite_etc(rs.getString("site_etc"));
				sd.setDispo_age(rs.getString("dispo_age"));
				sd.setDispo_sex(rs.getString("dispo_sex"));
				sd.setUsemoney(rs.getInt("usemoney"));
				sd.setUsedmoney(rs.getInt("usedmoney"));
				sd.setAd_rhour(rs.getString("ad_rhour"));
				sd.setPoint(rs.getInt("point"));
				
				Element element=new Element(rs.getString("site_code"),sd);
				adSiteCache.put(element);
				//logger.debug("loadAdSiteCode : "+rs.getString("site_code")+" Cached.");
			}
			logger.info("adSiteCache reload end.");
			logger.debug("loadAdSiteCode : succ");
		} catch(Exception e) {
			logger.error("loadAdSiteCode:"+e);
			RFServlet.instance.dbStatus=false;
		} finally {
			try{if(rs !=null) rs.close();} catch(Exception e){};
			try{if(stmt !=null) stmt.close();} catch(Exception e){};
			try{if(conn !=null) conn.close();} catch(Exception e){};
		}
	}
	public AdConfigData getBaseAdConfig(String media_code, String product, String dispo, String cho_sitecode){
		AdConfigData c=null;
		int limitCnt=0;
		try{
			logger.debug("getBaseAdConfig() "+ media_code );
			if( baseAdConfigCache.isKeyInCache( media_code ) ){
				ArrayList list = (ArrayList) ((ArrayList) baseAdConfigCache.get(media_code).getObjectValue()).clone();
				
				if( list!=null && list.size()>0 ){
					logger.debug("getBaseAdConfig() list.size()"+ list.size() );
					HashMap<String,String> overlapUID=new HashMap<String,String>();
					ArrayList baseAdWeightList=null;
					if(product.equals("")){
						baseAdWeightList=(ArrayList) baseAdConfigCache.get("icBaseAdWeightList").getObjectValue();
					}else if(product.equals("sky")){
						baseAdWeightList=(ArrayList) baseAdConfigCache.get("brBaseAdWeightList").getObjectValue();
					}
					while(c==null && list.size()>0 && limitCnt<baseAdWeightList.size()){
						limitCnt++;
						int rInt=new Random().nextInt(baseAdWeightList.size());
						PointData p=(PointData) baseAdWeightList.get(rInt);
						if(overlapUID.containsKey(p.getUSERID())){ 
							//baseAdWeightList.remove(rInt);
							continue;
						}
						for(int i=0;i<list.size();i++){
							try{
								AdConfigData tmp=((AdConfigData)list.get(i)).clone();
								String tmpKey=tmp.getUserid()+"_"+tmp.getSite_code();
								if(overlapUID.containsKey(tmpKey)){
									logger.debug("getBaceAdConfig overlapUID:"+tmp.getUserid()+" continued.");
									continue;
								}
								if(p.getUSERID().equals(tmpKey)){
								}else{
									logger.debug("getBaceAdConfig userid diff:"+","+limitCnt+","+list.size()+","+p.getUSERID()+","+tmp.getUserid());
									continue;
								}
								list.remove(i);
								i--;
								if( tmp.getSvc_type().equals(product) ){
									if(!cho_sitecode.equals("")){
										if( tmp.getSite_code().equals(cho_sitecode)){
											c=tmp.clone();
											break;
										}
									}else{
										if( dispo.equals("") ){
											logger.debug("getBaseAdConfig() dispo.equals('') "+ tmp.toString() );
											c=tmp.clone();
											break;
										}else{
											String []tmp_dispo= dispo.split("#");
											logger.debug("tmp_dispo.length "+ tmp_dispo.length );
											
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
			
												if( tmp.getDispo_sex().indexOf( sex )>-1 ){
													String age="";
													for( int j=0; j<tmp_dispo.length; j++ ) {// chk age
														try{
															age = tmp_dispo[j].substring(0, tmp_dispo[j].indexOf("=") );
														}catch(Exception e){
															age = "a";
														}
														logger.debug("getNormalBaceAdConfig[810 chking "+ tmp_dispo[j] );
														
														if( tmp.getDispo_age().indexOf( age )>-1 ){
															logger.debug("getNormalBaceAdConfig[810 chking age is "+ age);
															c=tmp.clone();
															break;
														}
													}
												}
											}
										}
									}
									
									if(c!=null) break;
								}
								overlapUID.put(p.getUSERID(),"");
							}catch(Exception e){
								logger.error("baseAdConfigCache loop:"+e);
							}
						} // for end
					} // while end
				}
			}
			if(c!=null){
				logger.debug("load getBaseAdConfig Status From Cache: key="+media_code);
			}
			
			
		}catch(Exception e){
			logger.error("getBaseAdConfig:"+media_code+":"+e);
		}
		//logger.info("getBaseAdConfig:limitCnt="+limitCnt);
		return c;
	}
	public ArrayList getNormalkeywordMatchList(String us,String s,String media_code,HashMap hm, String chk_ck,String igb, String keyIp){
		ArrayList rt_list=null;
		RFData c=null;
		AdConfigData cfg=null;
		try{
			if(noMatchKeywordCache.isKeyInCache(media_code+"^^^n")){
				logger.debug(media_code);
				return null;
			}
			
			try{
				ArrayList list=null;;
				if(normalKeywordDataCache.isKeyInCache(media_code)){
					list=(ArrayList) normalKeywordDataCache.get(media_code).getObjectValue();
					if(list!=null && list.size()==0) list=null;
				}
				if(list==null){
					HashMap khm=RFServlet.instance.dataMapper.getNormalKeywordDataFromKeywordDB(media_code);
					if(khm!=null && khm.size()>0){
						logger.debug("adbn keyword khm.size="+ khm.size());
						Iterator it = khm.keySet().iterator();
						list=new ArrayList();
						while(it.hasNext()){
							list.add(khm.get(it.next().toString()));
						}
						Element newElement=new Element(media_code,list);
						normalKeywordDataCache.put(newElement);
					}else{
						Element newElement=new Element(media_code+"^^^n","");
						noMatchKeywordCache.put(newElement);
					}
				}
				if(list!=null && list.size()>0){
					logger.debug("adinfo[899] list.size()"+ list.size());
					ArrayList<Integer> rdmList=StringUtils.getRandList(list.size());
					rt_list= new ArrayList();
					
					for(int rcnt=0;rcnt<rdmList.size();rcnt++){
						c=(RFData) list.get(rdmList.get(rcnt));
						if(c!=null){
							Element kElement=normalKeywordMatchConfigCache.get(c.getSITE_CODE());
							if(kElement!=null && hm!=null ){
								String key= "KL_"+c.getKno();
								if( hm.containsKey(key) ){
									continue;
								}
								if( chk_ck.indexOf(key +"=")>-1 ){
									continue;
								}
								//logger.debug(c.getInfo(hm +" "));
								
								String adLinkStatus=RFServlet.instance.adInfoCache.getAdLinkData(c.getSITE_CODE(),"",us,s,"adlink","");
								if(adLinkStatus!=null && adLinkStatus.equals("Y")){
									
									AdConfigData tmpCfg=(AdConfigData) kElement.getObjectValue();
									//logger.debug( tmpCfg.getInfo("getNormalkeywordMatchList[1447 tmpCfg.info") );
									
									if(igb.equals("2") && tmpCfg.getImgname2().length()>0){
										tmpCfg.setSite_url(c.getSITE_URL());
										tmpCfg.setKno(c.getKno());
										rt_list.add(tmpCfg);
									}else if(igb.equals("3") && tmpCfg.getImgname3().length()>0){
										tmpCfg.setSite_url(c.getSITE_URL());
										tmpCfg.setKno(c.getKno());
										rt_list.add(tmpCfg);
									}else if(igb.equals("4") && tmpCfg.getImgname4().length()>0){
										tmpCfg.setSite_url(c.getSITE_URL());
										tmpCfg.setKno(c.getKno());
										rt_list.add(tmpCfg);
									}else if(igb.equals("5") && tmpCfg.getImgname5().length()>0){
										tmpCfg.setSite_url(c.getSITE_URL());
										tmpCfg.setKno(c.getKno());
										rt_list.add(tmpCfg);
									}else if(tmpCfg.getImgname().length()>0){
										tmpCfg.setSite_url(c.getSITE_URL());
										tmpCfg.setKno(c.getKno());
										rt_list.add(tmpCfg);
									}
								}
							}else{
								logger.debug("±¤°í ½ÂÀÎ Á¤º¸ ½ÇÆÐ! : "+c.getSITE_CODE()+"_"+us+"_"+s+"_"+"adlink");
							}
						}
					}
					logger.debug("load getNormalkeywordMatchAdConfig Status From Cache: succ");
				}else{}
			}catch(Exception ex){
				logger.error("getNormalkeywordMatchList:"+ex);
			}
		}catch(Exception e){
			logger.error("getNormalkeywordMatchAdConfig3:"+media_code+":"+e);
		}
		return rt_list;
	}
	/* php ad conversion.. */
	public ArrayList getNormalkeywordMatchAdConfig(String us,String s,String media_code,int nCnt,String types){
		ArrayList rtn=new ArrayList();
		try{
			if(noMatchKeywordCache.isKeyInCache(media_code+"^^^n")){
				logger.debug(media_code);
				return null;
			}
			int limitCnt=0;
			try{
				ArrayList list=null;
				if(normalKeywordDataCache.isKeyInCache(media_code)){
					list=(ArrayList) ((ArrayList) normalKeywordDataCache.get(media_code).getObjectValue()).clone();
					if(list!=null && list.size()==0) list=null;
				}
				if(list==null){
					HashMap khm=RFServlet.instance.dataMapper.getNormalKeywordDataFromKeywordDB(media_code);
					if(khm!=null && khm.size()>0){
						logger.debug("adbnphp keyword khm.size="+ khm.size());
						Iterator it = khm.keySet().iterator();
						ArrayList clist=new ArrayList();
						while(it.hasNext()){
							clist.add(khm.get(it.next().toString()));
						}
						Element newElement=new Element(media_code,clist);
						normalKeywordDataCache.put(newElement);
						list=(ArrayList) clist.clone();
					}else{
						Element newElement=new Element(media_code+"^^^n","");
						noMatchKeywordCache.put(newElement);
					}
				}
				if(list!=null && list.size()>0){
					logger.debug("adinfo[899] list.size():"+ list.size());
					//ArrayList<Integer> rdmList=StringUtils.getRandList(list.size());
					int incnt=0;
					
					ArrayList kwAdWeightList=(ArrayList) normalKeywordMatchConfigCache.get("kwAdWeightList").getObjectValue();
					if(kwAdWeightList==null || kwAdWeightList.size()==0){
						logger.debug("kwAdWeightList is null");
						return null;
					}
					HashMap<String,String> overlapUID=new HashMap<String,String>();
					while(list.size()>0 && rtn.size()<nCnt && limitCnt<kwAdWeightList.size()){
						
						if(list.size()==0){
							logger.debug("aaaaaaabbbb");
							break;
						}
						int rInt=new Random().nextInt(kwAdWeightList.size());
						limitCnt++;
						PointData p=(PointData) kwAdWeightList.get(rInt);
						if(overlapUID.containsKey(p.getUSERID())){ 
							//kwAdWeightList.remove(rInt);
							continue;
						}
						for(int rcnt=0;rcnt<list.size();rcnt++){
							RFData c=((RFData) list.get(rcnt)).clone();
							SiteCodeData sd=getAdSiteStatus(c.getSITE_CODE());
							if(sd==null)	continue;
							if(overlapUID.containsKey(sd.getUserid()+"_"+sd.getSite_code())){
								//logger.debug("overlapUID contained.:"+sd.getUserid()+"_"+sd.getSite_code()+" continued.");
								continue;
							}else{
								//logger.debug("overlapUID nocontained.:"+sd.getUserid()+"_"+sd.getSite_code()+" continued.");
							}
							if(p.getUSERID().equals(sd.getUserid()+"_"+sd.getSite_code())){
								//logger.debug("userid mached:"+p.getUSERID()+","+sd.getUserid()+"_"+sd.getSite_code());
							}else{
								//logger.debug("userid diff:"+p.getUSERID()+","+sd.getUserid()+"_"+sd.getSite_code()+" continued.");
								continue;
							}
							list.remove(rcnt);
							rcnt--;
							if(c!=null){
								logger.debug("±¤°í Ã£À½1,types="+types);
								Element kElement=normalKeywordMatchConfigCache.get(c.getSITE_CODE());
								String adLinkStatus=RFServlet.instance.adInfoCache.getAdLinkData(c.getSITE_CODE(),"",us,s,"adlink","");
								logger.debug("±¤°í Ã£À½1-1,adLinkStatus="+adLinkStatus);
								if(adLinkStatus!=null && adLinkStatus.equals("Y")){
									AdConfigData tmpCfg=((AdConfigData) kElement.getObjectValue()).clone();
									logger.debug("types="+types+",tmpCfg.getStateM()="+tmpCfg.getStateM()+",tmpCfg.getBanner_path1()="+tmpCfg.getBanner_path1());
									if( types.equals("mobile") ){
										if( tmpCfg.getStateM().equals("Y") && tmpCfg.getBanner_path1()!=null && !tmpCfg.getBanner_path1().equals("") ){
											tmpCfg.setSite_url(c.getSITE_URL());
											tmpCfg.setKno(c.getKno());
											rtn.add(tmpCfg);
											//logger.debug("getNormalKwAdConfig rate1:"+limitCnt+","+p.getUSERID()+","+p.getAdWeight()+","+p.getAdWeightRate());
										}else if( tmpCfg.getStateM().equals("Y") && tmpCfg.getSite_desc()!=null && !tmpCfg.getSite_desc().equals("") ){
											tmpCfg.setSite_url(c.getSITE_URL());
											tmpCfg.setKno(c.getKno());
											rtn.add(tmpCfg);
										}
									}else{
										logger.debug("±¤°í Ã£À½2:"+adLinkStatus);
										logger.debug("getNormalkeywordMatchAdConfig[1798] tmpCfg="+ tmpCfg.toString());
										tmpCfg.setSite_url(c.getSITE_URL());
										tmpCfg.setKno(c.getKno());
										rtn.add(tmpCfg);
										//logger.debug("getNormalKwAdConfig rate2:"+limitCnt+","+p.getUSERID()+","+p.getAdWeight()+","+p.getAdWeightRate()+",list size="+list.size());
									}
								}else{
									logger.debug("±¤°í Ã£À½3:"+adLinkStatus);
									logger.debug("±¤°í ½ÂÀÎ Á¤º¸ ½ÇÆÐ! : "+c.getSITE_CODE()+"_"+us+"_"+s+"_"+"adlink");
								}
							}	//if(c==null end
						} // for end
						overlapUID.put(p.getUSERID(),"");
					}	//while end
					logger.debug("load getNormalkeywordMatchAdConfig Status From Cache: succ,"+limitCnt);
				}else{}	// if(list==null end
			}catch(Exception ex){
				logger.error("getNormalkeywordMatchAdConfig1:"+ex);
			}
			//logger.info("getNormalkeywordMatchAdConfig3:limitCnt="+limitCnt);
		}catch(Exception e){
			logger.error("getNormalkeywordMatchAdConfig3:"+media_code+":"+e);
		}
		return rtn;
	}
	public AdConfigData getNormalkeywordMatchAdConfig(String us,String s,String media_code,HashMap hm, String hm1,String igb,String types){
		RFData c=null;
		AdConfigData cfg=null;
		int limitCnt=0;
		try{
			if(noMatchKeywordCache.isKeyInCache(media_code+"^^^n")){
				logger.debug(media_code);
				return null;
			}
			try{
				String key="BNKL_"+java.net.URLEncoder.encode(media_code,"euc-kr");
				
				if( hm.containsKey(key) ){
					//if( ++incnt > 2 ){
						logger.debug("getNormalkeywordMatchAdConfig KL ¸ðµç»óÇ° ³ëÃâµÊ");
						return null;
					//}
				}
				
				ArrayList list=null;;
				if(normalKeywordDataCache.isKeyInCache(media_code)){
					list=(ArrayList) ((ArrayList) normalKeywordDataCache.get(media_code).getObjectValue()).clone();
					if(list!=null && list.size()==0) list=null;
				}
				if(list==null){
					HashMap khm=RFServlet.instance.dataMapper.getNormalKeywordDataFromKeywordDB(media_code);
					if(khm!=null && khm.size()>0){
						logger.debug("adbn keyword khm.size="+ khm.size());
						Iterator it = khm.keySet().iterator();
						ArrayList clist=new ArrayList();
						while(it.hasNext()){
							clist.add(khm.get(it.next().toString()));
						}
						Element newElement=new Element(media_code,clist);
						normalKeywordDataCache.put(newElement);
						list=(ArrayList) clist.clone();
					}else{
						Element newElement=new Element(media_code+"^^^n","");
						noMatchKeywordCache.put(newElement);
					}
				}
				if(list!=null && list.size()>0){
					logger.debug("adinfo[899] list.size()"+ list.size());
					//ArrayList<Integer> rdmList=StringUtils.getRandList(list.size());
					ArrayList kwAdWeightList=(ArrayList) normalKeywordMatchConfigCache.get("kwAdWeightList").getObjectValue();
					HashMap<String,String> overlapUID=new HashMap<String,String>();
					while(cfg==null && list.size()>0 && limitCnt<kwAdWeightList.size()){
						limitCnt++;
						int rInt=new Random().nextInt(kwAdWeightList.size());
						PointData p=(PointData) kwAdWeightList.get(rInt);
						if(overlapUID.containsKey(p.getUSERID())){ 
							//kwAdWeightList.remove(rInt);
							continue;
						}
						for(int rcnt=0;rcnt<list.size();rcnt++){
							c=((RFData) list.get(rcnt)).clone();
							SiteCodeData sd=getAdSiteStatus(c.getSITE_CODE());
							if(sd==null)	continue;
							if(overlapUID.containsKey(sd.getUserid()+"_"+sd.getSite_code())){
								logger.debug("overlapUID contained.:"+sd.getUserid()+"_"+sd.getSite_code()+" continued.");
								continue;
							}else{
								logger.debug("overlapUID nocontained.:"+sd.getUserid()+"_"+sd.getSite_code()+" continued.");
							}
							if(p.getUSERID().equals(sd.getUserid()+"_"+sd.getSite_code())){
								logger.debug("userid mached:"+p.getUSERID()+","+sd.getUserid()+"_"+sd.getSite_code());
							}else{
								logger.debug("userid diff:"+p.getUSERID()+","+sd.getUserid()+"_"+sd.getSite_code()+" continued.");
								continue;
							}
							list.remove(rcnt);
							rcnt--;
							if(c!=null){
								Element kElement=normalKeywordMatchConfigCache.get(c.getSITE_CODE());
								if(kElement!=null && hm!=null ){
									String adLinkStatus=RFServlet.instance.adInfoCache.getAdLinkData(c.getSITE_CODE(),"",us,s,"adlink","");
									if(adLinkStatus!=null && adLinkStatus.equals("Y")){
										AdConfigData tmpCfg=(AdConfigData) kElement.getObjectValue();
										logger.debug("getNormalkeywordMatchAdConfig[1798] tmpCfg="+ tmpCfg.toString());
										if( types.equals("mobile") ){
											if( tmpCfg.getStateM().equals("Y") && tmpCfg.getBanner_path1()!=null && !tmpCfg.getBanner_path1().equals("") ){
												tmpCfg.setSite_url(c.getSITE_URL());
												tmpCfg.setKno(c.getKno());
												cfg=tmpCfg;
												logger.debug("getNormalKwAdConfig rate1:"+limitCnt+","+p.getUSERID()+","+p.getAdWeight()+","+p.getAdWeightRate());
												break;
											}
										}else{
											logger.debug("cfg.getImgname()="+tmpCfg.getImgname()+","+tmpCfg.getImgname2()+","+tmpCfg.getImgname3()+","+tmpCfg.getImgname4()+","+tmpCfg.getImgname5()+",");
											if(igb.equals("1") && tmpCfg.getImgname().length()>0){
												tmpCfg.setSite_url(c.getSITE_URL());
												tmpCfg.setKno(c.getKno());
												cfg=tmpCfg;
												logger.debug("getNormalKwAdConfig rate2:"+limitCnt+","+p.getUSERID()+","+p.getAdWeight()+","+p.getAdWeightRate());
												break;
											}else if(igb.equals("2") && tmpCfg.getImgname2().length()>0){
												tmpCfg.setSite_url(c.getSITE_URL());
												tmpCfg.setKno(c.getKno());
												cfg=tmpCfg;
												logger.debug("getNormalKwAdConfig rate3:"+limitCnt+","+p.getUSERID()+","+p.getAdWeight()+","+p.getAdWeightRate());
												break;
											}else if(igb.equals("3") && tmpCfg.getImgname3().length()>0){
												tmpCfg.setSite_url(c.getSITE_URL());
												tmpCfg.setKno(c.getKno());
												cfg=tmpCfg;
												logger.debug("getNormalKwAdConfig rate4:"+limitCnt+","+p.getUSERID()+","+p.getAdWeight()+","+p.getAdWeightRate());
												break;
											}else if(igb.equals("4") && tmpCfg.getImgname4().length()>0){
												tmpCfg.setSite_url(c.getSITE_URL());
												tmpCfg.setKno(c.getKno());
												cfg=tmpCfg;
												logger.debug("getNormalKwAdConfig rate5:"+limitCnt+","+p.getUSERID()+","+p.getAdWeight()+","+p.getAdWeightRate());
												break;
											}else if(igb.equals("5") && tmpCfg.getImgname5().length()>0){
												tmpCfg.setSite_url(c.getSITE_URL());
												tmpCfg.setKno(c.getKno());
												cfg=tmpCfg;
												logger.debug("getNormalKwAdConfig rate6:"+limitCnt+","+p.getUSERID()+","+p.getAdWeight()+","+p.getAdWeightRate());
												break;
											}else if(igb.equals("7") && tmpCfg.getImgname6().length()>0){
												tmpCfg.setSite_url(c.getSITE_URL());
												tmpCfg.setKno(c.getKno());
												cfg=tmpCfg;
												logger.debug("getNormalKwAdConfig rate7:"+limitCnt+","+p.getUSERID()+","+p.getAdWeight()+","+p.getAdWeightRate());
												break;
											}else if(tmpCfg.getImgname().length()>0){
												if( igb.equals("7") && tmpCfg.getImgname6().length()<1 ){
													cfg=null;
												}else{
													tmpCfg.setSite_url(c.getSITE_URL());
													tmpCfg.setKno(c.getKno());
													cfg=tmpCfg;
													logger.debug("getNormalKwAdConfig rate8:"+limitCnt+","+p.getUSERID()+","+p.getAdWeight()+","+p.getAdWeightRate());
												}
												break;
											}
										}
										
									}
								}else{
									logger.debug("±¤°í ½ÂÀÎ Á¤º¸ ½ÇÆÐ! : "+c.getSITE_CODE()+"_"+us+"_"+s+"_"+"adlink");
								}
							}	//if(c==null end
						} // for end
						overlapUID.put(p.getUSERID(),"");
					}	//while end
					logger.debug("load getNormalkeywordMatchAdConfig Status From Cache: succ");
				}else{}	// if(list==null end
			}catch(Exception ex){
				logger.error("getNormalkeywordMatchAdConfig2:"+ex);
			}
		}catch(Exception e){
			logger.error("getNormalkeywordMatchAdConfig3:"+media_code+":"+e);
		}
		//logger.info("getNormalkeywordMatchAdConfig3:limitCnt="+limitCnt);
		return cfg;
	}
	public AdConfigData getIkeywordMatchAdConfig(String us,String s,String media_code, String chk_result, String chk_ck, String product, String gubun1){
		RFData c=null;
		AdConfigData cfg=null;
		String debug="";
		int limitCnt=0;
		try{
			if(noMatchKeywordCache.isKeyInCache(media_code+"^^^i")){
				logger.debug(media_code);
				return null;
			}
			try{

				HashMap<String,String> hm1= StringUtils.getConvertHashMap(chk_result, "#");
				
				int incnt=0;
				String key=gubun1+"_"+java.net.URLEncoder.encode(media_code,"euc-kr");
				
				if( hm1.containsKey(key) ){
					//if( ++incnt > 3 ){
						logger.debug("getNormalkeywordMatchAdConfig KL ¸ðµç»óÇ° ³ëÃâµÊ");
						return null;
					//}
				}
				
				ArrayList list=null;
				logger.debug("keyword iskeyin="+ iKeywordDataCache.isKeyInCache(media_code));
				if(iKeywordDataCache.isKeyInCache(media_code)){
					list=(ArrayList) ((ArrayList) iKeywordDataCache.get(media_code).getObjectValue()).clone();
				}else{
					HashMap hm=RFServlet.instance.dataMapper.getIKeywordDataFromKeyword(media_code);
					if(hm!=null && hm.size()>0){
						logger.debug("keyword hm.size="+ hm.size());
						Iterator it = hm.keySet().iterator();
						ArrayList tlist=new ArrayList();	
						while(it.hasNext()){
							RFData sd=(RFData) hm.get(it.next().toString());
							tlist.add(sd);
						}
						list=(ArrayList) tlist.clone();
						Element newElement=new Element(media_code,tlist);
						iKeywordDataCache.put(newElement);
					}
				}
				if(list!=null && list.size()>0){
					logger.debug("keyword list.size()"+ list.size());
					//ArrayList<Integer> rdmList=StringUtils.getRandList(list.size());

					logger.debug("chk_result="+ chk_result +" chk_ck="+ chk_ck);
					
					HashMap<String,String> overlapUID=new HashMap<String,String>();
					ArrayList kwAdWeightList=null;
					if(product.equals("")){
						kwAdWeightList=(ArrayList) iKeywordMatchConfigCache.get("icKwAdWeightList").getObjectValue();
					}else if(product.equals("sky")){
						kwAdWeightList=(ArrayList) iKeywordMatchConfigCache.get("brKwAdWeightList").getObjectValue();
					}
					
					while(cfg==null && list.size()>0 && limitCnt<kwAdWeightList.size()){
						limitCnt++;
						int rInt=new Random().nextInt(kwAdWeightList.size());
						PointData p=(PointData) kwAdWeightList.get(rInt);
						if(overlapUID.containsKey(p.getUSERID())){ 
							//kwAdWeightList.remove(rInt);
							continue;
						}
						logger.debug("p="+p.toString());
						for(int rcnt=0;rcnt<list.size();rcnt++){
							try{
								debug="0";
								c=(RFData) list.get(rcnt);
								debug="1";
								if(c!=null){
									debug="1-1,"+c.getSITE_CODE();
									if(iKeywordMatchConfigCache.isKeyInCache(c.getSITE_CODE())){
										debug="1-2,"+c.getSITE_CODE();
									}else{
										list.remove(rcnt);
										rcnt--;
										continue;
									}
									AdConfigData tmp=(AdConfigData) iKeywordMatchConfigCache.get(c.getSITE_CODE()).getObjectValue();
									//logger.debug("getIkeywordMatchAdConfig1:"+media_code+" "+ rdmList.get(rcnt) +" ¹øÂ° ·ÎµåµÊ.");
									debug="2";
									if(tmp!=null){
										String keyT1= tmp.getUserid()+"_"+tmp.getSite_code();
										if(overlapUID.containsKey(keyT1)){
											logger.debug("getKwAdConfig overlapUID:"+tmp.getUserid()+" continued.");
											continue;
										}
										debug="3";
										if(p.getUSERID().equals(keyT1)){
										}else{
											logger.debug("getKwAdConfig userid diff:"+","+limitCnt+","+list.size()+","+p.getUSERID()+","+keyT1);
											continue;
										}
										debug="4";
										list.remove(rcnt);
										rcnt--;
										String adLinkStatus=RFServlet.instance.adInfoCache.getAdLinkData(c.getSITE_CODE(),"KL",us,s,"iadlink","");
										debug="5";
										if(adLinkStatus!=null && adLinkStatus.equals("Y")){
											debug="6";
											if(c.getSvc_type().equals(product)){
												cfg=tmp.clone();
												cfg.setSite_url(c.getSITE_URL());
												cfg.setKno(c.getKno());
												break;
											}
											debug="7";
											logger.debug("load getIkeywordMatchAdConfig Status From Cache: key="+media_code+",status="+c.getMEDIA_CODE() +" product="+ product +" site_code="+ c.getSITE_CODE() +" svc_type="+ c.getSvc_type());
										}else{
											debug="8";
											logger.debug("±¤°í ½ÂÀÎ Á¤º¸ ½ÇÆÐ! : "+c.getSITE_CODE()+"_"+us+"_"+s+"_"+"iadlink");
										}
									}
								}
							}catch(Exception e){
								logger.error("getIkeywordMatchAdConfig : loop"+e+","+debug);
							}
						}	// for end..
						overlapUID.put(p.getUSERID(),"");
					}	// while end.
				}else{
					Element newElement=new Element(media_code+"^^^i","");
					noMatchKeywordCache.put(newElement);
				}
			}catch(Exception ex){
				//logger.error("getIkeywordMatchAdConfig1:media_code="+media_code+","+ex);
				logger.error("getIkeywordMatchAdConfig1:media_code="+media_code);
			}
		}catch(Exception e){
			logger.error("getIkeywordMatchAdConfig2:"+media_code+":"+e);
		}
		//logger.info("getIkeywordMatchAdConfig3:limitCnt="+limitCnt);
		return cfg;
	}
	public ArrayList getUrlMatchList(String us,String s,String media_code, String chk_result, String chk_ck, String product){
		AdConfigData c=null;
		ArrayList rt_list=null;
		try{
			if(urlMatchConfigCache.isKeyInCache(media_code)){
				ArrayList list=(ArrayList) urlMatchConfigCache.get(media_code).getObjectValue();
				//ArrayList<Integer> rndList=StringUtils.getRandList(list.size());
				int incnt=0;
				for(int i=0;i<list.size();i++){
					c=null;
					AdConfigData tmpc=(AdConfigData) list.get( i );
					String key = "UM_"+ tmpc.getKno();
					if( chk_result.indexOf(key +"#")>-1 ){
						if( ++incnt > 3 ){
							break;
						}
						continue;
					}
					if( chk_ck.indexOf(key)>-1 ){
						continue;
					}
					if( tmpc!=null && tmpc.getSvc_type().equals(product) ){
						if( !us.equals("") && !s.equals("") ){
							String adLinkStatus=RFServlet.instance.adInfoCache.getAdLinkData(tmpc.getSite_code(),"UM",us,s,"iadlink","");
							if( adLinkStatus==null || !adLinkStatus.equals("Y") ){
								logger.debug("±¤°í ½ÂÀÎ Á¤º¸ ½ÇÆÐ! : "+tmpc.getSite_code()+"_"+us+"_"+s+"_"+"iadlink");
								continue;
							}
						}
						rt_list.add(tmpc);
					}	
				}
			}
		}catch(Exception e){
			logger.error("getUrlMatchList:"+media_code+":"+e);
		}
		return rt_list;
	}
	public HashMap getUrlMatchAd(String um, String div){
		HashMap result=null;
		try{
			HashMap<String,String> um_h = StringUtils.getConvertHashMap(um, div);
			logger.debug("getUrlMatchAd()[1937] "+ um_h);
			for( String media_code : um_h.keySet() ){
				if(urlMatchConfigCache.isKeyInCache(media_code)){
					if( result==null ){
						result = new HashMap();
					}
					ArrayList list = null;
					list=(ArrayList) urlMatchConfigCache.get(media_code).getObjectValue();
					if( list!=null && list.size()>0 ){
						result.put(media_code, list);
					}
				}
			}
		}catch(Exception e){
			logger.debug("getUrlMatchAd()[1860] "+ e);
		}
		return result;
	}
	public AdConfigData getTodayMatchAdConfig(String us,String s,String media_code, HashMap addpq, String chk_ck, String product, String gubun, int view_limit){
		AdConfigData c=null;
		AdConfigData c1=null;
		AdConfigData c2=null;
		try{
			ArrayList list= null;
			if( product.equals("") || product.equals("sky") ){
				if(urlMatchConfigCache.isKeyInCache(media_code)){
					list=(ArrayList) urlMatchConfigCache.get(media_code).getObjectValue();
				}
			}else{
				if( normalUrlMatchConfigCache.isKeyInCache(media_code) ){
					list=(ArrayList) normalUrlMatchConfigCache.get(media_code).getObjectValue();
				}
			}
			
			if( list!=null ){
				ArrayList<Integer> rndList=StringUtils.getRandList(list.size());
				logger.debug("getTodayMatchAdConfig[1914] media_code="+ media_code +" list.size() "+ list.size() +" rndList="+ rndList);
				
				HashMap<String,String> targeting= new HashMap<String,String>();
				HashMap<String,String> tmp_chkck= StringUtils.getConvertHashMap(chk_ck, "#");
				for(String tmp_key : tmp_chkck.keySet() ){
					try{
						if( Integer.parseInt( StringUtils.gAt1(tmp_key, 1, "=") ) < view_limit ){
							targeting.put(StringUtils.gAt1(tmp_key, 0, "="), StringUtils.gAt1(tmp_key, 0, "="));
						}
					}catch(Exception e){
						logger.debug("getTodayMatchAdConfig[1924] err"+ e);
					}
				}
				logger.debug("getTodayMatchAdConfig[1927] targeting "+ targeting);
				
				for(int i=0;i<list.size();i++){
					AdConfigData tmpc=(AdConfigData) list.get( rndList.get(i) );
					logger.debug("getTodayMatchAdConfig[1931] tmpc="+ tmpc.toString());
					
					if( tmpc.getSvc_type().equals(product) && tmpc.getGubun().equals("ST") ){
						LinkedHashMap ldList= RFServlet.instance.adInfoCache.getShopStatsData(tmpc.getUserid(),"");
						if(ldList!=null && ldList.size()>0){
							//ArrayList sdlist=new ArrayList(ldList.values());
							List<String> sdlist = new ArrayList<String>(ldList.keySet());
							for(int j=0; j<sdlist.size(); j++){
								String pcode= (String)sdlist.get( j );
								ShopData sd= RFServlet.instance.adInfoCache.getShopPCodeData(tmpc.getUserid(), pcode);
								if(sd!=null){
									String key = gubun +"_"+ sd.getNO();
									if( targeting.containsKey( key ) ){
										c1= tmpc.clone();
										c1.setKno(sd.getNO()+"");
										// site_url, purl, imgname, pnm, price, pcode
										c1.setSite_url(sd.getPURL());
										c1.setPurl(sd.getPURL());
										c1.setImgpath(sd.getIMGPATH());
										c1.setPnm(sd.getPNM());
										c1.setPrice(sd.getPRICE());
										c1.setPcode(sd.getPCODE());
										break;
									}
								}
							}
							if( c1!=null ){
								break;
							}
						}
					}else{
						String key = gubun +"_"+ tmpc.getKno();
						if( targeting.containsKey( key ) ){
							if( tmpc.getSvc_type().equals(product) && tmpc.getGubun().equals(gubun) ){
								c1= tmpc.clone();
								break;
							}
						}
					}
				}
				if( c1==null ){
					int st_cnt=0;
					for(int i=0;i<list.size();i++){
						AdConfigData tmpc=(AdConfigData) list.get( rndList.get(i) );
						
						if( tmpc==null ) continue;
						
						String key = gubun +"_"+ tmpc.getKno();
						
						if( tmpc.getSvc_type().equals(product) && tmpc.getGubun().equals("ST") ){
							LinkedHashMap ldList= RFServlet.instance.adInfoCache.getShopStatsData(tmpc.getUserid(),"");
							if(ldList!=null && ldList.size()>0){
								//ArrayList sdlist=new ArrayList(ldList.values());
								List<String> sdlist = new ArrayList<String>(ldList.keySet());
								ArrayList<Integer> rndList1= StringUtils.getRandList( sdlist.size() );
								
								HashMap<String,ShopData> shpdata_list= new HashMap<String,ShopData>();
								
								for( int j=0; j<sdlist.size(); j++){
									String pcode= (String)sdlist.get( rndList1.get(j) );
									ShopData sd= RFServlet.instance.adInfoCache.getShopPCodeData(tmpc.getUserid(), pcode);
									if( sd!=null ){
										shpdata_list.put("ST_"+sd.getNO(), sd);
									}
								}

								ArrayList chk_result= new ArrayList();
								ArrayList item_list_arr= new ArrayList();
								chk_result.addAll(addpq.keySet());
								item_list_arr.addAll(shpdata_list.keySet());
								Collection<String> hap = CollectionUtils.intersection(chk_result, item_list_arr);
								if( hap.size()>0 ){
									logger.debug("getTodayMatchAdConfig[2032] hap "+ hap);
									break;
								}else{
									String pcode= (String)sdlist.get( rndList1.get(0) );
									ShopData sd= RFServlet.instance.adInfoCache.getShopPCodeData(tmpc.getUserid(), pcode);
									if( sd!=null ){
										c2= tmpc.clone();
										c2.setKno(sd.getNO()+"");
										c2.setSite_url(sd.getPURL());
										c2.setPurl(sd.getPURL());
										c2.setImgpath(sd.getIMGPATH());
										c2.setPnm(sd.getPNM());
										c2.setPrice(sd.getPRICE());
										c2.setPcode(sd.getPCODE());
									}
									break;
								}
							}
						}else{
							logger.debug("getTodayMatchAdConfig[2010] tmpc "+ tmpc.toString());
							
							if( addpq.containsKey(key) ){
								logger.debug("getTodayMatchAdConfig[2013] hm "+ addpq );
								continue;
							}
							if( tmpc.getSvc_type().equals(product) && tmpc.getGubun().equals(gubun) ){
								String adLinkStatus= RFServlet.instance.adInfoCache.getAdLinkData(tmpc.getSite_code(),gubun,us,s,"iadlink","");
								if( adLinkStatus!=null && adLinkStatus.equals("Y") ){
									logger.debug("getTodayMatchAdConfig "+ i);
									c2=tmpc.clone();
									break;
								}else{
									logger.debug("±¤°í ½ÂÀÎ Á¤º¸ ½ÇÆÐ! : "+tmpc.getSite_code()+"_"+us+"_"+s+"_"+"iadlink");
								}
							}
						}
					}
				}
				if( c1!=null ){
					c = c1.clone();
				}else if( c2!=null ) {
					c = c2.clone();
				}
			}
		}catch(IndexOutOfBoundsException e){
			c=null;
		}catch(Exception e){
			logger.error("getTodayMatchAdConfig:"+media_code+":"+e);
		}
		return c;
	}
	public AdConfigData getUrlMatchAdConfig(String us,String s,String media_code, HashMap hm, String chk_ck, String product, String gubun, String gubun1){
		AdConfigData c=null;
		AdConfigData c1=null;
		AdConfigData c2=null;
		int limitCnt=0;
		try{
			if(urlMatchConfigCache.isKeyInCache(media_code)){
				ArrayList list=(ArrayList) ((ArrayList) urlMatchConfigCache.get(media_code).getObjectValue()).clone();
				//ArrayList<Integer> rndList=StringUtils.getRandList(list.size());
				logger.debug("getUrlMatchAdConfig()[1871] media_code="+ media_code +" list.size() "+ list.size());
				
				HashMap<String,String> targeting= new HashMap<String,String>();
				HashMap<String,String> tmp_chkck= StringUtils.getConvertHashMap(chk_ck, "#");
				for(String tmp_key : tmp_chkck.keySet() ){
					try{
						if( Integer.parseInt( StringUtils.gAt1(tmp_key, 1, "=") ) < 1 ){
							targeting.put(StringUtils.gAt1(tmp_key, 0, "="), StringUtils.gAt1(tmp_key, 0, "="));
						}
					}catch(Exception e){
						logger.debug("getUrlMatchAdConfig()[1931] err"+ e);
					}
				}
				logger.debug("getUrlMatchAdConfig[1880] targeting "+ targeting);
				HashMap<String,String> overlapUID=new HashMap<String,String>();
				ArrayList urlAdWeightList=null;
				if(product.equals("")){
					urlAdWeightList=(ArrayList) urlMatchConfigCache.get("icUrlAdWeightList").getObjectValue();
				}else if(product.equals("sky")){
					urlAdWeightList=(ArrayList) urlMatchConfigCache.get("brUrlAdWeightList").getObjectValue();
				}
				
				while(c2==null && list.size()>0 && limitCnt<urlAdWeightList.size()){
					limitCnt++;
					int rInt=new Random().nextInt(urlAdWeightList.size());
					PointData p=(PointData) urlAdWeightList.get(rInt);
					if(overlapUID.containsKey(p.getUSERID())){ 
						//urlAdWeightList.remove(rInt);
						continue;
					}
					for(int i=0;i<list.size();i++){
						try{
							AdConfigData tmpc=(AdConfigData) list.get(i);
							String key=tmpc.getUserid()+"_"+tmpc.getSite_code();
							if(overlapUID.containsKey(key)){
								logger.debug("getKwAdConfig overlapUID:"+tmpc.getUserid()+" continued.");
								continue;
							}
							if(p.getUSERID().equals(key)){
							}else{
								logger.debug("getKwAdConfig userid diff:"+","+limitCnt+","+list.size()+","+key);
								continue;
							}
							String key1 = gubun1 +"_"+ media_code;
							logger.debug("getUrlMatchAdConfig()[1883] tmpc "+ tmpc.toString());
							
							if( hm.containsKey(key1) ){
								logger.debug("getUrlMatchAdConfig()[1888] hm "+ hm );
								continue;
							}
							list.remove(i);
							i--;
							if( tmpc!=null && tmpc.getSvc_type().equals(product) && tmpc.getGubun().equals(gubun) ){
								String adLinkStatus= RFServlet.instance.adInfoCache.getAdLinkData(tmpc.getSite_code(),gubun,us,s,"iadlink","");
								if( adLinkStatus!=null && adLinkStatus.equals("Y") ){
									logger.debug("getUrlMatchAdConfig "+ i);
									c2=tmpc.clone();
									break;
								}else{
									logger.debug("±¤°í ½ÂÀÎ Á¤º¸ ½ÇÆÐ! : "+tmpc.getSite_code()+"_"+us+"_"+s+"_"+"iadlink");
								}
							}
						}catch(Exception e){
							logger.error("getUrlMatchAdConfig 2 : loop "+e);
						}
					}
					overlapUID.put(p.getUSERID(),"");
				}
				if( c1!=null ){
					c = c1.clone();
				}else if( c2!=null ) {
					c = c2.clone();
				}
			}
		}catch(Exception e){
			logger.error("getUrlMatchAdConfig:"+media_code+":"+e);
		}
		//logger.info("getUrlMatchAdConfig:limitCnt="+limitCnt);
		return c;
	}
	public ArrayList getNormalUrlMatchList(String us,String s,String media_code,HashMap hm,String chk_ck,String igb){
		AdConfigData c=null;
		ArrayList list=null;
		ArrayList rt_list=null;
		try{
			if(normalUrlMatchConfigCache.isKeyInCache(media_code)){
				list=(ArrayList) normalUrlMatchConfigCache.get(media_code).getObjectValue();
				logger.debug("getNormalUrlMatchList[1784] list.size() "+ list.size());
				if(list!=null && list.size()>0){
					rt_list= new ArrayList();
					
					for(int i=0;i<list.size();i++){
						AdConfigData tmpC=(AdConfigData) list.get( i );
						String key= "UM_"+tmpC.getKno();
						logger.debug( tmpC.getInfo("getNormalUrlMatchList[1789] ") );
						
						String adLinkStatus=RFServlet.instance.adInfoCache.getAdLinkData(tmpC.getSite_code(),"",us,s,"adlink","");
						if( adLinkStatus==null || !adLinkStatus.equals("Y")) {
							logger.debug("±¤°í ½ÂÀÎ Á¤º¸ ½ÇÆÐ! : "+tmpC.getSite_code()+"_"+us+"_"+s+"_"+"adlink");
							continue;
						}
						// chk_result ¿¡ ÀÖÀ¸¸é ¸î°³ ÀÖ´ÂÁö Ã¤Å©ÇØ¼­ ¸ðµç »óÇ°³ëÃâ¿©ºÎ È®ÀÎ
						if( hm.containsKey(key) ){
							logger.debug("hm.containskey "+ key);
							continue;
						}
						if( chk_ck.indexOf(key +"=")>-1 ){
							logger.debug("chk_ck.indexof "+ key );
							continue;
						}
						
						if( igb.equals("1") && tmpC.getImgname().length()>0 ) {
							rt_list.add(tmpC);
						}else if( igb.equals("2") && tmpC.getImgname2()!=null && tmpC.getImgname2().length()>0 ){
							rt_list.add(tmpC);
						}else if( igb.equals("3") && tmpC.getImgname3()!=null && tmpC.getImgname3().length()>0 ){
							rt_list.add(tmpC);
						}else if( igb.equals("4") && tmpC.getImgname4()!=null && tmpC.getImgname4().length()>0 ){
							rt_list.add(tmpC);
						}else{
							rt_list.add(tmpC);
						}
					}
				}
			}
			logger.debug("load getNormalUrlMatchList Status From Cache: succ -"+media_code);
		}catch(Exception e){
			logger.error("getNormalUrlMatchList:"+media_code+":"+e+":rt_list.size()"+rt_list.size());
		}
		return rt_list;
	}
	public HashMap getNormalUrlMatchAd(String um, String div){
		HashMap result=null;
		try{
			
			HashMap<String,String> um_h = StringUtils.getConvertHashMap(um, div);
			logger.debug("getNormalUrlMatchAd()[2183] "+ um_h);
			for( String media_code : um_h.keySet() ){
				if(normalUrlMatchConfigCache.isKeyInCache(media_code)){
					if( result==null ){
						result = new HashMap();
					}
					ArrayList list = null;
					list=(ArrayList) normalUrlMatchConfigCache.get(media_code).getObjectValue();
					if( list!=null && list.size()>0 ){
						result.put(media_code, list);
					}
				}
			}
		}catch(Exception e){
			logger.debug("getNormalUrlMatchAd()[1951] "+ e);
		}
		return result;
	}
	public AdConfigData getShopAdConfig(String us,String s,String site_code,String adgubun){
		AdConfigData c=null;
		String key=site_code+"_"+adgubun;
		try{
			logger.debug("getShopAdConfig key="+ key);
			Element element=shopAdConfigCache.get(key); // sitecode_svctype_gubun
			if(element !=null){
				c=(AdConfigData) element.getObjectValue();
				String product="";
				if(site_code.indexOf("_sky")>=0){
					product="sky";
				}
				logger.debug("getShopAdConfig c "+c);
				
				String adLinkStatus=RFServlet.instance.adInfoCache.getAdLinkData(c.getSite_code(),adgubun,us,s,"iadlink",product);
				if(adLinkStatus!=null && adLinkStatus.equals("Y")){
					logger.debug("load getShopAdConfig Status From Cache: key="+key+",status="+c.getSite_code());
					logger.debug("±¤°í ½ÂÀÎ Á¤º¸ ¼º°ø! : "+key+"_"+us+"_"+s+"_"+"iadlink");
				}else{
					logger.debug("±¤°í ½ÂÀÎ Á¤º¸ ½ÇÆÐ! : "+key+"_"+us+"_"+s+"_"+"iadlink");
					c=null;
				}
			}else{
				logger.debug("getShopAdConfig[2010] site_code none "+ site_code  );
			}
		}catch(Exception e){
			logger.error("getShopAdConfig:"+key+":"+e);
		}
		return c;
	}
	public ArrayList getNormalUrlMatchAdConfig(String us,String s, String media_code,int nCnt, String types){
		ArrayList list=null;
		ArrayList rtn=new ArrayList();
		int limitCnt=0;
		try{
			if(normalUrlMatchConfigCache.isKeyInCache(media_code)){
				list=(ArrayList) ((ArrayList) normalUrlMatchConfigCache.get(media_code).getObjectValue()).clone();
				if(list!=null && list.size()>0){	
					logger.debug("getNormalUrlMatchAdConfig[2337] "+ list.size());
					//ArrayList<Integer> rdmList=StringUtils.getRandList(list.size());
					HashMap<String,String> overlapUID=new HashMap<String,String>();
					ArrayList urlAdWeightList=(ArrayList) normalUrlMatchConfigCache.get("urlAdWeightList").getObjectValue();
					logger.debug("urlAdWeightList size:"+urlAdWeightList.size());
					while(list.size()>0 && rtn.size()<nCnt && limitCnt<urlAdWeightList.size()){
						if(list.size()==0){
							logger.debug("aaaaaaabbbb");
							break;
						}
						limitCnt++;
						int rInt=new Random().nextInt(urlAdWeightList.size());
						PointData p=(PointData) urlAdWeightList.get(rInt);
						if(overlapUID.containsKey(p.getUSERID())){ 
							//urlAdWeightList.remove(rInt);
							continue;
						}
						for(int i=0;i<list.size();i++){
							AdConfigData tmpC=(AdConfigData) list.get(i);
							//String key = "UM_"+tmpC.getKno();
							logger.debug("getNormalUrlMatchAdConfig()[1968] tmpC "+ tmpC.toString());
							String key1=tmpC.getUserid()+"_"+tmpC.getSite_code();
							if(overlapUID.containsKey(key1)){
								logger.debug("overlapUID:"+key1+" continued.");
								continue;
							}else{
								logger.debug("p.userId="+p.getUSERID()+",+overlapUID:"+key1+" process.");
							}
							if(p.getUSERID().equals(key1)){
							}else{
								logger.debug("userid diff:"+p.getUSERID()+","+tmpC.getUserid());
								continue;
							}
							list.remove(i);
							i--;
							if( types.equals("mobile") ){
								if( tmpC.getStateM().equals("Y") ){
									String adLinkStatus=RFServlet.instance.adInfoCache.getAdLinkData(tmpC.getSite_code(),"",us,s,"adlink","");
									if( adLinkStatus==null || !adLinkStatus.equals("Y")) {
										logger.debug("±¤°í ½ÂÀÎ Á¤º¸ ½ÇÆÐ! : "+tmpC.getSite_code()+"_"+us+"_"+s+"_"+"adlink");
										continue;
									}
									if( tmpC.getBanner_path1()!=null && !tmpC.getBanner_path1().equals("") ){
										rtn.add(tmpC.clone());
										logger.debug("getNormalUrlAdConfig rate2:"+limitCnt+","+p.getUSERID()+","+p.getAdWeight()+","+p.getAdWeightRate());
									}else{
										rtn.add(tmpC.clone());
										logger.debug("getNormalUrlAdConfig rate3:"+limitCnt+","+p.getUSERID()+","+p.getAdWeight()+","+p.getAdWeightRate());
									}
								}
							}else{
								if( tmpC.getStateW().equals("Y") ){
									String adLinkStatus=RFServlet.instance.adInfoCache.getAdLinkData(tmpC.getSite_code(),"",us,s,"adlink","");
									if( adLinkStatus==null || !adLinkStatus.equals("Y")) {
										logger.debug("±¤°í ½ÂÀÎ Á¤º¸ ½ÇÆÐ! : "+tmpC.getSite_code()+"_"+us+"_"+s+"_"+"adlink");
										continue;
									}
									rtn.add(tmpC.clone());
									logger.debug("getNormalUrlAdConfig rate8:"+limitCnt+","+p.getUSERID()+","+p.getAdWeight()+","+p.getAdWeightRate());
									
								}
							}
						}	//for end.
						overlapUID.put(p.getUSERID(),"");
					}	//while end.
				} //list size end.
			}	// if end
			logger.debug("load getNormalUrlMatchAdConfig.php Status From Cache: succ -"+media_code+","+rtn.size());
		}catch(Exception e){
			logger.error("getNormalUrlMatchAdConfig.php:"+media_code+":"+e);
		}
		//logger.info("load getNormalUrlMatchAdConfig.php limitCnt="+limitCnt);
		return rtn;
	}
	public AdConfigData getNormalUrlMatchAdConfig(String us,String s, String media_code, HashMap hm, String hm1
			, String igb, String gubun, int target_cnt, String types){
		AdConfigData c=null;
		AdConfigData c1=null;
		AdConfigData c2=null;
		ArrayList list=null;
		int limitCnt=0;
		try{
			if(normalUrlMatchConfigCache.isKeyInCache(media_code)){
				//list=(ArrayList) normalUrlMatchConfigCache.get(media_code).getObjectValue();
				//if(list!=null && list.size()>0){
				//logger.debug("getNormalUrlMatchAdConfig[2305] "+media_code+","+ list.size());
					
				HashMap<String,String> targeting= new HashMap<String,String>();
				HashMap<String,String> tmp_chkck= StringUtils.getConvertHashMap(hm1, "#");
				try{
					for(String tmp_key : tmp_chkck.keySet() ){
						int tmpcnt = 0;
						try{
							tmpcnt = Integer.parseInt( StringUtils.gAt1(tmp_key, 1, "="));
						}catch(Exception e){
							tmpcnt = 0;
						}
						if( tmpcnt < target_cnt ){
							targeting.put(StringUtils.gAt1(tmp_key, 0, "="), StringUtils.gAt1(tmp_key, 0, "="));
						}
					}
				}catch(Exception e){
					logger.error("getNormalUrlMatchAdConfig()[2016] "+ e +" hm1="+ hm1);
				}
				logger.debug("getNormalUrlMatchAdConfig()[1974] targeting "+ targeting+","+media_code);
				String mKey = "BNUM_"+media_code;
				if( targeting.size()>0 ){
					//ArrayList list1= new ArrayList();
					/*
					for(int i=0;i<list.size();i++){
						AdConfigData tmpC=(AdConfigData) list.get( i );
						String key = "UM_"+media_code;
						if( targeting.containsKey( key ) ){
							list1.add(tmpC);
						}
					}
					list= (ArrayList) list1.clone();
					*/
					if( targeting.containsKey(mKey)){
						list=(ArrayList) ((ArrayList) normalUrlMatchConfigCache.get(media_code).getObjectValue()).clone();
						//list= (ArrayList) list1.clone();
					}
					
				}else{
					if(hm.containsKey(mKey)){
						return null;
					}else{
						list=(ArrayList) ((ArrayList) normalUrlMatchConfigCache.get(media_code).getObjectValue()).clone();
					}
				}
				if(list!=null && list.size()>0){	
					logger.debug("getNormalUrlMatchAdConfig[2337] "+ list.size());
					HashMap<String,String> overlapUID=new HashMap<String,String>();
					ArrayList urlAdWeightList=(ArrayList) normalUrlMatchConfigCache.get("urlAdWeightList").getObjectValue();
					logger.debug("urlAdWeightList size:"+urlAdWeightList.size());
					while(c==null && list.size()>0 && limitCnt<urlAdWeightList.size()){
						limitCnt++;
						int rInt=new Random().nextInt(urlAdWeightList.size());
						PointData p=(PointData) urlAdWeightList.get(rInt);
						if(overlapUID.containsKey(p.getUSERID())){ 
							//urlAdWeightList.remove(rInt);
							continue;
						}
						for(int i=0;i<list.size();i++){
							AdConfigData tmpC=((AdConfigData) list.get(i)).clone();
							//String key = "UM_"+tmpC.getKno();
							logger.debug("getNormalUrlMatchAdConfig()[1968] tmpC "+ tmpC.toString());
							String key1=tmpC.getUserid()+"_"+tmpC.getSite_code();
							if(overlapUID.containsKey(key1)){
								logger.debug("overlapUID:"+key1+" continued.");
								continue;
							}else{
								logger.debug("p.userId="+p.getUSERID()+",+overlapUID:"+key1+" process.");
							}
							if(p.getUSERID().equals(key1)){
							}else{
								logger.debug("userid diff:"+p.getUSERID()+","+tmpC.getUserid());
								continue;
							}
							list.remove(i);
							i--;
							if( types.equals("mobile") ){
								if( tmpC.getStateM().equals("Y") ){
									//if( targeting.containsKey( key ) ){
									//	c1=tmpC.clone();
									//	logger.debug("getNormalUrlAdConfig rate1:"+limitCnt+","+p.getUSERID()+"_"+tmpC.getSite_code()+","+p.getAdWeight()+","+p.getAdWeightRate());
									//	break;
									//}else{
										//if( hm.containsKey(key) ){
										//	logger.debug("getNormalUrlMatchAdConfig() "+ hm );
										//	continue;
										//}
										String adLinkStatus=RFServlet.instance.adInfoCache.getAdLinkData(tmpC.getSite_code(),"",us,s,"adlink","");
										if( adLinkStatus==null || !adLinkStatus.equals("Y")) {
											logger.debug("±¤°í ½ÂÀÎ Á¤º¸ ½ÇÆÐ! : "+tmpC.getSite_code()+"_"+us+"_"+s+"_"+"adlink");
											continue;
										}
										if( tmpC.getBanner_path1()!=null && !tmpC.getBanner_path1().equals("") ){
											c2=tmpC.clone();
											logger.debug("getNormalUrlAdConfig rate2:"+limitCnt+","+p.getUSERID()+","+p.getAdWeight()+","+p.getAdWeightRate());
											break;
										}else{
											c2=tmpC.clone();
											logger.debug("getNormalUrlAdConfig rate3:"+limitCnt+","+p.getUSERID()+","+p.getAdWeight()+","+p.getAdWeightRate());
										}
									//}
								}
							}else{
							if( tmpC.getStateW().equals("Y") ){
								//if( targeting.containsKey( key ) ){
								//	if( igb.equals("7") && tmpC.getImgname6().equals("") ){
								//	}else{
								//		c1=tmpC.clone();
								//	}
								//	break;
								//}else{
									//if( hm.containsKey(key) ){
									//	logger.debug("getNormalUrlMatchAdConfig() "+ hm );
									//	continue;
									//}
									String adLinkStatus=RFServlet.instance.adInfoCache.getAdLinkData(tmpC.getSite_code(),"",us,s,"adlink","");
									if( adLinkStatus==null || !adLinkStatus.equals("Y")) {
										logger.debug("±¤°í ½ÂÀÎ Á¤º¸ ½ÇÆÐ! : "+tmpC.getSite_code()+"_"+us+"_"+s+"_"+"adlink");
										continue;
									}
									if( tmpC.getGubun().equals(gubun) ){
										if( igb.equals("1") && !tmpC.getImgname().equals("") ){
											c2=tmpC.clone();
											logger.debug("getNormalUrlAdConfig rate4:"+limitCnt+","+p.getUSERID()+","+p.getAdWeight()+","+p.getAdWeightRate());
											break;
										}else if( igb.equals("2") && !tmpC.getImgname2().equals("") ){
											c2=tmpC.clone();
											logger.debug("getNormalUrlAdConfig rate5:"+limitCnt+","+p.getUSERID()+","+p.getAdWeight()+","+p.getAdWeightRate());
											break;
										}else if( igb.equals("3") && !tmpC.getImgname3().equals("") ){
											c2=tmpC.clone();
											logger.debug("getNormalUrlAdConfig rate6:"+limitCnt+","+p.getUSERID()+","+p.getAdWeight()+","+p.getAdWeightRate());
											break;
										}else if( igb.equals("4") && !tmpC.getImgname4().equals("") ){
											c2=tmpC.clone();
											logger.debug("getNormalUrlAdConfig rate7:"+limitCnt+","+p.getUSERID()+","+p.getAdWeight()+","+p.getAdWeightRate());
											break;
										}else if( igb.equals("7") && !tmpC.getImgname6().equals("") ){
											c2=tmpC.clone();
											logger.debug("getNormalUrlAdConfig rate8:"+limitCnt+","+p.getUSERID()+","+p.getAdWeight()+","+p.getAdWeightRate());
											break;
										}else{
											if( igb.equals("7") && tmpC.getImgname6().equals("") ){
												c2=null;
											}else{
												c2=tmpC.clone();
												logger.debug("getNormalUrlAdConfig rate9:"+limitCnt+","+p.getUSERID()+","+p.getAdWeight()+","+p.getAdWeightRate());
											}
										}
									}
								}
							}
						}	//for end.
						if( c1!=null ){
							c = c1.clone();
							logger.debug("getNormalUrlAdConfig rate10:"+limitCnt+","+p.getUSERID()+","+p.getAdWeight()+","+p.getAdWeightRate());
						}else if( c2!=null ){
							c = c2.clone();
							logger.debug("getNormalUrlAdConfig rate11:"+limitCnt+","+p.getUSERID()+","+p.getAdWeight()+","+p.getAdWeightRate());
						}
						overlapUID.put(p.getUSERID(),"");
					}	//while end.
				} //list size end.
			}	// if end
			logger.debug("load getNormalUrlMatchAdConfig Status From Cache: succ -"+media_code);
		}catch(Exception e){
			logger.error("getNormalUrlMatchAdConfig:"+media_code+":"+e+" "+hm+" "+hm1);
		}
		//logger.info("getNormalUrlMatchAdConfig:limitCnt="+limitCnt);
		return c;
	}
	public SiteCodeData getAdSiteStatus(String siteCode){
		SiteCodeData result= null;
		try{
			Element element=adSiteCache.get(siteCode);
			if(element !=null){
				result= (SiteCodeData)element.getObjectValue();
				logger.debug("load getAdSiteStatus Status From Cache: key="+siteCode
						+",status="+result.getState()+",site_code_s="+result.getSite_code_s());
			}else{
				
			}
		}catch(Exception e){
			logger.error("getAdSiteStatus:"+siteCode+":"+e.getMessage());
		}
		return result;
	}
	public String getCacheInfo(){
		StringBuffer rStr=new StringBuffer();
		try{
			/*
			String[] cacheList=cacheManager.getCacheNames();
			rStr.append("<table border=1 cellpadding=1 cellspacing=0 style='font-size:11px;'>");
			rStr.append("<tr>");
			rStr.append("<td>name</td>");
			rStr.append("<td>Size</td>");
			rStr.append("<td>HitCount</td>");
			rStr.append("<td>localHeapHitCount</td>");
			rStr.append("<td>localDiskHitCount</td>");
			rStr.append("<td>LocalHeapSize</td>");
			rStr.append("<td>LocalDiskSize</td>");
			rStr.append("<td>RemoteSize</td>");
			rStr.append("<td>MissCount</td>");
			rStr.append("<td>PutCount</td>");
			rStr.append("<td>PutAddedCount</td>");
			rStr.append("<td>PutUpdatedCount</td>");
			rStr.append("<td>RemoveCount</td>");
			rStr.append("</tr>");
			for(String me:cacheList){
				rStr.append("<tr>");
				Cache cache=cacheManager.getCache(me);
				FlatStatistics s = cache.getStatistics();
				rStr.append("<td>"+me+"</td>");
				rStr.append("<td>"+s.getSize()+"</td>");
				rStr.append("<td>"+s.cacheHitCount()+"</td>");
				rStr.append("<td>"+s.localHeapHitCount()+"</td>");
				rStr.append("<td>"+s.localDiskHitCount()+"</td>");
				rStr.append("<td>"+s.getLocalHeapSize()+"</td>");
				rStr.append("<td>"+s.getLocalDiskSize()+"</td>");
				rStr.append("<td>"+s.getRemoteSize()+"</td>");
				rStr.append("<td>"+s.cacheMissCount()+"</td>");
				rStr.append("<td>"+s.cachePutCount()+"</td>");
				rStr.append("<td>"+s.cachePutAddedCount()+"</td>");
				rStr.append("<td>"+s.cachePutUpdatedCount()+"</td>");
				rStr.append("<td>"+s.cacheRemoveCount()+"</td>");
			
				
				
				rStr.append("</tr>");
			}
			rStr.append("</table>");
			rStr.append("</table>");
			*/
			// "MediaCodeCache","AdSiteCache","IAdSiteCache","MediaSiteCache","ShopAdConfigCache","UrlMatchConfigCache"
			// ,"IKeywordMatchConfigCache","IKeywordDataCache","BaseAdConfigCache","AdCashCache","MediaScriptCache"
			// ,"NormalBaseAdConfigCache","NormalShopAdConfigCache","NormalKeywordMatchConfigCache","NormalKeywordDataCache"
			// ,"NormalUrlMatchConfigCache","ShopLogCache","NoMatchKeywordCache"
			String[] cacheList=cacheManager.getCacheNames();
			rStr.append("<table border=1 cellpadding=1 cellspacing=0 style='font-size:11px;'>");
			rStr.append("<tr>");
			rStr.append("<td>name</td><td>Size</td><td>MemStorSz</td><td>DskStorSz</td>");
			rStr.append("<td>HitCnt</td><td>MemHitCnt</td><td>DiskHitCnt</td><td>CacheMiss</td>");
			//rStr.append("<td>AvgGetTime</td><td>AvgSchTime</td><td>AvgSchSec</td>");
			rStr.append("</tr>");
			for(String me:cacheList){
				rStr.append("<tr>");
				Cache cache=cacheManager.getCache(me);
				rStr.append("<td>"+me+"</td>");
				rStr.append("<td>"+cache.getSize()+"</td>");
				rStr.append("<td>"+cache.getMemoryStoreSize()+"</td>");
				rStr.append("<td>"+cache.getDiskStoreSize()+"</td>");
				
				rStr.append("<td>"+cache.getStatistics().getCacheHits()+"</td>");
				rStr.append("<td>"+cache.getStatistics().getInMemoryHits()+"</td>");
				rStr.append("<td>"+cache.getStatistics().getOnDiskHits()+"</td>");
				rStr.append("<td>"+cache.getStatistics().getCacheMisses()+"</td>");
				
				//rStr.append("<td>"+cache.getAverageGetTime()+"</td>");
				//rStr.append("<td>"+cache.getAverageSearchTime()+"</td>");
				//rStr.append("<td>"+cache.getSearchesPerSecond()+"</td>");
				rStr.append("</tr>");
			}
			rStr.append("</table>");
		}catch(Exception e){
			logger.error("getCacheInfo:"+e);
			rStr.append(e);
		}
		return rStr.toString();
	}
}
