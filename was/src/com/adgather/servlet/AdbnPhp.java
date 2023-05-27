package com.adgather.servlet;


import java.io.File;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.context.ApplicationContext;

import com.adgather.beans.DumpDataToDB;
import com.adgather.beans.ManagementCookie;
import com.adgather.reportmodel.AdChargeData;
import com.adgather.reportmodel.AdConfigData;
import com.adgather.reportmodel.DumpObject;
import com.adgather.reportmodel.MediaLogData;
import com.adgather.reportmodel.MediaScriptData;
import com.adgather.reportmodel.RFData;
import com.adgather.reportmodel.ShopData;
import com.adgather.resource.db.DBManager;
import com.adgather.util.FileUtil;
import com.adgather.util.KDecoder;
import com.adgather.util.MassInformation;
import com.adgather.util.StringUtils;

public class AdbnPhp extends HttpServlet {
	private DumpObject dumpObj;
	private ApplicationContext appContext;
	static Logger logger = Logger.getLogger(AdbnPhp.class);
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		ArrayList scfg=null;
		String htmlStr="";
		String debug="";
		String img_path="http://www.dreamsearch.or.kr/ad/imgfile/";
		//http://www.dreamsearch.or.kr/servlet/pad?tnm=searchKT&mc=1327&kwd=%EC%9D%98%EB%A5%98
		try {
			request.setCharacterEncoding("8859_1");
			PrintWriter out=null;
			String mc=request.getParameter("mc")==null ? "" : request.getParameter("mc");
			String kwd=request.getParameter("kwd")==null ? "" : request.getParameter("kwd");
			String type=request.getParameter("type")==null ? "" : request.getParameter("type");
			String cnt=request.getParameter("cnt")==null ? "1" : request.getParameter("cnt");
			String playType=request.getParameter("playType")==null ? "xml" : request.getParameter("playType");
			String platform=request.getParameter("platform")==null ? "web" : request.getParameter("platform");
			String tnm=request.getParameter("tnm")==null ? "searchKT" : request.getParameter("tnm");
			//if(tnm.equals("")) return;
			//if(type.equals("")) return;
			MediaScriptData ms=RFServlet.instance.adInfoCache.getMediaScriptData(mc);
			String hostIP=request.getRemoteAddr();
			if( ms==null ){
				logger.debug("adbnphp mediascriptinfo null "+ mc);
				//return;
			}
			int nCnt=1;
			try{
				nCnt=Integer.parseInt(cnt);
			}catch(Exception e){
				logger.error("nCnt:"+e);
				nCnt=1;
			}
			debug="a-1";
			logger.debug("kwd1="+kwd);
			logger.debug("kwd2="+StringUtils.getURLDecodeStr(kwd, "euc-kr"));
			String keyword=new String(kwd.getBytes("8859_1"),"utf-8");
			logger.debug("kwd3="+keyword);
			logger.debug("kwd4="+keyword);
			KDecoder decoder = new KDecoder();		
			keyword=decoder.decoderkeywordString(keyword);
			logger.debug("kwd5="+keyword);
			debug="a-2";
			
			// searchKT.php
			String adType="";
			String product="nor";
			if(type.equals("11")){
				adType="KL";
			}else if(type.equals("12")){
				adType="UM";
			}else{
				adType="AD";
			}
			// searchKT:1327,searchKR:997
			debug="a-3";
			if(mc.equals("1327") || mc.equals("993")){ 
				adType="KL";
				nCnt=5;
				tnm="searchKT";
				playType="xml";
				platform="web";
			}else if(mc.equals("1320")){ //searchMoned
				nCnt=1;
				tnm="searchMoned";
				playType="json";
				platform="mobile";
				product="mbw";
				if(type.equals("11") && kwd.indexOf("\\x")==0){
					try{
						String kwTmp=kwd.replace("\\x","");
						byte[] hex2=org.bouncycastle.util.encoders.Hex.decode(kwTmp.getBytes());
						keyword=(new String(hex2,"utf-8"));
						logger.info("kwd6="+kwTmp);
					}catch(Exception e){
						logger.error("AdbnPhp,bouncycastle:"+e);
					}
				}
			}
			debug="a-4";
			Map root=new HashMap();
			ArrayList adList=new ArrayList();
			if(adType.equals("KL")){
				scfg=RFServlet.instance.adInfoCache.getNormalkeywordMatchAdConfig(ms.getMediasite_no(),mc,keyword,nCnt,platform);
			}else if(adType.equals("UM")){
				keyword=keyword.toLowerCase().replaceAll("http://","");
				keyword=keyword.toLowerCase().replaceAll("https://","");
				scfg=RFServlet.instance.adInfoCache.getNormalUrlMatchAdConfig(ms.getMediasite_no(),mc,keyword,nCnt, platform);
			}else if(adType.equals("AD")){
				img_path="http://www.dreamsearch.or.kr/ad/efile/";
				String baseadKey=mc+"_" +ms.getAD_TYPE();
				scfg=RFServlet.instance.adInfoCache.getNormalBaceAdConfig(baseadKey,nCnt);
			}
			debug="a-5";
			if(scfg!=null && scfg.size()>0){
				root.put("errcode","0");
				root.put("errstr","");
				String link="www.dreamsearch.or.kr/servlet/drc";
				SimpleDateFormat yyyymmdd = new SimpleDateFormat("yyyyMMdd");
				SimpleDateFormat yyyymm = new SimpleDateFormat("yyyyMM");
				SimpleDateFormat hhmiss = new SimpleDateFormat("kkmmss");
				SimpleDateFormat kk = new SimpleDateFormat("kk");
				java.util.Date date=new java.util.Date();
				String ymd=yyyymmdd.format(date);
				String ym=yyyymm.format(date);	
				String hms=hhmiss.format(date);
				String hk=kk.format(date);
				debug="a-6";
				for(int i=0;i<scfg.size();i++){
					Map adInfo=new HashMap();
					AdConfigData ac=(AdConfigData) scfg.get(i);
					debug="a-7";
					String siteUrl=ac.getHomepi().toLowerCase();
					debug="a-7-2";
					String sLink=ac.getSite_url();
					if(sLink.toLowerCase().indexOf("http")==0){
						logger.debug("http index=0");
					}else{
						sLink="http://"+ac.getSite_url();
						logger.debug("http index>0:"+ac.getSite_url());
					}
					String siteTitle="";
					String siteDesc="";
					String randLink="";
					if(product.equals("mbw")){
						siteTitle=ac.getSite_name();
						siteDesc=ac.getSite_descm();
						//siteTitle=new String(ac.getSite_name().getBytes(),"utf-8");
						//siteDesc=new String(ac.getSite_desc().getBytes(),"utf-8");
						randLink="http://"+link+"?no="+ac.getKno()+"&s="+mc+"&gb="+adType+"&sc="+ac.getSite_code()
						+"&mc"+mc+"&mcgb=&u="+ac.getUserid()+"&product=nor&slink="+URLEncoder.encode(sLink,"UTF-8");
					}else{
						if(ac.getSite_title().indexOf(keyword)>-1){
							siteTitle=ac.getSite_title().replaceAll(keyword,"<b>"+keyword+"</b>");
						}else{
							siteTitle="<b>"+keyword+"</b> "+ac.getSite_title();
						}
						if(ac.getSite_desc().indexOf(keyword)>-1){
							siteDesc=ac.getSite_desc().replaceAll(keyword,"<b>"+keyword+"</b>");
						}else{
							siteDesc=ac.getSite_desc()+" <b>"+keyword+"</b>";
						}
						randLink=link+"?no="+ac.getKno()+"&s="+mc+"&gb="+adType+"&sc="+ac.getSite_code()
						+"&mc"+mc+"&mcgb=&u="+ac.getUserid()+"&product=nor&slink="+URLEncoder.encode(sLink,"UTF-8");
					}
					adInfo.put("site_title",siteTitle);
					adInfo.put("site_desc",siteDesc);
					adInfo.put("site_url",siteUrl);
					if(ac.getBanner_path1()!=null && ac.getBanner_path1().length()>0){
						adInfo.put("bntype","2");
					}else{
						adInfo.put("bntype","1");
					}
					adInfo.put("bnpath",img_path+ac.getBanner_path1());
					adInfo.put("link",randLink);
					adList.add(adInfo);
					
					AdChargeData crd=(AdChargeData)appContext.getBean("AdChargeData");
					crd.setSITE_CODE(ac.getSite_code());
					crd.setADGUBUN(adType);
					crd.setTYPE("V");
					crd.setYYYYMMDD(ymd);
					crd.setHHMM(hk);
					crd.setUSERID(ac.getUserid());
					crd.setPOINT("0");
					crd.setPPOINT("0");
					crd.setNO(ac.getNo());
					crd.setPCODE("");
					crd.setS(mc);
					crd.setSCRIPTUSERID(ms.getUSERID());
					crd.setIP(hostIP);
					crd.setKNO(ac.getKno());
					if(i==0){
						crd.setSRView2(1);
					}else{
						crd.setViewcnt3(1);
					}
					crd.setMCGB(ac.getMcgb());
					debug="a-9";
					crd.setPRODUCT(product);
					dumpObj.getNormalChargeLogData().add(crd);
				}
				debug="a-10";
				root.put("data",adList);
				if(adType.equals("KL")){
					RFData rfd=(RFData)appContext.getBean("RFData");
					rfd.setURL("");
					rfd.setCURL("");
					rfd.setMCGB("KR");
					rfd.setRF("");
					rfd.setSC_TXT(keyword);
					rfd.setIP(hostIP);
					rfd.setRDATE(ymd);
					rfd.setRTIME(hms);
					rfd.setRURL("");
					rfd.setSetCnt(scfg.size());
					rfd.setTbName("KYEWORD_LOG_KR");
					dumpObj.getKeywordLogData().add(rfd);
				}
				debug="a-11";
			}else{
				root.put("errcode","66");
				root.put("errstr","Not Found Data!");
			}
			if(root!=null && root.size()>=2){
				if(playType.equals("xml")){
					response.setContentType("text/xml; charset=utf-8");
					htmlStr=StringUtils.getFreeMakerData(RFServlet.instance.cfg,tnm+".ftl",root,"utf-8");
				}else if(playType.equals("json")){
					Map room=new HashMap();
					JsonBuilderFactory factory = Json.createBuilderFactory(room); 
					JsonObjectBuilder ob1 = factory.createObjectBuilder();
					JsonObjectBuilder ob2 = factory.createObjectBuilder();
					JsonArrayBuilder dataList = factory.createArrayBuilder();
					ob1.add("errcd",root.get("errcode").toString());
					ob1.add("errmsg",root.get("errstr").toString());
					if(root.containsKey("data")){
						ArrayList tmp=(ArrayList) root.get("data");
						if(tmp!=null && tmp.size()>0){
							for(int i=0;i<tmp.size();i++){
								HashMap adInfo=(HashMap) tmp.get(i);
								ob2.add("bntype",adInfo.get("bntype").toString())
								.add("site_title",adInfo.get("site_title").toString())
								.add("site_desc",adInfo.get("site_desc").toString())
								//.add("site_url",adInfo.get("site_url").toString())
								.add("bnpath",adInfo.get("bnpath").toString())
								.add("link",adInfo.get("link").toString());
								dataList.add(ob2);
								ob1.add("adbn",dataList);
							}
						}
					}
					response.setContentType("text/html; charset=utf-8");
					htmlStr = ob1.build().toString();
				}
			}else{
				if(mc.equals("1320")){
					root.put("errcd","66");
					root.put("errmsg","Not Found Data!");
				}else{
					root.put("errcode","66");
					root.put("errstr","Not Found Data!");
				}
				
			}
			out=response.getWriter();
			out.print(htmlStr);
		} catch (Exception e) {
			logger.error("AdbnPhp"+e+",debug="+debug); 
		}
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response){
		doGet(request,response);
	}
	public void init() throws ServletException {
		logger.info("AdbnServlet started");
		if (appContext == null) {
			RFServlet.instance.init();
			appContext=RFServlet.appContext;
		}
		dumpObj=(DumpObject)appContext.getBean("DumpObject");
	}
	protected String[] getFileList() { 
        return new String[]{getServletConfig().getInitParameter("pathToContext")};
    }
}