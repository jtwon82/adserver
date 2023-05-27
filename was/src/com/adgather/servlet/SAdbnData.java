package com.adgather.servlet;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Stack;
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
import com.adgather.reportmodel.SkyAdData;
import com.adgather.reportmodel.SkyAdDataM;
import com.adgather.util.MassInformation;
import com.adgather.util.StringUtils;

public class SAdbnData extends HttpServlet {
	private DumpObject dumpObj;
	private ApplicationContext appContext;
	public ManagementCookie manageCookie;
	static Logger logger = Logger.getLogger(SAdbnData.class);
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		int id=0;
		String debug="";
		try {
			request.setCharacterEncoding("8859_1");
			response.setContentType("text/html;charset=utf-8");
			response.setHeader("Pragma", "no-cache");			
			response.setHeader("Cache-Control", "no-cache"); 
			//response.setHeader("P3P","CP='IDC DSP COR ADM DEVi TAIi PSA PSD IVAi IVDi CONi HIS OUR IND CNT'");
			response.setHeader("P3P","CP='CAO PSA CONi OTR OUR DEM ONL'");
			PrintWriter out=response.getWriter();

			String from=request.getParameter("from")==null ? "" : request.getParameter("from");
			String chk=request.getParameter("chk")==null ? "" : request.getParameter("chk");
			String her=request.getParameter("her")==null ? "" : request.getParameter("her");
			String referer=request.getHeader("Referer")==null ? "" : request.getHeader("Referer");
			
			if(from.equals("")){
				from=referer;
			}
			if(from.length()>200){
				from=from.substring(0,200);
			}
			
			/*
			String scTxt=from;
			*/
			String us=request.getParameter("us")==null ? "" : request.getParameter("us");
			String s=request.getParameter("s")==null ? "" : request.getParameter("s");
			String send_chk=manageCookie.getCookieInfo(request,"send_chk");
			String send_chk2=manageCookie.getCookieInfo(request,"send_chk2");
			
			logger.debug( "sadbn from="+ from +" us="+ us +" s="+ s );
			
			String mediaSiteStatus=RFServlet.instance.adInfoCache.getMediaSiteStatus(us);
			if(mediaSiteStatus.equals("N")){
				return;
			}
			SimpleDateFormat yyyymmdd = new SimpleDateFormat("yyyyMMdd");
			SimpleDateFormat hhmi = new SimpleDateFormat("kkmm");
			java.util.Date date=new java.util.Date();
			String ymd=yyyymmdd.format(date);
			String hm=hhmi.format(date);

			SimpleDateFormat yyyymm = new SimpleDateFormat("yyyyMM");
			SimpleDateFormat kk = new SimpleDateFormat("kk");
			String ym=yyyymm.format(date);
			String kh=kk.format(date);
			
			String keyIp=RFServlet.instance.manageCookie.makeKeyCookie(request, response);
			manageCookie.makeCookie("IPNewSky", "",0, response);

			String shopLog=manageCookie.getCookieInfo(request,"shop_log");
			shopLog=StringUtils.getURLDecodeStr(shopLog,"UTF-8");
			String i_cover=manageCookie.getCookieInfo(request,"i_cover");
			String ic_mcgb=manageCookie.getCookieInfo(request,"ic_mcgb");
			String ic_um=manageCookie.getCookieInfo(request,"ic_um");
			String ic_ki=manageCookie.getCookieInfo(request,"ic_ki");
			String ic_um_tgt= manageCookie.getCookieInfo(request,"ic_um_tgt");
			String ic_ki_tgt= manageCookie.getCookieInfo(request,"ic_ki_tgt");
			String chk_result=manageCookie.getCookieInfo(request,"chk_result");
			String chk_ck1=manageCookie.getCookieInfo(request,"chk_ck");
			StringBuffer chk_ck= new StringBuffer();
			chk_ck.append(chk_ck1);
			
			String ic_ki_real= manageCookie.getCookieInfo(request,"ic_ki_real");
			
			debug="sadbn[101]";
			
			ic_ki=StringUtils.getURLDecodeStr(ic_ki,"euc-kr");
			logger.debug("sadbn ic_um_tgt="+ ic_um_tgt);
			logger.debug("sadbn ic_ki_tgt="+ ic_ki_tgt);
			
			MediaLogData mLog1=(MediaLogData)appContext.getBean("MediaLogData");
			if( mLog1==null ){
				logger.debug("sadbn[109] mLog2 is null s="+ s +", keyip="+ keyIp );
				return;
			}
			mLog1.setS(s);
			mLog1.setIP(keyIp);
			mLog1.setSDATE(ymd);
			mLog1.setVIEWCNT(1);
			mLog1.setLogId(1);

			logger.debug("getS="+mLog1.getS());
			logger.debug("getIP="+mLog1.getIP());
			logger.debug("getSDATE="+mLog1.getSDATE());
			logger.debug("getVIEWCNT="+mLog1.getVIEWCNT());

			if( chk.equals("") ){
				dumpObj.getMediaLogData().add(mLog1);
			}
			////////////////////////////////////
			//  프리퀀시 확인(send_chk = 1) 일경우 송출 안함
			/////////////////////////////////
			
			HashMap<String,String> addpq=new HashMap<String,String>();
			if(chk_result!=null && chk_result.length()>0){
				StringTokenizer st=new StringTokenizer(chk_result,"#",false);
				while(st.hasMoreElements()){
					String stCode=st.nextElement().toString();
					addpq.put(stCode, stCode);
				}
			}
			
			debug="sadbn[135]";
			
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
					logger.error("SAdbn-MassInformation:"+e+" : "+ from);
				}
			}
			
			if(scTxt.length()>100){
				scTxt=scTxt.substring(0,100);
			}
			
			debug="sadbn[159]";

			MediaScriptData ms=RFServlet.instance.adInfoCache.getMediaScriptData(s);
			if( ms==null ){
				logger.debug("sadbn ms is null "+ s );
				return;
			}
			

			String protocal="http";
			if(request.isSecure()){
				protocal="https";
			}
			debug="sadbn[176]";
			
			String mcgb="";
			MediaLogData mLog2=(MediaLogData)appContext.getBean("MediaLogData");
			if( mLog2==null ){
				logger.debug("sadbn[170] mLog2 is null s="+ s +", keyip="+ keyIp );
				return;
			}
			mLog2.setS(s);
			mLog2.setIP(keyIp);
			mLog2.setSDATE(ymd);
			mLog2.setVIEWCNT(1);
			mLog2.setLogId(2);
				
			logger.debug("getS="+mLog2.getS());
			logger.debug("getIP="+mLog2.getIP());
			logger.debug("getSDATE="+mLog2.getSDATE());
			logger.debug("getVIEWCNT="+mLog2.getVIEWCNT());
			if( chk.equals("") ){
				//dumpObj.getMediaLogData().add(mLog2);
			}
			
			debug="sadbn[198]";
			//String adgubun="";
			//String site_code="";
			//String site_url="";
			//String gb="";
			//long no2=0;
			//AdConfigData scfg=null;
			
			AdConfigData scfg=null;
			AdConfigData scfgr=null;
			SkyAdDataM sadm= new SkyAdDataM();
			int chk_cnt= 0;
			int chk_cnt1= 0;
			String adgubun= "";
			String gubun_tmp="";
			
			debug="sadbn[238]";
			// gb^^^site_code^^^url||gb^^^site_code^^^url
			// step    1111111111111111111111111111111111111
			manageCookie.makeCookie("IPNewSky", "",0, response);
			String IPNewSky=manageCookie.getCookieInfo(request,"IPNewSky");
			if(IPNewSky==null || IPNewSky.equals("")){	// 키가 바로 생긴 것이 아니니  뭔가 있는지 뒤져본다..	  
				logger.debug("키가 바로 생긴 것이 아니니  뭔가 있는지 뒤져본다 :OK");
				
				if( shopLog!=null && shopLog.length()>0 && ms.getAccept_sr().equals("Y") ){
					if(URLEncoder.encode(shopLog,"UTF-8").length()>=1){	// 장바구니 없고,쿠키 잘린 상황이므로 db조회...
						logger.debug("쿠키 잘린 상황이므로 db조회...");
						try{
							HashMap<String,ShopData> shhm=RFServlet.instance.adInfoCache.getShopLogData(shopLog,keyIp,"sky");
							if(shhm==null || shhm.size()==0){
								manageCookie.makeCookie("shop_log", "", 0, response);
							}else{
								logger.debug("sadbn shhm.size()="+ shhm.size());
								List<String> it = new ArrayList<String>(shhm.keySet());
								Collections.sort(it);
								
							    for(String me:it){
							    	ShopData shop_log1= shhm.get(me);
							    	if( shop_log1.getTARGETGUBUN().indexOf("C")>-1 ) continue;
							    	if( shop_log1.getMCGB().equals("") || shop_log1.getMCGB().equals("C") ) {} else { continue; }
							    	
							    	gubun_tmp=shop_log1.getMCGB().equals("")?"SR":"SP";
							    	AdConfigData tmp_scfg= RFServlet.instance.adInfoCache.getShopAdConfig(us,s,shop_log1.getSITE_CODE() +"_sky", gubun_tmp);
							    	if( tmp_scfg!=null){
							    		
							    		AdConfigData scfg_chkrecom=RFServlet.instance.adInfoCache.getRecomAdConfig(tmp_scfg.getUserid(),us,s,"iadlink","sky");

								    	if( !shop_log1.getMCGB().equals("") && scfg_chkrecom==null ){
								    		continue;
								    	}else if ( !shop_log1.getMCGB().equals("") && scfg_chkrecom!=null ){
								    		if( !scfg_chkrecom.getSite_code().equals( shop_log1.getSITE_CODE() ) ){
								    			continue;
								    		}
								    	}
								    	
								    	ShopData log_item=RFServlet.instance.adInfoCache.getShopPCodeData(tmp_scfg.getUserid(),shop_log1.getPCODE());
								    	if( log_item!=null ){
								    	
									    	if( log_item.getPNM().equals("") || log_item.getPRICE().equals("") 
									    			|| log_item.getIMGPATH().equals("") || log_item.getPURL().equals("")){
									    		logger.debug("sadbn[251] "+log_item.toString());
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
													scfg.setSite_url(shop_log1.getURL());
													scfg.setImgname(tmp_scfg.getImgname());
												}
											}else if( shop_log1.getMCGB().equals("C") ){
												scfgr= tmp_scfg.clone();
												scfgr.setNo(shop_log1.getNO()+"");
												scfgr.setPcode(shop_log1.getPCODE());
												scfgr.setGb(shop_log1.getGB());
												scfgr.setSite_code(tmp_scfg.getSite_code());
												scfgr.setPurl(shop_log1.getPURL());
												scfgr.setMcgb(shop_log1.getMCGB());
												scfgr.setFlag(shop_log1.getFlag());
												scfgr.setCate1(log_item.getCATE1());
												scfgr.setPnm(log_item.getPNM());
												scfgr.setPrice(log_item.getPRICE());
												scfgr.setImgpath(log_item.getIMGPATH());
												scfgr.setSite_url(shop_log1.getURL());
												scfgr.setImgname(tmp_scfg.getImgname());
											}
								    	}
									}
								}
							    if( scfg==null && scfgr!=null ){
							    	scfg = scfgr.clone();
							    	mcgb=scfg.getMcgb();
							    }
							    
							    if( scfg!=null ){

							    	String logo= scfg.getImgname();
							    	logo= protocal+"://www.dreamsearch.or.kr/ad/imgfile/"+ logo;
							    	// 사이트코드가있으면 data에 추가 아니면 신규
							    	String pkey= scfg.getSite_code() +"_goods";
							    	logger.debug("pkey="+pkey);

							    	if(!sadm.list.containsKey(pkey)){
							    		SkyAdData sad= new SkyAdData();
							    		sad.setSite_code(scfg.getSite_code());
							    		sad.setTarget("goods");
							    		sad.setLogo(logo);
							    		sad.setNo( Integer.parseInt(scfg.getNo()) );
							    		sad.setPcode(scfg.getPcode());
							    		sad.setS(s);
							    		sad.setUs(us);
							    		sad.setMcgb(mcgb);
							    		//sad.setPurl(makPurl(new String[]{"",s,"SR",site_code1,s,ad.getUserid(),purl}));
							    		//sad.setPurl(purl);
							    		sad.setUserid(scfg.getUserid());
							    		sadm.list.put(pkey, sad);
							    		
							    		
							    		chk_cnt++;
							    	}
							    	
							    	String local_img= scfg.getImgpath();
									//try{
									//	local_img= "http://www.dreamsearch.or.kr/ad/sr/"+ scfg.getUserid() 
									//			+"/"+ scfg.getImgpath().split("/")[ scfg.getImgpath().split("/").length-1 ];
									//}catch(Exception e){
									//	local_img= scfg.getImgpath();
									//}
									
							    	ShopData sd= new ShopData();
							    	sd.setNO( Integer.parseInt(scfg.getNo()) );
							    	sd.setIP(keyIp);
							    	sd.setMCGB(scfg.getMcgb());
									sd.setPCODE( scfg.getPcode() );
									sd.setPNM( scfg.getPnm() );
									sd.setPRICE( scfg.getPrice() );
									sd.setIMGPATH( local_img );
									sd.setPURL(makPurl(new String[]{ scfg.getNo() ,s,(scfg.getMcgb().equals("")?"SR":"SP")
										, scfg.getSite_code(),s,scfg.getUserid(), scfg.getPurl(),mcgb,scfg.getPcode()}));
									sd.setURL(StringUtils.gAt1( scfg.getPurl(), 1, "/"));
									//sd.setPURL(purl);
									sd.setCATE1( scfg.getCate1() );
									sd.setUSERID(scfg.getUserid());
									sd.setSITE_CODE(scfg.getSite_code());
									
									ArrayList tmpArr=sadm.list.get(pkey).getData();
									tmpArr.add(sd);
									
									SkyAdData tmpS=sadm.list.get(pkey);
									tmpS.setData(tmpArr);
									sadm.list.put(pkey, tmpS);
							    }
						    	if( chk.equals("") ){
						    		manageCookie.makeCookie("push", "1", 60*6, response);
						    	}
							}
						}catch(Exception e){
							logger.error("상품  error: "+e);
						}
					}
				} //  로그 있을시 끝
			}

			
			
			
			
			id=0;
			debug="sadbn[385]";
			/* 카테고리정보 가져와서 data에 추가하기 */
			// step 2222222222222222222222222222222
	    	logger.debug("sadbn sadm.size()="+ sadm.list.size() +", chk="+chk +"|");
			if( sadm.list.size()>0 && ms.getAccept_sr().equals("Y") && adgubun.equals("") ){
				Iterator it = sadm.list.keySet().iterator();
				while(it.hasNext()){
					id++;
					String pkey=it.next().toString();
					SkyAdData sData=sadm.list.get(pkey);
					ShopData fitem=sData.getData().get(0);
					logger.debug(fitem.getInfo("sadbn fitem="));
					
					//String k1= fitem.getURL().replace("www.", "") +"_"+ fitem.getCATE1();
					//String k1= fitem.getURL().replace("www.", "") +"_"+ fitem.getCATE1();
					LinkedHashMap ldList= RFServlet.instance.adInfoCache.getShopStatsData(fitem.getUSERID(),fitem.getCATE1());
					if(ldList!=null && ldList.size()>0){
						logger.debug("sadbn ldList.size "+ldList.size() );
						List<String> shop_data_list = new ArrayList<String>(ldList.keySet());

						//ArrayList shop_data_list= RFServlet.instance.adInfoCache.getShopCategoryData(fitem.getUSERID(),fitem.getCATE1());
						if(shop_data_list!= null && shop_data_list.size()>0){
							ArrayList<Integer> rnd= StringUtils.getRandList(shop_data_list.size());
							for( int i=0; i<shop_data_list.size(); i++){
								//int k= (Integer) rnd.get(i);
					    		//ShopData f= (ShopData) shop_data_list.get(k);
								ShopData f =RFServlet.instance.adInfoCache.getShopPCodeData(fitem.getUSERID(),shop_data_list.get(rnd.get(i)).toString());
					    		if(f!=null){
									//logger.debug(fitem.getInfo("sadbn fitem.info="));
						    		//logger.debug(f.getInfo("sadbn f.info="));
						    		
						    		ArrayList tmpArr=sadm.list.get(pkey).getData();
									if( fitem.getPCODE().equals(f.getPCODE()) || tmpArr.size() > 8 ){
										continue;
									}
									
									// site_etc 를 가져오기위해 필요함
									gubun_tmp=fitem.getMCGB().equals("")?"SR":"SP";
									AdConfigData ad= RFServlet.instance.adInfoCache.getShopAdConfig(us,s,fitem.getSITE_CODE() +"_sky",gubun_tmp);
									String purl= f.getPURL() +(f.getPURL().indexOf("?")>-1?"&":"?")+ ad.getSite_etc();
									
									String local_img= f.getIMGPATH();
									//try{
									//	local_img= "http://www.dreamsearch.or.kr/ad/sr/"+ scfg.getUserid()
									//			+"/"+ f.getIMGPATH().split("/")[ f.getIMGPATH().split("/").length-1 ];
									//}catch(Exception e){
									//	local_img= f.getIMGPATH();
									//}
									
									ShopData sd= new ShopData();
									sd.setPCODE(f.getPCODE());
									sd.setPNM(f.getPNM());
									sd.setPRICE(f.getPRICE());
									sd.setIMGPATH( local_img );
									sd.setPURL(makPurl(new String[]{fitem.getNO()+"",s,(fitem.getMCGB().equals("")?"SR":"SP")
											,fitem.getSITE_CODE(),s,fitem.getUSERID(),purl,mcgb,fitem.getPCODE()}));
									sd.setURL(f.getURL());
									//sd.setPURL(purl);
									sd.setCATE1(f.getCATE1());
									sd.setUSERID(f.getUSERID());
									sd.setSITE_CODE(f.getSITE_CODE());
									
									adgubun=gubun_tmp;
									
									boolean ist1= false;
									for( int j=0; j<tmpArr.size(); j++ ){
										ShopData aa= (ShopData) tmpArr.get(j);
										if( aa.getPCODE().equals(f.getPCODE()) ){
											ist1= true;
										}
									}
									if( !ist1 ){
										tmpArr.add(sd);
										SkyAdData tmpS=sadm.list.get(pkey);
										tmpS.setData(tmpArr);
										sadm.list.put(pkey, tmpS);
									}
					    		}
							}
						}
					}
				}
				//logger.debug( "sadbn sadm.info="+ sadm.getInfo());
			}
			
			debug="sadbn[492]";
			// step 3333333333333333333333333333
			if( ic_um.length()>0 && sadm.list.size()<1 && ms.getAccept_um().equals("Y")
					&& ( !ms.getUSERID().equals("sndKorea") || !s.equals("781") )
					){
				
				logger.debug("sadbn url 리타게팅.."+ ic_um +" ic_um.length="+ ic_um.length());
				//StringTokenizer st=new StringTokenizer(ic_um,"|||",false);
				
//				Stack<String> stack= StringUtils.gListStack(ic_um);
				int tot_cnt= StringUtils.gAtCnt(ic_um, "|||");
				id=-1;
//				while( !stack.empty() ){
				StringTokenizer st=new StringTokenizer(ic_um,"|||");
				while(st.hasMoreElements()){
					id++;
					String ic_umSel= st.nextElement().toString();
					ic_umSel=StringUtils.getURLDecodeStr(ic_umSel,"euc-kr");
					String sel_ic_um_tgt= StringUtils.gAt1(ic_um_tgt, id, "|||");
					logger.debug("sadbn ic_um_tgt="+ ic_um_tgt +", sel_ic_um_tgt="+ sel_ic_um_tgt );
					if( sel_ic_um_tgt.indexOf("C") >=0 ){
						//continue;
					}
					AdConfigData ad= RFServlet.instance.adInfoCache.getUrlMatchAdConfig(us,s,ic_umSel, addpq, chk_ck.toString(), "sky", "UM", "BRUM");
					String resultvalue = "";
					
					if(ad!=null){
						logger.debug("sadbn url ic_um="+ ic_umSel +" sc="+ ad.getSite_code() +" st="+ ad.getSvc_type() );
				    	// 기본
				    	String site_code1= ad.getSite_code();
				    	String target="url";
				    	String logo= protocal+"://www.dreamsearch.or.kr/ad/imgfile/"+ ad.getImgname();
				    	String purl= ad.getSite_url() +(ad.getSite_url().indexOf("?")>-1?"&":"?")+ ad.getSite_etc();
				    	purl = (purl.substring(0,4).equals("http")?purl:"http://"+purl);
				    	
				    	// 사이트코드가있으면 data에 추가 아니면 신규
				    	if( ad.getSvc_type().equals("sky")){
					    	//sadm.add(new String[]{site_code1, target, logo, purl, ad.getUserid() });
				    		String pkey=site_code1+"_"+target;
					    	if(!sadm.list.containsKey(pkey)){
					    		SkyAdData sad= new SkyAdData();
					    		sad.setSite_code(site_code1);
					    		sad.setTarget(target);
					    		sad.setLogo(logo);
					    		sad.setS(s);
					    		sad.setUs(us);
					    		sad.setPurl(makPurl(new String[]{ad.getKno(),s,"UM",site_code1,s,ad.getUserid(),purl,mcgb,""}));
					    		//sad.setPurl(purl);
					    		sad.setUserid(ad.getUserid());
					    		sad.setKno(ad.getKno());
					    		sad.setId(id +"");
					    		//sad.setMcgb(mcgb);
					    		sadm.list.put(pkey, sad);
					    		
					    		adgubun="UM";

					    		logger.debug("sadbn UM chk="+ chk);
						    	if ( chk.equals("") ){ // 실제 타게팅될때
						    		String chk_code= "BRUM_"+ic_umSel;
						    		resultvalue = manageCookie.makeCookieStrNew(response, request, chk_result, chk_ck, chk_code, 1,"");
						    		
						    		if( resultvalue.equals("next") && tot_cnt==id ){
						    			manageCookie.clearChkCk(response, chk_result, chk_ck.toString(), ic_um, "sky","BRUM");
						    		}
									manageCookie.makeCookie("push", "1", 60*6, response);
						    	}
						    	
					    		chk_cnt++;
					    		break;
					    	}
							logger.debug("sadbn url 리타게팅.. :OK"+ ad.getInfo("sadbn ad.info="));
				    	}
					}
					if( (ad==null && tot_cnt==id ) ){
						if( chk.equals("") ){
							manageCookie.clearChkCk(response, chk_result, chk_ck.toString(), ic_um, "sky","BRUM");
						}
					}
				}
			}


				/*
			debug="sadbn[479] ST";
			// step 
			if( ic_um.length()>0 && sadm.list.size()<1 && ms.getAccept_st().equals("Y")
					&& ( !ms.getUSERID().equals("sndKorea") || !s.equals("781") )
					){
				
				logger.debug("sadbn 투데이 핫.."+ ic_um +" ic_um.length="+ ic_um.length());
				int tot_cnt= StringUtils.gAtCnt(ic_um, "|||");
				id=-1;
				StringTokenizer st=new StringTokenizer(ic_um,"|||");
				while(st.hasMoreElements()){
					id++;
					String ic_umSel= st.nextElement().toString();
					ic_umSel=StringUtils.getURLDecodeStr(ic_umSel,"euc-kr");
					AdConfigData ad= RFServlet.instance.adInfoCache.getTodayMatchAdConfig(us,s,ic_umSel, addpq, chk_ck.toString(), "sky", "ST", 3);
					String resultvalue = "";
					
					if(ad!=null){
						logger.debug("sadbn ST "+ ad.toString() );
				    	// 기본
				    	String site_code1= ad.getSite_code();
				    	String target="ST";
				    	String logo= protocal+"://www.dreamsearch.or.kr/ad/imgfile/"+ ad.getImgname();
				    	String purl= ad.getSite_url() +(ad.getSite_url().indexOf("?")>-1?"&":"?")+ ad.getSite_etc();
				    	purl = (purl.substring(0,4).equals("http")?purl:"http://"+purl);
				    	
				    	// 사이트코드가있으면 data에 추가 아니면 신규
				    	if( ad.getSvc_type().equals("sky")){
					    	//sadm.add(new String[]{site_code1, target, logo, purl, ad.getUserid() });
				    		String pkey= site_code1+"_"+target;
					    	if(!sadm.list.containsKey(pkey)){
					    		SkyAdData sad= new SkyAdData();
					    		sad.setSite_code(site_code1);
					    		sad.setTarget(target);
					    		sad.setPcode(ad.getPcode());
					    		sad.setLogo(logo);
					    		sad.setS(s);
					    		sad.setUs(us);
					    		sad.setPurl(makPurl(new String[]{ad.getKno(),s,"ST",site_code1,s,ad.getUserid(),purl,mcgb,""}));
					    		//sad.setPurl(purl);
					    		sad.setUserid(ad.getUserid());
					    		sad.setKno(ad.getKno());
					    		sad.setId(id +"");
					    		//sad.setMcgb(mcgb);
					    		sadm.list.put(pkey, sad);

					    		logger.debug("sadbn ST chk="+ chk);
						    	if ( chk.equals("") ){ // 실제 타게팅될때

									LinkedHashMap ldList= RFServlet.instance.adInfoCache.getShopStatsData(ad.getUserid(),"");
									if(ldList!=null && ldList.size()>0){
										ArrayList shop_data_list=new ArrayList(ldList.values());

										//ArrayList shop_data_list= RFServlet.instance.adInfoCache.getShopCategoryData(fitem.getUSERID(),fitem.getCATE1());
										if(shop_data_list!= null && shop_data_list.size()>0){
											ArrayList<Integer> rnd= StringUtils.getRandList(shop_data_list.size());
											for( int i=0; i<shop_data_list.size(); i++){
												if( i>9 ) break;
												String pcode= shop_data_list.get(rnd.get(i)).toString();
												if( i==0 ) pcode=ad.getPcode();
												
												ShopData f =RFServlet.instance.adInfoCache.getShopPCodeData(ad.getUserid(), pcode);
									    		if(f!=null){
													logger.debug("sadbn[508] f "+ f.toString() );
										    		ArrayList tmpArr=sadm.list.get(pkey).getData();
													
													String local_img= f.getIMGPATH();
													
													ShopData sd= new ShopData();
													sd.setPCODE(f.getPCODE());
													sd.setPNM(f.getPNM());
													sd.setPRICE(f.getPRICE());
													sd.setIMGPATH( local_img );
													sd.setPURL(makPurl(new String[]{f.getNO()+"",s,"ST",site_code1,s,ad.getUserid(),f.getPURL(),mcgb,f.getPCODE()}));
													sd.setURL(f.getURL());
													//sd.setPURL(purl);
													sd.setCATE1(f.getCATE1());
													sd.setUSERID(ad.getUserid());
													sd.setSITE_CODE(ad.getUserid());
													
													boolean ist1= false;
													for( int j=0; j<tmpArr.size(); j++ ){
														ShopData aa= (ShopData) tmpArr.get(j);
														if( aa.getPCODE().equals(f.getPCODE()) ){
															ist1= true;
														}
													}
													if( !ist1 ){
														tmpArr.add(sd);
														SkyAdData tmpS=sadm.list.get(pkey);
														tmpS.setData(tmpArr);
														sadm.list.put(pkey, tmpS);
													}
									    		}
											}
										}
									}
						    		manageCookie.makeCookie("push", "1", 60*6, response);
						    	}
						    	
					    		chk_cnt++;
					    		break;
					    	}
							logger.debug("sadbn ST .. :OK"+ ad.getInfo("sadbn ad.info="));
				    	}
					}
				}
			}
			
			if( scTxt.length()>0 && ms.getAccept_kl().equals("Y") && ( !ms.getUSERID().equals("sndKorea") || !s.equals("781") ) ){
				logger.debug("sadbn 실시간 키워드 리타게팅....==>"+ scTxt);
				scTxt=StringUtils.getURLDecodeStr(scTxt,"euc-kr");
				if(scTxt.length()>0){
					AdConfigData ad=RFServlet.instance.adInfoCache.getIkeywordMatchAdConfig(us,s,scTxt, chk_result, chk_ck,"sky");
					if( ad!=null ){
						adgubun="KL";
						mcgb="C";
						logger.debug("sadbn key ic_kl="+ scTxt +" sc="+ ad.getSite_code() +" st="+ ad.getSvc_type() );
						
				    	// 기본
				    	String site_code1= ad.getSite_code();
				    	String target="keyword";
				    	String logo= protocal+"://www.dreamsearch.or.kr/ad/imgfile/"+ ad.getImgname();
				    	String purl= ad.getSite_url() +(ad.getSite_url().indexOf("?")>-1?"&":"?")+ ad.getSite_etc();
				    	purl = (purl.substring(0,4).equals("http")?purl:"http://"+purl);

				    	// 사이트코드가있으면 data에 추가 아니면 신규
				    	if( ad.getSvc_type().equals("sky") ){
					    	//sadm.add(new String[]{site_code1, target, logo, purl, ad.getUserid() });
				    		String pkey=site_code1+"_"+target;
					    	if(!sadm.list.containsKey(pkey)){
					    		SkyAdData sad= new SkyAdData();
					    		sad.setSite_code(site_code1);
					    		sad.setTarget(target);
					    		sad.setLogo(logo);
					    		sad.setS(s);
					    		sad.setUs(us);
					    		sad.setKno(ad.getKno());
					    		sad.setPurl(makPurl(new String[]{ad.getKno(),s,"KL",site_code1,s,ad.getUserid(),purl,mcgb}));
					    		//sad.setPurl(purl);
					    		sad.setUserid(ad.getUserid());
					    		sad.setId(id+"");
					    		//sad.setMcgb(mcgb);
					    		sadm.list.put(pkey, sad);

						    	if( chk.equals("") ){
									manageCookie.makeCookie("push", "1", 60*6, response);
						    	}
					    		chk_cnt++;
					    	}
							
							logger.debug("키워드 리타게팅.... :OK");
				    	}
					}
				}
			}
			*/
			
			debug="sadbn[554]";
			// step 444444444444444444444444444444444444
			if( ic_ki.length()>0 && sadm.list.size()<1 && ms.getAccept_kl().equals("Y")
					&& ( !ms.getUSERID().equals("sndKorea") || !s.equals("781") )
					){
				
				logger.debug("sadbn 키워드 리타게팅....==>"+ ic_ki);
				scTxt=StringUtils.getURLDecodeStr(ic_ki,"euc-kr");
				if(scTxt.length()>0){
					//StringTokenizer st=new StringTokenizer(scTxt,"|||",false);
					
					Stack<String> stack= StringUtils.gListStack(scTxt);
					id= -1;
					while( !stack.empty() ){
						id++;
						String scTxtSel= stack.pop();
						String sel_ic_ki_tgt= StringUtils.gAt1(ic_ki_tgt, id, "|||");
						if( sel_ic_ki_tgt.indexOf("C") >=0 ){
							continue;
						}
						AdConfigData ad=RFServlet.instance.adInfoCache.getIkeywordMatchAdConfig(us,s,scTxtSel, chk_result, chk_ck.toString(),"sky","BRKL");
						if( ad!=null ){
							logger.debug("sadbn key ic_kl="+ scTxtSel +" sc="+ ad.getSite_code() +" st="+ ad.getSvc_type() );
							RFData keywordLog=(RFData)appContext.getBean("RFData");
							keywordLog.setRDATE(ymd);
							keywordLog.setRTIME(hm);
							keywordLog.setMCGB(mcgb);
							keywordLog.setSC_TXT(scTxtSel);
							keywordLog.setRURL(from);
							keywordLog.setDPOINT(0);
							keywordLog.setIP(keyIp);
							keywordLog.setMC(s);
							keywordLog.setK_GUBUN("i");
							if( chk.equals("") ){
								dumpObj.getKeywordLogData().add(keywordLog);
							}
							
					    	// 기본
					    	String site_code1= ad.getSite_code();
					    	String target="keyword";
					    	String logo= protocal+"://www.dreamsearch.or.kr/ad/imgfile/"+ ad.getImgname();
					    	String purl= ad.getSite_url() +(ad.getSite_url().indexOf("?")>-1?"&":"?")+ ad.getSite_etc();
					    	purl = (purl.substring(0,4).equals("http")?purl:"http://"+purl);

					    	// 사이트코드가있으면 data에 추가 아니면 신규
					    	if( ad.getSvc_type().equals("sky") ){
						    	//sadm.add(new String[]{site_code1, target, logo, purl, ad.getUserid() });
					    		String pkey=site_code1+"_"+target;
						    	if(!sadm.list.containsKey(pkey)){
						    		SkyAdData sad= new SkyAdData();
						    		sad.setSite_code(site_code1);
						    		sad.setTarget(target);
						    		sad.setLogo(logo);
						    		sad.setS(s);
						    		sad.setUs(us);
						    		sad.setKno(ad.getKno());
						    		sad.setPurl(makPurl(new String[]{ad.getKno(),s,"KL",site_code1,s,ad.getUserid(),purl,mcgb,""}));
						    		//sad.setPurl(purl);
						    		sad.setUserid(ad.getUserid());
						    		sad.setId(id+"");
						    		//sad.setMcgb(mcgb);
						    		sadm.list.put(pkey, sad);
						    		
						    		adgubun="KL";

							    	if( chk.equals("") ){
							    		String chk_code = "BRKL_"+ java.net.URLEncoder.encode(scTxtSel,"euc-kr");
							    		String resultvalue = manageCookie.makeCookieStrNew(response, request, chk_result, chk_ck, chk_code, 20,"");
							    		
										manageCookie.makeCookie("push", "1", 60*6, response);
							    	}
						    		chk_cnt++;
						    		break;
						    	}
								
								logger.debug("키워드 리타게팅.... :OK");
					    	}
						}
					}
				}
			}

			debug="sadbn[628]";
			// step 555555555555555555555555555555555555
			if( sadm.list.size()<1 && ms.getAccept_ad().equals("Y") ){

				logger.debug("베이스 광고 ..");
				String dispo=manageCookie.getCookieInfo(request,"dispo");
				AdConfigData ad =RFServlet.instance.adInfoCache.getBaseAdConfig( us+"_"+s, "sky", dispo, "" );
				if( ad!=null ){
					ad.setNo("0");
					logger.debug(ad.getInfo("sadbn ad"));
					// sadbn ad
			    	String site_code1= ad.getSite_code();
			    	String target="base";
			    	String logo= protocal+"://www.dreamsearch.or.kr/ad/imgfile/"+ ad.getImgname();
			    	String purl= ad.getSite_url() +(ad.getSite_url().indexOf("?")>-1?"&":"?")+ ad.getSite_etc();
			    	purl = (purl.substring(0,4).equals("http")?purl:"http://"+purl);
			    	
			    	// 사이트코드가있으면 data에 추가 아니면 신규
			    	if( ad.getSvc_type().equals("sky")){
			    		String pkey = site_code1+"_"+target;
				    	if(!sadm.list.containsKey(pkey)){
				    		SkyAdData sad= new SkyAdData();
				    		sad.setSite_code(site_code1);
				    		sad.setTarget(target);
				    		sad.setLogo(logo);
				    		sad.setS(s);
				    		sad.setUs(us);
				    		sad.setPurl(makPurl(new String[]{"",s,"AD",site_code1,s,ad.getUserid(),purl,mcgb,""}));
				    		//sad.setPurl(purl);
				    		sad.setUserid(ad.getUserid());
				    		sad.setId(id +"");
				    		sadm.list.put(pkey, sad);
				    		
				    		adgubun="AD";

				    		logger.debug("sadbn AD chk="+ chk);
					    	if ( chk.equals("") ){
								manageCookie.makeCookie("push", "1", 60*6, response);
					    	}
					    	
				    		chk_cnt++;
				    	}
						logger.debug("sadbn AD .. :OK "+ ad.getInfo("sadbn ad.info="));
			    	}
					
				}
			}

			debug="sadbn[674]";
			String h_banner="", h_types="";
			// step 6666666666666666666666666666666666666666666666666666666666666666666
			if( sadm.list.size()<1 ){
				logger.debug("sadbn hbanner start ");
				try{
					String pkey = "HB";
					h_banner=ms.getH_BANNER();
					h_types=ms.getH_TYPE();
					
					if( h_banner!=null && !h_banner.equals("") ){
						SkyAdData sad= new SkyAdData();
			    		sad.setTarget("hbanner");
			    		sad.setH_types(h_types);
			    		sad.setPurl(h_banner);
			    		sadm.list.put(pkey, sad);
	
			    		chk_cnt++;
					}
				}catch(Exception e ){
					logger.debug("sadbn hbanner err "+ e);
				}

			}

			debug="sadbn[697]";
			// 77777777777777777777777777777777777777777777777777777777
			logger.debug("sadbn sadm.size="+ sadm.size());
			//out.print( "adDATA= "+ StringUtils.getURLDecodeStr(sadm.getJsonData(),"UTF-8") +" ;" );
			
			
			
			if( chk.equals("") ){
				out.println( sadm.getJsonData(adgubun) );
				logger.debug(sadm.getJsonData(adgubun));
				if( scfg!=null && scfg.getSite_code()!=null && !scfg.getSite_code().equals("") ){
					manageCookie.makeCookie("site_code", scfg.getSite_code(), -1, response);
				}
				logger.debug("sadbn isAdout="+ sadm.size());
			}else{
				String ifr_src = "http://www.dreamsearch.or.kr/ad/skyBanner.jsp?from="+from+"&us="+us+"&s="+s;
				if( h_banner!=null && !h_banner.equals("") ){
					if( h_types.equals("script") ){
						if( chk_cnt >0 ){
							out.println("wp_htypes=\""+ h_types +"\"; ");
							out.println("wp_rcBody=\""+ h_banner +"\"; ");
						}else{
							out.print("wp_rcBody='';");
						}
						return;
					}else{
						ifr_src = h_banner;
					}
				}
				
				if( her.equals("1") ){
					
					out.print("wp_callback_her1({");
					if( chk_cnt >0 ){
						out.print("\"wp_rcBody\":\"<iframe name='widthPang' id='widthPang'"
								+" src='"+ ifr_src +"' "
								+"marginheight='0' marginwidth='0' scrolling='no' width='1200' height='830' frameborder='0' "
								+"onload='wp_display_skydiv();'></iframe>\"");
					}else{
						out.print("\"wp_rcBody\":\"\"");
					}
					out.print("});");
				}else{
					
					if( chk_cnt >0 ){
						out.println("wp_rcBody=\"<iframe name='widthPang' id='widthPang' "
								+"src='"+ ifr_src +"' "
								+"marginheight='0' marginwidth='0' scrolling='no' width='1200' height='830' frameborder='0' "
								+"onload='wp_display_skydiv();'></iframe>\"; ");
						out.println( sadm.getImgurls() );
					}else{
						out.print("wp_rcBody='';");
					}
				}
				
				logger.debug("sadbn isChkout="+ chk_cnt );
			}
			

		} catch (Exception e) {
			logger.error("sadbn :"+e+",debug="+debug); 
		}
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response){
		doGet(request,response);
	}
	public void init() throws ServletException {
		logger.info("SAdbnServlet started");
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
	private String makPurl(String []ar){	//makPurl(new String[]{"kno","s","gb","sc","mc","u","slink"});
		StringBuffer link= new StringBuffer();
		link.setLength(0);
		link.append("kno="+ ar[0]);
		link.append("&s="+ ar[1]);
		link.append("&gb="+ ar[2]);
		link.append("&sc="+ ar[3]);
		link.append("&mc="+ ar[4]);
		link.append("&u="+ ar[5]);
		link.append("&mcgb="+ ar[7]);
		link.append("&pCode="+ ar[8]);
		try {
			link.append("&slink="+ URLEncoder.encode(ar[6],"UTF-8"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "http://www.dreamsearch.or.kr/servlet/sdrc?"+ link.toString();
	}
}