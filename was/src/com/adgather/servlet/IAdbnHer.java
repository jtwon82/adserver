package com.adgather.servlet;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import com.adgather.beans.ManagementCookie;
import com.adgather.reportmodel.AdConfigData;
import com.adgather.reportmodel.DumpObject;
import com.adgather.reportmodel.MediaLogData;
import com.adgather.reportmodel.MediaScriptData;
import com.adgather.reportmodel.RFData;
import com.adgather.reportmodel.ShopData;
import com.adgather.util.MassInformation;
import com.adgather.util.StringUtils;

public class IAdbnHer extends HttpServlet {
	private DumpObject dumpObj;
	private ApplicationContext appContext;
	public ManagementCookie manageCookie;
	static Logger logger = Logger.getLogger(IAdbnHer.class);
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		String debug="";
		try {
			request.setCharacterEncoding("8859_1");
			response.setContentType("text/html");
			response.setHeader("Pragma", "no-cache");			
			response.setHeader("Cache-Control", "no-cache"); 
			//response.setHeader("P3P","CP='IDC DSP COR ADM DEVi TAIi PSA PSD IVAi IVDi CONi HIS OUR IND CNT'");
			response.setHeader("P3P","CP='CAO PSA CONi OTR OUR DEM ONL'");
			debug="1";			
			String from=request.getParameter("from")==null ? "" : request.getParameter("from");
			String referer=request.getHeader("Referer")==null ? "" : request.getHeader("Referer");
			
			if(from.equals("")){
				from=referer;
			}
			if(from.length()>200){
				from=from.substring(0,200);
			}
			
			String curl= StringUtils.getDestDomain(from);
			logger.debug("iadbn from="+ from +" curl="+ curl);
			
			debug="2";
			/*
			String scTxt=from;
			*/
			String us=request.getParameter("us")==null ? "" : request.getParameter("us");
			String s=request.getParameter("s")==null ? "" : request.getParameter("s");
			String send_chk=manageCookie.getCookieInfo(request,"send_chk");
			String send_chk2=manageCookie.getCookieInfo(request,"send_chk2");
			String chk_result=manageCookie.getCookieInfo(request,"chk_result");
			String chk_ck1=manageCookie.getCookieInfo(request,"chk_ck");
			StringBuffer chk_ck= new StringBuffer();
			chk_ck.append(chk_ck1);
			
			String ic_ki_real= manageCookie.getCookieInfo(request,"ic_ki_real");
			
			HashMap<String,String> addpq=new HashMap<String,String>();
			if(chk_result!=null && chk_result.length()>0){
				StringTokenizer st=new StringTokenizer(chk_result,"#",false);
				while(st.hasMoreElements()){
					String stCode=st.nextElement().toString();
					addpq.put(stCode, stCode);
				}
			}
			
			String mediaSiteStatus=RFServlet.instance.adInfoCache.getMediaSiteStatus(us);
			if(mediaSiteStatus.equals("N")){
				logger.debug("iadbn[79 us is null ");
				return;
			}
			PrintWriter out=response.getWriter();
			SimpleDateFormat yyyymmdd = new SimpleDateFormat("yyyyMMdd");
			SimpleDateFormat hhmi = new SimpleDateFormat("kkmm");
			java.util.Date date=new java.util.Date();
			String ymd=yyyymmdd.format(date);
			String hm=hhmi.format(date);
			String keyIp=RFServlet.instance.manageCookie.makeKeyCookie(request, response);
			manageCookie.makeCookie("IPNew", "",0, response);
			debug="3";
			String shopLog=manageCookie.getCookieInfo(request,"shop_log");
			shopLog=StringUtils.getURLDecodeStr(shopLog,"UTF-8");
			String ic_ki=manageCookie.getCookieInfo(request,"ic_ki");
			String ic_um=manageCookie.getCookieInfo(request,"ic_um");
			String i_cover=manageCookie.getCookieInfo(request,"i_cover");
			String ic_mcgb=manageCookie.getCookieInfo(request,"ic_mcgb");
			String ic_um_tgt= manageCookie.getCookieInfo(request,"ic_um_tgt");
			String ic_ki_tgt= manageCookie.getCookieInfo(request,"ic_ki_tgt");

			String scTxt="";
			if( from!=null && from.length()>0 ){
				try{
					MassInformation mf = new MassInformation(RFServlet.instance.domainTable,from);
					//if( ic_ki_real.equals("") ){
						scTxt=mf.GetDecodingKeyword();
						if( !scTxt.equals("") ){
							manageCookie.makeCookie("ic_ki_real", java.net.URLEncoder.encode(scTxt,"euc-kr"), -1, response);
						}
					//}else{
					//	scTxt=ic_ki_real;
					//}
					
				}catch(Exception e){
					logger.error("IAdbn-MassInformation:"+e+" : "+ from);
				}
			}
			debug="a-1";
			
			if(scTxt.length()>100){
				scTxt=scTxt.substring(0,100);
			}
			
			MediaLogData mLog1=(MediaLogData)appContext.getBean("MediaLogData");
			mLog1.setS(s);
			mLog1.setIP(keyIp);
			mLog1.setSDATE(ymd);
			mLog1.setVIEWCNT(1);
			mLog1.setLogId(1);
			debug="4";
			logger.debug("1 getS="+mLog1.getS());
			logger.debug("getIP="+mLog1.getIP());
			logger.debug("getSDATE="+mLog1.getSDATE());
			logger.debug("getVIEWCNT="+mLog1.getVIEWCNT());
			
			dumpObj.getMediaLogData().add(mLog1);
			
			////////////////////////////////////
			//  프리퀀시 확인(send_chk = 1) 일경우 송출 안함
			/////////////////////////////////  
			//  5분 지연 프리퀀시
			//  우선 오마이뉴스만 적용
			//  긴급 프리퀀시 10분
			if(send_chk.equals("1")) {
				String some= getScriptKhan(s);
				out.print( some);
				logger.debug("iadbn[146 send_chk limit ");
				return;
			}
			if(s.equals("399")){
			    if(send_chk2.equals("1")){}
			    	return;
			  }
			//  스포츠서울 지연처리
			if(s.equals("398")){
				if(send_chk.equals("1")){
					return;
				}
			}
			/*if( s.equals("613") || s.equals("835") ){
				String agent = ( request.getHeader("User-Agent") );
				
				if( agent.indexOf("Chrome")>-1 ){
					//return;
				}
			}*/
			
			debug="5";
			String mcgb="";
			//네이버(397),리뷰스타(411)인경우 광고 송출 안함
			if(ic_mcgb.equals("NV")){
				if(s.equals("397") || s.equals("411")){
					return;
				}
			}
			if(mcgb.equals("NV") && s.equals("411")) return;
			
			if(i_cover.equals("POP")){
				manageCookie.makeCookie("i_cover", "POP", 3600*24*7, response);
			}
			debug="6";	
			MediaLogData mLog2=(MediaLogData)appContext.getBean("MediaLogData");
			mLog2.setS(s);
			mLog2.setIP(keyIp);
			mLog2.setSDATE(ymd);
			mLog2.setVIEWCNT(1);
			mLog2.setLogId(2);
				
			logger.debug("2 getS="+mLog2.getS());
			logger.debug("getIP="+mLog2.getIP());
			logger.debug("getSDATE="+mLog2.getSDATE());
			logger.debug("getVIEWCNT="+mLog2.getVIEWCNT());
			//dumpObj.getMediaLogData().add(mLog2);
			
			String adgubun="";
			String site_code="";
			String site_url="";
			String gb="";
			AdConfigData scfg=null;
			AdConfigData scfgr=null;
			
			debug="7";
			// gb^^^site_code^^^url||gb^^^site_code^^^url
			String IPNew=manageCookie.getCookieInfo(request,"IPNew");
			MediaScriptData ms=RFServlet.instance.adInfoCache.getMediaScriptData(s);
			if( ms==null ){
				logger.debug("iadbn[236] media none "+s);
				return;
			}
			logger.debug(ms.getInfo("iadbn ms="));
			
			if( !curl.equals("") ){
				if( ms.getLimit_domains()!=null && ms.getLimit_domains().indexOf(curl)>-1 ){
					logger.debug("iadbn[179] icover referer limit ");
					return;
				}
			}
			
			/*if( ms.getUSERID().equals("asiatoday") ){
				String user_agent = request.getHeader("User-Agent").toLowerCase();
				if( user_agent.indexOf("chrome")>-1 ){
					logger.debug("iadbn chrome");
					//return;
				}
			}*/
			
			manageCookie.makeCookie("IPNew", "",0, response);
			if(IPNew==null || IPNew.equals("")){	// 키가 바로 생긴 것이 아니니  뭔가 있는지 뒤져본다..	  
				logger.debug("키가 바로 생긴 것이 아니니  뭔가 있는지 뒤져본다 :OK");
				String gb01="";String site_code01="";String url01="";
				String gb02="";String site_code02="";String url02="";
				String gb03="";String site_code03="";String url03="";
				long no201=0;long no202=0;long no203=0;
				
				logger.debug("iadbn shopLog="+ shopLog);
				
				if( shopLog!=null && shopLog.length()>0 && adgubun.equals("") ){
					if(URLEncoder.encode(shopLog,"UTF-8").length()>=1){	// 장바구니 없고,쿠키 잘린 상황이므로 db조회...
						logger.debug("쿠키 잘린 상황이므로 db조회...");
						try{
							HashMap<String,ShopData> shhm=RFServlet.instance.adInfoCache.getShopLogData(shopLog,keyIp,"icover");
							logger.debug("iadbn[202] shhm "+ shhm );
							
							if(shhm==null || shhm.size()==0){
								manageCookie.makeCookie("shop_log", "", 0, response);
								gb01="";
								gb02="";
								gb03="";
							}else{
								List<String> it = new ArrayList<String>(shhm.keySet());
							    Collections.sort(it);
								
							    for(String me:it){
							    	ShopData shop_log1= shhm.get(me);
							    	if( shop_log1.getTARGETGUBUN().indexOf("B")>-1 ) continue;
							    	if( shop_log1.getMCGB().equals("") || shop_log1.getMCGB().equals("B") ) {} else { continue; }

							    	String gubun_tmp=shop_log1.getMCGB().equals("")?"SR":"SP";
							    	AdConfigData tmp_scfg= RFServlet.instance.adInfoCache.getShopAdConfig(us,s,shop_log1.getSITE_CODE() +"_",gubun_tmp);
							    	if( tmp_scfg!=null){

							    		AdConfigData scfg_chkrecom=RFServlet.instance.adInfoCache.getRecomAdConfig(tmp_scfg.getUserid(),us,s,"iadlink","");

								    	if( !shop_log1.getMCGB().equals("") && scfg_chkrecom==null ){
								    		continue;
								    	}else if ( !shop_log1.getMCGB().equals("") && scfg_chkrecom!=null ){
								    		if( !scfg_chkrecom.getSite_code().equals( shop_log1.getSITE_CODE() ) ){
								    			continue;
								    		}
								    	}
								    	
								    	ShopData log_item= RFServlet.instance.adInfoCache.getShopPCodeData(tmp_scfg.getUserid(),shop_log1.getPCODE());
								    	
								    	if( log_item==null ){
								    		log_item= new ShopData();
								    		log_item.setCATE1("");
								    		log_item.setPNM("");
								    		log_item.setIMGPATH("");
								    		log_item.setPRICE("0");
								    	}

								    	if( shop_log1.getMCGB().equals("") ){
									    	if( scfg==null ){
												scfg= tmp_scfg.clone();
												scfg.setNo(shop_log1.getNO()+"");
												scfg.setPcode(shop_log1.getPCODE());
												scfg.setGb(shop_log1.getGB());
												scfg.setSite_code(shop_log1.getSITE_CODE());
												scfg.setPurl(shop_log1.getPURL());
												scfg.setSite_url(shop_log1.getURL());
												scfg.setMcgb(shop_log1.getMCGB());
												scfg.setFlag(shop_log1.getFlag());
												scfg.setCate1(log_item.getCATE1());
												scfg.setPnm(log_item.getPNM());
												scfg.setPrice(log_item.getPRICE());
												scfg.setImgpath(log_item.getIMGPATH());
											}
										}else if( shop_log1.getMCGB().equals("B") ){
											scfgr= tmp_scfg.clone();
											scfgr.setNo(shop_log1.getNO()+"");
											scfgr.setPcode(shop_log1.getPCODE());
											scfgr.setGb(shop_log1.getGB());
											scfgr.setSite_code(shop_log1.getSITE_CODE());
											scfgr.setPurl(shop_log1.getPURL());
											scfgr.setSite_url(shop_log1.getURL());
											scfgr.setMcgb(shop_log1.getMCGB());
											scfgr.setFlag(shop_log1.getFlag());
											scfgr.setCate1(log_item.getCATE1());
											scfgr.setPnm(log_item.getPNM());
											scfgr.setPrice(log_item.getPRICE());
											scfgr.setImgpath(log_item.getIMGPATH());
										}
								    	
							    	}
								}

							    if( scfg==null && scfgr!=null ){
							    	scfg = scfgr.clone();
							    }

								logger.debug("장바구니 없고,쿠키 잘린 상황이므로 db조회... :OK - gb01="+gb01+",gb02="+gb02+",gb03="+gb03);
							}
						}catch(Exception e){
							logger.error("장바구니 없고,쿠키 잘린 상황이므로 db조회 error:"+e);
						}
					}
					debug="8";
					if( scfg!=null && ms.getAccept_sr().equals("Y") ){	// 광고 설정이 온라인이면...
						logger.debug("광고 설정이 온라인이면...");
						adgubun  = "SR";
						
						if( !scfg.getMcgb().equals("") ){
							adgubun="SP";
						}
						
						logger.debug(scfg.getInfo("iadbn[306] scfg.info "));
						debug="8-a";
						manageCookie.makeCookie("push", "1", 60*6, response);
					}
					debug="9";
				} // 로그 있을시 끝
				
				if( adgubun.length()==0 && ms.getAccept_um().equals("Y") ){	// url 리타게팅..
					ic_um=StringUtils.getURLDecodeStr(ic_um,"euc-kr");
					logger.debug("url 리타게팅.."+ ic_um);
					
					if(ic_um.length()>0){
						StringTokenizer st=new StringTokenizer(ic_um,"|||");
						int tot_cnt= StringUtils.gAtCnt(ic_um, "|||");
						int id=-1;
						while(st.hasMoreElements()){
							id++;
							String ic_umSel=st.nextElement().toString();
							
							scfg=RFServlet.instance.adInfoCache.getUrlMatchAdConfig(us,s,ic_umSel, addpq, chk_ck.toString(),"", "UM", "ICUM");
							String str_tmp="";
							
							if(scfg!=null){
								logger.debug(scfg.getInfo("iadbn[319] scfg.info  > "));
								
								adgubun  = "UM";
								
								String chk_code= "ICUM_"+ic_umSel;
								str_tmp = manageCookie.makeCookieStrNew(response, request, chk_result, chk_ck, chk_code, 1,"");
								logger.debug("iadbn[328] resultvalue="+ str_tmp);
								
								if( str_tmp.equals("next") && tot_cnt==id ){
									manageCookie.clearChkCk(response, chk_result, chk_ck.toString(), ic_um, "","ICUM");
								}
								
								manageCookie.makeCookie("push", "1", 60*6, response);
								
								logger.debug("url 리타게팅.. :OK");
								break;
							}
							logger.debug( "iadbn[361] tot_cnt="+ tot_cnt +" id="+ id );
							if( (scfg==null && tot_cnt==id) ){
								manageCookie.clearChkCk(response, chk_result, chk_ck.toString(), ic_um, "","ICUM");
							}
						}
					}
				}

				/*
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
							
							scfg=RFServlet.instance.adInfoCache.getTodayMatchAdConfig(us,s,ic_umSel, addpq, chk_ck.toString(), "", "ST", 2);
							String str_tmp="";
							
							if(scfg!=null){
								logger.debug(scfg.getInfo("iadbn[347] scfg.info  > "));
								
								adgubun  = "ST";
								
								String chk_code= adgubun+"_"+scfg.getKno();
								str_tmp = manageCookie.makeCookieStrNew(response, request, chk_result, chk_ck, chk_code, 2);
								logger.debug("iadbn[353] resultvalue="+ str_tmp);
								
								manageCookie.makeCookie("push", "1", 60*6, response);
								
								logger.debug("투데이추천 .. :OK");
								break;
							}
							
						}
					}
				}
				
				debug="9-1";
				if( !scTxt.equals("") && ms.getAccept_kl().equals("Y") ){	// 키워드 리타게팅..
					scTxt=StringUtils.getURLDecodeStr(scTxt,"euc-kr");
					logger.debug("실시간 키워드 리타게팅....==> "+scTxt);
					if(scTxt.length()>0){
						scfg=RFServlet.instance.adInfoCache.getIkeywordMatchAdConfig(us,s,scTxt, chk_result, chk_ck,"");
						if(scfg!=null){
							adgubun = "KL";
							scfg.setMcgb("B");
							logger.debug("키워드 리타게팅.... :OK");
						}
					}
				}
				*/
				
				debug="10";
				if( adgubun.length()==0 && ms.getAccept_kl().equals("Y") ){	// 키워드 리타게팅..
					scTxt=StringUtils.getURLDecodeStr(ic_ki,"euc-kr");
					logger.debug("키워드 리타게팅....==>"+ scTxt);
					if(scTxt.length()>0){
						StringTokenizer st=new StringTokenizer(scTxt,"|||",false);
						int id=-1;
						while(st.hasMoreElements()){
							id++;
							String scTxtSel=st.nextElement().toString();
							String sel_ic_ki_tgt= StringUtils.gAt1(ic_ki_tgt, id, "|||");
							if( sel_ic_ki_tgt.indexOf("B") >=0 ){
								//continue;
							}
							scfg=RFServlet.instance.adInfoCache.getIkeywordMatchAdConfig(us,s,scTxtSel, chk_result, chk_ck.toString(),"", "ICKL");
							if(scfg!=null){
								sel_ic_ki_tgt+= "B";
								ic_ki_tgt= StringUtils.gAtSet(ic_ki_tgt, id, sel_ic_ki_tgt, "|||");
								
								adgubun  = "KL";
								
								String chk_code= "ICKL_"+java.net.URLEncoder.encode(scTxtSel,"euc-kr");
								String resultvalue = manageCookie.makeCookieStrNew(response, request, chk_result, chk_ck, chk_code, 1,"");
								logger.debug("iadbn[328] resultvalue="+ resultvalue);
														    	
								if( resultvalue.equals("next") ){
									//manageCookie.makeCookie("ic_ki_tgt", ic_ki_tgt, 60*24*60*60, response);
									manageCookie.makeCookie("push", "1", 60*6, response);
									RFData keywordLog=(RFData)appContext.getBean("RFData");
									keywordLog.setRDATE(ymd);
									keywordLog.setRTIME(hm);
									//keywordLog.setMCGB(mcgb);
									keywordLog.setSC_TXT(scTxtSel);
									keywordLog.setRURL(from);
									keywordLog.setDPOINT(0);
									keywordLog.setIP(keyIp);
									keywordLog.setMC(s);
									keywordLog.setK_GUBUN("i");
									dumpObj.getKeywordLogData().add(keywordLog);
									logger.debug("키워드 리타게팅.... :OK");
									break;
								}
							}
						}
					}
				}
				debug="11";
			}
			
			logger.debug("iadbn base us_s="+ us+"_"+s);
			if( adgubun.length()==0 && ms.getAccept_ad().equals("Y") ){	// 베이스 광고 ..
				logger.debug("베이스 광고 ..");
				
				String dispo=manageCookie.getCookieInfo(request,"dispo");
				scfg=RFServlet.instance.adInfoCache.getBaseAdConfig( us+"_"+s, "", dispo,"");
				if(scfg!=null){
					adgubun  = "AD";
					scfg.setNo("0");
					//manageCookie.makeCookie("ic_ki", "", 0, response);
					manageCookie.makeCookie("push", "1", 60*6, response);
					logger.debug("베이스 광고 ..OK");
				}
			}
			

			if( !adgubun.equals("SR") && ( ms.getUSERID().equals("sndKorea") || s.equals("781") ) ){
				logger.debug("상품만 노출되게처리");
				adgubun = "";
			}
			
			if(adgubun.length()==0 || scfg==null){
				String hbanner = ms.getH_BANNER();
				logger.debug("iadbn hbanner="+ hbanner);
				StringBuffer html = new StringBuffer();
				
				html.append(" <html>                                                                                                                                           \n");
				html.append(" <head>                                                                                                                                           \n");
				html.append(" <meta http-equiv=\"content-type\" content=\"text/html; charset=euc-kr\">                                                                         \n");
				html.append(" <script type=\"text/javascript\" src=\"/js/jquery-1.6.2.min.js\"></script>                                                                     \n");
				html.append(" <script type=\"text/javascript\" src=\"/js/jquery-ui-1.8.10.custom.min.js\"></script>                                                          \n");
				html.append(" </head>                                                                                                                                        \n");
				html.append(" <body>                                                                                                                                        \n");
				if( ms.getH_TYPE().equals("script") ){
					html.append(" <script type=\"text/javascript\" src=\""+ms.getH_BANNER()+"\"></script>                                                                                                                                        \n");
				}else{
					html.append(" <script language = \"javascript\">                                                                                                               \n");
					html.append(" 	var openURL = '"+ hbanner +"'; \n");
					html.append(" 			var newWin = window.open(openURL,\"new_win\",\"width=1024,height=768,scrollbars=yes,menubar=yes,location=yes,toolbar=yes,resizable=yes\"); \n");
					html.append(" 			if(newWin != null) {                                                                                                                   \n");
					html.append(" 			  //  송출 이후처리                                                                                                                    \n");
					html.append("         		//setCharge(site_code,adgubun,gb,userid,no,kno,s,shopurl);			                                                                               \n");
					html.append(" 			}else{                                                                                                                                 \n");
					html.append(" 			  //  팝업 차단일경우 카운터를 하자                                                                                                    \n");
					html.append("         		//setNoCharge(site_code,adgubun,gb,userid,no,kno,s);			  			                                                               \n");
					html.append(" 			}                                                                                                                                      \n");
					html.append(" 			newWin.blur();                                                                                                                         \n");
					html.append(" 			window.focus();                                                                                                                        \n");
					html.append(" </script>                                                                                                                                        \n");
				}
				html.append(" </body>                                                                                                                                        \n");
				html.append(" </html>                                                                                                                                        \n");

				if( hbanner!=null && !hbanner.equals("") ){
					out.println( html.toString() );
				}
				logger.debug("조건 없으므로 h_banner 노출.");
				return;
			} 
			logger.debug(scfg.getInfo("iadbn[459] scfg.info "));
			
			String r_site_code  = scfg.getSite_code()==null ? "" : scfg.getSite_code();
			String r_no         = scfg.getNo();
			//String r_no2        = scfg.getNo2();  
			String r_userid     = scfg.getUserid()==null ? "" : scfg.getUserid();
			//String r_purl       = scfg.get
			String r_pop        = scfg.getPop()==null ? "" : scfg.getPop();
			String r_pcode 		= scfg.getPcode();
			mcgb			= scfg.getMcgb();
			if(scfg.getKno()==null){
				scfg.setKno("");
			}
			if(scfg.getSite_etc()==null){
				scfg.setSite_etc("");
			}
			String r_kno = scfg.getKno();
			try{
				int t=Integer.parseInt(r_kno);
			}catch(Exception e){
				r_kno="0";
			}
			debug="12";
			String r_site_url=scfg.getSite_url()==null ? "" : scfg.getSite_url();
			String r_purl=scfg.getPurl()==null ? "" : scfg.getPurl();
			try{
				r_site_url = (r_site_url.substring(0,4).equals("http")?r_site_url:"http://"+r_site_url);
			}catch(Exception e){
				r_site_url = r_purl;
				r_site_url = (r_site_url.substring(0,4).equals("http")?r_site_url:"http://"+r_site_url);
			}
			if(adgubun.equals("SR")){
				String url_at="";
				if( r_site_url.indexOf("?") > -1 ){
					url_at="&";
				}else{
					url_at="?";
				}
				r_site_url += url_at + scfg.getSite_etc();
			}

			
			debug="13";
			

			String protocal="http";
			if(request.isSecure()){
				protocal="https";
			}
			
			
			String rurl="";
			String slink="servlet/idrc?kno="+r_kno+"&no="+r_no+"&s="+s+"&gb="+adgubun+"&sc="+r_site_code+"&mc="+s+"&mcgb="+mcgb+"&u="+r_userid;
			if(adgubun.equals("KL")){
				rurl   = r_site_url;
				slink+="&slink="+URLEncoder.encode(rurl,"UTF-8");
			}else if(adgubun.equals("SR")){
				rurl   = r_site_url;
				slink+="&slink="+URLEncoder.encode(rurl,"UTF-8");
			}else if(adgubun.equals("AD")){
				rurl   = r_site_url;
				slink+="&slink="+URLEncoder.encode(rurl,"UTF-8")+"&pop="+r_pop;
			}else if(adgubun.equals("UM")){
				rurl   = r_site_url;
				slink+="&slink="+URLEncoder.encode(rurl,"UTF-8");
			}else if(adgubun.equals("ST")){
				rurl   = r_site_url;
				slink+="&slink="+URLEncoder.encode(rurl,"UTF-8");
			}else if(adgubun.equals("SP")){
				rurl   = r_site_url;
				slink+="&slink="+URLEncoder.encode(rurl,"UTF-8");
			}else{
				logger.error("iadbn slink is null");
			}
			logger.debug("iadbn : slink="+slink);
			debug="14";
			MediaLogData mLog4=(MediaLogData)appContext.getBean("MediaLogData");
			mLog4.setS(s);
			mLog4.setIP(keyIp);
			mLog4.setSDATE(ymd);
			mLog4.setVIEWCNT(1);
			mLog4.setLogId(4);
			debug="15";	
			logger.debug("3 getS="+mLog4.getS());
			logger.debug("getIP="+mLog4.getIP());
			logger.debug("getSDATE="+mLog4.getSDATE());
			logger.debug("getVIEWCNT="+mLog4.getVIEWCNT());
			//dumpObj.getMediaLogData().add(mLog4);
			StringBuffer html=new StringBuffer();

			String charge= "site_code="+scfg.getSite_code()+"&adgubun="+adgubun+"&userid="+scfg.getUserid()+"&gb="+gb+"&no="+r_no+"&s="+s+"&kno="+r_kno+"&shopurl="+URLEncoder.encode(scfg.getSite_url(),"UTF-8");
			logger.debug("IAdbnHer:charge="+charge);
			html.append("callback_her( {istrue:'true', baseurl:'http://www.dreamsearch.or.kr/', openURI: '"+ slink+"', chargeURI:'servlet/setcharge?"+ charge+"', nochargeURI:'servlet/setnocharge?"+charge+"'} );");

			out.print(html.toString());
			manageCookie.makeCookie("site_code", scfg.getSite_code(), -1, response);
			
		} catch (Exception e) {
			logger.error("IAdbnHer:"+e+",debug="+debug); 
		}
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response){
		doGet(request,response);
	}
	public void init() throws ServletException {
		logger.info("IAdbnHerServlet started");
		if (appContext == null) {
			RFServlet.instance.init();
			appContext=RFServlet.appContext;
		}
		manageCookie=(ManagementCookie)appContext.getBean("ManagementCookie");
		dumpObj=(DumpObject)appContext.getBean("DumpObject");
	}
	protected String getFailStr(){
		return "callback_her( {istrue:'false'} );";
	}
	protected String[] getFileList() { 
        return new String[]{getServletConfig().getInitParameter("pathToContext")};
    }
	private String getScriptKhan(String s){
		String rtval= "";
		if(s.equals("440")){
			rtval= "<script type='text/javascript' src='http://ads.khan.co.kr/RealMedia/ads/adstream_jx.ads/sports.khan.co.kr/sub@x74'></script>";
		}
		return rtval;
	}
}