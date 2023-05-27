package com.adgather.servlet;


import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import com.adgather.beans.ManagementCookie;
import com.adgather.reportmodel.DumpObject;
import com.adgather.reportmodel.PointData;
import com.adgather.reportmodel.ShopData;
import com.adgather.reportmodel.SiteCodeData;
import com.adgather.util.MassInformation;
import com.adgather.util.StringUtils;

public class RFShopEServlet extends HttpServlet {
	private DumpObject dumpObj;
	private ApplicationContext appContext;
	public ManagementCookie manageCookie;
	static Logger logger = Logger.getLogger(RFShopEServlet.class);
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		int debug=0;
		try {
			// 구현안해도됨
			// 131211 하프클럽에서만 사용하던거 하프클럽 광고 off됨
			// 구현안해도됨
			
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
			
			try{
				int tmp=Integer.parseInt(price);
			}catch(Exception e){
				price="0";
			}
			
			
			if(from.length()>200){
				from=from.substring(0,200);
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
			String scTxt=cate1;
			if(from!=null && from.length()>0){
				try{
					MassInformation mf = new MassInformation(RFServlet.instance.domainTable,from);				
					//scTxt=mf.GetDecodingKeyword();
				}catch(Exception e){
					logger.error("RFShopEServlet-MassInformation:"+e+" : "+ from);
				}
			}
			debug++;
			//if(scTxt.length()>100){
			//	scTxt=scTxt.substring(0,100);
			//}
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
			if(push.equals(url)){//ShopLog와 차이나는 부분 쿠키 push값
				SiteCodeData iad= null;
				SiteCodeData ad= null;
				PointData pd=null;		
				ad= RFServlet.instance.adInfoCache.getAdSiteStatus(sc);
				if( ad==null || ad.getSite_code().length()==0){
					iad= RFServlet.instance.adInfoCache.getIadSiteStatus(sc);
					String iSiteCode= iad.getSite_code();
					if(iSiteCode==null || iSiteCode.length()==0){
						return;
					}else{
						pd=RFServlet.instance.adInfoCache.getAdCacheData(iad.getUserid());
					}
				}else{
					pd=RFServlet.instance.adInfoCache.getAdCacheData(ad.getUserid());
				}
				
				crd.setURL(url);
				crd.setRF(from);
				crd.setSC_TXT(scTxt);
				crd.setIP(keyIp);
				crd.setMCGB("");
				crd.setRDATE(ymd);
				crd.setRTIME(hm);
				pnm=new String(pnm.getBytes("8859_1"),"euc-kr");
				if(pnm.length()>100){
					pnm=pnm.substring(0,95)+"...";
				}
				crd.setUSERID(ad.getUserid());
				crd.setPNM(pnm);
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
				
				dumpObj.getShopLogData().add(crd);
				int cookieDay=31;
				if(pd!=null) cookieDay=pd.getCookieDay();
				//manageCookie.makeCookie("shop_log","shop",cookieDay*24*60*60, response);
				String oldLog=manageCookie.getCookieInfo(request,"shop_log");
				oldLog=java.net.URLDecoder.decode(oldLog,"utf-8");
				String newVal=manageCookie.shopLogManage(1, oldLog,crd,"append");
				newVal=java.net.URLEncoder.encode(newVal,"utf-8");
				manageCookie.makeCookie("shop_log",newVal,cookieDay*24*60*60, response);
			}else{
				manageCookie.makeCookie("push", "",0, response);  
			}
		} catch (Exception e) {
			logger.error("RFShopEServlet:"+e+",debug="+debug); 
		}
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response){
		doGet(request,response);
	}
	public void init() throws ServletException {
		logger.info("RFShopEServlet started");
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