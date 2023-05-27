package com.adgather.batchjob;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.adgather.resource.db.DBManager;
import com.adgather.util.NetworkUtils;


public class ShopDataItem {
	public static String dbip;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		new ShopDataItem().runProc1();
	}
	public void runProc1(){
		// TODO Auto-generated method stub
		
		ShopDataItem.dbip = NetworkUtils.getServerType().equals("real")?"54":"5";
		//ShopDataItem.dbip = "54";
        
        if( ShopDataItem.dbip==null ) return;
        
        System.out.println( "selected db "+ ShopDataItem.dbip );
		
		Connection conn = null;
		Statement stmt=null;
		Statement stmt2=null;
		Statement stmt3=null;
		Statement stmt4=null;
		Statement stmt_itemdown=null;
		ResultSet rs=null;
		try{
			StringBuffer sql= new StringBuffer();
			
			/*
			if( DateUtils.getDate("HH").equals("00") ){
				for( int i=0; i<30; i++ ) {
					sql.setLength(0);
					sql.append("INSERT INTO SHOP_DATA(USERID, URL, PNM, PCODE, PRICE, IMGPATH, PURL, GB, RDATE, RTIME, STATUS						\n");
					sql.append("	, CATE1, CATE2, CATE3, CATE4, CAID1, CAID2, CAID3, CAID4,LOADINFO,REGDATE,LASTUPDATE)                                           \n");
					sql.append("	SELECT * FROM (                                                                                                                 \n");
					sql.append("			SELECT B.userid, B.homepi URL, A.PNM, A.PCODE, A.PRICE, A.IMGPATH, A.PURL, '02' GB                                \n");
					sql.append("				, A.RDATE, A.RTIME, 'Y' STATUS                                                                          \n");
					sql.append("				, A.SC_TXT CATE1, '' CATE2, '' CATE3, '' CATE4, '' CAID1, '' CAID2, '' CAID3, '' CAID4                  \n");
					sql.append("				, 'S',NOW() REGDATE,NOW() LASTUPDATE                                                                     \n");
					sql.append("			FROM SHOP_LOG A, admember B,adsite C                                                                            \n");
					sql.append("			WHERE B.SHOPLOADURL='' AND A.RDATE= LEFT(REPLACE(DATE_ADD(NOW(), INTERVAL -1 DAY),'-',''),8)					\n");
					sql.append("			AND A.PARTID IN ( '"+StringUtils.getPartIdKey(i+"")+"' )           	\n");
					sql.append("			AND A.site_code=C.site_code                                                                                     \n");
					sql.append("			AND B.userid=C.userid                                                                                           \n");
					sql.append("			AND A.PNM!='' AND A.IMGPATH!='' AND A.PRICE>0 AND INSTR(A.IMGPATH,'http:')					\n");
					sql.append("			GROUP BY C.USERID,A.PCODE                                                                                       \n");
					sql.append("			ORDER BY C.USERID,A.PCODE                                                                                       \n");
					sql.append("		) A                                                                                                                     \n");
					sql.append("	ON DUPLICATE KEY UPDATE PNM=A.PNM                                                                                               \n");
					sql.append("		, PRICE=A.PRICE, IMGPATH=A.IMGPATH, PURL=A.PURL, GB=A.GB                                                                \n");
					sql.append("		, RDATE=A.RDATE, RTIME=A.RTIME, STATUS=A.STATUS                                                                         \n");
					sql.append("		, CATE1=A.CATE1, CATE2=A.CATE2, CATE3=A.CATE3, CATE4=A.CATE4                                                            \n");
					sql.append("		, CAID1=A.CAID1, CAID2=A.CAID2, CAID3=A.CAID3, CAID4=A.CAID4                                                            \n");
					sql.append("		, LOADINFO='S',LASTUPDATE=A.LASTUPDATE                                                                                  \n");
					
					System.out.println( sql.toString() );
					conn=DBManager.getConnection(ShopDataItem.dbip, "dreamsearch");
					stmt=conn.createStatement();
					stmt.executeUpdate(sql.toString());
					stmt.close();
				}
			}
			*/
			
			sql.setLength(0);
			sql.append(" SELECT a.userid, a.homepi, a.MALLNM, a.SHOPLOADURL \n"); 
			sql.append(" FROM admember a, ( \n");
			sql.append(" 	SELECT userid, state FROM adsite WHERE gubun='sr' AND state='Y' GROUP BY userid, state UNION \n");
			sql.append(" 	SELECT userid, state FROM iadsite WHERE gubun='sr' AND state='Y' GROUP BY userid, state \n");
			sql.append(" ) b \n");
			sql.append(" WHERE a.userid=b.userid \n"); 
			sql.append(" AND a.MALLNM NOT IN ('','') AND INSTR(a.SHOPLOADURL,'http') \n");
			sql.append(" AND LENGTH(shoploadurl)>0 \n");

			conn=DBManager.getConnection(ShopDataItem.dbip, "dreamsearch");
			stmt=conn.createStatement();
			rs= stmt.executeQuery(sql.toString());
			while(rs.next()){
				String MALLNM= rs.getString("MALLNM");
				System.out.println( rs.getString("userid") );
				System.out.println( rs.getString("MALLNM") );
				
				if( MALLNM.equals("makeshop") ){
					ShopDataItem.getEnuri( conn, rs.getString("userid"), rs.getString("SHOPLOADURL") );
				}else {
					ShopDataItem.getNaver( conn, rs.getString("userid"), rs.getString("SHOPLOADURL") );
				}
			}
			/*
			stmt2=conn.createStatement();
			stmt2.executeUpdate("truncate table SHOP_DATA_CATECNT");
			
			stmt3=conn.createStatement();
			sql.setLength(0);
			sql.append("INSERT INTO SHOP_DATA_CATECNT(userid,cate1,pcnt) \n");
			sql.append("SELECT * FROM ( \n");
			sql.append("	SELECT userid, cate1, COUNT(*) c \n");
			sql.append("	FROM SHOP_DATA \n");
			sql.append("	WHERE regdate > NOW() + INTERVAL -30 DAY AND STATUS='Y' \n");
			sql.append("	GROUP BY userid, cate1 \n");
			sql.append(")a \n");
			sql.append("ON DUPLICATE KEY UPDATE pcnt=a.c \n");
			stmt3.executeUpdate(sql.toString());
			*/
			sql.setLength(0);
			sql.append("UPDATE SHOP_DATA \n");
			sql.append("SET STATUS='N' \n");
			sql.append("WHERE LASTUPDATE < DATE_ADD(NOW(), INTERVAL -1 DAY); \n");
			stmt_itemdown = conn.createStatement();
			stmt_itemdown.executeUpdate(sql.toString());
			
			stmt4=conn.createStatement();
			stmt4.executeUpdate("CALL proc_CacheUpdateExec('shopDataCache','') ");
			
		}catch(Exception e){
			System.out.println(e );
		}finally{
			try{if(rs !=null) rs.close();} catch(Exception e){}
			try{if(stmt !=null) stmt.close();} catch(Exception e){}
			try{if(stmt2 !=null) stmt2.close();} catch(Exception e){}
			try{if(stmt3 !=null) stmt3.close();} catch(Exception e){}
			try{if(stmt4 !=null) stmt4.close();} catch(Exception e){}
			try{if(stmt_itemdown!=null) stmt_itemdown.close();} catch(Exception e){}
			try{if(conn !=null) conn.close();} catch(Exception e){}
		}
	}
	public static void getNaver(Connection conn, String userid, String uu){
		String massUrl= uu;
		
		if( massUrl.indexOf("http:")==-1 ) massUrl= "http://"+ massUrl;
		String domain= massUrl.split("/")[ 2 ];
		System.out.println( massUrl ) ;

		String line = "";
		StringBuffer sql= new StringBuffer();
		InputStream input=null;	
		BufferedReader reader=null;
	
		try {
		    URL url = new URL(massUrl);
		    HttpURLConnection connect = (HttpURLConnection)url.openConnection();
		    connect.setRequestProperty("Content-Type", "text/html;charset-UTF8");
		    connect.connect();
		    Object content = connect.getContent();
		    input = (InputStream)content;
		    reader = new BufferedReader(new InputStreamReader(input,"euc-kr"));
		    
			String pcode,pnm,price,purl,imgpath,cate;
			pcode=pnm=price=purl=imgpath=cate="";
			
			while((line = reader.readLine()) != null){
				if( line.indexOf("<<<mapid")>-1 ){
					pcode= line.substring(line.indexOf(">>>")+3, line.length() );
					pcode=pcode.replaceAll("'","");
				}
				if( line.indexOf("<<<pname")>-1 ){
					pnm= line.substring(line.indexOf(">>>")+3, line.length() );
					if( pnm.length()>=50 ) pnm = pnm.substring(0, 45);
				}
				if( line.indexOf("<<<price")>-1 ){
					price= line.substring(line.indexOf(">>>")+3, line.length() );
				}
				if( line.indexOf("<<<pgurl")>-1 ){
					purl= line.substring(line.indexOf(">>>")+3, line.length() );
				}
				if( line.indexOf("<<<igurl")>-1 ){
					imgpath= line.substring(line.indexOf(">>>")+3, line.length() );
				}
				if( line.indexOf("<<<caid1")>-1 ){
					cate= line.substring(line.indexOf(">>>")+3, line.length() );
				}
				if( line.indexOf("<<<begin")>-1 ){
					if( !pcode.equals("") ){
						System.out.println( pcode +" "+ pnm +" "+ price +" "+ purl +" "+ imgpath +" "+ cate );
						
						sql.setLength(0);
						sql.append("insert into SHOP_DATA(userid, url, pnm, pcode, price, imgpath,purl,gb,rdate,rtime \n");
						sql.append(", cate1, cate2, cate3, cate4, caid1, caid2, caid3, caid4,LOADINFO,REGDATE) \n");
						sql.append("values('"+ userid +"', '"+ domain +"', '"+ pnm +"', '"+ pcode +"', '"+ price +"' \n");
						sql.append(", '"+ imgpath +"', '"+ purl +"', '' \n");
						sql.append(", replace(left(now(),10),'-',''), '0101', '"+cate+"', '', '', '', '', '', '', '','U',now()) \n");
						sql.append("ON DUPLICATE KEY update pnm='"+ pnm +"', price='"+price+"', imgpath='"+imgpath+"', cate1='"+cate+"' \n");
						sql.append(", status='Y', LOADINFO='U', LASTUPDATE=NOW() ; \n");
						
						Statement stmt = null;
						try {
							stmt=conn.createStatement();
							stmt.executeUpdate( sql.toString() );
						} catch(Exception e) {
							System.out.println( "execute err "+ e.toString() +" sql ="+ sql.toString() );
						} finally {
							try{if(stmt !=null) stmt.close();} catch(Exception e){}
						}
					}
					pnm=price=purl=imgpath=cate="";
				}
			}
			
		}catch(Exception e){
			System.out.println( "getNaver(220) err"+ e.toString() );
		}finally{
			try{		if(input !=null) input.close();		}catch(Exception e){}
			try{		if(reader !=null) reader.close();	}catch(Exception e){}
		}
	}
	public static void getEnuri(Connection conn, String userid, String url){
		try{
			String massUrl= url;
			if( massUrl.indexOf("http:")==-1 ) massUrl= "http://"+ massUrl;
			String domain= massUrl.split("/")[ 2 ];
			
			String filename = massUrl.split("/")[ massUrl.split("/").length-1 ];
			String path = massUrl.replace(filename, "");
			
			System.out.println( massUrl );
			
			Document doc = Jsoup.connect( massUrl ).get();
			Elements item= doc.select("tr a");
			String cate= "";
			StringBuffer sql= new StringBuffer();
			
			for( int i=0; i<item.size(); i++){
				
				String href= path + item.get(i).attr("href");
				
				cate= item.get(i).text();
				
				if( href.indexOf("=")>-1 ){
					for( int page=1; page<50; page++ ){
						String suburl= href+"&page="+page;
						System.out.println( suburl );
						
						Document doc2= Jsoup.connect( suburl ).get();
						if( doc2.select("tbody tr").size()>1 ){
							Elements tr= doc2.select("tbody tr");
							for( int k=1; k<tr.size(); k++ ){
								String pnm= tr.get(k).select("td a").get(0).text();
								String purl= tr.get(k).select("td a").get(0).attr("href");
								String price= tr.get(k).select("td").get(2).html().replace(",", "");
								String state= tr.get(k).select("td").get(3).html();
								String imgpath= tr.get(k).select("td").get(5).html();
								String pcode= tr.get(k).select("td").get(9).html();
								pcode=pcode.replaceAll("'","");
								
								if( pnm.length()>=50 ) pnm = pnm.substring(0, 45);
								if( state.indexOf("ÀÖÀ½")>-1 ) state="Y"; else state="N";
								
								System.out.println( cate +", "+ pnm +"| "+ url +"| "+ price +"| "+ state +"| "+ imgpath +"| "+ pcode ); 
								
								sql.setLength(0);
								sql.append("insert into SHOP_DATA(userid, url, pnm, pcode, price, imgpath,purl,gb,rdate,rtime \n");
								sql.append(", cate1, cate2, cate3, cate4, caid1, caid2, caid3, caid4,LOADINFO,REGDATE,LASTUPDATE) \n");
								sql.append("values('"+ userid +"', '"+ domain +"', '"+ pnm +"', '"+ pcode +"', '"+ price +"' \n");
								sql.append(", '"+ imgpath +"', '"+ purl +"', '', replace(left(now(),10),'-',''), '0101', '"+cate+"', '', '', '', '', '', '', '','U',now(),now()) \n");
								sql.append("ON DUPLICATE KEY update pnm='"+pnm+"', price='"+price+"', imgpath='"+imgpath+"' \n");
								sql.append(", cate1='"+cate+"', status='"+state+"', LOADINFO='U', LASTUPDATE=NOW() ; \n");
								
								Statement stmt = null;
								try {
									stmt=conn.createStatement();
									stmt.executeUpdate( sql.toString() );
								} catch(Exception e) {
									System.out.println( "execute err "+ e.toString() +" sql="+ sql.toString() );
								} finally {
									try{if(stmt !=null) stmt.close();} catch(Exception e){}
								}
							}
						}else{
							break;
						}
					}
				}				
			}
			
		}catch(Exception e){
			System.out.println( "getEnuri err "+ e.toString() );
		}
	}

}
