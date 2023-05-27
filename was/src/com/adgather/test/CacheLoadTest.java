package com.adgather.test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import com.adgather.reportmodel.AdConfigData;
import com.adgather.resource.db.DBManager;
import com.adgather.util.DateUtils;

public class CacheLoadTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		CacheManager cache= CacheManager.create("D:/work/enliple/www/webapp/public_html/WEB-INF/classes/ehcache.xml");

        Cache caches = cache.getCache("MediaCodeCache");

        Element element= new Element("key", "inserted to cache");
        caches.put(element);

        System.out.println("Put in to cache"+ caches.get("key").getObjectValue() );
        
		
	}
	public void test(){
		CacheManager cacheManager= CacheManager.create();
		Cache MediaCodeCache=cacheManager.getCache("MediaCodeCache");
		
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT a.no, a.media_no,f.ad_dcodeno,a.site_code, b.userid,b.site_url,b.url_style,a.script_no,f.adtxt                   \n");
		sql.append(" , b.dispo_sex, b.dispo_age,usemoney,usedmoney,ad_rhour								\n");
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
			MediaCodeCache.removeAll();
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
				//System.out.println( s.getInfo("aa") );
				if(tmpS.equals(keyStr)){
					list=(ArrayList)MediaCodeCache.get(keyStr).getObjectValue();
					list.add(s);
				}else{
					list=new ArrayList();
					list.add(s);
				}
				Element element=new Element(keyStr,list);
				MediaCodeCache.put(element);
				tmpS=keyStr;
			}
		}catch(Exception ex){
			System.out.println( ex );
		}finally{
			DBManager.close(rs);
			DBManager.close(stmt);
			DBManager.close(conn);
		}
		System.out.println( MediaCodeCache.getSize() );
	}

}
