package com.adgather.servlet;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import com.adgather.beans.ManagementCookie;
import com.adgather.reportmodel.Dispo;
import com.adgather.reportmodel.DumpObject;
import com.adgather.reportmodel.PointData;
import com.adgather.reportmodel.ShopData;
import com.adgather.reportmodel.SiteCodeData;
import com.adgather.util.DateUtils;
import com.adgather.util.MassInformation;
import com.adgather.util.StringUtils;


public class RFShopServlet extends HttpServlet {
	private DumpObject dumpObj;
	private ApplicationContext appContext;
	public ManagementCookie manageCookie;
	static Logger logger = Logger.getLogger(RFShopServlet.class);
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		int debug=0;
		try {

			
			request.setCharacterEncoding("8859_1");
			response.setHeader("Pragma", "no-cache");			
			response.setHeader("Cache-Control", "no-cache"); 
			//response.setHeader("P3P","CP='IDC DSP COR ADM DEVi TAIi PSA PSD IVAi IVDi CONi HIS OUR IND CNT'");
			response.setHeader("P3P","CP='CAO PSA CONi OTR OUR DEM ONL'");
			debug++;			
			ShopData crd=(ShopData)appContext.getBean("ShopData");
			
			String url=request.getParameter("url")==null ? "" : request.getParameter("url");
			String from=request.getParameter("from")==null ? "" : request.getParameter("from");
			if(from.equals("")){
				from=request.getHeader("Referer")==null ? "" : request.getHeader("Referer");
			}
			String pcode=request.getParameter("pcode")==null ? "" : request.getParameter("pcode");
			String pnm=request.getParameter("pnm")==null ? "" : request.getParameter("pnm");
			String price=request.getParameter("price")==null ? "0" : request.getParameter("price");
			String gb=request.getParameter("gb")==null ? "" : request.getParameter("gb");
			String purl=request.getParameter("purl")==null ? "" : request.getParameter("purl");
			String img=request.getParameter("img")==null ? "" : request.getParameter("img");
			String sc=request.getParameter("sc")==null ? "" : request.getParameter("sc");
			String cate=request.getParameter("cate")==null ? "" : request.getParameter("cate");
			String i_cover=request.getParameter("i_cover")==null ? "" : request.getParameter("i_cover");
			String cate1=request.getParameter("cate1")==null ? "" : request.getParameter("cate1");
			String ic_dur= manageCookie.getCookieInfo(request,"ic_dur");
			
			try{// d[{sc,st,et}] append, edit, delete
				if( ic_dur.indexOf(sc)>-1 ){
					String newday= DateUtils.getDate("yyyy-MM-dd HH:mm:ss");
				}else{
					ic_dur += sc+"=0#";
					//manageCookie.makeCookie("ic_dur", ic_dur, -1, response);
				}
				long diff_second = manageCookie.chkDuringTarget(request, ic_dur);
			    //is_diff5=true;
			}catch(Exception e){}
		    
			try{
				int tmp=Integer.parseInt(price);
			}catch(Exception e){
				price="0";
			}
			
			
			if(from.length()>200){
				from=from.substring(0,200);
			}
			if( url.equals("") ){
				url= from;
			}
			if( purl.equals("") ){
				purl=from;
			}
			String keyIp=RFServlet.instance.manageCookie.makeKeyCookie(request, response);
			manageCookie.makeCookie("IPNew", "",0, response);
			debug++;
			String ic_sr=manageCookie.getCookieInfo(request,"ic_sr");
			String push=manageCookie.getCookieInfo(request,"push");
			String dispo=manageCookie.getCookieInfo(request,"dispo");
			
			// url= java.net.URLDecoder.decode(url,"utf-8");
			String cUrl=StringUtils.getDestDomain(url);
			if(url.length()>200){
				url=url.substring(0,200);
			}
			String mcgb = "";
			logger.debug(from);

			debug++;
			if(from.toLowerCase().indexOf("search.naver.com")>0){
				mcgb="NV";
			}else if(from.toLowerCase().indexOf("search.yahoo.com")>0){
				mcgb="YH";
			}else if(from.toLowerCase().indexOf("search.daum.net")>0){
				mcgb="DU";
			}else if(from.toLowerCase().indexOf("search.zum.com")>0){
				mcgb="ZU";
			}else if(from.toLowerCase().indexOf("google.co.kr")>0){
				mcgb="GG";
			}else if(from.toLowerCase().indexOf("search.nate.com")>0){
				mcgb="NT";
			}else if(from.toLowerCase().indexOf("masterplan.co.kr")>0){
				mcgb="MP";
			}
			if(gb.equals("02")){
				purl = url;
			}else{
				purl = from;
			}
			
			if( from.indexOf("%")>-1 ){
				try{
					from=java.net.URLDecoder.decode(from,"utf-8");
				}catch(Exception e){}
			}
			if( url.indexOf("%")>-1 ){
				try{
					url=java.net.URLDecoder.decode(url,"utf-8");
				}catch(Exception e){}
			}
			if( purl.indexOf("%")>-1 ){
				try{
					purl=java.net.URLDecoder.decode(purl,"utf-8");
				}catch(Exception e){}
			}
			
			debug++;

			Calendar cal=Calendar.getInstance();
			SimpleDateFormat yyyymmdd = new SimpleDateFormat("yyyyMMdd");
			SimpleDateFormat hhmi = new SimpleDateFormat("kkmm");
			java.util.Date date=new java.util.Date();
			date.setTime(cal.getTimeInMillis());
			String ymd=yyyymmdd.format(date);
			String hm=hhmi.format(date);
			debug++;
			
			logger.debug("SHOP_LOG push="+ push);
			
			if(push==null || push.equals("")){
				//RFServlet.instance.adInfoCache.shopLogCache.remove(keyIp);
				
				//¼ºÇâÅ¸°Ù¿ë ÄíÅ°±Á±â 
				
				SiteCodeData iad= null;
				SiteCodeData ad= null;
				PointData pd=null;
				ad= RFServlet.instance.adInfoCache.getAdSiteStatus(sc);
				if( ad==null){
					iad= RFServlet.instance.adInfoCache.getIadSiteStatus(sc);
					if(iad!=null){
						String iSiteCode= iad.getSite_code();
						ad = iad.clone();
						
						if(iSiteCode==null || iSiteCode.length()==0){
							return;
						}else{
							pd=RFServlet.instance.adInfoCache.getAdCacheData(iad.getUserid());
						}
					}else{
						return;
					}
				}else{
					pd=RFServlet.instance.adInfoCache.getAdCacheData(ad.getUserid());
				}
				if( ad!= null){
					logger.debug(ad.getInfo("rfshop iad1"));
					
					try{
						// ¼ºÇâÅ¸°Ù¿ë ÄíÅ°
						String dispo_sex= ad.getDispo_sex();	if( dispo_sex==null ) dispo_sex="";
						String dispo_age= ad.getDispo_age();	if( dispo_age==null ) dispo_age="";
						String dispo_sum= dispo_sex +","+ dispo_age;
						
						if( dispo_sum!=null && !dispo_sum.equals("") ){
							String []tmp= dispo_sum.split(",");
							if( tmp.length>0 ){
								for(int i=0; i<tmp.length; i++){
									if( !tmp[i].equals("") && dispo.indexOf(tmp[i] +"=")<0 ){
										dispo += tmp[i] +"=0#";
									}
								}
							}
						}
						
						String rebuild= "";
						StringTokenizer st=new StringTokenizer(dispo,"#",false);
						while( st.hasMoreElements() ){
							String oneSell=st.nextElement().toString();
							StringTokenizer st2=new StringTokenizer(oneSell,"=",false);
							
							if( st2.countTokens()==2 ){
								String sCode= st2.nextElement().toString();
								String sValue= st2.nextElement().toString();
								if( StringUtils.gAtData("1,1,1,1,1,1,1,1,1,1", dispo_sum, sCode, ",").equals("1") ){
									int i= Integer.parseInt(sValue);
									i++;
									rebuild += sCode+"="+ (i) +"#";
								}else{
									rebuild += oneSell +"#" ;
								}
							}
						}
						
						// ÀçÁ¤·Ä
						String tmp_rebuild= "";
						
						String []tmp_dispo= rebuild.split("#");
						int dispocnt= tmp_dispo.length;
						Dispo []dispo_obj= new Dispo[dispocnt];
						
						for( int i=0; i<dispo_obj.length; i++ ){
							String name= tmp_dispo[i].substring(0, tmp_dispo[i].indexOf("="));
							String cnt= tmp_dispo[i].substring(tmp_dispo[i].indexOf("=")+1, tmp_dispo[i].length() );
							
							dispo_obj[i]= new Dispo(name, Integer.parseInt(cnt));
						}
						Arrays.sort(dispo_obj);
						for(int i=0; i<dispo_obj.length; i++){
							tmp_rebuild += dispo_obj[i].getName() +"="+ dispo_obj[i].getCnt() +"#";
						}
						
						manageCookie.makeCookie("dispo", tmp_rebuild, 365*24*60*60, response);
						
					}catch(Exception e){
						logger.error("rfshop[225 err "+ e);
					}
					
					
					if( ad.getUserid().equals("akmall") ){
						
						String real_url = "http://www.akmall.com/assc/assc_conv.jsp?assc_comp_id=27491&url="+ url;
						
						url = purl = real_url;
						
					} else if( url.indexOf("halfclub.com")>-1 ){
						
						String params = "";
						try{
							params = url.substring( url.indexOf("Detail?") );
						}catch(Exception e){		}
						
						String real_url = "http://www.halfclub.com/joins/withpang.asp?/"+ params;
						
						url = purl = real_url;
						
					} else if( ad.getSales_url()!=null && ad.getSales_url().indexOf("lotteimall.com")>-1 ){
						
						String goods_cd = StringUtils.getReg("goods_no=([\\d]*)", url).replace("goods_no=", "");
						
						if( !goods_cd.equals("")){
							url= ad.getSales_url().replace("{site_etc}", ad.getSite_etc() ) + goods_cd;
						}
						purl = url;
					}
					logger.debug("RFShop SHOP_URL : "+ url);
				}
				crd.setURL(url);
				crd.setRF(from);
				cate1=java.net.URLDecoder.decode(cate1,"utf-8");
				if(cate1.length()>100){
					cate1=cate1.substring(0,50)+"...";
				}
				crd.setSC_TXT(cate1);
				crd.setIP(keyIp);
				crd.setMCGB("");
				crd.setRDATE(ymd);
				crd.setRTIME(hm);
				//pnm=new String(pnm.getBytes("8859_1"),"euc-kr");
				pnm= java.net.URLDecoder.decode(pnm,"utf-8");
				if(pnm.length()>100){
					pnm=pnm.substring(0,50)+"...";
				}
				crd.setPNM(pnm);
				crd.setUSERID(ad.getUserid());
				crd.setPCODE(pcode);
				crd.setPURL(purl);
				crd.setGB(gb);
				crd.setSITE_CODE(sc);
				crd.setIMGPATH(img);
				crd.setPRICE(price);
				crd.setTARGETGUBUN("");
				
				logger.debug("crd.getURL()     ="+crd.getURL()     );
				logger.debug("crd.getRF()     ="+crd.getRF()     );
				logger.debug("crd.getSC_TXT()="+crd.getSC_TXT());
				logger.debug("crd.getIP()    ="+crd.getIP()    );
				logger.debug("crd.getMCGB()   ="+crd.getMCGB()   );
				logger.debug("crd.getRDATE()   ="+crd.getRDATE()   );
				logger.debug("crd.getRTIME()    ="+crd.getRTIME()    );
				logger.debug("crd.getPNM()     ="+crd.getPNM()     );
				logger.debug("crd.getPCODE() ="+crd.getPCODE() );
				logger.debug("crd.getPURL()   ="+crd.getPURL()   );
				logger.debug("crd.getGB()       ="+crd.getGB()       );
				logger.debug("crd.getSITE_CODE()="+crd.getSITE_CODE());
				logger.debug("crd.getIMGPATH() ="+crd.getIMGPATH() );
				logger.debug("crd.getPRICE() ="+crd.getPRICE() );
				
				if( crd.getURL().equals("") ){
					logger.error("rfshop[293] url none ");
				}
				
				dumpObj.getShopLogData().add(crd);
				int cookieDay=31;
				if(pd!=null) {
					logger.debug(pd.getInfo("shop[265] pd.info "));
					cookieDay=pd.getCookieDay();
				}
				//manageCookie.makeCookie("shop_log","shop_"+ pcode,cookieDay*24*60*60, response);
				String oldLog=manageCookie.getCookieInfo(request,"shop_log");
				oldLog=java.net.URLDecoder.decode(oldLog,"utf-8");
				String newVal=manageCookie.shopLogManage(1, oldLog,crd,"append");
				logger.debug("newVal="+newVal);
				newVal=java.net.URLEncoder.encode(newVal,"utf-8");
				manageCookie.makeCookie("shop_log",newVal,cookieDay*24*60*60, response);
				
				
			}else{
				manageCookie.makeCookie("push", "",0, response);  
			}
			

		} catch (Exception e) {
			logger.error("RFShopServlet:"+e+",debug="+debug); 
		}
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response){
		doGet(request,response);
	}
	public void init() throws ServletException {
		logger.info("RFShopServlet started");
		if (appContext == null) {
			RFServlet.instance.init();
			appContext=RFServlet.appContext;
		}
		manageCookie=(ManagementCookie)appContext.getBean("ManagementCookie");
		dumpObj=(DumpObject)appContext.getBean("DumpObject");
	}
	protected String[] getFileList() { 
        return new String[]{getServletConfig().getInitParameter("pathToContext")};
    }
}