package com.adgather.servlet;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Locale;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.adgather.beans.AdInfoCache;
import com.adgather.beans.DataMapper;
import com.adgather.beans.DumpDataToDB;
import com.adgather.beans.ManagementCookie;
import com.adgather.reportmodel.DumpObject;
import com.adgather.reportmodel.RFData;
import com.adgather.util.FileUtil;
import com.adgather.util.MassInformation;
import com.adgather.util.StringUtils;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.ObjectWrapper;

public class RFServlet extends HttpServlet {
	public DumpObject dumpObj;
	public AdInfoCache adInfoCache;
	public DataMapper dataMapper;
	public ManagementCookie manageCookie;
	public DumpDataToDB dumpDb;
	public static RFServlet instance;
	public static ApplicationContext appContext;
	public static String DumpFailList;
	public static String MediaCodeXML;
	public static String AdTemplateDir;
	public static String CheckName;
	static Logger logger = Logger.getLogger(RFServlet.class);
	private long requestCounter=0;
	private int dumpThreadCounter=0;
	public int currDay=0;
	private boolean failDumpStatus=false;
	public static boolean dbStatus=true;
	public Hashtable<String, String> domainTable;
	public static boolean scheduleStatus=false;
	public static HashMap xml;
	public static Configuration cfg;
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		int debug=0;
		try {
			request.setCharacterEncoding("8859_1");
			response.setContentType("text/html;charset=euc-kr");
			response.setHeader("Pragma", "no-cache");			
			response.setHeader("Cache-Control", "no-cache"); 
			//response.setHeader("P3P","CP='IDC DSP COR ADM DEVi TAIi PSA PSD IVAi IVDi CONi HIS OUR IND CNT'");
			response.setHeader("P3P","CP='CAO PSA CONi OTR OUR DEM ONL'");
			debug++;			
			RFData crd=(RFData)appContext.getBean("RFData");
			String from=request.getParameter("from")==null ? "" : request.getParameter("from");
			String form=request.getParameter("form")==null ? "" : request.getParameter("form");
			if( from.equals("") && !form.equals("") ){
				from=form;
			}
			if(from.equals("")){
				from=request.getHeader("Referer")==null ? "" : request.getHeader("Referer");
			}
			String url=request.getParameter("url")==null ? "" : request.getParameter("url");
			
			try{
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
			}catch(Exception e){}
			
			if(from.length()>200){
				from=from.substring(0,200);
			}
			String keyIp=manageCookie.makeKeyCookie(request, response);
			manageCookie.makeCookie("IPNew", "",0, response);
			debug++;
			String ic_um=manageCookie.getCookieInfo(request,"ic_um");
			String ic_ki=manageCookie.getCookieInfo(request,"ic_ki");
			String push=manageCookie.getCookieInfo(request,"push");
			String ic_ki_tgt= manageCookie.getCookieInfo(request, "ic_ki_tgt");
			String ic_um_tgt= manageCookie.getCookieInfo(request, "ic_um_tgt");
			String chk_result=manageCookie.getCookieInfo(request,"chk_result");
			String chk_ck=manageCookie.getCookieInfo(request,"chk_ck");
			String cUrl=StringUtils.getDestDomain(url);
			 
			String mcgb = "";
			logger.debug("rf from="+ from);
			String scTxt=from;
			try{
				MassInformation mf = new MassInformation(domainTable,from);				
				scTxt=mf.GetDecodingKeyword();
				if(scTxt==null && scTxt.length()==0){
					logger.error("scTxt is empty : "+from);
				}
			}catch(Exception e){
				logger.error("RFServlet:"+e+" : "+ from);
			}
			debug++;
			if(scTxt.length()>100){
				scTxt=scTxt.substring(0,100);
			}
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
			debug++;
			
			String adgubun="";
			HashMap<String,String> addpq=new HashMap<String,String>();
			if(chk_result!=null && chk_result.length()>0){
				StringTokenizer st=new StringTokenizer(chk_result,"#",false);
				while(st.hasMoreElements()){
					String stCode=st.nextElement().toString();
					addpq.put(stCode, stCode);
				}
			}
			
			//	네이버 송출 차단용
			if(mcgb.equals("NV")){
				manageCookie.makeCookie("ic_mcgb",mcgb,60, response);	
			}else{
				manageCookie.makeCookie("ic_mcgb",mcgb,0, response);	
			}
			debug++;
			if(url.length()>200){
				url=url.substring(0,200);
			}
			
			SimpleDateFormat yyyymmdd = new SimpleDateFormat("yyyyMMdd");
			SimpleDateFormat hhmi = new SimpleDateFormat("kkmm");
			java.util.Date date=new java.util.Date();
			String ymd=yyyymmdd.format(date);
			String hm=hhmi.format(date);
			debug++;
			crd.setURL(url);
			crd.setCURL(cUrl);
			crd.setMCGB("");
			crd.setRF(from);
			crd.setSC_TXT(scTxt);
			crd.setIP(keyIp);
			crd.setRDATE(ymd);
			crd.setRTIME(hm);
			
			logger.debug("rf getURL="+crd.getURL());
			logger.debug("rf getCURL="+crd.getCURL());
			logger.debug("rf getMCGB="+crd.getMCGB());
			logger.debug("rf getRF="+crd.getRF());
			logger.debug("rf getSC_TXT="+crd.getSC_TXT());
			logger.debug("rf getIP="+crd.getIP());
			logger.debug("rf getRDATE="+crd.getRDATE());
			logger.debug("rf getRTIME="+crd.getRTIME());
			
			debug++;
			if(mcgb.length()>0 && scTxt.length()>0 ){
				dumpObj.getRf_KeywordData().add(crd);
				scTxt= java.net.URLEncoder.encode(scTxt,"euc-kr");
				
				int id1= StringUtils.gAtId(ic_ki, scTxt, "|||");
				ic_ki_tgt= StringUtils.gAtFirst1(ic_ki_tgt, id1, "|||");
				
				ic_ki= StringUtils.gAtFirst(ic_ki, scTxt, "|||");
				
				if(ic_ki.length()>100){
					ic_ki=ic_ki.substring(0,100);
				}
				manageCookie.makeCookie("ic_ki", ic_ki,60*24*60*60, response);
				//manageCookie.makeCookie("ic_ki_tgt", ic_ki_tgt,60*24*60*60, response);
			}else{
				//dumpObj.getRfData().add(crd);
			}
			debug++;
		    //  광고 전송중이 아닐경우만 url리타켓팅 등록
			//* test *//
			//push="aaaa";
			//cUrl="lottorich.co.kr";
			logger.debug("rf cUrl"+ cUrl);
			if(push==null || push.equals("")){
				debug++;
				String urlRet=adInfoCache.getMediaCode(cUrl);
				cUrl=java.net.URLEncoder.encode(cUrl,"euc-kr");
				
				debug++;
				logger.debug("rf urlRet="+ urlRet);
				if(urlRet==null || urlRet.equals("")){
				}else{
					
					HashMap addpq1= new HashMap();
					
					int id1= StringUtils.gAtId(ic_um, cUrl, "|||");
					ic_um_tgt= StringUtils.gAtFirst1(ic_um_tgt, id1, "|||");
					
					ic_um = StringUtils.gAtFirst(ic_um, cUrl, "|||");
					
					if(ic_um.length()>200){
						ic_um=ic_um.substring(0,200);
					}
					
					manageCookie.makeCookie("ic_um", ic_um,60*24*60*60, response);
					//manageCookie.makeCookie("ic_um_tgt", ic_um_tgt,60*24*60*60, response);
					
				}
			}
			
		} catch (Exception e) {
			System.out.println(e);
			logger.error("RFServlet2:"+e+",debug="+debug); 
		}
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response){
		doGet(request,response);
	}
	public void init() throws ServletException {
		logger.info("RFServlet started");
		try{
			DumpFailList=getServletConfig().getInitParameter("DumpFailList");
			AdTemplateDir=getServletConfig().getInitParameter("AdTemplate");
			MediaCodeXML=getServletConfig().getInitParameter("MediaCodeXML");
			CheckName=getServletConfig().getInitParameter("CheckName");
			instance = this;
			if (appContext == null) {
	        	appContext = new ClassPathXmlApplicationContext(getFileList());
	        }
			adInfoCache=(AdInfoCache)appContext.getBean("AdInfoCache");
			dumpObj=(DumpObject) appContext.getBean("DumpObject");
			dataMapper=(DataMapper)appContext.getBean("DataMapper");
			manageCookie=(ManagementCookie)appContext.getBean("ManagementCookie");
			dumpDb=(DumpDataToDB)appContext.getBean("DumpDataToDB");
			initdomainMap();
			loadXmlData();
			FreeMakerSet("utf-8");
			
		}catch(Exception e){
			logger.error("RF init error : "+e);
		}
	}
	public void FreeMakerSet(String charset){
		charset = org.apache.commons.lang3.StringUtils.defaultIfEmpty(charset, "utf-8");
		try	{
			cfg = new Configuration();
			cfg.setDirectoryForTemplateLoading(new File(AdTemplateDir));
			cfg.setObjectWrapper(new DefaultObjectWrapper());
			cfg.setDefaultEncoding(charset);
			cfg.setEncoding(Locale.KOREAN, charset);
			cfg.setSetting(Configuration.CACHE_STORAGE_KEY, "strong:50, soft:250"); 
			cfg.setObjectWrapper(ObjectWrapper.BEANS_WRAPPER); 
		}catch (Exception e) {
			logger.error("FreeMakerSet:"+e);
		}
	}
	public static HashMap loadXmlData(){
		try{
			logger.debug("loadXmlData init ");
			
			if(xml!=null)xml=null;
			xml = new HashMap();
			
			//adbn partner
			Document adbn_partner=null;
			try{
				adbn_partner = Jsoup.parse(new File("/home/dreamsearch/public_html/adnet/adbn_partner.xml"), "UTF-8");
			}catch(Exception e){
				try{
					adbn_partner = Jsoup.parse(new File("D:/work/enliple/www/webapp/public_html/adnet/adbn_partner.xml"), "UTF-8");
				}catch(Exception e1){}
			}
			xml.put("adbn_partner", adbn_partner);
			
			
			//adbn
			Document adbn_adnew =null;
			try{
				// real..test..
				adbn_adnew = Jsoup.parse(new File("/home/dreamsearch/public_html/adnet/adbn_adnew.xml"), "UTF-8");
			}catch(Exception e){
				try{
					// won local
					adbn_adnew= Jsoup.parse(new File("D:/work/enliple/www/webapp/public_html/adnet/adbn_adnew.xml"), "UTF-8");
				}catch(Exception e1){
					// l2ms local
					try{
						adbn_adnew= Jsoup.parse(new File("G:/WebService/dreamsearch/public_html/adnet/adbn_adnew.xml"), "UTF-8");
					}catch(Exception e2){}
				}
			}
			xml.put("adbn_adnew", adbn_adnew);
			
			//iadbn
			Document iadbn_ebay=null;
			try{
				iadbn_ebay = Jsoup.parse(new File("/home/dreamsearch/public_html/adnet/iadbn_ebay.xml"), "UTF-8");
			}catch(Exception e){
				try{
					iadbn_ebay = Jsoup.parse(new File("D:/work/enliple/www/webapp/public_html/adnet/iadbn_ebay.xml"), "UTF-8");
				}catch(Exception e1){}
			}
			xml.put("iadbn_ebay", iadbn_ebay);
			
			//iadbn_base
			String iadbn_base= FileUtil.readFile("/home/dreamsearch/public_html/ad/iadbn_base.html");
			if( iadbn_base.equals("") ){
				iadbn_base= FileUtil.readFile("D:/work/enliple/www/webapp/public_html/ad/iadbn_base.html");
			}
			xml.put("iadbn_base", iadbn_base);
			
			// adbn
			String adbn_video= FileUtil.readFile("/home/dreamsearch/public_html/ad/adbn_video.html");
			if( adbn_video.equals("") ){
				adbn_video= FileUtil.readFile("D:/work/enliple/www/webapp/public_html/ad/adbn_video.html");
			}
			xml.put("adbn_video",adbn_video);
			
			String adbn_flash= FileUtil.readFile("/home/dreamsearch/public_html/ad/adbn_flash.html");
			if( adbn_flash.equals("") ){
				adbn_flash= FileUtil.readFile("D:/work/enliple/www/webapp/public_html/ad/adbn_flash.html");
			}
			xml.put("adbn_flash",adbn_flash);
			
			String adbn_igb4= FileUtil.readFile("/home/dreamsearch/public_html/ad/adbn_igb4.html");
			if( adbn_igb4.equals("") ){
				adbn_igb4= FileUtil.readFile("D:/work/enliple/www/webapp/public_html/ad/adbn_igb4.html");
			}
			xml.put("adbn_igb4",adbn_igb4);
			
			String adbn_igb6= FileUtil.readFile("/home/dreamsearch/public_html/ad/adbn_igb6.html");
			if( adbn_igb6.equals("") ){
				adbn_igb6= FileUtil.readFile("D:/work/enliple/www/webapp/public_html/ad/adbn_igb6.html");
			}
			xml.put("adbn_igb6", adbn_igb6);
			
			
			//adfloating
			String adbn_edge_js= FileUtil.readFile("/home/dreamsearch/public_html/ad/adbn_edge.js");
			if( adbn_edge_js.equals("") ){
				adbn_edge_js= FileUtil.readFile("D:/work/enliple/www/webapp/public_html/ad/adbn_edge.js");
			}
			xml.put("adbn_edge_js", adbn_edge_js);
			
			String adbn_video_js= FileUtil.readFile("/home/dreamsearch/public_html/ad/adbn_video.js");
			if( adbn_video_js.equals("") ){
				adbn_video_js= FileUtil.readFile("D:/work/enliple/www/webapp/public_html/ad/adbn_video.js");
			}
			xml.put("adbn_video_js", adbn_video_js);
			
			String adbn_mobile_js= FileUtil.readFile("/home/dreamsearch/public_html/ad/adbn_mobile.js");
			if( adbn_mobile_js.equals("") ){
				adbn_mobile_js= FileUtil.readFile("D:/work/enliple/www/webapp/public_html/ad/adbn_mobile.js");
			}
			xml.put("adbn_mobile_js", adbn_mobile_js);
			
			String adbn_mobile1_js= FileUtil.readFile("/home/dreamsearch/public_html/ad/adbn_mobile1.js");
			if( adbn_mobile1_js.equals("") ){
				adbn_mobile1_js= FileUtil.readFile("D:/work/enliple/www/webapp/public_html/ad/adbn_mobile1.js");
			}
			xml.put("adbn_mobile1_js", adbn_mobile1_js);
			
			String adbn_floating_js= FileUtil.readFile("/home/dreamsearch/public_html/ad/adbn_floating.js");
			if( adbn_floating_js.equals("") ){
				adbn_floating_js= FileUtil.readFile("D:/work/enliple/www/webapp/public_html/ad/adbn_floating.js");
			}
			xml.put("adbn_floating_js", adbn_floating_js);
			
			
			//sadbn
			String skyScript_js= FileUtil.readFile("/home/dreamsearch/public_html/ad/skyScript.js");
			if( skyScript_js.equals("") ){
				skyScript_js= FileUtil.readFile("d:/work/enliple/www/webapp/public_html/ad/skyScript.js");
			}
			xml.put("skyScript_js", skyScript_js);
			
			logger.debug("loadXmlData initd ");
		}catch(Exception e){
			logger.debug("RFServlet loadXmlData err "+e);
		}
		return xml;
		
	}
	public void CacheReload(String item,String key){
		try{
			if(item.equals("all")){
				adInfoCache.loadMediaCode();
				adInfoCache.loadAdSiteCode();
				adInfoCache.loadIadSiteCode();
				adInfoCache.loadMediaSite();
				adInfoCache.loadshopAdConfig("SR");
				adInfoCache.loadshopAdConfig("UM");
				adInfoCache.loadshopAdConfig("KL");	
				adInfoCache.loadshopAdConfig("AD");
				adInfoCache.loadAdCashData();
				adInfoCache.loadMediaScriptData();
				adInfoCache.loadNormalAdConfig("SR");
				adInfoCache.loadNormalAdConfig("UM");
				adInfoCache.loadNormalAdConfig("KL");	
				adInfoCache.loadNormalAdConfig("AD");
				adInfoCache.loadShopCategoryData();
				adInfoCache.loadShopPCodeData();
				adInfoCache.loadAdSiteCode();
				adInfoCache.loadAdLinkData("");
				
			}else if(item.equals("mediaScriptCache")){
				adInfoCache.loadMediaScriptData();
			}else if(item.equals("adCashCache")){
				adInfoCache.loadAdCashData();
			}else if(item.equals("normalBaseAdConfigCache")){
				adInfoCache.loadNormalAdConfig("AD");
			}else if(item.equals("normalShopAdConfigCache")){
				adInfoCache.loadNormalAdConfig("SR");
			}else if(item.equals("normalKeywordMatchConfigCache")){
				adInfoCache.loadNormalAdConfig("KL");
			}else if(item.equals("normalUrlMatchConfigCache")){
				adInfoCache.loadNormalAdConfig("UM");
			}else if(item.equals("shopAdConfigCache")){
				adInfoCache.loadshopAdConfig("SR");
			}else if(item.equals("shopDataCache")){
				adInfoCache.loadShopCategoryData();
				adInfoCache.loadShopPCodeData();;
			}else if(item.equals("urlMatchConfigCache")){
				adInfoCache.loadshopAdConfig("UM");
			}else if(item.equals("iKeywordMatchConfigCache")){
				adInfoCache.loadshopAdConfig("KL");
			}else if(item.equals("baseAdConfigCache")){
				adInfoCache.loadshopAdConfig("AD");
			}else if(item.equals("mediaSiteCache")){
				adInfoCache.loadMediaSite();
			}else if(item.equals("mediaCodeCache")){
				adInfoCache.loadMediaCode();
			}else if(item.equals("iAdSiteCache")){
				adInfoCache.loadIadSiteCode();
			}else if(item.equals("adsiteCache")){
				adInfoCache.loadAdSiteCode();
			}else if(item.equals("adLinkCache")){
				adInfoCache.loadAdLinkData(key);
			}else if(item.equals("shopCategoryDataCache")){
				dataMapper.loadShopStatsDataFromDB(key,"1nd");
			}
		}catch(Exception e){
			logger.error("CacheReload:"+e);
		}
	}
	protected String[] getFileList() { 
        return new String[]{getServletConfig().getInitParameter("pathToContext")};
    }
	public void destroy(){
		try{
			dumpDb.dumpExecute(dumpObj,"file");
		}catch(Exception e){
			logger.error(e.getMessage());
		}
		
		System.out.println(" RFServlet destroy execute.");
	}
	public long getRequestCounter(){
		return requestCounter;
	}
	public void setRequestCounter(long cnt){
		this.requestCounter=requestCounter+cnt;
	}
	public synchronized int getDumpThreadCounter(){
		return dumpThreadCounter;
	}
	public synchronized void setDumpThreadCounter(int cnt){
		this.dumpThreadCounter=cnt+dumpThreadCounter;
	}
	public boolean getFailDumpStatus(){
		return failDumpStatus;
	}
	public void setFailDumpStatus(boolean failDumpStatus){
		this.failDumpStatus=failDumpStatus;
	}
	public void initdomainMap(){
		domainTable = new Hashtable<String, String>();
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
	}
}