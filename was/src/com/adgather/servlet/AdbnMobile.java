package com.adgather.servlet;


import java.io.PrintWriter;
import java.net.URLEncoder;
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
import org.springframework.context.ApplicationContext;

import com.adgather.beans.ManagementCookie;
import com.adgather.reportmodel.AdChargeData;
import com.adgather.reportmodel.AdConfigData;
import com.adgather.reportmodel.DumpObject;
import com.adgather.reportmodel.MediaScriptData;
import com.adgather.reportmodel.RFData;
import com.adgather.reportmodel.ShopData;
import com.adgather.util.StringUtils;

public class AdbnMobile extends HttpServlet {
	private DumpObject dumpObj;
	private ApplicationContext appContext;
	public ManagementCookie manageCookie;
	static Logger logger = Logger.getLogger(AdbnMobile.class);
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		String debug="";
		String imgdomain="www.dreamsearch.or.kr";
		AdConfigData scfg=null;
		AdConfigData scfgr=null;
		StringBuffer htmlStr=new StringBuffer();
		StringBuffer html_flash= new StringBuffer();
		try {
			request.setCharacterEncoding("8859_1");
			response.setContentType("text/html;charset=euc-kr");
			response.setHeader("Pragma", "no-cache");			
			response.setHeader("Cache-Control", "no-cache"); 
			//response.setHeader("P3P","CP='IDC DSP COR ADM DEVi TAIi PSA PSD IVAi IVDi CONi HIS OUR IND CNT'");
			response.setHeader("P3P","CP='CAO PSA CONi OTR OUR DEM ONL'");
			PrintWriter out=response.getWriter();
			String from=request.getParameter("from")==null ? "" : request.getParameter("from");
			String us=request.getParameter("us")==null ? "" : request.getParameter("us");
			String s=request.getParameter("s")==null ? "" : request.getParameter("s");
			String pc=request.getParameter("pc")==null ? "" : request.getParameter("pc");
			String igb=request.getParameter("igb")==null ? "1" : request.getParameter("igb");if(igb.equals("")) igb="1";
			String types=request.getParameter("types")==null ? "" : request.getParameter("types");if(types.equals(""))types="mbw";
			String rnd_no=request.getParameter("rnd_no")==null ? "" : request.getParameter("rnd_no");
			String cntad=request.getParameter("cntad")==null ? "" : request.getParameter("cntad");
			String bntype=request.getParameter("bntype")==null ? "" : request.getParameter("bntype");
			String adchk=request.getParameter("adchk")==null ? "" : request.getParameter("adchk");
			
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
			
			String keyIp=RFServlet.instance.manageCookie.makeKeyCookie(request, response);
			String chk_result=manageCookie.getCookieInfo(request,"chk_result");
			String chk_ck1=manageCookie.getCookieInfo(request,"chk_ck");
			String ic_um=manageCookie.getCookieInfo(request,"ic_um");
			String ic_ki=manageCookie.getCookieInfo(request,"ic_ki");
			String shop_log=manageCookie.getCookieInfo(request,"shop_log");
			
			String adgubun="";
			String scTxtSel="";
			String mcgb="";
			String scTxt=from;

			debug="a-3";
			
			String mediaSiteStatus=RFServlet.instance.adInfoCache.getMediaSiteStatus(us);
			if(mediaSiteStatus.equals("N")){
				return;
			}
			MediaScriptData ms=RFServlet.instance.adInfoCache.getMediaScriptData(s);
			if( ms==null ){
				logger.debug("adbnMobile[216] mediainfo null "+ s);
				return;
			}
			logger.debug(ms.getInfo("adbn ms="));
			
			if(from.equals("")){
				from=request.getHeader("Referer")==null ? "" : request.getHeader("Referer");
			}
			if(from.length()>200){
				from=from.substring(0,200);
			}
			
			if(scTxt.length()>100){
				scTxt=scTxt.substring(0,100);
			}
			logger.debug("adbnMobile[146] scTxt="+scTxt);
			logger.debug("adbnMobile[132] from="+ from);
			
			StringBuffer chk_ck= new StringBuffer();
			chk_ck.append(chk_ck1);
			
			HashMap<String,String> addpq=StringUtils.getConvertHashMap(chk_result, "#");
			
			shop_log=StringUtils.getURLDecodeStr(shop_log,"UTF-8");

			String newVal=manageCookie.getCookieInfo(request,"shop_log");
			newVal=java.net.URLDecoder.decode(newVal,"utf-8");
			
			debug="a-3-1";
			if( shop_log!=null && shop_log.length()>0 ) {
				HashMap<String,ShopData> shhm= RFServlet.instance.adInfoCache.getShopLogData(shop_log,keyIp,"normal");
				
				if( shhm!=null && shhm.size()>0 ){
					logger.debug("Adbn==>keyIp history "+ shhm.size() );
					List<String> it = new ArrayList<String>(shhm.keySet());
				    Collections.sort(it);
				    
				    for(String me:it){
				    	ShopData shop_log1= shhm.get(me);
				    	if( shop_log1.getTARGETGUBUN().indexOf("A")>-1 ) continue;
				    	if( shop_log1.getMCGB().equals("") || shop_log1.getMCGB().equals("A") ) {} else { continue; }
				    	
				    	AdConfigData tmp_scfg=RFServlet.instance.adInfoCache.getNormalShopAdConfig(shop_log1.getSITE_CODE(),us,s,"adlink");
				    	if( tmp_scfg!=null && tmp_scfg.getStateM().equals("Y") ) {
				    		logger.debug(tmp_scfg.getInfo("adbnMobile[254] tmp_scfg.info "));

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
										scfg.setBanner_path1(tmp_scfg.getBanner_path1());
										scfg.setSite_descm(tmp_scfg.getSite_descm());
										scfg.setSite_urlm(tmp_scfg.getSite_urlm());
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
									scfgr.setBanner_path1(tmp_scfg.getBanner_path1());
									scfgr.setSite_descm(tmp_scfg.getSite_descm());
									scfgr.setSite_urlm(tmp_scfg.getSite_urlm());
								}
							}
						}
					}
				    if( scfg==null && scfgr!=null ){
				    	scfg = scfgr;
				    }
				}else{
					manageCookie.makeCookie("shop_log","",0, response);
				}

				
				if( scfg!=null ){
					if( ms.getAccept_sr().equals("Y") ){
						adgubun = "SR";
						
						String chk_code="SR_"+ scfg.getSite_code()+"^"+scfg.getPcode();
						String resultvalue=manageCookie.makeCookieStrNew(response, request, chk_result, chk_ck, chk_code, 10,adchk);
						logger.debug("adbn sr igb=1 resultvalue="+resultvalue);
						
						
						if( resultvalue.equals("next") ){
							ShopData slog= new ShopData();
							slog.setIP( keyIp );
							slog.setGB(adgubun);
							slog.setNO(Long.parseLong( scfg.getNo() ));
							slog.setSvc_type("normal");
							slog.setSITE_CODE(scfg.getSite_code());
							slog.setPCODE(scfg.getPcode());
							dumpObj.getDelShopLogData().add(slog);
							//manageCookie.shopLogReSet(scfg.getUserid(),slog,request, response);
							newVal=manageCookie.shopLogManage(1, newVal,slog,"edit");
						}
						
						AdConfigData tmpFg=RFServlet.instance.adInfoCache.getRecomAdConfig(scfg.getUserid(),us,s,"adlink","banner");
						
						// 상품 추가노출
						if( tmpFg!=null && (resultvalue.indexOf(chk_code+"=10")>-1 || resultvalue.equals("next")) 
								&& scfg.getFlag().equals("1") && tmpFg.getStateM().equals("Y") ){
							
							if( scfg.getMcgb().equals("") || !scfg.getMcgb().equals("A") ){
								LinkedHashMap ldList= RFServlet.instance.adInfoCache.getShopStatsData(scfg.getUserid(),"");
								if(ldList!=null && ldList.size()>0){
									//ArrayList sdlist=new ArrayList(ldList.values());
									List<String> sdlist = new ArrayList<String>(ldList.keySet());
									ArrayList<Integer> rndList = StringUtils.getRandList( sdlist.size() );
									logger.debug("adbnMobile[306] sdlist.size"+ sdlist.size());
									ArrayList addsList=new ArrayList();
									
									for( int i=0; i<rndList.size() && i<2; i++){
										ShopData adds = new ShopData();
										ShopData ss =RFServlet.instance.adInfoCache.getShopPCodeData(scfg.getUserid(),sdlist.get(rndList.get(i)).toString());
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
											adds.setSITE_CODE(scfg.getSite_code());
											//adds.setPARTID(scfg.getPARTID());
											adds.setGB("02");
											adds.setRF("");
											adds.setRDATE(ymd);
											adds.setRTIME(hm);
											adds.setMCGB("A");
											
											dumpObj.getShopLogData().add(adds);
											//manageCookie.shopLogReSet(scfg.getUserid(),adds,request, response);
											addsList.add(adds);
											logger.debug(adds.getInfo("adbnMobile[325] adds.info "+ i));
										}
									}
									newVal=manageCookie.shopLogManage(newVal,addsList,"append");
								}
							}
						}
					}
				}
			}
			newVal=java.net.URLEncoder.encode(newVal,"utf-8");
			manageCookie.makeCookie("shop_log",newVal,60*24*60*60, response);
			
			logger.debug("adbnMobile[661] um");
			if( (adgubun==null || adgubun.equals("")) && ms.getAccept_um().equals("Y") ){
				ic_um=StringUtils.getURLDecodeStr(ic_um,"euc-kr");
				debug="a-8-4";
				if(ic_um.length()>0){
					
					logger.debug("일반광고 url 리타게팅.. :START "+ ic_um);
					StringTokenizer st=new StringTokenizer(ic_um,"|||",false);
					int tot_cnt= StringUtils.gAtCnt(ic_um, "|||");
					int id=-1;
					
					while(st.hasMoreElements()){
						id++;
						String ic_umSel=st.nextElement().toString();
						
						scfg=RFServlet.instance.adInfoCache.getNormalUrlMatchAdConfig(us,s,ic_umSel,addpq,chk_ck.toString(), igb, "UM", 10, "mobile");
						String str_tmp="";
						
						if(scfg!=null ){
							adgubun  = "UM";
							
							String chk_code= "BNUM_"+ ic_umSel; //scfg.getSite_code();
							str_tmp=manageCookie.makeCookieStrNew(response, request, chk_result, chk_ck, chk_code, 10,adchk);
							logger.debug("adbnMobile[712] str_tmp "+ str_tmp);
							
							if( str_tmp.equals("next") && tot_cnt==id ){
								manageCookie.clearChkCk(response, chk_result, chk_ck.toString(), ic_um, "banner","BNUM");
							}
							
							logger.debug("일반광고 url 리타게팅.. :OK");
							break;
						}
						logger.debug( "adbnMobile[361] tot_cnt="+ tot_cnt +" id="+ id +" chk_result="+ chk_result +" chk_ck="+ chk_ck.toString() );
						if( (scfg==null && tot_cnt==id) ){
							manageCookie.clearChkCk(response, chk_result, chk_ck.toString(), ic_um, "banner","BNUM");
						}
					}
				}
			}
			/*
			logger.debug("adbnMobile[704] st");
			if( adgubun.length()==0 && ms.getAccept_st().equals("Y") ){
				ic_um=StringUtils.getURLDecodeStr(ic_um,"euc-kr");
				logger.debug("투데이 추천 노출.."+ ic_um);
				
				if(ic_um.length()>0){
					StringTokenizer st=new StringTokenizer(ic_um,"|||");
					int tot_cnt= StringUtils.gAtCnt(ic_um, "|||");
					while(st.hasMoreElements()){
						String ic_umSel=st.nextElement().toString();
						
						scfg=RFServlet.instance.adInfoCache.getTodayMatchAdConfig(us,s,ic_umSel, addpq, chk_ck.toString(), "banner", "ST", 20);
						String str_tmp="";
						
						if(scfg!=null && scfg.getStatem()!=null && scfg.getStatem().equals("Y") ){
							logger.debug(scfg.getInfo("iadbnMobile[347] scfg.info  > "));
							
							adgubun  = "ST";
							
							String chk_code= adgubun+"_"+scfg.getKno();
							str_tmp = manageCookie.makeCookieStrNew(response, request, chk_result, chk_ck, chk_code, 10);
							logger.debug("adbnMobile[353] resultvalue="+ str_tmp);
							
							manageCookie.makeCookie("push", "1", 60*6, response);
							
							logger.debug("투데이추천 .. :OK");
							break;
						}
						
					}
				}
			}*/
			
			logger.debug("adbnMobile[691] kl");
			debug="a-8-5";
			if( (adgubun==null || adgubun.equals("")) && ms.getAccept_kl().equals("Y") ){
				ic_ki= StringUtils.getURLDecodeStr(ic_ki, "euc-kr");
				logger.debug("일반광고 과거 키워드 .... :START "+ ic_ki);
				
				if( ic_ki!=null && ic_ki.length()>0 ){
					StringTokenizer st = new StringTokenizer(ic_ki,"|||",false);
					while(st.hasMoreElements()){
						String t = st.nextElement().toString();
						scTxtSel=StringUtils.getURLDecodeStr(t, "euc-kr");;
						
						if( scTxtSel!=null ){
							logger.debug("adbnMobile[489] scTxtSel="+ scTxtSel);
							
							scfg = RFServlet.instance.adInfoCache.getNormalkeywordMatchAdConfig(us,s,scTxtSel, addpq, chk_ck.toString(), igb,"mobile");
							
							if( scfg!=null ){
								logger.debug( scfg.getInfo("adbnMobile[759 scfg.info ") );
								adgubun  = "KL";

								String chk_code = "BRKL_"+ java.net.URLEncoder.encode(scTxtSel,"euc-kr");
								String resultvalue= manageCookie.makeCookieStrNew(response, request, chk_result, chk_ck, chk_code, 10,adchk);
								logger.debug("adbn kl 2 resultvalue="+ resultvalue);
																	
								logger.debug("일반광고 과거 키워드 .... :OK");
								break;
							}
						}
					}
				}
			}
			

			logger.debug("adbnMobile[733] ad");
			debug="a-8-6";
			if( (adgubun==null || adgubun.equals("")) && ms.getAccept_ad().equals("Y") ){
				try{
					logger.debug("adgubun==null 이니 기본 광고 전송...");
					String baseadKey=us+"_" + (bntype.equals("3")||bntype.equals("4")||bntype.equals("5")?"i250_250":ms.getAD_TYPE());
					logger.debug("baseadKey="+baseadKey);
					
					String dispo=manageCookie.getCookieInfo(request,"dispo");
					logger.debug(dispo.toString());
					scfg=RFServlet.instance.adInfoCache.getNormalBaceAdConfig(baseadKey,addpq,"",dispo,"");
					if(scfg!=null && scfg!=null && scfg.getStateM().equals("Y") ){
						logger.debug("기본 광고 전송 :OK");
						adgubun="AD";
						scfg.setNo("0");
					}
				}catch(Exception e){
					logger.error(e+":basead loaad error");
					adgubun="";
				}
			}
			debug="a-8-7";
			
			logger.debug("adbnMobile[763] hb");
			if(adgubun==null || adgubun.equals("")){
				try{
					String h_banner=ms.getH_BANNER();

					if(h_banner==null || h_banner.equals("")){
						return;
					}else{
						adgubun="HB";
						
						htmlStr.append("var wp_adbn_scfg"+rnd_no+"={}; \n");
						htmlStr.append("wp_adbn_scfg"+rnd_no+"._wp_adgubun='h_banner'; \n");
						htmlStr.append("wp_adbn_scfg"+rnd_no+"._wp_url='"+h_banner+"'; \n");
						htmlStr.append("wp_adbn_scfg"+rnd_no+"._wp_htype='"+ms.getH_TYPE()+"'; \n");
						
						out.print(htmlStr.toString());
						
						return;
					}
				}catch(Exception e){
					logger.error(e+":ms.getH_BANNER() error");
					return;
				}
			}
			
			if(scfg==null || adgubun==null || adgubun.equals("") ){
				logger.error("adgubun is null or scfg is null");
				return;
			}
			
			
			
			
			logger.debug("adbnMobile[690] adgubun="+adgubun);
			logger.debug(scfg.getInfo("adbnMobile[839] scfg.info "));
			
			String r_site_code  = scfg.getSite_code();
			String r_site_url   = scfg.getSite_url();	
			String r_no         = s;
			String r_userid     = scfg.getUserid();
			String r_purl       = scfg.getPurl();
			String r_site_etc   = scfg.getSite_etcm();
			String r_imgpath    = scfg.getImgpath();
			String r_adtxt      = scfg.getAdtxt();
			String strimg       = scfg.getImgname();
			String keylog_no	= scfg.getKno();		if( keylog_no==null || keylog_no.equals("") )keylog_no="0";
			String shopname		= scfg.getPnm();		if( shopname==null )shopname="오늘만 특가!";
			String imgstr="";
			String shop_logno	= scfg.getNo();			if( shop_logno==null || shop_logno.equals("") )shop_logno="0";
			int viewcnt2		= 0;
			String rurl			= "";
			
			if( scfg.getMcgb()==null ) scfg.setMcgb("");
			
			debug="a-8-81";
			String slink  = "/servlet/drc?no="+shop_logno+"&kno="+keylog_no+"&s="+s+"&gb="+adgubun+"&sc="+r_site_code+"&mc="+r_no
							+"&mcgb="+scfg.getMcgb()+"&u="+r_userid+"&product="+ types;
			String oslink= slink;
			
			logger.debug("slink="+slink);
			
			debug="a-8-83";
			if(adgubun.equals("SR") || adgubun.equals("ST") ){
				imgstr = "http://"+imgdomain+"/ad/imgfile/"+strimg;
				if( r_site_etc!=null && r_site_etc.length()>0 ){
					r_purl += r_site_etc.substring(0, 1).equals("&")? r_site_etc: "&"+ r_site_etc ;
				}
				slink+="&pCode="+scfg.getPcode()+"&slink="+URLEncoder.encode(r_purl,"UTF-8");
			}else{
				shopname = scfg.getMedia_code();
			}
			
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
				dumpObj.getKeywordLogData().add(keywordLog);
				imgstr = "http://"+imgdomain+"/ad/imgfile/"+strimg;
				slink+="&slink="+URLEncoder.encode(rurl,"UTF-8");
			}
			if(adgubun.equals("AD")){
				r_site_url = (r_site_url.substring(0,4).equals("http")?r_site_url:"http://"+r_site_url);
				rurl	=URLEncoder.encode(r_site_url,"UTF-8");
				imgstr = "http://"+imgdomain+"/ad/efile/"+r_adtxt;
				slink +="&slink="+rurl;
			}
			if(adgubun.equals("UM")){
				r_site_url = (r_site_url.substring(0,4).equals("http")?r_site_url:"http://"+r_site_url);
				rurl	=URLEncoder.encode(r_site_url,"UTF-8");
				imgstr = "http://"+imgdomain+"/ad/imgfile/"+strimg;
				slink+="&slink="+rurl;
			}
			if(adgubun.equals("ST")){
				r_site_url = (r_site_url.substring(0,4).equals("http")?r_site_url:"http://"+r_site_url);
				rurl	=URLEncoder.encode(r_site_url,"UTF-8");
				slink+="&slink="+rurl;
			}
			
			int length=1;
			

			debug="a-8-84";
			int max=0;
			int cntad_tmp=0;
			try{
				cntad_tmp= Integer.parseInt(cntad);
			}catch(Exception e){
				cntad_tmp=1;
			}
			
			if( bntype.equals("1") ){
				if( adgubun.equals("SR") || adgubun.equals("SP") || adgubun.equals("ST") ){
					max=2;
				}else{
					max=1;
				}
			}else if( bntype.equals("2") ){
				max=cntad_tmp;
			}else if( bntype.equals("3") ){
				if( adgubun.equals("SR") || adgubun.equals("SP") || adgubun.equals("ST") ){
					max=2;
				}else{
					max=3;
				}
			}else if( bntype.equals("4") ){
				if( adgubun.equals("SR") || adgubun.equals("SP") || adgubun.equals("ST") ){
					max=cntad_tmp;
				}else{
					max=1;
				}
			}else if( bntype.equals("5") ){
				if( adgubun.equals("SR") || adgubun.equals("SP") || adgubun.equals("ST") ){
					max=2;
				}else{
					max=1;
				}
			}else{
				max=1;
			}
			max--;
			
			logger.debug("adbnM[519] max=" +max );
			
			debug="a-8-85";
			if( adgubun.equals("SR") || adgubun.equals("SP") || adgubun.equals("ST") ){

				String purl= scfg.getPurl();
				String site_etc="";
				if( scfg.getSite_etcm()!=null && scfg.getSite_etcm().length()>0 ){
					site_etc = (scfg.getSite_etcm().substring(0, 1).equals("&")? scfg.getSite_etcm(): "&"+ scfg.getSite_etcm());
					purl += site_etc;
				}
				r_purl = oslink +"&pCode="+scfg.getPcode()+"&slink="+URLEncoder.encode(r_purl,"UTF-8");
				String pnm=scfg.getPnm();
				
				String local_img= scfg.getImgpath();
				
				html_flash.append("{\"t0\":\"\",\"pcode\":\""+scfg.getPcode()
						+"\",\"pnm\":\""+pnm+"\",\"price\":\""+scfg.getPrice()+"\",\"img\":\""+ local_img
						+"\",\"purl\":\""+ r_purl +"\"}");
				
				LinkedHashMap ldList= RFServlet.instance.adInfoCache.getShopStatsData(scfg.getUserid(),scfg.getCate1());
				if(ldList!=null && ldList.size()>0){
					//ArrayList sdlist=new ArrayList(ldList.values());
					List<String> sdlist = new ArrayList<String>(ldList.keySet());
					ArrayList<Integer> rnd = StringUtils.getRandList( sdlist.size() );
					
					Iterator it = rnd.iterator();
					
					//for(int i=0; i<ldList.size(); i++){
					for( int i=0; it.hasNext();   ){
						if( i>=max ) break;
						
						String pcode=sdlist.get( (Integer)it.next() ).toString();
						ShopData item =RFServlet.instance.adInfoCache.getShopPCodeData(scfg.getUserid(), pcode);
						if(item!=null && !pcode.equals(scfg.getPcode())){
							i++;
							logger.debug(item.getInfo("adbnMobile[1055] ex_item.info "));

							local_img= item.getIMGPATH();
							
							String site_url= item.getPURL();
							if( site_url.indexOf("http")==-1 ){
								site_url = "http://"+ site_url;
							}
							
							if( site_url.length()>0 && r_site_etc.length()>0 ){
								site_url += r_site_etc.substring(0, 1).equals("&")? r_site_etc: "&"+ r_site_etc ;
							}
							site_url= oslink +"&pCode="+item.getPCODE()+"&slink="+ URLEncoder.encode(site_url,"UTF-8");
							
							pnm= item.getPNM();
							
							html_flash.append(",{\"t"+(i+1)+"\":\"\""
									+",\"pcode\":\""+item.getPCODE()+"\",\"pnm\":\""+pnm+"\""
									+",\"price\":\""+item.getPRICE()+"\""
									+",\"img\":\""+ local_img +"\",\"purl\":\""+ site_url +"\"} ");
							
							if(item.getPCODE()!=null && item.getPCODE().length()>0){
								RFServlet.instance.dumpDb.setShopStatsData(scfg.getUserid(),item.getPCODE(),item,"adview");
							}
							length++;
							viewcnt2++;
						}
					}
				}
				
				htmlStr.append("{\"client\":[{\"target\":\""+adgubun+"\",\"length\":\""+length
						+"\",\"bntype\":\""+bntype+"\",\"logo\":\""+ scfg.getImgname_logo() +"\",\"data\":[");
				htmlStr.append(html_flash.toString());
				htmlStr.append(" ] } ] } ");
				
				//viewcnt2=max;
				
			}else if ( adgubun.equals("UM") ){
				logger.debug("adbnMobile[1159] "+ adgubun);
				String img, site_url;
				img = site_url = "";
				
				if( bntype.equals("3") || bntype.equals("4") || bntype.equals("5") ){
					img = "http://"+imgdomain+"/ad/imgfile/"+ scfg.getImgname();
				}else{
					img = "http://"+imgdomain+"/ad/imgfile/"+ scfg.getBanner_path1();
				}
				
				if( scfg.getSite_urlm()!=null ){
					site_url = scfg.getSite_urlm();
				}else{
					site_url = scfg.getSite_url();
				}
				
				if( site_url.indexOf("http")==-1 ) site_url= "http://"+ site_url;
				
				site_url = oslink +"&slink="+ URLEncoder.encode(site_url,"UTF-8");
				
				html_flash.append("{\"t0\":\"\",\"desc\":\""+ scfg.getSite_descm()
						+"\",\"img\":\""+ img +"\",\"purl\":\""+ site_url +"\",\"logo\":\""+ scfg.getImgname_logo() +"\"}");
				
				ArrayList list = RFServlet.instance.adInfoCache.getNormalUrlMatchList(us,s,scfg.getMedia_code(),addpq,chk_ck.toString(), igb);
				logger.debug("adbnMobile[1168] list.size "+ list.size() );
				logger.debug(scfg.toString());
				
				if( list!=null && list.size()>0 ){
					ArrayList<Integer> rnd= StringUtils.getRandList( list.size() );
					
					Iterator it = rnd.iterator();
					
					while( it.hasNext() && max>0 ){
						AdConfigData ex_item= (AdConfigData) list.get( (Integer)it.next() );
						
						if( ex_item.getStateM().equals("Y") ){
							max--;
						}else{
							continue;
						}
							
						if( bntype.equals("3") || bntype.equals("4") || bntype.equals("5") ){
							img = "http://"+imgdomain+"/ad/imgfile/"+ ex_item.getImgname();
						}else{
							img = "http://"+imgdomain+"/ad/imgfile/"+ ex_item.getBanner_path1();
						}
						
						oslink= "/servlet/drc?no=0&kno="+ex_item.getKno()+"&s="+s+"&gb="+adgubun+"&sc="+ex_item.getSite_code()+"&mc="+r_no
								+"&mcgb="+scfg.getMcgb()+"&u="+ex_item.getUserid()
								+"&product="+ types;
						
						if( scfg.getSite_urlm()!=null ){
							site_url = scfg.getSite_urlm();
						}else{
							site_url = scfg.getSite_url();
						}
						
						if( site_url.indexOf("http")==-1 ) site_url= "http://"+ site_url;
						site_url = oslink +"&slink="+ URLEncoder.encode(site_url,"UTF-8");
						
						html_flash.append(",{\"t"+(max+1)+"\":\"\",\"desc\":\""+ex_item.getSite_descm()+"\""
								+",\"img\":\""+ img +"\",\"purl\":\""+ site_url +"\",\"logo\":\""+ ex_item.getImgname_logo() +"\"}");
						
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
							crd.setPRODUCT(types);
							dumpObj.getMobileChargeLogData().add(crd);
						}
						length++;
					}
				}
				
				htmlStr.append("{\"client\":[{\"target\":\""+adgubun+"\",\"length\":\""+length+"\",\"bntype\":\""+bntype+"\",\"data\":[");
				htmlStr.append(html_flash.toString());
				htmlStr.append(" ] } ] } ");
				
			}else if( adgubun.equals("KL") ){
				
				String img, site_url;
				img = site_url = "";
				
				if( bntype.equals("3") || bntype.equals("4") || bntype.equals("5") ){
					img = "http://"+imgdomain+"/ad/imgfile/"+ scfg.getImgname();
				}else{
					img = "http://"+imgdomain+"/ad/imgfile/"+ scfg.getBanner_path1();
				}
				
				if( scfg.getSite_urlm()!=null ){
					site_url = scfg.getSite_urlm();
				}else{
					site_url = scfg.getSite_url();
				}
				if( site_url.indexOf("http")==-1 ) site_url= "http://"+ site_url;
				site_url = oslink +"&slink="+ URLEncoder.encode(site_url,"UTF-8");
				
				html_flash.append("{\"t0\":\"\",\"desc\":\""+ scfg.getSite_descm()
						+"\",\"img\":\""+ img +"\",\"purl\":\""+ site_url +"\",\"logo\":\""+ scfg.getImgname_logo() +"\"}");

				ArrayList list= RFServlet.instance.adInfoCache.getNormalkeywordMatchList(us,s, scTxtSel, addpq, chk_ck.toString(), igb, keyIp);
				if( list!=null && list.size()>0 ){
					ArrayList<Integer> rnd= StringUtils.getRandList( list.size() );
					
					Iterator it = rnd.iterator();
					
					while( it.hasNext() && max>0 ){
						AdConfigData ex_item= (AdConfigData) list.get( (Integer)it.next() );
						
						if( ex_item.getStateM().equals("Y") ){
							max--;
						}else{
							continue;
						}

						if( bntype.equals("3") || bntype.equals("4") || bntype.equals("5") ){
							img = "http://"+imgdomain+"/ad/imgfile/"+ ex_item.getImgname();
						}else{
							img = "http://"+imgdomain+"/ad/imgfile/"+ ex_item.getBanner_path1();
						}
						
						oslink= "/servlet/drc?no=0&kno="+ex_item.getKno()+"&s="+s+"&gb="+adgubun+"&sc="+ex_item.getSite_code()+"&mc="+r_no
								+"&mcgb="+scfg.getMcgb()+"&u="+ex_item.getUserid()
								+"&product="+ types;
						
						if( scfg.getSite_urlm()!=null ){
							site_url = scfg.getSite_urlm();
						}else{
							site_url = scfg.getSite_url();
						}
						if( site_url.indexOf("http")==-1 ) site_url= "http://"+ site_url;
						site_url = oslink +"&slink="+ URLEncoder.encode(site_url,"UTF-8");

						html_flash.append(",{\"t"+(max+1)+"\":\"\",\"desc\":\""+ex_item.getSite_descm()+"\""
								+",\"img\":\""+ img +"\",\"purl\":\""+ site_url +"\",\"logo\":\""+ ex_item.getImgname_logo() +"\"}");
						
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
							crd.setPRODUCT(types);
							dumpObj.getMobileChargeLogData().add(crd);
						}
						length++;
					}
				}
				
				htmlStr.append("{\"client\":[{\"target\":\""+adgubun+"\",\"length\":\""+length+"\",\"bntype\":\""+bntype+"\",\"data\":[");
				htmlStr.append(html_flash.toString());
				htmlStr.append(" ] } ] } ");
				
			}else if( adgubun.equals("AD") ){

				String img, site_url;
				img = site_url = "";
				
				if( bntype.equals("3") || bntype.equals("4") || bntype.equals("5") ){
					img = "http://"+imgdomain+"/ad/efile/"+ scfg.getAdtxt();
				}else{
					img = "http://"+imgdomain+"/ad/efile/"+ scfg.getBanner_path1();
				}
				
				if( scfg.getSite_urlm()!=null ){
					site_url = scfg.getSite_urlm();
				}else{
					site_url = scfg.getSite_url();
				}
				if( site_url.indexOf("http")==-1 ) site_url= "http://"+ site_url;
				site_url = oslink +"&slink="+ URLEncoder.encode(site_url,"UTF-8");
				
				html_flash.append("{\"t0\":\"\",\"desc\":\""+ scfg.getSite_descm()
						+"\",\"img\":\""+ img +"\",\"purl\":\""+ site_url +"\",\"logo\":\""+ scfg.getImgname_logo() +"\"}");
				
				String baseadKey=us+"_"+(bntype.equals("3")||bntype.equals("4")||bntype.equals("5")?"i250_250":ms.getAD_TYPE());
				
				logger.debug("adbnmobile[831] baseadKey="+ baseadKey);
				
				String dispo=manageCookie.getCookieInfo(request,"dispo");
				ArrayList list= RFServlet.instance.adInfoCache.getNormalBaceAdList(baseadKey,addpq, scfg.getKno(),"",dispo);
				
				if( list!=null && list.size()>0 ){
					ArrayList<Integer> rnd= StringUtils.getRandList( list.size() );
					
					logger.debug("adbnmobile[839] list.size "+ list.size() );
					
					Iterator it = rnd.iterator();
					
					while( it.hasNext() && max>0 ){
						AdConfigData ex_item= (AdConfigData) list.get( (Integer)it.next() );
						logger.debug("adbnmobile[843] ex_item="+ ex_item.toString() );
						
						if( ex_item.getStateM().equals("Y") ){
							max--;
						}else{
							continue;
						}

						if( bntype.equals("3") || bntype.equals("4") || bntype.equals("5") ){
							img = "http://"+imgdomain+"/ad/efile/"+ ex_item.getAdtxt();
						}else{
							img = "http://"+imgdomain+"/ad/efile/"+ ex_item.getBanner_path1();
						}
						
						oslink= "/servlet/drc?no=0&kno="+ex_item.getKno()+"&s="+s+"&gb="+adgubun+"&sc="+ex_item.getSite_code()+"&mc="+r_no
								+"&mcgb="+scfg.getMcgb()+"&u="+ex_item.getUserid()
								+"&product="+ types;
						
						if( scfg.getSite_urlm()!=null ){
							site_url = scfg.getSite_urlm();
						}else{
							site_url = scfg.getSite_url();
						}
						if( site_url.indexOf("http")==-1 ) site_url= "http://"+ site_url;
						site_url = oslink +"&slink="+ URLEncoder.encode(site_url,"UTF-8");

						html_flash.append(",{\"t"+(max+1)+"\":\"\",\"desc\":\""+ex_item.getSite_descm()+"\""
								+",\"img\":\""+ img +"\",\"purl\":\""+ site_url +"\",\"logo\":\""+ ex_item.getImgname_logo() +"\"}");
						
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
							crd.setPRODUCT(types);
							dumpObj.getMobileChargeLogData().add(crd);
						}
						length++;
					}
				}
				
				htmlStr.append("{\"client\":[{\"target\":\""+adgubun+"\",\"length\":\""+length+"\",\"bntype\":\""+bntype+"\",\"data\":[");
				htmlStr.append(html_flash.toString());
				htmlStr.append(" ] } ] } ");
				
			}
			logger.debug( htmlStr.toString() );
			
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
			if(s.length()>0 && ms.getUSERID()!=null && ms.getUSERID().length()>0){
				crd.setPRODUCT(types);
				dumpObj.getMobileChargeLogData().add(crd);
			}
			
			if( types.equals("mbw") ){
				try{
					out.print("var wp_adbn_scfg"+rnd_no+"={}; \n");
					out.print("wp_adbn_scfg"+rnd_no+"._wp_rnd_no='"+rnd_no+"'; \n");
					out.print("wp_adbn_scfg"+rnd_no+"._wp_adgubun='"+adgubun+"'; \n");
					//out.print("wp_adbn_scfg"+rnd_no+"._wp_slink='"+slink+"'; \n");
					//out.print("wp_adbn_scfg"+rnd_no+"._wp_shopname='"+shopname+"'; \n");
					//out.print("wp_adbn_scfg"+rnd_no+"._wp_imgstr='"+imgstr+"'; \n");
					out.print("wp_adbn_scfg"+rnd_no+"._sub = "+ htmlStr.toString() +"; \n" );
				}catch(Exception e){
					logger.error("adbnMobile[1565] video out err"+ e);
				}
				htmlStr.setLength(0);
			}else{
				
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
		logger.info("AdbnServlet started");
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