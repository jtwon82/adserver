package com.adgather.servlet;


import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.URLEncoder;
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
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.context.ApplicationContext;

import com.adgather.beans.ManagementCookie;
import com.adgather.reportmodel.AdChargeData;
import com.adgather.reportmodel.AdConfigData;
import com.adgather.reportmodel.DumpObject;
import com.adgather.reportmodel.MediaLogData;
import com.adgather.reportmodel.MediaScriptData;
import com.adgather.reportmodel.RFData;
import com.adgather.reportmodel.ShopData;
import com.adgather.util.FileUtil;
import com.adgather.util.MassInformation;
import com.adgather.util.StringUtils;

public class AdBanner extends HttpServlet {
	private DumpObject dumpObj;
	private ApplicationContext appContext;
	public ManagementCookie manageCookie;
	static Logger logger = Logger.getLogger(AdBanner.class);
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		String debug="";
		int viewcnt2=0;
		AdConfigData scfg=null;
		AdConfigData scfgr=null;
		StringBuffer htmlStr=new StringBuffer();
		StringBuffer htmlStr1=new StringBuffer();
		StringBuffer htmlStr2=new StringBuffer();
		StringBuffer html_flash= new StringBuffer();
		StringBuffer html_flash_img= new StringBuffer();
		StringBuffer html_flash_link= new StringBuffer();
		String servertype="test";
		String domain ="www.dreamsearch.or.kr";
		try {
			InetAddress addr=InetAddress.getLocalHost();
			String hostIp=addr.getHostAddress();
			if( hostIp.indexOf("183.111.148")>-1 ){
				servertype="real";
			}
			request.setCharacterEncoding("8859_1");
			response.setContentType("text/html;charset=euc-kr");
			response.setHeader("Pragma", "no-cache");			
			response.setHeader("Cache-Control", "no-cache"); 
			//response.setHeader("P3P","CP='IDC DSP COR ADM DEVi TAIi PSA PSD IVAi IVDi CONi HIS OUR IND CNT'");
			response.setHeader("P3P","CP='CAO PSA CONi OTR OUR DEM ONL'");
			PrintWriter out=response.getWriter();
			String u=request.getParameter("u")==null ? "" : request.getParameter("u");
			String us=request.getParameter("us")==null ? "" : request.getParameter("us");
			String s=request.getParameter("s")==null ? "" : request.getParameter("s");
			String pc=request.getParameter("pc")==null ? "" : request.getParameter("pc");
			String iwh=request.getParameter("iwh")==null ? "" : request.getParameter("iwh");	iwh=iwh.equals("")?"250_250":iwh;
			String igb=request.getParameter("igb")==null ? "1" : request.getParameter("igb");
			String types=request.getParameter("types")==null ? "" : request.getParameter("types");
			String rnd_no=request.getParameter("rnd_no")==null ? "" : request.getParameter("rnd_no");
			String psb=request.getParameter("psb")==null ? "" : request.getParameter("psb");
			String psburi=request.getParameter("psburi")==null ? "" : request.getParameter("psburi");
			String clk_param=request.getParameter("clk_param")==null ? "" : request.getParameter("clk_param");
			String chk=request.getParameter("chk")==null ? "" : request.getParameter("chk");
			//상품 가져올 겟수
			String cntsr=request.getParameter("cntsr")==null ? "" : request.getParameter("cntsr");
			//상품 제외 가져올 겟수
			String cntad=request.getParameter("cntad")==null ? "" : request.getParameter("cntad");
			//가져올 이미지 사이즈
			String imgtype=request.getParameter("imgtype")==null ? "" : request.getParameter("imgtype");
			
			String alCnt=request.getParameter("alCnt")==null ? "20" : request.getParameter("alCnt"); 
			
			if(igb.equals("")) igb="1";
			String mediaSiteStatus=RFServlet.instance.adInfoCache.getMediaSiteStatus(us);
			String keyIp=RFServlet.instance.manageCookie.makeKeyCookie(request, response);
			SimpleDateFormat yyyymmdd = new SimpleDateFormat("yyyyMMdd");
			SimpleDateFormat yyyymm = new SimpleDateFormat("yyyyMM");
			SimpleDateFormat kk = new SimpleDateFormat("kk");
			java.util.Date date=new java.util.Date();
			String ymd=yyyymmdd.format(date);
			String ym=yyyymm.format(date);
			String kh=kk.format(date);
			
			Calendar cal=Calendar.getInstance();
			SimpleDateFormat hhmi = new SimpleDateFormat("kkmm");
			date.setTime(cal.getTimeInMillis());
			String hm=hhmi.format(date);
			
			if(true){
				//s="810";
				//us="302";
			}
			
			if(mediaSiteStatus.equals("N")){
				return;
			}
			htmlStr.append(" <!DOCTYPE html>                                                            \n");
			htmlStr.append(" <html xmlns='http://www.w3.org/1999/xhtml'>                                \n");
			htmlStr.append(" <head>                                                                     \n");
			htmlStr.append(" <title>배너광고</title>                                                    \n");
			htmlStr.append(" <meta http-equiv=\"Content-Type\" content=\"text/html; charset=euc-kr\" /> \n");
			htmlStr.append(" <style>                                                                    \n");
			htmlStr.append(" body {                                                                     \n");
			htmlStr.append("   color : #9A9A9A ;                                                        \n");
			htmlStr.append("   font-size : 9pt ;                                                        \n");
			htmlStr.append("   font-family : \"돋움\" ;                                                 \n");
			htmlStr.append("   background-color : #ffffff ;                                             \n");
			htmlStr.append("   margin : 0px 0px 0px 0px ;                                               \n");
			htmlStr.append("   scrollbar-face-color: #fffffff;                                          \n");
			htmlStr.append("   scrollbar-highlight-color:#ffffff;                                       \n");
			htmlStr.append("   scrollbar-3dlight-color: #fffffff;                                       \n");
			htmlStr.append("   scrollbar-shadow-color: #fffffff;                                        \n");
			htmlStr.append("   scrollbar-darkshadow-color: #ffffff;                                     \n");
			htmlStr.append("   scrollbar-track-color: #fffffff;                                         \n");
			htmlStr.append("   scrollbar-arrow-color: #000000                                           \n");
			htmlStr.append(" }                                                                          \n");
			htmlStr.append("   A:link { text-decoration:none; color: 00448B;}                           \n");
			htmlStr.append("   A:visited { text-decoration:none; color: green;}                         \n");
			htmlStr.append("   A:active { text-decoration:none; color:red ;}                            \n");
			htmlStr.append("   A:hover {color: black; text-decoration:none;}                            \n");
			htmlStr.append(" </style>                                                                   \n");
			htmlStr.append(" <style type='text/css'>                                                    \n");
			htmlStr.append(" #ad2 {                                                                     \n");
			htmlStr.append("     BORDER-BOTTOM: #CCCCCC 2px solid;                                      \n");
			htmlStr.append("     BORDER-LEFT: #CCCCCC 2px solid;                                        \n");
			htmlStr.append("     DISPLAY: block;                                                        \n");
			htmlStr.append("     BORDER-TOP: #CCCCCC 2px solid;                                         \n");
			htmlStr.append("     BORDER-RIGHT: #CCCCCC 2px solid                                        \n");
			htmlStr.append("                                                                            \n");
			htmlStr.append(" }                                                                          \n");
			htmlStr.append(" #ad2:hover {                                                               \n");
			htmlStr.append("     BORDER-BOTTOM: #ff4164 2px solid;                                      \n");
			htmlStr.append("     BORDER-LEFT: #ff4164 2px solid;                                        \n");
			htmlStr.append("     DISPLAY: block;                                                        \n");
			htmlStr.append("     BORDER-TOP: #ff4164 2px solid;                                         \n");
			htmlStr.append("     BORDER-RIGHT: #ff4164 2px solid                                        \n");
			htmlStr.append(" }                                                                          \n");
			htmlStr.append(" #pname {                                                                   \n");
			htmlStr.append("     color:#FFFFFF;                                                         \n");
			htmlStr.append(" 	font-size:12px;                                                         \n");
			htmlStr.append(" 	font-weight:bold;                                                         \n");
			htmlStr.append(" }                                                                          \n");
			htmlStr.append(" </style>                                                                   \n");
			htmlStr.append(" <script>                                                                   \n");
			htmlStr.append(" function chk(){                                                            \n");
			htmlStr.append("   alert(screen.width);                                                     \n");
			htmlStr.append(" }                                                                          \n");
			htmlStr.append(" function imgcheck(imgObj, bool){                                           \n");
			htmlStr.append("   var w = screen.width;                                                     \n");
			htmlStr.append(" 	imgObj.width = w;                                            \n");
			htmlStr.append(" 	//document.getElementById('divtxt').style.width=w;                        \n");
			htmlStr.append(" }                                                                          \n");
			htmlStr.append(" </script>                                                                  \n");
			htmlStr.append(" </head>                                                                    \n");
			htmlStr.append(" <body >                                                                    \n");
			htmlStr.append(" <div id=\"container\">                                                     \n");
			
			String dwidth="0";
			String dheight="0";
			String scTxtSel="";

			String from=request.getParameter("from")==null ? "" : request.getParameter("from");
			if(from.equals("")){
				from=request.getHeader("Referer")==null ? "" : request.getHeader("Referer");
			}
			if(from.length()>200){
				from=from.substring(0,200);
			}
			logger.debug("adbn[132] from="+ from);
			
			String mcgb="";
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
			debug="a-2";
			String chk_result=manageCookie.getCookieInfo(request,"chk_result");
			String chk_ck1 ="";
			/*
			if(alCnt =="20"||alCnt.equals("20")){
				chk_ck1=manageCookie.getCookieInfo(request,"chk_ck");
			}else{
				chk_ck1=manageCookie.getCookieInfo(request,"chk_ck_Al");
			}
			*/
			StringBuffer chk_ck= new StringBuffer();
			chk_ck.append(chk_ck1);
			
			String ic_um=manageCookie.getCookieInfo(request,"ic_um");
			String ic_ki=manageCookie.getCookieInfo(request,"ic_ki");
			String shop_log=manageCookie.getCookieInfo(request,"shop_log");
			String ic_um_tgt= manageCookie.getCookieInfo(request,"ic_um_tgt");
			String ic_ki_tgt= manageCookie.getCookieInfo(request,"ic_ki_tgt");
			String ic_ki_real= manageCookie.getCookieInfo(request,"ic_ki_real");
			
			String scTxt=from;

			if( from!=null && from.length()>0 ){
				try{
					MassInformation mf = new MassInformation(RFServlet.instance.domainTable,from);
					if( ic_ki_real.equals("") ){
						scTxt=mf.GetDecodingKeyword();
						if( !scTxt.equals("") ){
							manageCookie.makeCookie("ic_ki_real", java.net.URLEncoder.encode(scTxt,"euc-kr"), -1, response);
						}
					}else{
						scTxt=ic_ki_real;
					}
					
				}catch(Exception e){
					logger.error("Adbn-MassInformation:"+e+" : "+ from);
				}
			}
			debug="a-1";
			
			if(scTxt.length()>100){
				scTxt=scTxt.substring(0,100);
			}
			logger.debug("adbn[146] scTxt="+scTxt);
			logger.debug("shop_log1="+shop_log);
			shop_log=StringUtils.getURLDecodeStr(shop_log,"UTF-8");
			logger.debug("shop_log2="+shop_log);
			String adgubun="";
			HashMap<String,String> addpq=new HashMap<String,String>();
			if(chk_result!=null && chk_result.length()>0){
				StringTokenizer st=new StringTokenizer(chk_result,"#",false);
				while(st.hasMoreElements()){
					String stCode=st.nextElement().toString();
					addpq.put(stCode, stCode);
				}
			}
			
			
			
			debug="a-3";
			MediaScriptData ms=RFServlet.instance.adInfoCache.getMediaScriptData(s);
			if( ms==null ){
				logger.debug("adbn[216] mediainfo null "+ s);
				return;
			}
			logger.debug(ms.getInfo("adbn ms="));
			
			MediaLogData mLog1=(MediaLogData)appContext.getBean("MediaLogData");
			mLog1.setS(s);
			mLog1.setIP(keyIp);
			mLog1.setSDATE(ymd);
			mLog1.setVIEWCNT(1);
			mLog1.setLogId(1);
			dumpObj.getMediaLogData().add(mLog1);

			ArrayList<ShopData> rList= new ArrayList();
			
			
			//String IPNewBN=manageCookie.getCookieInfo(request,"IPNewBN");
			//manageCookie.makeCookie("IPNewBN", "",0, response);
			debug="a-3-1";
			if(s.equals("184")){
				logger.debug("184광고 시작");
				String baseadKey=us+"_"+ms.getAD_TYPE();
				HashMap tmp=new HashMap();
				scfg=RFServlet.instance.adInfoCache.getNormalBaceAdConfig(baseadKey,tmp,s,"","");
				if(scfg!=null){
					adgubun="AD";
					logger.debug("184광고 셋팅됨");
				}
				debug="a-4";
			}else{	// 키가 바로 생긴 것이 아니니  뭔가 있는지 뒤져본다..
				
				debug="a-50";
				
				logger.debug("키가 바로 생긴 것이 아니니  뭔가 있는지 뒤져본다 :OK,"+shop_log);
				//int limit_cnt=1;
				//if(igb.equals("2") || igb.equals("3")){ limit_cnt=3;}

				String newVal=manageCookie.getCookieInfo(request,"shop_log");
				newVal=java.net.URLDecoder.decode(newVal,"utf-8");
				
				
				if( adgubun==null || adgubun.equals("") ){
					HashMap<String,ShopData> shhm=null;
					if( shop_log!=null && shop_log.length()>0 ) {
						shhm=RFServlet.instance.adInfoCache.getShopLogData(shop_log,keyIp,"normal");
						
						if( shhm!=null && shhm.size()>0 ){
							logger.debug("Adbn==>keyIp history "+ shhm.size() );
							List<String> it = new ArrayList<String>(shhm.keySet());
						    Collections.sort(it);
						    
						    for(String me:it){
						    	ShopData shop_log1= shhm.get(me);
						    	if( shop_log1.getTARGETGUBUN().indexOf("A")>-1 ) continue;
						    	if( shop_log1.getMCGB().equals("") || shop_log1.getMCGB().equals("A") ) {} else { continue; }
						    	
						    	
						    	AdConfigData tmp_scfg=RFServlet.instance.adInfoCache.getNormalShopAdConfig(shop_log1.getSITE_CODE(),us,s,"adlink");
						    	debug="a-50-3";
						    	if( tmp_scfg!=null && tmp_scfg.getStateW().equals("Y") ) {
						    		logger.debug(tmp_scfg.getInfo("adbn[254] tmp_scfg.info "));

							    	AdConfigData scfg_chkrecom = RFServlet.instance.adInfoCache.getRecomAdConfig(tmp_scfg.getUserid(),us,s,"adlink","banner");
							    	
							    	if( !shop_log1.getMCGB().equals("") && scfg_chkrecom==null ){
							    		continue;
							    	}else if ( !shop_log1.getMCGB().equals("") && scfg_chkrecom!=null ){
							    		if( !scfg_chkrecom.getSite_code().equals( shop_log1.getSITE_CODE() ) ){
							    			continue;
							    		}
							    	}
							    	
						    		ShopData log_item=RFServlet.instance.adInfoCache.getShopPCodeData(tmp_scfg.getUserid(),shop_log1.getPCODE());
						    		if(log_item!=null){
						    			
										if( log_item.getIMGPATH()==null || log_item.getIMGPATH().length()==0 ){
											continue;
										}
										
										if( shop_log1.getMCGB().equals("") ){
									    	if( scfg==null ){
									    		scfg= tmp_scfg.clone();
												scfg.setNo(shop_log1.getNO()+"");
												scfg.setPcode(shop_log1.getPCODE());
												scfg.setGb(shop_log1.getGB());
												scfg.setSite_code(shop_log1.getSITE_CODE());
												scfg.setPurl(shop_log1.getPURL());
												scfg.setMcgb(shop_log1.getMCGB());
												scfg.setFlag(shop_log1.getFlag());
												scfg.setCate1(log_item.getCATE1());
												scfg.setPnm(log_item.getPNM());
												scfg.setPrice(log_item.getPRICE());
												scfg.setImgpath(log_item.getIMGPATH());
												scfg.setSite_url(log_item.getPURL());
											}
										}else if( shop_log1.getMCGB().equals("A") ){
								    		scfgr= tmp_scfg.clone();
								    		scfgr.setNo(shop_log1.getNO()+"");
											scfgr.setPcode(shop_log1.getPCODE());
											scfgr.setGb(shop_log1.getGB());
											scfgr.setSite_code(shop_log1.getSITE_CODE());
											scfgr.setPurl(shop_log1.getPURL());
											scfgr.setMcgb(shop_log1.getMCGB());
											scfgr.setFlag(shop_log1.getFlag());
											scfgr.setCate1(log_item.getCATE1());
											scfgr.setPnm(log_item.getPNM());
											scfgr.setPrice(log_item.getPRICE());
											scfgr.setImgpath(log_item.getIMGPATH());
											scfgr.setSite_url(log_item.getPURL());
										}
									}
								}
							}
						    if( scfg==null && scfgr!=null ){
						    	scfg = scfgr;
						    }
						    if( scfg!=null ){
							    ShopData shop_log1= new ShopData(); 
							    shop_log1.setNO( Integer.parseInt(scfg.getNo()) );	
							    shop_log1.setUSERID(scfg.getUserid());	
							    shop_log1.setSITE_CODE(scfg.getSite_code());
								shop_log1.setSite_etc(scfg.getSite_etc());	
								shop_log1.setCATE1(scfg.getCate1());
								shop_log1.setIMGPATH(scfg.getImgpath());
								shop_log1.setPNM(scfg.getPnm());
								shop_log1.setPRICE(scfg.getPrice());
								shop_log1.setPURL(scfg.getPurl());
								shop_log1.setMCGB(scfg.getMcgb());
								shop_log1.setPCODE(scfg.getPcode());
								rList.add(shop_log1);
						    }
						}else{
							manageCookie.makeCookie("shop_log","",0, response);
						}
						
						logger.debug("adbn[383] scfg="+scfg);
						
						if( igb.equals("2") || igb.equals("3") && scfg!=null && scfg.getStateW().equals("Y") ) { 
							
							if( rList.size()>0 && ms.getAccept_sr().equals("Y") ){
								int rList_size= rList.size();
								logger.debug("adbn[378] rList.size "+ rList.size() );
								
								adgubun="SR";
								if(igb.equals("3")){
									dwidth="724";
									dheight="86";
								}else if(igb.equals("2")){
									dwidth="116";
									dheight="596";
								}
								htmlStr.append("<div id='ad2' align='center' style='width:\""+dwidth+"\"px; height:\""+dheight+"\"px;'><table><tr>");
								debug="a-7";
								int validCnt=0;
								ShopData sd2 = rList.get(0);
								
								debug="a-7-aa";
								// 빈공간 있으면 다른상품으로 채우기
								if( sd2!=null && rList.size()<3 ){
									logger.debug(sd2.getInfo("adbn[361] sd2.info "));
									
									LinkedHashMap ldList= RFServlet.instance.adInfoCache.getShopStatsData(sd2.getUSERID(),sd2.getCATE1());
									if(ldList!=null && ldList.size()>0){
										//ArrayList sdlist=new ArrayList(ldList.values());
										List<String> sdlist = new ArrayList<String>(ldList.keySet());
										ArrayList<Integer> rndList = StringUtils.getRandList( sdlist.size() );
									
										debug="a-7-aa sdlist.size()="+ sdlist.size();
										
										Iterator it = rndList.iterator();
										
										for( int i=0; it.hasNext(); i++  ){
											if( rList.size()>2 ) break;
											
											String pcode=sdlist.get( (Integer)it.next() ).toString();
											ShopData ss =RFServlet.instance.adInfoCache.getShopPCodeData(sd2.getUSERID(), pcode);
											if(ss!=null){
												logger.debug(ss.getInfo("adbn[370] ss.info "));
												
												String local_img= ss.getIMGPATH();
												
												ShopData a = new ShopData();
												a.setNO(0);	// 추가상품은 노출할때마다 랜덤으로 노출.
												a.setUSERID(sd2.getUSERID());
												a.setSite_etc(sd2.getSite_etc());
												a.setSITE_CODE(sd2.getSITE_CODE());
												a.setCATE1(ss.getCATE1());
												a.setPCODE(ss.getPCODE());
												a.setIMGPATH( local_img );
												a.setPNM(ss.getPNM());
												a.setPRICE(ss.getPRICE());
												a.setPURL(ss.getPURL());
												a.setMCGB(sd2.getMCGB());
												
												a.setEtc_type("long_banner"); // 빈공간 생겼을때 체우기위한 변수
												if( !sd2.getPCODE().equals( ss.getPCODE() ) ){
													rList.add( a );
												}
											}
										}
									}
								}
								debug="a-7-bb";
								
								String chk_code="SR_"+ scfg.getSite_code()+"^"+scfg.getPcode();
								//String direct=manageCookie.makeCookieStr(response, request, adgubun, chk_code, chk_result, chk_ck, 20);
								
						//		int alCnte = Integer.parseInt(alCnt);
								String direct=manageCookie.makeCookieStrNew(response, request, chk_result, chk_ck, chk_code, 20,chk);
								
								if( direct.equals("next") ){
									ShopData slog= new ShopData();
									slog.setIP( keyIp );
									slog.setGB(adgubun);
									slog.setNO(Long.parseLong( scfg.getNo() ));
									slog.setSvc_type("normal");
									slog.setSITE_CODE(scfg.getSite_code());
									slog.setPCODE(scfg.getPcode());
									if(chk.equals("")){
										dumpObj.getDelShopLogData().add(slog);
										newVal=manageCookie.shopLogManage(1, newVal,slog,"edit");
									}
								}
								
								for(int i=0;i<rList.size();i++){
									
									ShopData data = rList.get(i);
									logger.debug(data.getInfo("adbn[405] aaa "));
									
									debug="a-7-cc";
									String imgstr=rList.get(i).getIMGPATH();
									debug="a-7-cc1";
									String slink="";
									String pnm="";
									String price="0";
									if(imgstr!=null && imgstr.length()>0){
										String p_url = rList.get(i).getPURL();
										String r_site_etc = rList.get(i).getSite_etc();
										debug="a-7-cc2";
										if( r_site_etc.length()>0 ){
											p_url += p_url.substring(0, 1).equals("&")? r_site_etc: "&"+ r_site_etc ;
										}
										slink  = "/servlet/drc?no="+rList.get(i).getNO()+"&kno=0&s="+s+"&gb=SR&sc="+rList.get(i).getSITE_CODE()
												+"&mcgb="+rList.get(i).getMCGB()+"&mc="+s+"&u="+rList.get(i).getUSERID()+"&pCode="+rList.get(i).getPCODE()
												+"&product="+ StringUtils.gAtData("nor,video,nor", "nor,video,flash", types, ",", "nor")
												+"&slink="+URLEncoder.encode(p_url,"euc-kr")
												+"&clk_param="+URLEncoder.encode(clk_param,"euc-kr");
										pnm    = rList.get(i).getPNM();
										price  = rList.get(i).getPRICE();
										
										rList.get(i).setPURL(slink);
										debug="a-7-cc3";
										try{
											int tmpChk=Integer.parseInt(price);
										}catch(Exception xx){
											logger.error(xx);
											price="0";
										}
										debug="a-7-dd";
										//String tempchk = chk_ck;
										//String chk_code= rList.get(i).getSITE_CODE();
										//노출 순서 정보를 site_code기준에서 shop_log no 로 변경.. 
										if( rList.get(i).getEtc_type()!=null && rList.get(i).getEtc_type().equals("long_banner") ){
											chk_code = "0";
										}else{										
											chk_code = rList.get(i).getNO()+"";
										}
										logger.debug("chk_result="+chk_result);
										logger.debug("chk_ck="+chk_ck);
										logger.debug("chk_code="+chk_code);
										
										
										debug="a-7-ee";
										//chk_ck=resultvalue;
										logger.debug("adbn sr igb=2,3 resultvalue="+chk_ck);
										
										// 상품 추가노출 2개
										if( direct.equals("next") && rList.get(i).getNO()!=0 && scfg.getFlag().equals("1") ){
											AdConfigData tmpFg=RFServlet.instance.adInfoCache.getNormalShopAdConfig(data.getSITE_CODE(),us,s,"adlink");
											
											if( tmpFg!=null ){
												
												if( data.getMCGB().equals("") || !data.getMCGB().equals("A") ){
													LinkedHashMap ldList= RFServlet.instance.adInfoCache.getShopStatsData(data.getUSERID(),data.getCATE1());
													if(ldList!=null && ldList.size()>0){
														//ArrayList sdlist=new ArrayList(ldList.values());
														List<String> sdlist = new ArrayList<String>(ldList.keySet());
														//ArrayList sdlist= RFServlet.instance.adInfoCache.getShopCategoryData(data.getUSERID(),data.getCATE1());
														ArrayList<Integer> rndList = StringUtils.getRandList( sdlist.size() );
														logger.debug("adbn[524] sdlist.size"+ sdlist.size());
														ArrayList addsList=new ArrayList();
													
														for( int j=0; j<1; j++){
															
															ShopData adds = new ShopData();
															//ShopData ss = (ShopData) sdlist.get( rndList.get(j) );
															ShopData ss =RFServlet.instance.adInfoCache.getShopPCodeData(data.getUSERID(),sdlist.get(rndList.get(i)).toString());
															if(ss!=null){
																adds.setPNM(ss.getPNM());
																adds.setPCODE(ss.getPCODE());
																adds.setPARTID(ss.getPRICE());
																adds.setSC_TXT(ss.getCATE1());
																adds.setURL(ss.getURL());
																adds.setPURL(ss.getPURL());
																adds.setIMGPATH(ss.getIMGPATH());
																adds.setPRICE(ss.getPRICE());
																
																adds.setIP(keyIp);
																adds.setUSERID(data.getUSERID());
																adds.setSITE_CODE(data.getSITE_CODE());
																//adds.setPARTID(aaa.getPARTID());
																adds.setGB(data.getGB());
																adds.setRF("");
																adds.setRDATE(ymd);
																adds.setRTIME(hm);
																adds.setSvc_type("normal");
																adds.setMCGB("A");	// 확장노출 키값 A:배너, B:아이커버, C:브랜드링크
																
																if(chk.equals("")){
																	dumpObj.getShopLogData().add(adds);
																	//manageCookie.shopLogReSet(scfg.getUserid(),adds,request, response);
																	addsList.add(adds);
																}
																logger.debug(adds.getInfo("adbn[551] adds.info "+ j));
															}
														}
														newVal=manageCookie.shopLogManage(newVal,addsList,"append");
													}
												}
											}
										}
										
										debug="a-7-ff";
										NumberFormat nf= new DecimalFormat("###,###,###");
										if(igb.equals("3")){
											htmlStr.append(" <td width=\"125\"><a href=\""+slink+"\" target='_blank'><img id=\"drad_img\"  src=\""+imgstr+"\" width=\"120\" height=\"82\" border=0></a></td> \n");
											htmlStr.append(" <td width=\"115\"><a href=\""+slink+"\" target='_blank'><font color=\"black\"><b>"+pnm+"</b></font><br><font color=\"red\"><b>\\ "+nf.format(Integer.parseInt(price))+"원<b></font></a></td> \n");
										}else if(igb.equals("2")){
											htmlStr.append(" <tr>                                                                                                                                                                                      \n");
											htmlStr.append(" 	<td width=\"120\"><a href=\""+slink+"\" target='_blank'><img id=\"drad_img\"  src=\""+imgstr+"\" width=\"112\" height=\"90\" border=0></a></td>                                                \n");
											htmlStr.append(" 	</tr>                                                                                                                                                                                  \n");
											htmlStr.append(" 	<tr>                                                                                                                                                                                   \n");
											htmlStr.append(" 	<td width=\"120\" height=\"90\"><a href=\""+slink+"\" target='_blank'><font color=\"black\"><b>"+pnm+"</b></font><br><font color=\"red\"><b>\\ "+nf.format(Integer.parseInt(price))+"원</font></a></td> \n");
											htmlStr.append(" 	</tr>                                                                                                                                                                                  \n");
										}
										validCnt++;
										if(i==rList.size()-1){
											AdChargeData crd=(AdChargeData)appContext.getBean("AdChargeData");
											crd.setSITE_CODE(rList.get(i).getSITE_CODE());
											crd.setADGUBUN("SR");
											crd.setTYPE("V");
											crd.setYYYYMMDD(ymd);
											crd.setHHMM(kh);
											crd.setPC(pc);
											crd.setUSERID(rList.get(i).getUSERID());
											crd.setPOINT("0");
											crd.setPPOINT("0");
											crd.setYM(ym);
											crd.setNO(rList.get(i).getNO()+"");
											logger.debug("rList.get(i).getNO()="+rList.get(i).getNO()+","+rList.get(i).getPCODE());
											crd.setS(s);
											crd.setPCODE(rList.get(i).getPCODE());
											crd.setSCRIPTUSERID(ms.getUSERID());
											crd.setIP(keyIp);
											crd.setSRView2(validCnt-1);
											crd.setMCGB( sd2.getMCGB()); // 첫번째 상품의 mcgb를 가지고 통계잡기
											if(s.length()>0 && ms.getUSERID()!=null && ms.getUSERID().length()>0 && !types.equals("floating") ){
												crd.setPRODUCT( StringUtils.gAtData("nor,video,nor", "nor,video,flash", types, ",", "nor") );
												if(chk.equals(""))dumpObj.getNormalChargeLogData().add(crd);
											}
											
											logger.debug(crd.getInfo("adbn crd.info="));
										}
										/// shop adview stats ///
										if(data.getPCODE()!=null && data.getPCODE().length()>0){
											RFServlet.instance.dumpDb.setShopStatsData(data.getUSERID(),data.getPCODE(),data,"adview");
										}
										if(validCnt==3) break;
										/*
										DayMediaLogData mld=(DayMediaLogData)appContext.getBean("DayMediaLogData");
										mld.setMC(s);
										mld.setMCODE(rList.get(i).getUSERID());
										mld.setSDATE(ymd);
										mld.setVIEWCNT(1);
										mld.setCLICKCNT(0);
										mld.setPOINT(0);
										mld.setGUBUN("SR");
										mld.setMEDIA_ID(ms.getUSERID());
										mld.setActGubun("V");
										logger.debug(mld.getInfo("adbn mld.info="));
										dumpObj.getNormalDayMediaLogData().add(mld);
										*/
										debug="a-8,"+i;
									}
								}// for 종료..
								htmlStr.append("</tr></table></div>");
								
								logger.debug("for 문 종료됨....");
								if( !types.equals("floating") && chk.equals("") ){
									out.println(htmlStr.toString());
									return;
								}else{
									viewcnt2=2;
								}
							} // rList.size 종료
						} else {

							if( rList.size()>0 && ms.getAccept_sr().equals("Y") && scfg!=null && scfg.getStateW().equals("Y") ){
								logger.debug("adbn[321] rList.size="+rList.size());
								adgubun = "SR";
								
							/*	if( !scfg.getMcgb().equals("") ){
									adgubun="SP";
								}*/
								ArrayList addsList=new ArrayList();
								
								//String chk_code= scfg.getSite_code();
								//노출 순서 정보를 site_code기준에서 shop_log no 로 변경..
								String chk_code="SR_"+ scfg.getSite_code()+"^"+scfg.getPcode();
								
								String resultvalue = "";
								
								//int alCnte = Integer.parseInt(alCnt);
								resultvalue=manageCookie.makeCookieStrNew(response, request, chk_result, chk_ck, chk_code, 20, chk);
					
								logger.debug("adbn sr igb=1 resultvalue="+resultvalue);

								if( resultvalue.equals("next") ){
									ShopData slog= new ShopData();
									slog.setIP( keyIp );
									slog.setGB(adgubun);
									slog.setNO(Long.parseLong( scfg.getNo() ));
									slog.setSvc_type("normal");
									slog.setSITE_CODE(scfg.getSite_code());
									slog.setPCODE(scfg.getPcode());
									if(chk.equals("")){
										dumpObj.getDelShopLogData().add(slog);
										//manageCookie.shopLogReSet(scfg.getUserid(),slog,request, response);
										newVal=manageCookie.shopLogManage(1,newVal,slog,"edit");
									}
								}
								
								// 상품 추가노출
								if( (resultvalue.equals("next")) && scfg.getFlag().equals("1") ){
									
									AdConfigData tmpFg=RFServlet.instance.adInfoCache.getRecomAdConfig(scfg.getUserid(),us,s,"adlink","banner");
									
									if( tmpFg!=null && scfg.getMcgb().equals("") || !scfg.getMcgb().equals("A") ){
										LinkedHashMap ldList= RFServlet.instance.adInfoCache.getShopStatsData(scfg.getUserid(),scfg.getCate1());
										if(ldList!=null && ldList.size()>0){
											//ArrayList sdlist=new ArrayList(ldList.values());
											List<String> sdlist = new ArrayList<String>(ldList.keySet());
											ArrayList<Integer> rndList = StringUtils.getRandList( sdlist.size() );
											logger.debug("adbn[306] sdlist.size"+ sdlist.size());
											
											for( int i=0; i<rndList.size() && i<2; i++){
												ShopData adds = new ShopData();
												ShopData ss =RFServlet.instance.adInfoCache.getShopPCodeData(scfg.getUserid(),sdlist.get(rndList.get(i)).toString());
												logger.debug("adbn[697] 1 "+ss);
												if(ss!=null){
													adds.setPNM(ss.getPNM());
													adds.setPCODE(ss.getPCODE());
													adds.setPARTID(ss.getPRICE());
													adds.setSC_TXT(ss.getCATE1());
													adds.setURL(ss.getURL());
													adds.setPURL(ss.getPURL());
													adds.setIMGPATH(ss.getIMGPATH());
													adds.setPRICE(ss.getPRICE());
													
													adds.setIP(keyIp);
													adds.setUSERID(scfg.getUserid());
													adds.setSITE_CODE(tmpFg.getSite_code());
													//adds.setPARTID(scfg.getPARTID());
													adds.setGB("02");
													adds.setRF("");
													adds.setRDATE(ymd);
													adds.setRTIME(hm);
													adds.setSvc_type("normal");
													adds.setMCGB("A");	// 확장노출 키값 A:배너, B:아이커버, C:브랜드링크
													
													logger.debug("adbn[697] 15 ");
													
													if(chk.equals("")){
														dumpObj.getShopLogData().add(adds);
														//manageCookie.shopLogReSet(scfg.getUserid(),adds,request, response);
														addsList.add(adds);
													}
													logger.debug(adds.getInfo("adbn[325] adds.info "+ i));
												}
												logger.debug("adbn[697] 2 "+ss);
											}
											newVal=manageCookie.shopLogManage(newVal,addsList,"append");
										}
									}
								}
							}
							
						}	
					}
				}
				newVal=java.net.URLEncoder.encode(newVal,"utf-8");
				manageCookie.makeCookie("shop_log",newVal,60*24*60*60, response);
				
				logger.debug("adbn[661] um");
				if( (adgubun==null || adgubun.equals("")) && ms.getAccept_um().equals("Y") ){
					ic_um=StringUtils.getURLDecodeStr(ic_um,"euc-kr");
					debug="a-8-4";
					if(ic_um.length()>0){
						
						logger.debug("일반광고 url 리타게팅.. :START");
						logger.debug("ic_um="+ic_um);
						StringTokenizer st=new StringTokenizer(ic_um,"|||",false);
						int tot_cnt= StringUtils.gAtCnt(ic_um, "|||");
						int id=-1;
						
						while(st.hasMoreElements()){
							id++;
							String ic_umSel=st.nextElement().toString();
							
							scfg=RFServlet.instance.adInfoCache.getNormalUrlMatchAdConfig(us,s,ic_umSel,addpq,chk_ck.toString(), igb, "UM", 20, "web");
							String str_tmp="";
							
							if(scfg!=null){
								adgubun  = "UM";
								
								String chk_code= "BNUM_"+ ic_umSel; //scfg.getSite_code();
								str_tmp=manageCookie.makeCookieStr(scfg.getUserid(),response, request,"BNUM", chk_code, chk_result, chk_ck, 20,chk);
								//chk_ck=resultvalue;
								
								logger.debug("adbn[712] str_tmp "+ str_tmp+",tot_cnt="+tot_cnt+",id="+id);
								
								if( str_tmp.equals("next") && tot_cnt==id ){
									manageCookie.clearChkCk(response, chk_result, chk_ck.toString(), ic_um, "banner","BNUM");
								}
								
								logger.debug("일반광고 url 리타게팅.. :OK");
								break;
							}else{
								logger.debug("일반광고 url 리타게팅 scfg=null,"+ic_umSel);
							}
							logger.debug( "adbn[361] tot_cnt="+ tot_cnt +" id="+ id +" chk_result="+ chk_result +" chk_ck="+ chk_ck.toString() );
							if( (scfg==null && tot_cnt==id) ){
								manageCookie.clearChkCk(response, chk_result, chk_ck.toString(), ic_um, "banner","BNUM");
							}
						}
					}
				}

				/*
				logger.debug("adbn[704] st");
				if( adgubun.length()==0 && ms.getAccept_st().equals("Y") ){
					ic_um=StringUtils.getURLDecodeStr(ic_um,"euc-kr");
					logger.debug("투데이 추천 노출.."+ ic_um);
					
					if(ic_um.length()>0){
						StringTokenizer st=new StringTokenizer(ic_um,"|||");
						int tot_cnt= StringUtils.gAtCnt(ic_um, "|||");
						int id=-1;
						while(st.hasMoreElements()){
							id++;
							String ic_umSel=st.nextElement().toString();
							
							scfg=RFServlet.instance.adInfoCache.getTodayMatchAdConfig(us,s,ic_umSel, addpq, chk_ck.toString(), "banner", "ST", 20);
							String str_tmp="";
							
							if(scfg!=null ){
								logger.debug(scfg.getInfo("iadbn[347] scfg.info  > "));
								
								adgubun  = "ST";
								
								String chk_code= adgubun+"_"+scfg.getKno();
								str_tmp = manageCookie.makeCookieStr(response, request, adgubun, chk_code, chk_result, chk_ck, 20);
								logger.debug("iadbn[353] resultvalue="+ str_tmp);
								
								manageCookie.makeCookie("push", "1", 60*6, response);
								
								logger.debug("투데이추천 .. :OK");
								break;
							}
							
						}
					}
				}
				
				logger.debug("adbn[749] real kl");
				if(scTxt!=null && scTxt.length()>0 && ms.getAccept_kl().equals("Y") ){
					scTxt=StringUtils.getURLDecodeStr(scTxt,"euc-kr");
					logger.debug("실시간 키워드 리타게팅....==>"+ scTxt);
					
					scfg=RFServlet.instance.adInfoCache.getNormalkeywordMatchAdConfig(us,s,scTxt, new HashMap(), chk_ck, igb,keyIp);
					if( scfg!=null ){
						adgubun  = "KL";
						mcgb="A";
						scfg.setMcgb(mcgb);
						scTxtSel=scTxt;

						String chk_code= "KL_"+ scfg.getKno();
						String resultvalue= manageCookie.makeCookieStr(adgubun,chk_code, chk_result,chk_ck,response, request);
						chk_ck=resultvalue;
						logger.debug("adbn real kl resultvalue="+ resultvalue);
						
						logger.debug("실시간 키워드 리타게팅.... :OK");
					}
				}
				*/
				
				logger.debug("adbn[691] kl");
				//과거 키워드 조회 조건. scTxt 길이 100넘었으므로 로그 참조..
				debug="a-8-5";
				if( (adgubun==null || adgubun.equals("")) && ms.getAccept_kl().equals("Y") ){
					ic_ki= StringUtils.getURLDecodeStr(ic_ki, "euc-kr");
					logger.debug("일반광고 과거 키워드 .... :START "+ ic_ki);
					//HashMap khm=RFServlet.instance.adInfoCache.getRfKeywordLogData(keyIp);
					
					if( ic_ki!=null && ic_ki.length()>0 ){
						StringTokenizer st = new StringTokenizer(ic_ki,"|||",false);
						int id= -1;
						while(st.hasMoreElements()){
							id++;
							String t = st.nextElement().toString();
							scTxtSel=StringUtils.getURLDecodeStr(t, "euc-kr");;
							
							if( scTxtSel!=null ){
								logger.debug("adbn[489] scTxtSel="+ scTxtSel);
								
								scfg = RFServlet.instance.adInfoCache.getNormalkeywordMatchAdConfig(us,s,scTxtSel, addpq, chk_ck.toString(), igb, "web");
								
								if( scfg!=null ){
									logger.debug( scfg.getInfo("adbn[759 scfg.info ") );
									adgubun  = "KL";

									String chk_code = "BNKL_"+java.net.URLEncoder.encode(scTxtSel,"euc-kr");
//									String resultvalue= manageCookie.makeCookieStr(adgubun,chk_code, chk_result,chk_ck,response, request);
									String resultvalue= manageCookie.makeCookieStr(scfg.getUserid(),response, request, "BNKL", chk_code, chk_result, chk_ck, 20,chk);
									//chk_ck=resultvalue;
									logger.debug("adbn kl 2 resultvalue="+ resultvalue);
																		
									logger.debug("일반광고 과거 키워드 .... :OK");
									break;
								}else{
									logger.debug("일반광고 과거 키워드:scfg="+scfg);
								}
							}
							
						}
					}
					
				}
			}
			

			logger.debug("adbn[733] ad igb-"+ igb +" types-"+ types);
			debug="a-8-6";
			if( (adgubun==null || adgubun.equals("")) && ms.getAccept_ad().equals("Y") ){
				logger.debug("adgubun==null 이니 기본 광고 전송...");
				
				try{
					// 1 노출할 솔루션선택
					// 2 제외할s값
					// 3 제한되면 1로 
					// 4 현재배너의 사이즈로 선택된 솔루션의 광고주 선택
					// 5 광고노출
					Document doc= (Document) RFServlet.instance.xml.get("adbn_partner");

					//logger.debug("adbn doc="+doc);
					
					ArrayList<String> list= StringUtils.getDocToArray(doc.select("partner config names item"));
					ArrayList list1= StringUtils.getRandList(list.size());
					
					logger.debug("adbn list1="+list1);
					
					String sel_solution="";
					for(int i=0; i<list1.size(); i++){
						String item = list.get( (Integer) list1.get(i) );
						if(item.equals("withpang")) break;
						
						ArrayList<String> list_deny= StringUtils.getDocToArray(doc.select("partner config deny "+ item +" s" ));
						logger.debug("adbn item="+item+", list_deny="+ list_deny);
						
						if( list_deny==null){
							sel_solution=item;
							break;
						}else if( !list_deny.contains(s) ){
							sel_solution=item;
							break;
						}
					}

					logger.debug("adbn sel_solution="+sel_solution);
					if( sel_solution.equals("") ){
						sel_solution="withpang";
					}
					
					Elements site= doc.select("partner adinput "+sel_solution+" site item#"+iwh);
					logger.debug("adbn site="+site);
					if(site.select("sitecode").html().equals("")){
						sel_solution="withpang";
					}
					
					logger.debug("adbn sel_solution="+sel_solution);
					
					if( psb.equals("99") || sel_solution.equals("withpang")
							|| ( types.equals("flash")||types.equals("floating")||types.equals("video")) ){
						
						String baseadKey=us+"_"+ms.getAD_TYPE();
						logger.debug("baseadKey="+baseadKey);
						
						String dispo=manageCookie.getCookieInfo(request,"dispo");
						scfg=RFServlet.instance.adInfoCache.getNormalBaceAdConfig(baseadKey,addpq,"",dispo,"");
						if(scfg!=null){
							logger.debug("기본 광고 전송 :OK");
							adgubun="AD";
							scfg.setNo("0");
						}else{
							logger.debug("기본 광고 전송 ERROR: scfg is null.");
						}
					}else{
						
						String html = site.select("tags").text();
						
						html = html.replace("{us}", us);
						html = html.replace("{s}", s);
						html = html.replace("{u}", sel_solution);
						html = html.replace("{site_code}", site.select("sitecode").text());
						
						if(sel_solution.equals("adnew01")){
							String get_xmldata= doc.select("partner adinput adnew01 site item#"+iwh+" get_xmldata").html();
							//logger.debug("adbn get_data="+get_xmldata);
							
							Document adnew_tmp=(Document) Jsoup.connect(get_xmldata).get();
							String imgurl= adnew_tmp.select("item imgurl").html();
							String linkurl= adnew_tmp.select("item linkurl").html();
							
							//logger.info("adbn adnew_tmp="+adnew_tmp);
							
							html = html.replace("{img}", imgurl );
							html = html.replace("{link}", URLEncoder.encode(linkurl,"utf-8") );
						}
						
						adgubun="AD";
						scfg=new AdConfigData(); 
						scfg.setSite_code( site.select("sitecode").text() );
						scfg.setNo("0");
						scfg.setPcode("");
						scfg.setUserid( sel_solution );
						scfg.setSite_url("http://"+domain+"/");
						scfg.setAdnew_html( html );
						scfg.setAdnew(true);
						
						logger.debug("adbn941 scfg="+scfg);
					}
				}catch(Exception e){
					logger.error(e+":basead loaad error");
					adgubun="";
				}
			}
			
			if( !chk.equals("") ){
				Document doc = (Document) RFServlet.instance.xml.get("adbn_partner");
				String html = "";
				
				ArrayList list= StringUtils.getDocToArray(doc.select("partner config names item"));
				
				String partner_uid= chk;
				
				logger.debug("adbn 964 list="+list +" partner_uid="+ partner_uid +" adgubun="+ adgubun);
				
				if( !partner_uid.equals("") && list.contains(partner_uid) ){
					
					if( scfg==null ){
						html = doc.select("partner adchk "+partner_uid+" tags fail").html();
						
					}else{
						html = doc.select("partner adchk "+partner_uid+" tags succ").html();
						
						if( partner_uid.equals("realclick01") ){
							
						}else{
							
						}
						
						String partner_url = doc.select("partner adchk "+partner_uid+" type url").html();
						logger.debug("adbn 1022 partner_url="+partner_url);
						partner_url = partner_url.replace("{u}", u);
						partner_url = partner_url.replace("{us}", us);
						partner_url = partner_url.replace("{s}", s);
						partner_url = partner_url.replace("{iwh}", iwh);
						partner_url = partner_url.replace("{igb}", igb);
						html=html.replace("{value}", "html");
						html=html.replace("{link}", partner_url);
					}
				}
				
				out.println(html);
				return;
			}
			
			debug="a-8-7";
			if( !adgubun.equals("SR") && ( ms.getUSERID().equals("sndKorea") || s.equals("781") ) ){
				logger.debug("상품만 노출되게처리");
				adgubun = "";
			}
			
			
			
			logger.debug("adbn[763] hb");
			if(adgubun==null || adgubun.equals("")){
				try{
					String h_banner=ms.getH_BANNER();
					
					if( !psburi.equals("") ){
						htmlStr.append(" <script language = \"javascript\"> \n");
						htmlStr.append(" location.replace('"+psburi+"'); \n");
						htmlStr.append(" </script> \n");
						out.print(htmlStr.toString());
						return;
					}else{
						if(h_banner==null || h_banner.equals("")){
							return;
						}else{
							adgubun="HB";
							
							if( ms.getH_TYPE().equals("script") ){
								htmlStr.append(" <script language = \"javascript\" src='"+ms.getH_BANNER()+"'></script> \n");
							}else{
								htmlStr.append(" <script language='javascript' type='text/javascript'> \n");
								if( ms.getUSERID().equals("sndKorea") || ms.getUSERID().equals("etorrent") || ms.getUSERID().equals("hnscom") ){
									htmlStr.append(" document.location.replace('"+h_banner+"'); \n");
								}else{
									htmlStr.append(" document.location.href = \""+h_banner+"\"; \n");
								}
								htmlStr.append(" </script> \n");
							}
							
							
							if( types.equals("floating") || igb.equals("7") ){
								htmlStr.setLength(0);
								htmlStr.append("var wp_adbn_scfg"+rnd_no+"={}; \n");
								htmlStr.append("wp_adbn_scfg"+rnd_no+"._wp_adgubun='h_banner'; \n");
								htmlStr.append("wp_adbn_scfg"+rnd_no+"._wp_url='"+h_banner+"'; \n");
							}
							out.print(htmlStr.toString());
							
							return;
						}	
					}
				}catch(Exception e){
					logger.error(e+":ms.getH_BANNER() error");
					return;
				}
			}
			if(adgubun==null || adgubun.equals("")){ 
				logger.debug("final process : adgubun==null");
				return;
			}
			
			
			
			if(scfg==null){
				logger.error("adgubun="+adgubun+",scfg is null");
				return;
			}
			
			
			logger.debug("adbn[690] adgubun="+adgubun);
			logger.debug(scfg.getInfo("adbn[839] scfg.info "));
			
			String r_site_code  = scfg.getSite_code();
			String r_site_url   = scfg.getSite_url();	
			String r_no         = s;
			String r_userid     = scfg.getUserid();
			String r_purl       = scfg.getPurl();
			String r_site_etc   = scfg.getSite_etc();
			String r_imgpath    = scfg.getImgpath();
			String r_adtxt      = scfg.getAdtxt();
			String strimg       = scfg.getImgname();
			String shopname="";
			String imgstr="";
			String shop_logno="";
			
			debug="a-8-8";
			//keylog_no 값은 당장 알 수 없으므로 최근IP정보로 동작되도록 한다..
			String keylog_no= scfg.getKno(); if(keylog_no==null)keylog_no="";
			
			if( adgubun.equals("SR") ){
				shop_logno= scfg.getNo();
				if( shop_logno==null ) shop_logno="";
			}
			
			if( scfg.getMcgb()==null ) scfg.setMcgb("");
			
			imgstr = "http://"+domain+"/ad/imgfile/"+strimg;
			debug="a-8-81";
			String slink  = "/servlet/drc?no="+shop_logno+"&kno="+keylog_no+"&s="+s+"&gb="+adgubun+"&sc="+r_site_code+"&mc="+r_no
							+"&mcgb="+scfg.getMcgb()+"&u="+r_userid
							+"&product="+ StringUtils.gAtData("nor,video,nor", "nor,video,flash", types, ",", "nor")
							+"&clk_param="+URLEncoder.encode(clk_param,"euc-kr");
			String oslink= slink;
			
			debug="a-8-82";
			logger.debug("slink="+slink);
			int bprice=0;
			debug="a-8-83";
			if(adgubun.equals("SR") || adgubun.equals("ST") || adgubun.equals("SP") ){
				shopname = scfg.getPnm();
				if(scfg.getPnm().length()>=30){
					//shopname = scfg.getPnm().substring(0,30)+"...";
				}
				debug="a-8-84";
				try{
					bprice=Integer.parseInt(scfg.getPrice());
				}catch(Exception e){
					bprice=0;
					logger.error(e);
				}
				debug="a-8-85";
				String local_img= r_imgpath;
				//try{
				//	local_img= "http://"+domain+"/ad/sr/"+ scfg.getUserid() 
				//			+"/"+ r_imgpath.split("/")[ r_imgpath.split("/").length-1 ];
				//}catch(Exception e){
				//	local_img= r_imgpath;
				//}
				imgstr=local_img;
				if( r_site_etc!=null && r_site_etc.length()>0 ){
					r_purl += r_site_etc.substring(0, 1).equals("&")? r_site_etc: "&"+ r_site_etc ;
				}
				slink+="&pCode="+scfg.getPcode()+"&slink="+URLEncoder.encode(r_purl,"UTF-8");
			}else{
				shopname = scfg.getMedia_code();
			}
			debug="a-8-8";
			if(shopname==null || shopname.equals("")) shopname="오늘만 특가!";

//			if(!adgubun.equals("AD")){
//				String chk_code= scfg.getSite_code();
//				String resultvalue=manageCookie.makeCookieStr(adgubun, scfg.getNo2(), chk_result,chk_ck,chk_code, 0, 0, response, request);
//				logger.debug("resultvalue="+resultvalue);
//			}
			debug="a-8-9";
			if(igb.equals("2")){
				strimg =scfg.getImgname2();
			}else if(igb.equals("3")){
				strimg =scfg.getImgname3();
			}else if(igb.equals("4")){
				strimg =scfg.getImgname4();
			}else if(igb.equals("5")){
				strimg =scfg.getImgname5();
			}else{
				strimg =scfg.getImgname();
			}
			String rurl   = "";
			if(adgubun.equals("KL")){
				r_site_url = (r_site_url.substring(0,4).equals("http")?r_site_url:"http://"+r_site_url);
				rurl   = URLEncoder.encode(r_site_url,"UTF-8");
				RFData keywordLog=(RFData)appContext.getBean("RFData");
				keywordLog.setRDATE(ymd);
				keywordLog.setRTIME(kh);
				keywordLog.setMCGB(mcgb);
				keywordLog.setSC_TXT(scTxt);
				keywordLog.setRURL(from);
				keywordLog.setDPOINT(0);
				keywordLog.setIP(keyIp);
				keywordLog.setMC(s);
				keywordLog.setK_GUBUN("n");
				if(chk.equals(""))dumpObj.getKeywordLogData().add(keywordLog);
				imgstr = "http://"+domain+"/ad/imgfile/"+strimg;
				slink+="&slink="+URLEncoder.encode(rurl,"UTF-8");
			}
			String protocal="http";
			if(request.isSecure()){
				protocal="https";
			}
			if(adgubun.equals("AD")){
				r_site_url = (r_site_url.substring(0,4).equals("http")?r_site_url:"http://"+r_site_url);
				rurl	=URLEncoder.encode(r_site_url,"UTF-8");
				imgstr = "http://"+domain+"/ad/efile/"+r_adtxt;
				slink +="&slink="+rurl;
			}
			if(adgubun.equals("UM")){
				r_site_url = (r_site_url.substring(0,4).equals("http")?r_site_url:"http://"+r_site_url);
				rurl	=URLEncoder.encode(r_site_url,"UTF-8");
				imgstr = "http://"+domain+"/ad/imgfile/"+strimg;
				slink+="&slink="+rurl;
			}
			if(adgubun.equals("ST")){
				r_site_url = (r_site_url.substring(0,4).equals("http")?r_site_url:"http://"+r_site_url);
				rurl	=URLEncoder.encode(r_site_url,"UTF-8");
				slink+="&slink="+rurl;
			}
			debug="a-8-10";
			String starget = "_blank";
			String adSizeStr=iwh;
			adSizeStr=adSizeStr.replace("i","");
			adSizeStr=adSizeStr.replace("m","");
			StringTokenizer st=new StringTokenizer(adSizeStr,"_",false);
			int wvalue=250;
			int hvalue=250;
			int chkInt=0;
			String chkim=ms.getAD_TYPE();
			while(st.hasMoreElements()){
				try{
					if(chkInt==0){
						wvalue=Integer.parseInt(st.nextElement().toString());
					}else if(chkInt==2){
						hvalue=Integer.parseInt(st.nextElement().toString());
					}
				}catch(Exception ex){
					logger.error(ex);
				}
				chkInt++;
			}
			if(s.equals("330")){
				wvalue=240;
				hvalue=220;
			}
			if(wvalue==0){
				wvalue=250;
			}
			if(hvalue==0){
				hvalue=250;
			}
			if(igb.equals("2")){
				wvalue = 120;
				hvalue = 600;
			}else if(igb.equals("3")){
				wvalue = 728;
				hvalue = 90;
			}else if(igb.equals("4")){
				wvalue = 150;
				hvalue = 150;
			}else if(igb.equals("5")){
				wvalue = 90;
				hvalue = 90;
			}

			logger.debug("adbn iwh="+iwh);
			if( !iwh.equals("") ){
				try{
					wvalue = Integer.parseInt(StringUtils.gAt1(iwh, 0, "_"));
					hvalue = Integer.parseInt(StringUtils.gAt1(iwh, 1, "_"));
				}catch(Exception e){}
			}
			shopname = StringUtils.strCut(shopname, null, (wvalue/16)*2, 0, true, true);

			int divtop=0;
			int divtop2=0;
			try{
				wvalue = wvalue-6;
				hvalue = hvalue-8;
				divtop = hvalue - 65;
				divtop2 = hvalue - 53;
			}catch(Exception e){
				logger.error(e);
			}
			debug="a-8-11";
			String chk2=""; //용도를 알수 없는 값..
			NumberFormat nf= new DecimalFormat("###,###,###");  
			if(adgubun.equals("SR") || adgubun.equals("SP") || adgubun.equals("ST") ){
				htmlStr.append(" <div id=\"divimg\" style='position:absolute;top:2px; left:"+(wvalue-18)+"px;width:20px; height:20px;text-align:center;background-color:#000000; background:url(http://"+domain+"/image/1px_black.png); '><a href=\"http://www.withpang.co.kr/ad/linfo.php\" target=\"_blank\"><img id=\"drad_img\"  src=\"/image/btn_w.gif\" width=\"20\" border=0></a></div> \n");
				htmlStr.append(" 	<div id=\"divtxt\" style='position:absolute; top:"+divtop+"px; left:0px; z-index:3; padding-top:25px; width:100%; height:50px; vertical-align:bottom;text-align:center;'>                                                                         \n");
				htmlStr.append(" 	<a href=\""+slink+"\" target='"+starget+"'><!-- 상품이름 시작 --><font id=\"pname\">"+shopname+"</font><!-- 상품이름 끝 --><br/>                                                                                                                    \n");
				htmlStr.append(" 	<font color=#FFFF00 style=font-size:14px;><b>\\ "+nf.format(bprice)+"원</b></font><!-- 상품가격 끝 --></a></div>                                                                                                                          \n");
				htmlStr.append(" 	<div style='position:absolute;top:"+divtop2+"px;left:2px;z-index:1; background-color:#000000; background:url(http://"+domain+"/image/1px.png); width:"+wvalue+"px; height:55px;'></div>                                                        \n");
				htmlStr.append(" 	<div id=\"ad2\" align=\"center\" style=\"width:"+wvalue+"px; height:"+hvalue+"px;\"><a href=\""+slink+"\" target='"+starget+"'><img id=\"drad_img\" \n");
				if(chkim.equals("m") && !chk2.equals("N")){
					htmlStr.append(" onload=\"javascript:imgcheck(this, true);\" \n");
				}
				htmlStr.append(" src=\""+imgstr+"\" width=\"100%\" height=\""+hvalue+"\" border=0></a></div> \n");
			}else{
				htmlStr.append(" <div id=\"divimg\" style='position:absolute;top:2px;left:"+(wvalue-18)+"px;width:20px; height:20px;text-align:center;background-color:#000000;'> \n");
				htmlStr.append(" <a href=\"http://www.withpang.co.kr/ad/linfo.php\" target=\"_blank\"><img id=\"drad_img\"  src=\"/image/btn_w.gif\" width=\"20\" border=0></a></div> \n");
				htmlStr.append(" <div style='position:absolute;top:"+(divtop2+10)+"px;left:2px;z-index:1; '></div> \n");
				htmlStr.append(" <div id=\"ad2\" align=\"center\" style=\"width:"+wvalue+"px; height:"+hvalue+"px;\"><a href=\""+slink+"\" target='"+starget+"'><img id=\"drad_img\" \n");
				if(chkim.equals("m") && !chk2.equals("N")){
					htmlStr.append(" onload=\"javascript:imgcheck(this, true);\" \n");
				}
				htmlStr.append(" src=\""+imgstr+"\" width=\"100%\" height=\""+hvalue+"\" border=0></a></div> \n");
			}
			htmlStr.append("</div></body></html>");
			//htmlStr.setLength(0);
			
			
			debug="a-8-11-1";																		 // 베너신규 타입 추가 igb가 20보다 클때만
			if( types.equals("flash") || types.equals("video") || igb.equals("6") || igb.equals("4") || Integer.parseInt(igb) > 20
					|| (adgubun.equals("ST") && (igb.equals("2")||igb.equals("3"))) ){
				
				logger.debug(scfg.getInfo("adbn[1022] info "+ adgubun ));
				htmlStr.setLength(0);
				
				int length=1;
				int max=0;
				if( adgubun.equals("SR") || adgubun.equals("ST") ){
					
					String purl= scfg.getPurl();
					String site_etc="";
					if( scfg.getSite_etc()!=null && scfg.getSite_etc().length()>0 ){
						site_etc = (scfg.getSite_etc().substring(0, 1).equals("&")? scfg.getSite_etc(): "&"+ scfg.getSite_etc());
						purl += site_etc;
					}
					r_purl = oslink +"&pCode="+scfg.getPcode()+"&slink="+URLEncoder.encode(r_purl,"UTF-8");
					String pnm=scfg.getPnm();
					//if( igb.equals("2") || igb.equals("3") ){
						if( pnm.length()>10 ){
							pnm= scfg.getPnm().substring(0,10) +"..";
						}
					//}
					
					String local_img= scfg.getImgpath();
					
					String logo2 = scfg.getSchonlogo();
					//try{
					//	local_img= "ad/sr/"+ scfg.getUserid() 
					//			+"/"+ scfg.getImgpath().split("/")[ scfg.getImgpath().split("/").length-1 ];
					//}catch(Exception e){
					//	local_img= scfg.getImgpath();
					//}
					
					html_flash.append("{\"t0\":\"\",\"pcode\":\""+scfg.getPcode()
							+"\",\"pnm\":\""+pnm+"\",\"price\":\""+scfg.getPrice()+"\",\"img\":\""+ local_img
							+"\",\"purl\":\""+ r_purl +"\",\"logo\":\""+ scfg.getImgname_logo() +"\",\"logo2\":\""+ logo2 +"\"}");
					//html_flash_img.append("{\"t0\":\"\",\"img\":\""+local_img+"\",\"logo\":\""+scfg.getImgname_logo()+"\"}");
					//html_flash_link.append("{\"t0\":\"\",\"purl\":\""+ r_purl +"\"}");
					
					
					LinkedHashMap ldList= RFServlet.instance.adInfoCache.getShopStatsData(scfg.getUserid(),scfg.getCate1());
					if(ldList!=null && ldList.size()>0){
						//ArrayList sdlist=new ArrayList(ldList.values());
						List<String> sdlist = new ArrayList<String>(ldList.keySet());
						ArrayList<Integer> rnd = StringUtils.getRandList( sdlist.size() );
						
						if( types.equals("video") ){
							max=4;
						}else if( types.equals("flash") && igb.equals("1") ){
							max=3;
						}else if( types.equals("flash") && (igb.equals("2") || igb.equals("3")) ){
							max=4;
						}else if( adgubun.equals("ST") && ( igb.equals("2") || igb.equals("3")) ){
							max=2;
							if( igb.equals("2") || igb.equals("3") ){
								max=4;
							}
							if( !types.equals("flash") ){
								max=2;
							}
						}else if( igb.equals("4") ){
							max=1;
						}else if( igb.equals("6") ){
							max=1;
						}else if (Integer.parseInt(igb) > 20){
							if(cntsr !=""||!cntsr.equals("")){
							    int cnt = Integer.valueOf(cntsr);
							   	max = cnt == 1 || cnt ==0 ?max++:cnt-1;
						    }else{
							 max=2;
							}	
						}
						viewcnt2=max;
						
						Iterator it = rnd.iterator();
						
						for( int i=0; it.hasNext();   ){
							if( i>=max ) break;
							
							String pcode=sdlist.get( (Integer)it.next() ).toString();
							ShopData item =RFServlet.instance.adInfoCache.getShopPCodeData(scfg.getUserid(), pcode);
							if(item!=null && !pcode.equals(scfg.getPcode()) ){
								i++;
								logger.debug(item.getInfo("adbn[1055] ex_item.info "));
								
								String imgpath= item.getIMGPATH();
								
								local_img= imgpath;
								//try{
								//	local_img= "ad/sr/"+ scfg.getUserid()
								//			+"/"+ item.getIMGPATH().split("/")[ item.getIMGPATH().split("/").length-1 ];
								//}catch(Exception e){
								//	local_img= item.getIMGPATH();
								//}
								
								String site_url= item.getPURL();
								if( site_url.indexOf("http")==-1 ){
									site_url = "http://"+ site_url;
								}
								
								if( site_url.length()>0 && r_site_etc.length()>0 ){
									site_url += r_site_etc.substring(0, 1).equals("&")? r_site_etc: "&"+ r_site_etc ;
								}
								site_url= oslink +"&pCode="+item.getPCODE()+"&slink="+ URLEncoder.encode(site_url,"UTF-8");
								
								
								pnm= item.getPNM();
								if( pnm.length()>12 ) pnm= pnm.substring(0, 12) +"...";
								
								if( igb.equals("2") || igb.equals("3") ){
									html_flash.append(",{\"t"+(i+1)+"\":\"\""
											+",\"pcode\":\""+item.getPCODE()+"\",\"pnm\":\""+pnm+"\""
											+",\"price\":\""+item.getPRICE()+"\""
											+",\"img\":\""+ local_img +"\",\"purl\":\""+ site_url +"\""
											+",\"logo\":\""+scfg.getImgname_logo()+"\",\"logo2\":\""+ logo2 +"\"} ");
									//html_flash_img.append(",{\"img\":\""+ local_img +"\",\"logo\":\""+scfg.getImgname_logo()+"\"}");
									//html_flash_link.append(",{\"purl\":\""+ site_url +"\"}");
								}else{
									html_flash.append(",{\"t"+(i+1)+"\":\"\""
											+",\"pcode\":\""+item.getPCODE()+"\",\"pnm\":\""+pnm+"\""
											+",\"price\":\""+item.getPRICE()+"\""
											+",\"img\":\""+ local_img +"\",\"purl\":\""+ site_url +"\""
											+",\"logo\":\""+scfg.getImgname_logo()+"\",\"logo2\":\""+ logo2 +"\"} ");
									//html_flash_img.append(",{\"img\":\""+ local_img +"\",\"logo\":\""+scfg.getImgname_logo()+"\"}");
									//html_flash_link.append(",{\"purl\":\""+ site_url +"\"}");
								}
								if(item.getPCODE()!=null && item.getPCODE().length()>0){
									RFServlet.instance.dumpDb.setShopStatsData(scfg.getUserid(),item.getPCODE(),item,"adview");
								}
								length++;
							}
						}
					}
					
					htmlStr.append("{\"client\":[{\"target\":\""+adgubun+"\",\"length\":\""+length+"\",\"logo\":\"\",\"oslink\":\""+""+"\",\"site_title\":\""+ scfg.getSite_title() +"\",\"data\":[");
					htmlStr.append(html_flash.toString());
					htmlStr.append(" ] } ] } ");

					//htmlStr1.append("{\"client\":[{\"target\":\"SR\",\"length\":\""+length+"\",\"logo\":\"\",\"data\":[");
					//htmlStr1.append(html_flash_img.toString());
					//htmlStr1.append(" ] } ] } ");
					
					//htmlStr2.append("{\"client\":[{\"target\":\"SR\",\"length\":\""+length+"\",\"logo\":\"\",\"oslink\":\""+oslink+"\",\"data\":[");
					//htmlStr2.append(html_flash_link.toString());
					//htmlStr2.append(" ] } ] } ");
					
				}else if ( adgubun.equals("UM") ){
					logger.debug("adbn[1159 "+ adgubun);
					String url = slink;
					String img = "http://"+domain+"/ad/imgfile/"+ strimg;
					
					html_flash.append("{\"t0\":\"\",\"pcode\":\"x\",\"pnm\":\""+scfg.getSite_name()+"\",\"site_name\":\""+scfg.getSite_name()+"\",\"price\":\"\",\"img\":\""+ img
							+"\",\"purl\":\""+ url +"\",\"logo\":\""+scfg.getImgname_logo()+"\"}");
					//html_flash_img.append("{\"img\":\""+ img +"\",\"logo\":\""+scfg.getImgname_logo()+"\"}");
					//html_flash_link.append("{\"purl\":\""+ url +"\"}");
					ArrayList list = RFServlet.instance.adInfoCache.getNormalUrlMatchList(us,s,scfg.getMedia_code(),addpq,chk_ck.toString(), igb);
					logger.debug("adbn[1168 list "+ list );
					logger.debug(scfg.toString());
					
					if( list!=null && list.size()>0 ){
						ArrayList<Integer> rnd= StringUtils.getRandList( list.size() );
						
						for(int i=0; i<list.size(); i++){
							
							if (Integer.parseInt(igb) > 20){	
								 if(cntad !=""||!cntad.equals("")){
								    int cnt = Integer.valueOf(cntad);
								   	max = cnt == 1 || cnt ==0 ?max++:cnt-1;
									if( i>=max ) break;
								  }else{
								   max=2;
								  }
								}
							
							if( i>3 ) break;
							
							if( igb.equals("6") && i>0 ){
								break;
							}else if( igb.equals("4") && i>0 ){
								break;
							}
							AdConfigData ex_item= (AdConfigData) list.get( rnd.get(i) );
							
							if( igb.equals("1") && ex_item.getImgname()!=null && ex_item.getImgname().length()>0 ){
								img= ex_item.getImgname();
							}else if( igb.equals("2") && ex_item.getImgname2()!=null && ex_item.getImgname2().length()>0 ){
								img= ex_item.getImgname2();
							}else if( igb.equals("3") && ex_item.getImgname3()!=null && ex_item.getImgname3().length()>0 ){
								img= ex_item.getImgname3();
							}else if( igb.equals("4") && ex_item.getImgname4()!=null && ex_item.getImgname4().length()>0 ){
								img= ex_item.getImgname4();
							}else{
								img= ex_item.getImgname();
							}
							img = "http://"+domain+"/ad/imgfile/"+ img;
							
							oslink= "/servlet/drc?no=&kno="+ex_item.getKno()+"&s="+s+"&gb="+adgubun+"&sc="+ex_item.getSite_code()+"&mc="+r_no
									+"&mcgb="+scfg.getMcgb()+"&u="+ex_item.getUserid()
									+"&product="+ StringUtils.gAtData("nor,video,nor", "nor,video,flash", types, ",", "nor");
							
							String site_url = ex_item.getSite_url();
							if( site_url.indexOf("http")==-1 ) site_url= "http://"+ site_url;
							site_url = oslink +"&slink="+ URLEncoder.encode(site_url,"UTF-8");
							
							html_flash.append(",{\"t"+(i+1)+"\":\"\""
									+",\"pcode\":\"x\",\"pnm\":\""+ ex_item.getSite_name() +"\",\"site_name\":\""+ ex_item.getSite_name() +"\",\"price\":\"\""
									+",\"img\":\""+ img +"\",\"purl\":\""+ site_url +"\",\"logo\":\""+ex_item.getImgname_logo()+"\"}");
							//html_flash_img.append(",{\"img\":\""+ img +"\",\"logo\":\""+ex_item.getImgname_logo()+"\"}");
							//html_flash_link.append(",{\"purl\":\""+ site_url +"\"}");
							
							AdChargeData crd=(AdChargeData)appContext.getBean("AdChargeData");
							crd.setSITE_CODE(ex_item.getSite_code());
							crd.setADGUBUN(adgubun);
							crd.setTYPE("V");
							crd.setYYYYMMDD(ymd);
							crd.setHHMM(kh);
							crd.setPC(pc);
							crd.setUSERID(ex_item.getUserid());
							crd.setPOINT("0");
							crd.setPPOINT("0");
							crd.setYM(ym);
							crd.setNO(ex_item.getNo());
							crd.setPCODE(scfg.getPcode());
							crd.setS(s);
							crd.setSCRIPTUSERID(ms.getUSERID());
							crd.setIP(keyIp);
							crd.setKNO(ex_item.getKno());
							crd.setSRView2(0);
							crd.setMCGB(scfg.getMcgb());
							crd.setViewcnt3(1);
							debug="a-9";
							if(s.length()>0 && ms.getUSERID()!=null && ms.getUSERID().length()>0){
								crd.setPRODUCT(StringUtils.gAtData("nor,video,nor", "nor,video,flash", types, ",", "nor"));
								if(chk.equals(""))dumpObj.getNormalChargeLogData().add(crd);
							}
							length++;
						}
					}
					
					htmlStr.append("{\"client\":[{\"target\":\""+adgubun+"\",\"length\":\""+length+"\",\"logo\":\"\",\"oslink\":\""+""+"\",\"data\":[");
					htmlStr.append(html_flash.toString());
					htmlStr.append(" ] } ] } ");
					
					//htmlStr1.append("{\"client\":[{\"target\":\"UM\",\"length\":\""+length+"\",\"logo\":\"\",\"data\":[");
					//htmlStr1.append(html_flash_img.toString());
					//htmlStr1.append(" ] } ] } ");

					//htmlStr2.append("{\"client\":[{\"target\":\"UM\",\"length\":\""+length+"\",\"logo\":\"\",\"oslink\":\""+""+"\",\"data\":[");
					//htmlStr2.append(html_flash_link.toString());
					//htmlStr2.append(" ] } ] } ");
					
				}else if( adgubun.equals("KL") ){

					String url = slink;
					String img = "http://"+domain+"/ad/imgfile/"+ strimg;
					
					html_flash.append("{\"t0\":\"\",\"pcode\":\"x\",\"pnm\":\""+scfg.getSite_name()+"\",\"site_name\":\""+scfg.getSite_name()+"\",\"price\":\"\",\"img\":\""+ img
							+"\",\"purl\":\""+ url +"\",\"logo\":\""+scfg.getImgname_logo()+"\"}");

					//html_flash_img.append("{\"img\":\""+ img +"\",\"logo\":\""+scfg.getImgname_logo()+"\"}");
					//html_flash_link.append("{\"purl\":\""+ url +"\"}");
					
					ArrayList list= RFServlet.instance.adInfoCache.getNormalkeywordMatchList(us,s, scTxtSel, addpq, chk_ck.toString(), igb, keyIp);
					if( list!=null && list.size()>0 ){
						ArrayList<Integer> rnd= StringUtils.getRandList( list.size() );
						
						for(int i=0; i<list.size(); i++){
							
							if (Integer.parseInt(igb) > 20){	
								 if(cntad !=""||!cntad.equals("")){
								    int cnt = Integer.valueOf(cntad);
								   	max = cnt == 1 || cnt ==0 ?max++:cnt-1;
									if( i>=max ) break;
								  }else{
								   max=2;
								  }
								}
							
							if( i>3 ) break;
							
							if( igb.equals("6") && i>0 ){
								break;
							}else if( igb.equals("4") && i>0 ){
								break;
							}
							
							AdConfigData ex_item= (AdConfigData) list.get( rnd.get(i) );
							
							if( igb.equals("1") && ex_item.getImgname()!=null && ex_item.getImgname().length()>0 ){
								img= ex_item.getImgname();
							}else if( igb.equals("2") && ex_item.getImgname2()!=null && ex_item.getImgname2().length()>0 ){
								img= ex_item.getImgname2();
							}else if( igb.equals("3") && ex_item.getImgname3()!=null && ex_item.getImgname3().length()>0 ){
								img= ex_item.getImgname3();
							}else if( igb.equals("4") && ex_item.getImgname4()!=null && ex_item.getImgname4().length()>0 ){
								img= ex_item.getImgname4();
							}else{
								img= ex_item.getImgname();
							}
							img = "http://"+domain+"/ad/imgfile/"+ img;
							
							oslink= "/servlet/drc?no=&kno="+ex_item.getKno()+"&s="+s+"&gb="+adgubun+"&sc="+ex_item.getSite_code()+"&mc="+r_no
									+"&mcgb="+scfg.getMcgb()+"&u="+ex_item.getUserid()
									+"&product="+ StringUtils.gAtData("nor,video,nor", "nor,video,flash", types, ",", "nor");
							
							String site_url = ex_item.getSite_url();
							if( site_url.indexOf("http")==-1 ) site_url= "http://"+ site_url;
							site_url = oslink +"&slink="+ URLEncoder.encode(site_url,"UTF-8");
							
							html_flash.append(",{\"t"+(i+1)+"\":\"\""
									+",\"pcode\":\"x\",\"pnm\":\""+ex_item.getSite_name()+"\",\"site_name\":\""+ ex_item.getSite_name() +"\",\"price\":\"\""
									+",\"img\":\""+ img +"\",\"purl\":\""+ site_url +"\",\"logo\":\""+ex_item.getImgname_logo()+"\"}");
							//html_flash_img.append(",{\"img\":\""+ img +"\",\"logo\":\""+ex_item.getImgname_logo()+"\"}");
							//html_flash_link.append(",{\"purl\":\""+ site_url +"\"}");
							

							AdChargeData crd=(AdChargeData)appContext.getBean("AdChargeData");
							crd.setSITE_CODE(ex_item.getSite_code());
							crd.setADGUBUN(adgubun);
							crd.setTYPE("V");
							crd.setYYYYMMDD(ymd);
							crd.setHHMM(kh);
							crd.setPC(pc);
							crd.setUSERID(ex_item.getUserid());
							crd.setPOINT("0");
							crd.setPPOINT("0");
							crd.setYM(ym);
							crd.setNO(ex_item.getNo());
							crd.setPCODE(scfg.getPcode());
							crd.setS(s);
							crd.setSCRIPTUSERID(ms.getUSERID());
							crd.setIP(keyIp);
							crd.setKNO(ex_item.getKno());
							crd.setSRView2(0);
							crd.setMCGB(scfg.getMcgb());
							crd.setViewcnt3(1);
							debug="a-9";
							if(s.length()>0 && ms.getUSERID()!=null && ms.getUSERID().length()>0){
								crd.setPRODUCT(StringUtils.gAtData("nor,video,nor", "nor,video,flash", types, ",", "nor"));
								if(chk.equals(""))dumpObj.getNormalChargeLogData().add(crd);
							}
							length++;
						}
					}
					
					htmlStr.append("{\"client\":[{\"target\":\""+adgubun+"\",\"length\":\""+length+"\",\"logo\":\"\",\"oslink\":\""+""+"\",\"data\":[");
					htmlStr.append(html_flash.toString());
					htmlStr.append(" ] } ] } ");

					//htmlStr1.append("{\"client\":[{\"target\":\"KL\",\"length\":\""+length+"\",\"logo\":\"\",\"data\":[");
					//htmlStr1.append(html_flash_img.toString());
					//htmlStr1.append(" ] } ] } ");
					
					//htmlStr2.append("{\"client\":[{\"target\":\"KL\",\"length\":\""+length+"\",\"logo\":\"\",\"oslink\":\""+""+"\",\"data\":[");
					//htmlStr2.append(html_flash_link.toString());
					//htmlStr2.append(" ] } ] } ");
					
				}else if( adgubun.equals("AD") ){

					String url = slink;
					String img = "http://"+domain+"/ad/efile/"+ r_adtxt;
					
					html_flash.append("{\"t0\":\"\",\"pcode\":\"x\",\"pnm\":\""+scfg.getSite_name()+"\",\"site_name\":\""+ scfg.getSite_name() +"\",\"price\":\"\""
							+",\"img\":\""+ img +"\",\"purl\":\""+ url +"\",\"logo\":\""+scfg.getImgname_logo()+"\"}");

					//html_flash_img.append("{\"img\":\""+ img +"\",\"logo\":\""+scfg.getImgname_logo()+"\"}");
					//html_flash_link.append("{\"purl\":\""+ url +"\"}");
					
					String baseadKey=us+"_"+ms.getAD_TYPE();
					
					String dispo=manageCookie.getCookieInfo(request,"dispo");
					ArrayList list= RFServlet.instance.adInfoCache.getNormalBaceAdList(baseadKey,addpq, scfg.getKno(),"",dispo);
					
					if( list!=null && list.size()>0 ){
						ArrayList<Integer> rnd= StringUtils.getRandList( list.size() );
						
						for(int i=0; i<list.size(); i++){
							
							if (Integer.parseInt(igb) > 20){	
								 if(cntad !=""||!cntad.equals("")){
								    int cnt = Integer.valueOf(cntad);
								   	max = cnt == 1 || cnt ==0 ?max++:cnt-1;
									if( i>=max ) break;
								  }else{
								   max=2;
								  }
								}
								
							
							if( i>3 ) break;
							if( igb.equals("6") && i>0 ){
								break;
							}else if( igb.equals("4") && i>0 ){
								break;
							}
							
							AdConfigData ex_item= (AdConfigData) list.get( rnd.get(i) );
							
							img = "http://"+domain+"/ad/efile/"+ ex_item.getAdtxt();
							
							oslink= "/servlet/drc?no=&kno="+ex_item.getKno()+"&s="+s+"&gb="+adgubun+"&sc="+ex_item.getSite_code()+"&mc="+r_no
									+"&mcgb="+scfg.getMcgb()+"&u="+ex_item.getUserid()
									+"&product="+ StringUtils.gAtData("nor,video,nor", "nor,video,flash", types, ",", "nor");
							
							String site_url = ex_item.getSite_url();
							if( site_url.indexOf("http")==-1 ) site_url= "http://"+ site_url;
							site_url = oslink +"&slink="+ URLEncoder.encode(site_url,"UTF-8");
							
							html_flash.append(",{\"t"+(i+1)+"\":\"\""
									+",\"pcode\":\"x\",\"pnm\":\""+ex_item.getSite_name()+"\",\"site_name\":\""+ ex_item.getSite_name() +"\",\"price\":\"\""
									+",\"img\":\""+ img +"\",\"purl\":\""+ site_url +"\",\"logo\":\""+ex_item.getImgname_logo()+"\"}");
							//html_flash_img.append(",{\"img\":\""+ img +"\",\"logo\":\""+ex_item.getImgname_logo()+"\"}");
							//html_flash_link.append(",{\"purl\":\""+ site_url +"\"}");
							
							AdChargeData crd=(AdChargeData)appContext.getBean("AdChargeData");
							crd.setSITE_CODE(ex_item.getSite_code());
							crd.setADGUBUN(adgubun);
							crd.setTYPE("V");
							crd.setYYYYMMDD(ymd);
							crd.setHHMM(kh);
							crd.setPC(pc);
							crd.setUSERID(ex_item.getUserid());
							crd.setPOINT("0");
							crd.setPPOINT("0");
							crd.setYM(ym);
							crd.setNO(ex_item.getNo());
							crd.setPCODE(scfg.getPcode());
							crd.setS(s);
							crd.setSCRIPTUSERID(ms.getUSERID());
							crd.setIP(keyIp);
							crd.setKNO(ex_item.getKno());
							crd.setSRView2(0);
							crd.setMCGB(scfg.getMcgb());
							crd.setViewcnt3(1);
							debug="a-9";
							if(s.length()>0 && ms.getUSERID()!=null && ms.getUSERID().length()>0){
								crd.setPRODUCT(StringUtils.gAtData("nor,video,nor", "nor,video,flash", types, ",", "nor"));
								if(chk.equals(""))dumpObj.getNormalChargeLogData().add(crd);
							}
							length++;
						}
					}
					
					htmlStr.append("{\"client\":[{\"target\":\""+adgubun+"\",\"length\":\""+length+"\",\"logo\":\"\",\"oslink\":\""+""+"\",\"data\":[");
					htmlStr.append(html_flash.toString());
					htmlStr.append(" ] } ] } ");

					//htmlStr1.append("{\"client\":[{\"target\":\"AD\",\"length\":\""+length+"\",\"logo\":\"\",\"data\":[");
					//htmlStr1.append(html_flash_img.toString());
					//htmlStr1.append(" ] } ] } ");

					//htmlStr2.append("{\"client\":[{\"target\":\"AD\",\"length\":\""+length+"\",\"logo\":\"\",\"oslink\":\""+""+"\",\"data\":[");
					//htmlStr2.append(html_flash_link.toString());
					//htmlStr2.append(" ] } ] } ");
				}
				logger.debug( htmlStr.toString() );
			}
			
			
			int point = 0;
			int ppoint=0;
			//manageCookie.makeCookie("dsvckgb",adgubun,3600*24*30, response);
			//manageCookie.makeCookie("dsvcksc",scfg.getSite_code(),3600*24*30, response);
			//manageCookie.makeCookie("dsvckmc",s,3600*24*30, response);
			//manageCookie.makeCookie("IPNewBN", "",0, response);
			debug="a-8-12";
			AdChargeData crd=(AdChargeData)appContext.getBean("AdChargeData");
			crd.setSITE_CODE(scfg.getSite_code());
			crd.setADGUBUN(adgubun);
			crd.setTYPE("V");
			crd.setYYYYMMDD(ymd);
			crd.setHHMM(kh);
			crd.setPC(pc);
			crd.setUSERID(scfg.getUserid());
			crd.setPOINT("0");
			crd.setPPOINT("0");
			crd.setYM(ym);
			crd.setNO(scfg.getNo());
			crd.setPCODE(scfg.getPcode());
			crd.setS(s);
			crd.setSCRIPTUSERID(ms.getUSERID());
			crd.setIP(keyIp);
			crd.setKNO(keylog_no);
			crd.setSRView2(viewcnt2);
			crd.setMCGB(scfg.getMcgb());
			debug="a-9";
			if(s.length()>0 && ms.getUSERID()!=null && ms.getUSERID().length()>0  ){
				crd.setPRODUCT(StringUtils.gAtData("nor,video,nor", "nor,video,flash", types, ",", "nor"));
				if(chk.equals(""))dumpObj.getNormalChargeLogData().add(crd);
			}
			logger.debug(crd.getInfo("adbn crd.info"));
			/*
			DayMediaLogData mld=(DayMediaLogData)appContext.getBean("DayMediaLogData");
			mld.setMC(s);
			mld.setMCODE(scfg.getUserid());
			mld.setSDATE(ymd);
			mld.setVIEWCNT(1);
			mld.setCLICKCNT(0);
			mld.setPOINT(0);
			mld.setGUBUN(adgubun);
			mld.setMEDIA_ID(ms.getUSERID());
			mld.setActGubun("V");
			logger.debug("mld.getMC()="+mld.getMC());
			logger.debug("mld.getMCODE()="+mld.getMCODE());
			logger.debug("mld.getSDATE()="+mld.getSDATE());
			logger.debug("mld.getVIEWCNT()="+mld.getVIEWCNT());
			logger.debug("mld.getCLICKCNT()="+mld.getCLICKCNT());
			logger.debug("mld.getPOINT()="+mld.getPOINT());
			logger.debug("mld.getGUBUN()="+mld.getGUBUN());
			logger.debug("mld.getMEDIA_ID()="+mld.getMEDIA_ID());
			logger.debug("getActGubun()="+mld.getActGubun());
			dumpObj.getNormalDayMediaLogData().add(mld);
			*/
			if( types.equals("floating") ){
				htmlStr.setLength(0);
				htmlStr.append("var wp_adbn_scfg"+rnd_no+"={}; \n");
				htmlStr.append("wp_adbn_scfg"+rnd_no+"._wp_rnd_no='"+rnd_no+"'; \n");
				htmlStr.append("wp_adbn_scfg"+rnd_no+"._wp_adgubun='"+adgubun+"'; \n");
				htmlStr.append("wp_adbn_scfg"+rnd_no+"._wp_igb='"+igb+"'; \n");
				htmlStr.append("wp_adbn_scfg"+rnd_no+"._wp_divtop='"+divtop+"'; \n");
				htmlStr.append("wp_adbn_scfg"+rnd_no+"._wp_wvalue='"+wvalue+"'; \n");
				htmlStr.append("wp_adbn_scfg"+rnd_no+"._wp_hvalue='"+hvalue+"'; \n");
				htmlStr.append("wp_adbn_scfg"+rnd_no+"._wp_slink='"+slink+"'; \n");
				htmlStr.append("wp_adbn_scfg"+rnd_no+"._wp_starget='"+starget+"'; \n");
				htmlStr.append("wp_adbn_scfg"+rnd_no+"._wp_shopname='"+shopname+"'; \n");
				htmlStr.append("wp_adbn_scfg"+rnd_no+"._wp_bprice='"+bprice+"'; \n");
				htmlStr.append("wp_adbn_scfg"+rnd_no+"._wp_imgstr='"+imgstr+"'; \n");
				
				if( igb.equals("2") || igb.equals("3") ){
					htmlStr.append("wp_adbn_scfg"+rnd_no+".sub=[]; \n");
					if( adgubun.equals("UM") || adgubun.equals("KL") || adgubun.equals("AD") ){
					    ShopData shop_log1= new ShopData(); 
					    shop_log1.setPNM(scfg.getSite_title());
					    shop_log1.setPURL(slink);
					    shop_log1.setPRICE(0+"");
					    shop_log1.setIMGPATH(imgstr);
						rList.add(shop_log1);
					}
					for( int i=0; i<rList.size(); i++ ){
						if( i>2 )break;
						
						ShopData data = rList.get(i);
						htmlStr.append("wp_adbn_scfg"+rnd_no+".sub["+i+"]={\"pnm\":\""+data.getPNM()
								+"\",\"purl\":\""+data.getPURL()+"\",\"price\":\""+data.getPRICE()+"\",\"img\":\""+data.getIMGPATH()+"\"}; \n");
					}
				}
			}else if( types.equals("video") ){
//				String sss= FileUtil.readFile("/home/dreamsearch/public_html/ad/adbn_video.html");
//				if( sss.equals("") ){
//					sss= FileUtil.readFile("D:/work/enliple/www/webapp/public_html/ad/adbn_video.html");
//				}
				String sss=(String) RFServlet.instance.xml.get("adbn_video");
				if(sss.equals(""))logger.error("adbn adbn_video none ");
				
				try{
					sss= sss.replace("{{wp_wvalue}}", Integer.parseInt(StringUtils.gAt1(iwh, 0, "_")) +"");
					sss= sss.replace("{{wp_hvalue}}", Integer.parseInt(StringUtils.gAt1(iwh, 1, "_")) +"");
					sss= sss.replace("{{wp_igb}}", igb);
					sss= sss.replace("{{wp_target}}", adgubun);
					sss= sss.replace("{{wp_json}}", htmlStr.toString() );
					//sss= sss.replace("{{wp_json_img}}", htmlStr1.toString() );
					//sss= sss.replace("{{wp_json_link}}", htmlStr2.toString() );
					
				}catch(Exception e){
					logger.error("adbn[1565 video out err"+ e);
				}
				htmlStr.setLength(0);
				htmlStr.append(sss);

			}else if( types.equals("flash") ){	// 250, |, -
//				String sss= FileUtil.readFile("/home/dreamsearch/public_html/ad/adbn_flash.html");
//				if( sss.equals("") ){
//					sss= FileUtil.readFile("D:/work/enliple/www/webapp/public_html/ad/adbn_flash.html");
//				}
				String sss=(String) RFServlet.instance.xml.get("adbn_flash");
				if(sss.equals(""))logger.error("adbn adbn_flash none ");
				
				//out.println("var wp_target='"+ adgubun +"'");
				//out.println("var wp_json='"+ htmlStr.toString() +"';" );
				try{
					sss= sss.replace("{{wp_wvalue}}", Integer.parseInt(StringUtils.gAt1(iwh, 0, "_")) +"");
					sss= sss.replace("{{wp_hvalue}}", Integer.parseInt(StringUtils.gAt1(iwh, 1, "_")) +"");
					sss= sss.replace("{{wp_igb}}", igb);
					sss= sss.replace("{{wp_target}}", adgubun);
					sss= sss.replace("{{wp_json}}", htmlStr.toString() );
					//sss= sss.replace("{{wp_json_img}}", htmlStr1.toString() );
					//sss= sss.replace("{{wp_json_link}}", htmlStr2.toString() );
					
				}catch(Exception e){
					logger.error("adbn[1586 flash out err"+ e);
				}
				htmlStr.setLength(0);
				htmlStr.append(sss);

			}else if( igb.equals("4") ){	// 250, 300, 
//				String sss= FileUtil.readFile("/home/dreamsearch/public_html/ad/adbn_igb4.html");
//				if( sss.equals("") ){
//					sss= FileUtil.readFile("D:/work/enliple/www/webapp/public_html/ad/adbn_igb4.html");
//				}
				String sss=(String) RFServlet.instance.xml.get("adbn_igb4");
				if(sss.equals(""))logger.error("adbn adbn_igb4 none ");
				
				try{
					sss= sss.replace("{{wp_wvalue}}", Integer.parseInt(StringUtils.gAt1(iwh, 0, "_")) +"");
					sss= sss.replace("{{wp_hvalue}}", Integer.parseInt(StringUtils.gAt1(iwh, 1, "_")) +"");
					sss= sss.replace("{{wp_igb}}", igb);
					sss= sss.replace("{{wp_target}}", adgubun);
					sss= sss.replace("{{wp_json}}", htmlStr.toString() );
					
				}catch(Exception e){
					logger.error("adbn[1648] err"+ e);
				}
				htmlStr.setLength(0);
				htmlStr.append(sss);
				
			}else if( igb.equals("7") ){	//edge
				htmlStr.setLength(0);
				try{
					
					String edge_img = scfg.getImgname6();
					if( adgubun.equals("AD") ){
						edge_img = scfg.getAdtxt();
					}
					htmlStr.append("var wp_adbn_scfg"+rnd_no+"={}; \n");
					htmlStr.append("wp_adbn_scfg"+rnd_no+"._wp_rnd_no='"+rnd_no+"'; \n");
					htmlStr.append("wp_adbn_scfg"+rnd_no+"._wp_adgubun='"+adgubun+"'; \n");
					htmlStr.append("wp_adbn_scfg"+rnd_no+"._wp_igb='"+igb+"'; \n");
					htmlStr.append("wp_adbn_scfg"+rnd_no+"._wp_wvalue='"+wvalue+"'; \n");
					htmlStr.append("wp_adbn_scfg"+rnd_no+"._wp_hvalue='"+hvalue+"'; \n");
					htmlStr.append("wp_adbn_scfg"+rnd_no+"._wp_slink='"+slink+"'; \n");
					htmlStr.append("wp_adbn_scfg"+rnd_no+"._wp_starget='"+starget+"'; \n");
					htmlStr.append("wp_adbn_scfg"+rnd_no+"._wp_shopname='"+shopname+"'; \n");
					htmlStr.append("wp_adbn_scfg"+rnd_no+"._wp_bprice='"+bprice+"'; \n");
					htmlStr.append("wp_adbn_scfg"+rnd_no+"._wp_imgstr='"+imgstr+"'; \n");
					htmlStr.append("wp_adbn_scfg"+rnd_no+"._wp_imgstr_edge='"+edge_img+"'; \n");
					
				}catch(Exception e){
					logger.error("adbn[1586 flash out err"+ e);
				}
				
			}else if( igb.equals("6") ){	// 300
//				String sss= FileUtil.readFile("/home/dreamsearch/public_html/ad/adbn_igb6.html");
//				if( sss.equals("") ){
//					sss= FileUtil.readFile("D:/work/enliple/www/webapp/public_html/ad/adbn_igb6.html");
//				}
				String sss=(String) RFServlet.instance.xml.get("adbn_igb6");
				try{
					sss= sss.replace("{{wp_wvalue}}", Integer.parseInt(StringUtils.gAt1(iwh, 0, "_")) +"");
					sss= sss.replace("{{wp_hvalue}}", Integer.parseInt(StringUtils.gAt1(iwh, 1, "_")) +"");
					sss= sss.replace("{{wp_igb}}", igb);
					sss= sss.replace("{{wp_target}}", adgubun);
					sss= sss.replace("{{wp_json}}", htmlStr.toString() );
					//sss= sss.replace("{{wp_json_img}}", htmlStr1.toString() );
					//sss= sss.replace("{{wp_json_link}}", htmlStr2.toString() );
					
				}catch(Exception e){
					logger.error("adbn[1565 video out err"+ e);
				}
				htmlStr.setLength(0);
				htmlStr.append(sss);
			
			}else if( Integer.parseInt(igb) > 20 ){	//신규 베너
				String sss= FileUtil.readFile("/home/dreamsearch/public_html/newAd/banner/adbn_banner.html");
				if( sss.equals("") ){
					sss= FileUtil.readFile("D:/work/enliple/www/webapp/public_html/newAd/banner/adbn_banner.html");
				}
				//String sss=(String) RFServlet.instance.xml.get("adbn_igb6");
				try{
					
					logger.debug("adbn [1954] igb="+igb);
					sss= sss.replace("{{wp_wvalue}}", Integer.parseInt(StringUtils.gAt1(iwh, 0, "_")) +"");
					sss= sss.replace("{{wp_hvalue}}", Integer.parseInt(StringUtils.gAt1(iwh, 1, "_")) +"");
					sss= sss.replace("{{wp_igb}}", igb);
					sss= sss.replace("{{wp_target}}", adgubun);
					sss= sss.replace("{{wp_json}}", htmlStr.toString() );
					//sss= sss.replace("{{wp_json_img}}", htmlStr1.toString() );
					//sss= sss.replace("{{wp_json_link}}", htmlStr2.toString() );
					
				}catch(Exception e){
					logger.error("adbn[1954 video out err"+ e);
				}
				htmlStr.setLength(0);
				htmlStr.append(sss);
				
			}else if( adgubun.equals("ST") && ( igb.equals("2") || igb.equals("3") ) ){
				String sss= FileUtil.readFile("/home/dreamsearch/public_html/ad/adbn_igb"+igb+".html");
				if( sss.equals("") ){
					sss= FileUtil.readFile("D:/work/enliple/www/webapp/public_html/ad/adbn_igb"+igb+".html");
				}
				try{
					sss= sss.replace("{{wp_wvalue}}", Integer.parseInt(StringUtils.gAt1(iwh, 0, "_")) +"");
					sss= sss.replace("{{wp_hvalue}}", Integer.parseInt(StringUtils.gAt1(iwh, 1, "_")) +"");
					sss= sss.replace("{{wp_igb}}", igb);
					sss= sss.replace("{{wp_target}}", adgubun);
					sss= sss.replace("{{wp_json}}", htmlStr.toString() );
					//sss= sss.replace("{{wp_json_img}}", htmlStr1.toString() );
					//sss= sss.replace("{{wp_json_link}}", htmlStr2.toString() );
					
				}catch(Exception e){
					logger.error("adbn[1565 video out err"+ e);
				}
				htmlStr.setLength(0);
				htmlStr.append(sss);
				
			}else{
				if( scfg.isAdnew() ){

					logger.debug("adbn[1782] ad "+scfg.toString());
					
					htmlStr.setLength(0);
					htmlStr.append( scfg.getAdnew_html() );
				}
			}
			out.print(htmlStr);
			manageCookie.makeCookie("site_code", scfg.getSite_code(), -1, response);
		} catch (Exception e) {
			System.out.println(e);
			logger.error("Adbn"+e+",debug="+debug); 
		}
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response){
		doGet(request,response);
	}
	public void init() throws ServletException {
		logger.info("AdBanner started");
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