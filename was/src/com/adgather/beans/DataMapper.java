package com.adgather.beans;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import net.sf.ehcache.Element;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;

import com.adgather.reportmodel.AdConfigData;
import com.adgather.reportmodel.PointData;
import com.adgather.reportmodel.RFData;
import com.adgather.reportmodel.ShopData;
import com.adgather.reportmodel.SiteCodeData;
import com.adgather.resource.db.DBManager;
import com.adgather.servlet.RFServlet;
import com.adgather.util.StringUtils;
public class DataMapper {
	static Logger logger = Logger.getLogger(DataMapper.class);
	public HashMap getIKeywordDataFromKeyword(String keyword){
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		StringBuffer sql=new StringBuffer();
		HashMap<String,RFData> sh=new HashMap<String,RFData>();
		if(RFServlet.instance.instance.dbStatus==false){
			return sh;
		}
		try {
//			sql.append(" select site_code,site_url,state \n");
//			sql.append(" from ikeywordlink \n");
//			sql.append(" where media_code='"+keyword+"' \n");
//			sql.append(" and state='Y' \n");

			sql.append(" SELECT a.site_code,a.site_url,a.state, b.svc_type, a.no \n");
			sql.append(" FROM ikeywordlink a, iadsite b  \n");
			sql.append(" WHERE a.site_code=b.site_code \n");
			sql.append(" AND a.media_code='"+keyword+"' \n");
			sql.append(" AND a.state='Y'; \n");

			logger.debug(sql.toString());
			conn=DBManager.getConnection("dreamdb");
			stmt=conn.createStatement();
			rs=stmt.executeQuery(sql.toString());
			
			while(rs.next()){
				RFData sd=new RFData();
				sd.setSITE_CODE(rs.getString("site_code"));
				sd.setMEDIA_CODE(keyword);
				sd.setSITE_URL(rs.getString("site_url"));
				sd.setSvc_type(rs.getString("svc_type"));
				sd.setKno(rs.getString("no"));
				sh.put(rs.getString("site_code"),sd);
			}
		} catch(Exception e) {
			logger.error("getIKeywordDataFromKeyword:"+e+" keyword="+keyword);
			//RFServlet.instance.instance.dbStatus=false;
		} finally {
			try{if(rs !=null) rs.close();} catch(Exception e){};
			try{if(stmt !=null) stmt.close();} catch(Exception e){};
			try{if(conn !=null) conn.close();} catch(Exception e){};
		}
		return sh;
	}
	public HashMap getNormalKeywordDataFromKeywordDB(String keyword){
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		StringBuffer sql=new StringBuffer();
		HashMap<String,RFData> sh=new HashMap<String,RFData>();
		if(RFServlet.instance.instance.dbStatus==false){
			return sh;
		}
		try {
			sql.append(" select site_code,site_url,state,no \n");
			sql.append(" from keywordlink \n");
			sql.append(" where media_code='"+keyword+"' \n");
			//sql.append(" and state='Y' \n");
			logger.debug(sql.toString());
			conn=DBManager.getConnection("dreamdb");
			stmt=conn.createStatement();
			rs=stmt.executeQuery(sql.toString());
			while(rs.next()){
				RFData sd=new RFData();
				sd.setSITE_CODE(rs.getString("site_code"));
				sd.setMEDIA_CODE(keyword);
				sd.setSITE_URL(rs.getString("site_url"));
				sd.setKno(rs.getString("no"));
				sh.put(rs.getString("site_code"),sd);
			}
		} catch(Exception e) {
			// after fix
			//logger.error("getNormalKeywordDataFromKeywordDB:"+e+", keyIp="+ keyIp +","+ keyword );
			if(e.toString().indexOf("euckr_korean_ci,IMPLICIT)")>-1){
				//String convKeyword="keyword 조회 실패.";
				//sh=getNormalKeywordDataFromKeywordDB(convKeyword,exceptHm);
			}
			//RFServlet.instance.dbStatus=false;
		} finally {
			try{if(rs !=null) rs.close();} catch(Exception e){};
			try{if(stmt !=null) stmt.close();} catch(Exception e){};
			try{if(conn !=null) conn.close();} catch(Exception e){};
		}
		return sh;
	}
	public HashMap getRF_KeywordDataFromIp(String ip){
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		StringBuffer sql=new StringBuffer();
		HashMap<String,ShopData> sh=new HashMap<String,ShopData>();
		if(RFServlet.instance.dbStatus==false){
			return sh;
		}
		try {
			sql.append(" select no, sc_txt \n");
			sql.append(" from RF_KEYWORD \n");
			sql.append(" where ip='"+ip+"' \n");
			sql.append(" and partid in('"+StringUtils.getPartIdKey(ip)+"','"+StringUtils.getPrevPartIdKey(ip)+"')");
			sql.append(" order by no desc limit 1 \n");
			logger.debug(sql.toString());
			conn=DBManager.getConnection("dreamdb");
			stmt=conn.createStatement();
			rs=stmt.executeQuery(sql.toString());
			if(rs.next()){
				ShopData sd=new ShopData();
				sd.setSC_TXT(rs.getString("sc_txt"));
				sd.setNO( Integer.parseInt(rs.getString("no")) );
				sh.put(ip,sd);
			}
			Element in=new Element(ip,sh);
			RFServlet.instance.adInfoCache.rfKeywordLogCache.put(in);
			//logger.info("getRF_KeywordDataFromIp : "+ip);
		} catch(Exception e) {
			logger.error("getRF_KeywordDataFromIp:"+e);
			RFServlet.instance.dbStatus=false;
		} finally {
			try{if(rs !=null) rs.close();} catch(Exception e){};
			try{if(stmt !=null) stmt.close();} catch(Exception e){};
			try{if(conn !=null) conn.close();} catch(Exception e){};
		}
		return sh;
	}
	public String getClickChkDataInfo(String ukey,String ip, String svc_type, String adgubun){
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		StringBuffer sql=new StringBuffer();
		SimpleDateFormat yyyymmddHHmm = new SimpleDateFormat("yyyyMMddHHmm");
		java.util.Date date=new java.util.Date();
		Calendar now=Calendar.getInstance();
		now.add(Calendar.HOUR_OF_DAY,-1);
		date.setTime(now.getTimeInMillis());
		String ymdhm=yyyymmddHHmm.format(date);
		String rCode="";
		if(RFServlet.instance.instance.dbStatus==false){
			return rCode;
		}
		try {
			sql.append(" select ukey \n");
			sql.append(" from chksite_new \n");
			sql.append(" where ukey='"+ukey+"' and sdatetime > '"+ymdhm+"' and ip='"+ip+"' \n");
			if(!svc_type.equals(""))sql.append(" and svc_type='"+ svc_type +"' \n");
			if(!adgubun.equals(""))	sql.append(" and gubun='"+ adgubun +"' \n");
			logger.debug(sql.toString());
			conn=DBManager.getConnection("dreamdb");
			stmt=conn.createStatement();
			rs=stmt.executeQuery(sql.toString());
			if(rs.next()){
				rCode=rs.getString("ukey");
			}
		} catch(Exception e) {
			logger.error("getClickChkDataInfo:"+e);
			RFServlet.instance.instance.dbStatus=false;
		} finally {
			try{if(rs !=null) rs.close();} catch(Exception e){};
			try{if(stmt !=null) stmt.close();} catch(Exception e){};
			try{if(conn !=null) conn.close();} catch(Exception e){};
		}
		return rCode;
	}
	public int getKEYWORD_CLICK_SCCnt(String ip){
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		StringBuffer sql=new StringBuffer();
		SimpleDateFormat yyyymmdd = new SimpleDateFormat("yyyyMMdd");
		java.util.Date date=new java.util.Date();
		String ymd=yyyymmdd.format(date);
		int cnt=0;
		if(RFServlet.instance.instance.dbStatus==false){
			return 0;
		}
		try {
			sql.append(" select count(*) as cnt \n");
			sql.append(" from KEYWORD_CLICK_SC \n");
			sql.append(" where ip='"+ip+"' \n");
			sql.append(" and RDATE='"+ymd+"' \n");
			logger.debug(sql.toString());
			conn=DBManager.getConnection("dreamdb");
			stmt=conn.createStatement();
			rs=stmt.executeQuery(sql.toString());
			if(rs.next()){
				cnt=rs.getInt("cnt");
			}
		} catch(Exception e) {
			logger.error("getKEYWORD_CLICK_SCCnt:"+e);
			RFServlet.instance.instance.dbStatus=false;
		} finally {
			try{if(rs !=null) rs.close();} catch(Exception e){};
			try{if(stmt !=null) stmt.close();} catch(Exception e){};
			try{if(conn !=null) conn.close();} catch(Exception e){};
		}
		return cnt;
	}
	public HashMap getRFDataFromIp(String ip){
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		StringBuffer sql=new StringBuffer();
		HashMap<String,RFData> sh=new HashMap<String,RFData>();
		if(RFServlet.instance.instance.dbStatus==false){
			return sh;
		}
		try {
			sql.append(" select sc_txt \n");
			sql.append(" from RF \n");
			sql.append(" where ip='"+ip+"' \n");
			sql.append(" and partid in('"+StringUtils.getPartIdKey(ip)+"','"+StringUtils.getPrevPartIdKey(ip)+"')");
			sql.append(" order by no desc limit 1\n");
			logger.debug(sql.toString());
			conn=DBManager.getConnection("dreamdb");
			stmt=conn.createStatement();
			rs=stmt.executeQuery(sql.toString());
			while(rs.next()){
				RFData sd=new RFData();
				sd.setSC_TXT(rs.getString("sc_txt"));
				sh.put(rs.getString("sc_txt"),sd);
			}
		} catch(Exception e) {
			logger.error("getRFDataFromIp:"+e);
			RFServlet.instance.dbStatus=false;
		} finally {
			try{if(rs !=null) rs.close();} catch(Exception e){};
			try{if(stmt !=null) stmt.close();} catch(Exception e){};
			try{if(conn !=null) conn.close();} catch(Exception e){};
		}
		return sh;
	}
	public String loadAdLinkDataFromDB(String site_code,String adgubun,String media_no,String script_no,String tbname,String product){
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		StringBuffer sql=new StringBuffer();
		String adyn="Y";
		if(RFServlet.instance.instance.dbStatus==false){
			return adyn;
		}
		try {
			sql.append("select media_no,adyn \n");
			sql.append("FROM "+tbname+" \n");
			sql.append("where script_no="+script_no+" \n");
			if(tbname.equals("adlink")){
				sql.append("and site_code='"+site_code+"' \n");
			}else{
				logger.debug("loadAdLinkDataFromDB="+site_code+","+adgubun+","+media_no+","+script_no+","+tbname+","+product);
				if( adgubun.equals("SR") || adgubun.equals("ST") ){
					sql.append("and site_code in(select site_code \n");
					sql.append("                 from iadsite \n");
					sql.append("                 where site_code_s='"+site_code+"' \n");
					sql.append("                 and gubun='"+adgubun+"' \n");
					if(product.equals("sky")){
						sql.append("              and svc_type='sky') \n");	
					}else{
						sql.append("              and svc_type='') \n");	
						
					}
				}else if( adgubun.equals("SP") ){
					sql.append("and site_code in(select site_code \n");
					sql.append("                 from iadsite \n");
					sql.append("                 where site_code='"+site_code+"' \n");
					sql.append("                 and gubun='"+adgubun+"' \n");
					if(product.equals("sky")){
						sql.append("              and svc_type='sky') \n");	
					}else{
						sql.append("              and svc_type='') \n");
					}
				}else{
					sql.append("and site_code='"+site_code+"' \n");
				}
			}
			sql.append("and media_no='"+media_no+"' \n");
			sql.append("and del_fg='N' \n");
			logger.debug(sql.toString());
			conn=DBManager.getConnection("dreamdb");
			stmt=conn.createStatement();
			rs=stmt.executeQuery(sql.toString());
			String keyStr=site_code+"_"+media_no+"_"+script_no+"_"+tbname;
			Element element=null;
			if(rs.next()){
				element=new Element(keyStr,rs.getString("adyn"));
				adyn=rs.getString("adyn");
			}else{
				element=new Element(keyStr,"D");
				adyn="D";
			}
			RFServlet.instance.adInfoCache.adLinkCache.put(element);
			logger.debug("adLinkCache : succ");
		} catch(Exception e) {
			logger.error("adLinkCache:"+e);
			RFServlet.instance.dbStatus=false;
		} finally {
			try{if(rs !=null) rs.close();} catch(Exception e){};
			try{if(stmt !=null) stmt.close();} catch(Exception e){};
			try{if(conn !=null) conn.close();} catch(Exception e){};
		}
		return  adyn;
	}
	public LinkedHashMap loadShopStatsDataFromDB(String key,String chkMd){
		Connection conn = null;
		Statement stmt = null;
		Statement ustmt = null;
		ResultSet rs = null;
		StringBuffer sql=new StringBuffer();
		LinkedHashMap<String,String> list1=new LinkedHashMap();
		if(RFServlet.instance.instance.dbStatus==false){
			return list1;
		}
		String userid="";
		String cate="";
		String[] aKey=key.split("#_#");
		if(aKey!=null && aKey.length>=1){
			userid=aKey[0];
		}
		if(aKey!=null && aKey.length>=2){
			cate=aKey[1];
		}
		try {
			conn=DBManager.getConnection("dreamdb");
			// 정상화 시켜야 함.. 
			if(chkMd.equals("1nd")){
				if(cate.equals("")){
					sql.append(" SELECT pcode,sum(viewcnt) viewcnt                     \n");
					sql.append(" FROM stats_shopdata_new \n");
					sql.append(" WHERE sdate>= DATE_FORMAT(NOW() + INTERVAL -1 DAY,'%Y%m%d')      \n");
					sql.append(" AND viewcnt>0                              \n");
					sql.append(" AND userid='"+userid+"'                        \n");
					sql.append(" GROUP BY pcode                             \n");
					sql.append(" ORDER BY sum(viewcnt) DESC                      \n");
					sql.append(" LIMIT 20                                     \n");
				}else{
					sql.append(" SELECT pcode,sum(viewcnt)                                                          \n");
					sql.append(" FROM stats_shopdata_new \n");
					sql.append(" where userid='"+userid+"'                                                          \n");
					sql.append(" AND CATE1='"+cate+"' COLLATE utf8_general_ci     \n");
					sql.append(" AND sdate>= DATE_FORMAT(NOW() + INTERVAL -30 DAY,'%Y%m%d')                                             \n");
					sql.append(" GROUP BY pcode                                                                  \n");
					sql.append(" ORDER BY sum(viewcnt) DESC                                                           \n");
					sql.append(" LIMIT 20                                                                          \n");
				
				}
			}else if(chkMd.equals("2nd")){
				sql.append(" SELECT pcode                     \n");
				sql.append(" FROM stats_shopdata_v2 \n");
				sql.append(" where userid='"+userid+"'                        \n");
				sql.append(" AND cate1='"+cate+"'                        \n");
				sql.append(" ORDER BY ordno                      \n");
				sql.append(" LIMIT 20 \n");
				
			}
			//logger.info(chkMd+":"+sql.toString());
			
			stmt=conn.createStatement();
			rs=stmt.executeQuery(sql.toString());
			int i=0;
			while(rs.next()){
				if(i==0 && chkMd.equals("1nd")){
					sql.delete(0,sql.length());
					sql.append("delete from stats_shopdata_v2 where userid='"+userid+"' and cate1='"+cate+"'");
					logger.info(sql.toString());
					ustmt=conn.createStatement();
					ustmt.executeUpdate(sql.toString());
					if(ustmt!=null) ustmt.close();
				}
				
				if(chkMd.equals("1nd")){
					sql.delete(0,sql.length());
					sql.append("insert into stats_shopdata_v2(userid,cate1,pcode,ordno) values ( \n");
					sql.append("'"+userid+"','"+cate+"','"+rs.getString("pcode").replaceAll("'","")+"',"+i+") \n");
					logger.info(sql.toString());
					ustmt=conn.createStatement();
					ustmt.executeUpdate(sql.toString());
					if(ustmt!=null) ustmt.close();
				}
				list1.put(rs.getString("pcode"),Calendar.getInstance().getTimeInMillis()+"");
				i++;
				
			}
			if(list1.size()>0){ 
				Element element=new Element(userid+"#_#"+cate,list1);
				RFServlet.instance.adInfoCache.shopCategoryDataCache.put(element);
				logger.info("loadShopCategoryDataFromDB : succ,"+userid+"#_#"+cate+","+list1.size());
			}
		} catch(Exception e) {
			logger.error("loadShopCategoryDataFromDB:"+e+",chkMd="+chkMd+","+sql.toString());
			//RFServlet.instance.dbStatus=false;
		} finally {
			try{if(rs !=null) rs.close();} catch(Exception e){};
			try{if(stmt !=null) stmt.close();} catch(Exception e){};
			try{if(ustmt !=null) ustmt.close();} catch(Exception e){};
			try{if(conn !=null) conn.close();} catch(Exception e){};
		}
		return  list1;
	}
	/*
	public ArrayList loadShopCategoryDataFromDB(String userid,String cate){
		Connection conn = null;
		Statement stmt = null;
		Statement stmt1 = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		StringBuffer sql=new StringBuffer();
		ArrayList list1=new ArrayList();
		if(RFServlet.instance.instance.dbStatus==false){
			return list1;
		}
		try {
			conn=DBManager.getConnection("dreamdb");
			int pcnt= 0;
			
			if( cate!=null && !cate.equals("") ){
				try{
					stmt1=conn.createStatement();
					rs1=stmt1.executeQuery("SELECT pcnt FROM SHOP_DATA_CATECNT b WHERE userid='"+ userid +"' AND cate1='"+ cate +"' COLLATE utf8_general_ci limit 1 ");
					while(rs1.next()){
						pcnt= rs1.getInt("pcnt");
					}
				}catch(Exception e){
					pcnt=0;
				}finally{
					try{if(rs1 !=null) rs1.close();} catch(Exception e){}
					try{if(stmt1 !=null) stmt1.close();} catch(Exception e){}
				}
			}
			
			sql.append("SELECT CONCAT(userid,'_',cate1) k1 \n");
			sql.append(", no, url, pcode, pnm, price, imgpath, purl, cate1 \n");
			//sql.append(", CASE WHEN LEFT(lastupdate,10)!=LEFT(NOW(),10) THEN 1 ELSE 0 END chk_img \n");
			sql.append("FROM SHOP_DATA WHERE IMGPATH!='' \n");
			sql.append("and status='Y' \n");
			//sql.append("and down_ok='Y' \n");
			sql.append("and userid='"+userid+"' \n");
			if( pcnt!=0 ){
				sql.append("and regdate > NOW() + INTERVAL -30 DAY \n");
			}
			if( cate!=null && cate.length()>0 && pcnt>6 ){
				sql.append("and cate1='"+cate+"' COLLATE utf8_general_ci \n");
			}
			sql.append("order by rand() limit 20 \n");
			logger.debug(sql.toString());
			
			stmt=conn.createStatement();
			rs=stmt.executeQuery(sql.toString());
			while(rs.next()){
				if(list1.size()>=20) break;
				
				String imgpath= rs.getString("imgpath");
				//filename= "http://www.dreamsearch.or.kr/ad/sr/"+ userid +"/"+ filename;
				
				ShopData sd= new ShopData();
				String keyStr=rs.getString("k1");
				sd.setNO(rs.getLong("no"));
				sd.setURL(rs.getString("url"));
				sd.setPCODE(rs.getString("pcode"));
				sd.setPNM(rs.getString("pnm"));
				sd.setPRICE(rs.getString("price"));
				sd.setIMGPATH( imgpath );
				sd.setPURL(rs.getString("purl"));
				sd.setCATE1(rs.getString("cate1"));
				list1.add(sd);
			}
			if(cate!=null && cate.length()>0){
				Element element=new Element(userid+"#_#"+cate,list1);
				RFServlet.instance.adInfoCache.shopCategoryDataCache.put(element);
			}
			logger.debug("loadShopCategoryDataFromDB : succ");
		} catch(Exception e) {
			logger.error("loadShopCategoryDataFromDB:"+e);
			RFServlet.instance.dbStatus=false;
		} finally {
			try{if(rs !=null) rs.close();} catch(Exception e){};
			try{if(stmt !=null) stmt.close();} catch(Exception e){};
			try{if(conn !=null) conn.close();} catch(Exception e){};
		}
		return  list1;
	}*/
	public ShopData loadShopPCodeDataFromDB(String userid,String pcode){
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		StringBuffer sql=new StringBuffer();
		ShopData sd=null;
		if(RFServlet.instance.instance.dbStatus==false){
			return sd;
		}
		try {
			pcode=pcode.replaceAll("'","");
			sql.append("SELECT ifnull(CONCAT(userid,'_',pcode),'') k1													\n");
			sql.append(", ifnull(url,'') url, ifnull(pcode,'') pcode, ifnull(pnm,'') pnm, ifnull(price,'') price        \n");
			sql.append(", ifnull(imgpath,'') imgpath, ifnull(purl,'') purl,ifnull(cate1,'') cate1                       \n");
			sql.append(", lastupdate, no                                                                                \n");
			sql.append("FROM SHOP_DATA WHERE IMGPATH!=''                                                                \n");
			sql.append("and status='Y'                                                                                  \n");
			sql.append("and userid='"+userid+"' \n");
			sql.append("and pcode='"+pcode+"' \n");
			logger.debug(sql.toString());
			conn=DBManager.getConnection("dreamdb");
			stmt=conn.createStatement();
			rs=stmt.executeQuery(sql.toString());
			if(rs.next()){
				String imgpath= rs.getString("imgpath");
				//String imafile= "";//imgpath.split("/")[ imgpath.split("/").length-1 ];
				//imafile= "http://www.dreamsearch.or.kr/ad/sr/"+ userid +"/"+ imafile;
				//imafile=imgpath;

				sd= new ShopData();
				String keyStr=rs.getString("k1");
				sd.setURL(rs.getString("url"));
				sd.setPCODE(rs.getString("pcode"));
				sd.setPNM(rs.getString("pnm"));
				sd.setPRICE(rs.getString("price"));
				sd.setIMGPATH(imgpath);
				sd.setPURL(rs.getString("purl"));
				sd.setCATE1(rs.getString("cate1"));
				sd.setLASTUPDATE(rs.getTimestamp("lastupdate"));
				sd.setNO(rs.getLong("no"));
				Element element=new Element(keyStr,sd);
				RFServlet.instance.adInfoCache.shopPCodeDataCache.put(element);
				/*
				if( FileUtil.imgURLCheck(rs.getString("imgpath"))){
					RFServlet.instance.adInfoCache.shopPCodeDataCache.put(element);
					Statement stmt2=null;
					try{
						stmt2=conn.createStatement();
						String sql2="update SHOP_DATA set status='Y',lastupdate=now() where userid='"+userid+"' and pcode='"+rs.getString("pcode")+"'";
						stmt2.executeUpdate(sql2);
					}catch(Exception e){
						logger.error("loadShopCategoryDataFromDB:"+e);
						logger.debug("rs.getString(imgpath)="+rs.getString("imgpath"));
					}finally{
						if(stmt2!=null) stmt2.close();
					}
				}else{
					Statement stmt2=null;
					try{
						stmt2=conn.createStatement();
						String sql2="update SHOP_DATA set status='N',lastupdate=now() where userid='"+userid+"' and pcode='"+rs.getString("pcode")+"'";
						stmt2.executeUpdate(sql2);
					}catch(Exception e){
						logger.error("loadShopCategoryDataFromDB:"+e);
						logger.debug("rs.getString(imgpath)="+rs.getString("imgpath"));
					}finally{
						if(stmt2!=null) stmt2.close();
					}
				}
				*/
			}
			logger.debug("loadShopPCodeDataFromDB : succ");
		} catch(Exception e) {
			logger.error("loadShopPCodeDataFromDB:"+e+","+sql.toString());
			//RFServlet.instance.dbStatus=false;
		} finally {
			try{if(rs !=null) rs.close();} catch(Exception e){};
			try{if(stmt !=null) stmt.close();} catch(Exception e){};
			try{if(conn !=null) conn.close();} catch(Exception e){};
		}
		return sd;
	}
	public HashMap getShopLogDataFromIp(String ip,String product){
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		StringBuffer sql=new StringBuffer();
		HashMap<String,ShopData> sh=new HashMap<String,ShopData>();
		if(RFServlet.instance.instance.dbStatus==false){
			return sh;
		}
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
			sql.append(" order by no desc \n");
			sql.append(" limit 20 \n");
			//if(limit>0){
			//	sql.append(" limit "+ limit);
			//}
			logger.debug("Adbn==>"+sql.toString());
			conn=DBManager.getConnection("dreamdb");
			stmt=conn.createStatement();
			rs=stmt.executeQuery(sql.toString());
			logger.debug("SHOP_LOG Query : "+ product+","+ip);
			String site_codes="";
			HashMap filterMap=new HashMap();
			int rCnt=0;
			while(rs.next()){
				rCnt++;
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
				String key=String.format("%020d",(1000000000000000l-rs.getLong("no")));
				
				if( site_codes.indexOf( rs.getString("site_code") )==-1 && rs.getString("mcgb").equals("") ){
					sd.setFlag("1");
				}else{
					sd.setFlag("0");
				}
				if( rs.getString("mcgb").equals("") ){
					site_codes += rs.getString("site_code") +",";
				}
				/* 상품로그 필터 추가 */
				if(!filterMap.containsKey(rs.getString("site_code"))){
					SiteCodeData sfd=RFServlet.instance.adInfoCache.getAdSiteStatus(rs.getString("site_code"));
					if(sfd==null){
						sfd=RFServlet.instance.adInfoCache.getIadSiteStatus(rs.getString("site_code"));
						if(sfd==null){
							continue;
						}
					}else{
						PointData p=RFServlet.instance.adInfoCache.getAdCacheData(sfd.getUserid());
						if(p==null || p.getPoint()<=0) continue;
					}
				}
				if(filterMap.containsKey(rs.getString("site_code"))){
					int pcnt=Integer.parseInt(filterMap.get(rs.getString("site_code")).toString());
					pcnt++;
					if(pcnt<10){
						sh.put(key,sd);
						filterMap.put(rs.getString("site_code"),pcnt);
					}else{
						continue;
					}	
				}else{
					filterMap.put(rs.getString("site_code"),1);
					sh.put(key,sd);
				}
				if(filterMap.size()>5) break;
				
			}
			if(sh.size()>0){
				Element in=new Element(ip,sh);
				RFServlet.instance.adInfoCache.shopLogCache.put(in);
			}
		} catch(Exception e) {
			logger.error("getShopLogDataFromIp:"+e);
			RFServlet.instance.dbStatus=false;
		} finally {
			try{if(rs !=null) rs.close();} catch(Exception e){};
			try{if(stmt !=null) stmt.close();} catch(Exception e){};
			try{if(conn !=null) conn.close();} catch(Exception e){};
		}
		return sh;
	}
	/*public void getMediaCodeFromDB(AdInfoCache mediaCodeCache){
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		StringBuffer sql=new StringBuffer();
		ArrayList list=new ArrayList();
		if(RFServlet.instance.instance.dbStatus==false){
			return;
		}
		try {
			sql.append(" select a.media_code                     \n");
			sql.append(" from iurlmatch a, iadsite b, admember c                                         \n");
			sql.append(" where a.site_code=b.site_code and b.userid=c.userid \n");
			sql.append(" and b.state='Y' and b.gubun='UM'                                                \n");
			logger.debug(sql.toString());
			conn=DBManager.getConnection("dreamdb");
			stmt=conn.createStatement();
			rs=stmt.executeQuery(sql.toString());
			while(rs.next()){
				logger.debug(rs.getString("media_code")+" Cached.");
				Element element=new Element(rs.getString("media_code"),rs.getString("media_code"));
				((Ehcache) mediaCodeCache).put(element);
				logger.debug("load mediaCode From Database: mediaCode");
			}
		} catch(Exception e) {
			logger.error("getMediaCodeFromDB:"+e);
			RFServlet.instance.dbStatus=false;
		} finally {
			try{if(rs !=null) rs.close();} catch(Exception e){};
			try{if(stmt !=null) stmt.close();} catch(Exception e){};
			try{if(conn !=null) conn.close();} catch(Exception e){};
		}
	}*/
	public static void main(String[] args){
		DataMapper up=new DataMapper();
	}
	
	public ArrayList<AdConfigData> getApiList(String keyeord1, String keyeord2, String adgubun, String mc,int length) {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		StringBuffer sql=new StringBuffer();
		ArrayList<AdConfigData> list = new ArrayList<AdConfigData>();
		//날짜생성
		Calendar cal=Calendar.getInstance();
		int w=cal.get(Calendar.DAY_OF_WEEK);
		SimpleDateFormat yyyymmdd = new SimpleDateFormat("yyyyMMdd");
		java.util.Date date=new java.util.Date();
		String ymd=yyyymmdd.format(date);
		String wSql="";
		switch(w){
			case 1:wSql=" AND b.sun = 'y' ";break;
			case 2:wSql=" AND b.mon = 'y' ";break;
			case 3:wSql=" AND b.tue = 'y' ";break;
			case 4:wSql=" AND b.wed = 'y' ";break;
			case 5:wSql=" AND b.thu = 'y' ";break;
			case 6:wSql=" AND b.fri = 'y' ";break;
			case 7:wSql=" AND b.sat = 'y' ";break;
		}
		//기본쿼리
		sql.append("SELECT IFNULL(b.state,'') stateW, IFNULL(m.state,'') stateM, a.no, a.media_no, a.site_code, a.script_no								\n");
		sql.append(", b.userid, b.site_url, b.url_style, f.adtxt, f.ad_dcodeno                      \n");
		sql.append(", b.dispo_sex, b.dispo_age, usemoney, usedmoney, ad_rhour ,m.site_etc                     \n");
		sql.append(", c.imgname_logo, c.site_title, c.site_name, c.site_desc,b.adweight                        \n");
		sql.append(", m.banner_path1, m.banner_path2, m.site_desc site_descm, m.site_url site_urlm	,IFNULL(c.imgname_schonlogo,c.imgname_logo)AS logo	  \n");
		sql.append("FROM adsite b JOIN adtype_list f                                                \n");
		sql.append("ON b.site_code=f.site_code JOIN media_script g                                  \n");
		sql.append("ON g.ad_type=f.ad_dcodeno JOIN admember c										\n");
		sql.append("ON b.userid=c.userid JOIN adlink a USE INDEX(site_media)												\n");
		sql.append("ON a.script_no=g.no AND a.site_code=b.site_code LEFT JOIN adsite_mobile m       \n");
		sql.append("ON b.site_code=m.site_code 														\n");
		sql.append("WHERE a.adyn='Y' AND b.gubun='"+adgubun+"'										\n");
		sql.append("AND (m.state='Y' OR b.state='Y')                                                \n");
		sql.append("AND c.point >= 1000                                                             \n");
		sql.append("AND (b.usemoney > (b.usedmoney+b.usemoney/10) OR b.usemoney=0)                  \n");
		sql.append("AND ((b.use_f_day='' AND b.use_t_day = '') OR (b.use_f_day <= '"+ymd+"' AND b.use_t_day >= '"+ymd+"')) \n");
		sql.append(""+wSql+"																		\n");
		sql.append("AND a.script_no = '"+mc+"'														\n");
		try {
			//광고주 아이디,상품코드 가 있을 경우에만 데이터 조회
			if(adgubun.equals("SR")){
				logger.debug("::::::::상품조회::::::::::");
				StringBuffer srSql=new StringBuffer();
				srSql.append("SELECT IFNULL(b.state,'') stateW, IFNULL(m.state,'') stateM, a.no, a.media_no, a.site_code, a.script_no								\n");
				srSql.append(", b.userid, b.site_url, b.url_style, f.adtxt, f.ad_dcodeno, q.price ,q.purl,q.pnm          \n");
				srSql.append(", b.dispo_sex, b.dispo_age, usemoney, usedmoney, ad_rhour ,m.site_etc                \n");
				srSql.append(", c.imgname_logo, c.site_title, c.site_name, c.site_desc,b.adweight                        \n");
				srSql.append(", q.imgpath AS banner_path1, m.banner_path2, m.site_desc site_descm, m.site_url site_urlm	,IFNULL(c.imgname_schonlogo,c.imgname_logo)AS logo	  \n");
				srSql.append("FROM adsite b JOIN adtype_list f                                                \n");
				srSql.append("ON b.site_code=f.site_code JOIN media_script g                                  \n");
				srSql.append("ON g.ad_type=f.ad_dcodeno JOIN admember c										\n");
				srSql.append("ON b.userid=c.userid JOIN adlink a USE INDEX(site_media)												\n");
				srSql.append("ON a.script_no=g.no AND a.site_code=b.site_code LEFT JOIN adsite_mobile m       \n");
				srSql.append("ON b.site_code=m.site_code 														\n");
				srSql.append("JOIN (SELECT * FROM SHOP_DATA WHERE userid = '"+keyeord1+"' AND pcode = '"+keyeord2+"') q	\n");
				srSql.append("ON c.userid = q.userid 														\n");
				srSql.append("WHERE a.adyn='Y' AND b.gubun='"+adgubun+"'									\n");
				srSql.append("AND (m.state='Y' OR b.state='Y')                                                \n");
				srSql.append("AND c.point >= 1000                                                             \n");
				srSql.append("AND (b.usemoney > (b.usedmoney+b.usemoney/10) OR b.usemoney=0)                  \n");
				srSql.append("AND ((b.use_f_day='' AND b.use_t_day = '') OR (b.use_f_day <= '"+ymd+"' AND b.use_t_day >= '"+ymd+"')) \n");
				srSql.append(""+wSql+"																		\n");
				srSql.append("AND a.script_no = '"+mc+"'														\n");
				srSql.append("ORDER BY RAND() LIMIT "+length+"\n");
				conn=DBManager.getConnection("dreamdb");
				stmt=conn.createStatement();
				rs=stmt.executeQuery(srSql.toString());
				while(rs.next()){
					AdConfigData afd = new AdConfigData();
					afd.setUserid(rs.getString("userid"));
					afd.setSite_code(rs.getString("site_code"));
					afd.setSite_name(rs.getString("pnm"));
					afd.setSite_desc(rs.getString("site_desc"));
					afd.setPrice(rs.getString("price"));
					afd.setImgname_logo(rs.getString("logo"));
					afd.setBanner_path1(rs.getString("banner_path1"));
					afd.setBanner_path2(rs.getString("banner_path2"));
					afd.setMcgb("");
					afd.setPurl(rs.getString("purl"));
					afd.setSite_urlm(rs.getString("site_urlm"));
					afd.setSite_url(rs.getString("site_url"));
					afd.setSite_etc(rs.getString("site_etc"));
					list.add(afd);
				}
			}
			//키워드 가 있을 경우에만 데이터 조회
			else if(adgubun.equals("KL")){
				logger.debug("::::::::키워드조회::::::::::");
				sql.append("AND EXISTS (SELECT 'X' FROM keywordlink WHERE site_code = a.site_code \n");
				sql.append("AND media_code like '%"+keyeord1+"%')\n");
				sql.append("ORDER BY RAND() LIMIT "+length+"\n");
				conn=DBManager.getConnection("dreamdb");
				stmt=conn.createStatement();
				rs=stmt.executeQuery(sql.toString());
				while(rs.next()){
					AdConfigData afd = new AdConfigData();
					afd.setUserid(rs.getString("userid"));
					afd.setSite_code(rs.getString("site_code"));
					afd.setSite_name(rs.getString("site_name"));
					afd.setSite_desc(rs.getString("site_desc"));
					afd.setPrice("");
					afd.setImgname_logo("");
					afd.setBanner_path1(rs.getString("banner_path1"));
					afd.setBanner_path2(rs.getString("banner_path2"));
					afd.setMcgb("");
					afd.setSite_urlm(rs.getString("site_urlm"));
					afd.setSite_url(rs.getString("site_url"));
					afd.setSite_etc(rs.getString("site_etc"));
					list.add(afd);
				}
			}
			//url 이 있을 경우에만 데이터 조회
			else if(adgubun.equals("UM")){
				logger.debug("::::::::URL조회::::::::::");
				sql.append("AND EXISTS (SELECT 'X' FROM urlmatch WHERE site_code = a.site_code \n");
				sql.append("AND media_code like '%"+keyeord1+"%')\n");
				sql.append("ORDER BY RAND() LIMIT "+length+"\n");
				conn=DBManager.getConnection("dreamdb");
				stmt=conn.createStatement();
				rs=stmt.executeQuery(sql.toString());
				while(rs.next()){
					AdConfigData afd = new AdConfigData();
					afd.setUserid(rs.getString("userid"));
					afd.setSite_code(rs.getString("site_code"));
					afd.setSite_name(rs.getString("site_name"));
					afd.setSite_desc(rs.getString("site_desc"));
					afd.setPrice("");
					afd.setImgname_logo("");
					afd.setBanner_path1(rs.getString("banner_path1"));
					afd.setBanner_path2(rs.getString("banner_path2"));
					afd.setMcgb("");
					afd.setSite_urlm(rs.getString("site_urlm"));
					afd.setSite_url(rs.getString("site_url"));
					afd.setSite_etc(rs.getString("site_etc"));
					list.add(afd);
				}
			}
			//베이스조회
			else{
				logger.debug("::::::::BASE조회::::::::::");
				sql.append("ORDER BY RAND() LIMIT "+length+"\n");
				logger.debug(sql.toString());
				conn=DBManager.getConnection("dreamdb");
				stmt=conn.createStatement();
				rs=stmt.executeQuery(sql.toString());
				
				while(rs.next()){
					AdConfigData afd = new AdConfigData();
					afd.setUserid(rs.getString("userid"));
					afd.setSite_code(rs.getString("site_code"));
					afd.setSite_name(rs.getString("site_name"));
					afd.setSite_desc(rs.getString("site_desc"));
					afd.setPrice("");
					afd.setImgname_logo("");
					afd.setBanner_path1(rs.getString("banner_path1"));
					afd.setBanner_path2(rs.getString("banner_path2"));
					afd.setMcgb("");
					afd.setSite_urlm(rs.getString("site_urlm"));
					afd.setSite_url(rs.getString("site_url"));
					afd.setSite_etc(rs.getString("site_etc"));
					afd.setAdtxt(rs.getString("adtxt"));
					list.add(afd);
				}
			}
		} catch (Exception e) {
			logger.info(e.getMessage());
		} finally {
			try{if(rs !=null) rs.close();} catch(Exception e){};
			try{if(stmt !=null) stmt.close();} catch(Exception e){};
			try{if(conn !=null) conn.close();} catch(Exception e){};
		}
		return list;
	}
	
}