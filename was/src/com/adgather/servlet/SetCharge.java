package com.adgather.servlet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import com.adgather.beans.DumpDataToDB;
import com.adgather.beans.ManagementCookie;
import com.adgather.reportmodel.AdChargeData;
import com.adgather.reportmodel.AdConfigData;
import com.adgather.reportmodel.DumpObject;
import com.adgather.reportmodel.MediaLogData;
import com.adgather.reportmodel.MediaScriptData;
import com.adgather.reportmodel.PointData;
import com.adgather.reportmodel.ShopData;
import com.adgather.reportmodel.SiteCodeData;
import com.adgather.util.StringUtils;

public class SetCharge extends HttpServlet {
	private DumpObject dumpObj;
	private DumpDataToDB dumpDb;
	private ApplicationContext appContext;
	public ManagementCookie manageCookie;
	static Logger logger = Logger.getLogger(SetCharge.class);
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		String debug="";
		try {
			request.setCharacterEncoding("8859_1");
			response.setHeader("Pragma", "no-cache");			
			response.setHeader("Cache-Control", "no-cache"); 
			//response.setHeader("P3P","CP='IDC DSP COR ADM DEVi TAIi PSA PSD IVAi IVDi CONi HIS OUR IND CNT'");
			response.setHeader("P3P","CP='CAO PSA CONi OTR OUR DEM ONL'");
			String keyIp=manageCookie.makeKeyCookie(request, response);
			String site_code=request.getParameter("site_code")==null ? "" : request.getParameter("site_code");
			String userid=request.getParameter("userid")==null ? "" : request.getParameter("userid");
			String kno=request.getParameter("kno")==null ? "" : request.getParameter("kno");
			String adgubun=request.getParameter("adgubun")==null ? "" : request.getParameter("adgubun");
			String gb=request.getParameter("gb")==null ? "" : request.getParameter("gb");
			String no=request.getParameter("no")==null ? "" : request.getParameter("no");
			//String no2=request.getParameter("no2")==null ? "0" : request.getParameter("no2");
			String mcgb=request.getParameter("mcgb")==null ? "" : request.getParameter("mcgb");
			String us=request.getParameter("us")==null ? "" : request.getParameter("us");
			String s=request.getParameter("s")==null ? "" : request.getParameter("s");
			String pcode=request.getParameter("pcode")==null ? "" : request.getParameter("pcode");
			String shopurl=request.getParameter("shopurl")==null ? "" : request.getParameter("shopurl");
			String from=request.getParameter("from")==null ? "" : request.getParameter("from");
			

			String ic_um=manageCookie.getCookieInfo(request,"ic_um");
			String ic_ki=manageCookie.getCookieInfo(request,"ic_ki");
			String chk_ck1=manageCookie.getCookieInfo(request,"chk_ck");
			StringBuffer chk_ck= new StringBuffer();
			chk_ck.append(chk_ck1);
			
			String chk_result=manageCookie.getCookieInfo(request,"chk_result");
			
			shopurl=StringUtils.getURLDecodeStr(shopurl, "UTF-8");
			MediaScriptData ms=RFServlet.instance.adInfoCache.getMediaScriptData(s);
			if( ms==null ){
				logger.error("setCharge s null "+ keyIp);
				return;
			}
			if(site_code.equals("")){
				logger.error("SetCharge : site_code is null:"+ms.getUSERID());
				return;
			}	
			if(adgubun.equals("")){
				logger.error("SetCharge : adgubun is null:"+ms.getUSERID());
				return;
			}	
			if(no.equals("") || kno.equals("")){
				logger.error("SetCharge : no is null:"+ms.getUSERID());
				return;
			}
			//if(no2.equals("") || no2.equals("null")){
			//	logger.debug("SetCharge : no2 is null:"+ms.getUSERID());
			//	no2="0";
			//}
			if(s.equals("")){
				logger.error("SetCharge : s is null:"+ms.getUSERID());
				return;
			}	
			debug="sadbn[91] ";
			
			PointData acd=RFServlet.instance.adInfoCache.getAdCacheData(userid);
			if( acd==null ){
				logger.error("SetCharge[86] AdCache info none "+ userid);
				return;
			}
			
			int point = 0;
			double weight_pct, tmp_point = 0;
			
			weight_pct = tmp_point = point = 0;
			
			weight_pct = (double)ms.getWeight_pct();
			
			if(adgubun.equals("AD") && acd.getIAD()>0){
				tmp_point= (double)acd.getIAD();
			}else if(adgubun.equals("KL") && acd.getIKL()>0){
				tmp_point= (double)acd.getIKL();
			}else if(adgubun.equals("SR") && acd.getISR()>0){
				tmp_point= (double)acd.getISR();
			}else if(adgubun.equals("UM") && acd.getIUM()>0){
				tmp_point= (double)acd.getIUM();
			}else if(adgubun.equals("ST") && acd.getIST()>0){
				tmp_point= (double)acd.getIST();
			}else if(adgubun.equals("SP") && acd.getIST()>0){
				tmp_point= (double)acd.getISP();
			}
			
			try{
				tmp_point *= ( weight_pct / (double)100 );
			}catch(Exception e ){
				logger.error("drcpct[1553] ");
			}
			point = (int)tmp_point;
			
			debug="sadbn[123]";
			
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
			
			AdChargeData crd=(AdChargeData)appContext.getBean("AdChargeData");
			crd.setSITE_CODE(site_code);
			crd.setADGUBUN(adgubun);
			crd.setYYYYMMDD(ymd);
			crd.setHHMM(kh);
			crd.setPC("");
			crd.setUSERID(userid);
			crd.setPOINT(point+"");
			crd.setPPOINT("0");
			crd.setYM(ym);
			crd.setNO(no);
			crd.setIP(keyIp);
			crd.setKNO(kno);
			crd.setS(s);
			crd.setSCRIPTUSERID(ms.getUSERID());
			crd.setPRODUCT("ico");
			crd.setTYPE("V");
			crd.setMCGB(mcgb);
			crd.setPCODE(pcode);
			dumpObj.getChargeLogData().add(crd);
			if(crd.getPCODE()!=null && crd.getPCODE().length()>0){
				//RFServlet.instance.dumpDb.setShopStatsData(crd.getUSERID(),crd.getPCODE(),crd,"adview");
			}
			logger.debug("★ SetCharge  start");
			logger.debug(crd.getInfo("SetCharge crd.info "));


			PointData pd=null;
			SiteCodeData iad= null;
			SiteCodeData ad= null;
			ad= RFServlet.instance.adInfoCache.getAdSiteStatus(site_code);
			if( ad==null){
				iad= RFServlet.instance.adInfoCache.getIadSiteStatus(site_code);
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
			int cookieDay=31;
			if(pd!=null) {
				cookieDay=pd.getCookieDay();
			}
			
			
			debug="sadbn[207]";
			MediaLogData mLog3=(MediaLogData)appContext.getBean("MediaLogData");
			mLog3.setS(s);
			mLog3.setIP(keyIp);
			mLog3.setSDATE(ymd);
			mLog3.setVIEWCNT(1);
			mLog3.setLogId(3);
			
			logger.debug("getS="+mLog3.getS());
			logger.debug("getIP="+mLog3.getIP());
			logger.debug("getSDATE="+mLog3.getSDATE());
			logger.debug("setChargege.VIEWCNT="+mLog3.getVIEWCNT());
			dumpObj.getMediaLogData().add(mLog3);


			String newVal=manageCookie.getCookieInfo(request,"shop_log");
			newVal=java.net.URLDecoder.decode(newVal,"utf-8");

			
			if(adgubun.equals("SR") || adgubun.equals("ST") || adgubun.equals("SP")){	//shoplog 쿠키 및 데이타 초기화 
				debug="sadbn[222] 1";
				
				String chk_code= adgubun+"_"+site_code+"^"+pcode;
				String resultvalue = manageCookie.makeCookieStrNew(response, request, chk_result, chk_ck, chk_code, 2,"");
				
				if( resultvalue.equals("next")){
					ShopData slog=(ShopData)appContext.getBean("ShopData");
					slog.setGB(gb);
					slog.setIP(keyIp);
					slog.setNO(Long.parseLong(no));
					slog.setSITE_URL(shopurl);
					slog.setSvc_type("");
					slog.setSITE_CODE(site_code);
					slog.setPCODE(pcode);
					logger.debug("slog.setSITE_CODE="+site_code);
					logger.debug("slog.setPCODE"+pcode);
					logger.debug("slog.setGb="+gb);
					logger.debug("slog.setNO="+keyIp);
					logger.debug("slog.setNO="+Long.parseLong(no));
					logger.debug("slog.setSITE_URL="+shopurl);
					dumpObj.getDelShopLogData().add(slog);
					
					newVal=manageCookie.shopLogManage(1, newVal,slog,"edit");
				}
				
				if( (resultvalue.equals("next")) && !mcgb.equals("B") ){
					
					debug="sadbn[222] 2 us="+us +" s="+s+" site_code="+ site_code;
					
					if( us.equals("") ){
						us= ms.getMediasite_no();
					}
					
			    	String gubun_tmp=mcgb.equals("")?"SR":"SP";
					AdConfigData scfg= RFServlet.instance.adInfoCache.getShopAdConfig(us,s,site_code +"_",gubun_tmp);
					
					
					if( scfg!=null ){
						debug="sadbn[222] 2 scfg="+ scfg.getInfo("") ;
						
						String shopLog=manageCookie.getCookieInfo(request,"shop_log");
						shopLog=StringUtils.getURLDecodeStr(shopLog,"UTF-8");
						HashMap<String,ShopData> shhm=RFServlet.instance.adInfoCache.getShopLogData(shopLog,keyIp,"icover");
						List<String> it = new ArrayList<String>(shhm.keySet());
						Collections.sort(it);
						
						debug="sadbn[222] 2 2";
						
					    try{
					    	for(String me:it){
						    	ShopData shop_log1= shhm.get(me);
						    	logger.debug("setcharge shop_log1 "+shop_log1);
						    	if( shop_log1.getPCODE().equals(pcode) ){
						    		scfg.setFlag(shop_log1.getFlag());
						    		scfg.setMcgb(shop_log1.getMCGB());
						    		break;
						    	}
						    }
					    }catch(Exception e1){
					    	scfg.setFlag("");
					    	scfg.setMcgb("");
					    }
					    debug="sadbn[222] 2 3";
					}
				    
					if( scfg!=null && scfg.getFlag()!=null && scfg.getFlag().equals("1") && mcgb.equals("") ){
						logger.debug("iadbn[280] scfg "+scfg);
						
						AdConfigData tmpFg=RFServlet.instance.adInfoCache.getRecomAdConfig(scfg.getUserid(),us,s,"iadlink","");
						
						if(tmpFg!=null && (scfg.getMcgb()!=null && scfg.getMcgb().equals("") || !scfg.getMcgb().equals("B")) ){
							String cate1 = "";
							ShopData log_item= RFServlet.instance.adInfoCache.getShopPCodeData(scfg.getUserid(),pcode);
							if( log_item!=null ){
								cate1 = log_item.getCATE1();
							}
							debug="sadbn[222] 3 1";
							//ArrayList sdlist= RFServlet.instance.adInfoCache.getShopCategoryData(scfg.getUserid(), cate1 );
							LinkedHashMap ldList= RFServlet.instance.adInfoCache.getShopStatsData(scfg.getUserid(),cate1);
							if(ldList!=null && ldList.size()>0){
								//ArrayList sdlist=new ArrayList(ldList.values());
								List<String> sdlist = new ArrayList<String>(ldList.keySet());
								ArrayList<Integer> rndList = StringUtils.getRandList( sdlist.size() );
								ArrayList addsList= new ArrayList();
								for( int i=0; i<rndList.size() && i<2; i++){
									
									ShopData adds = new ShopData();
									//ShopData ss = (ShopData) sdlist.get( rndList.get(i) );
									ShopData ss =RFServlet.instance.adInfoCache.getShopPCodeData(scfg.getUserid(),sdlist.get(rndList.get(i)).toString());
									if(ss!=null){
										adds.setPNM(ss.getPNM());
										adds.setPCODE(ss.getPCODE());
										adds.setPARTID(ss.getPRICE());
										adds.setSC_TXT(ss.getCATE1());
										adds.setURL(ss.getPURL());
										adds.setPURL(ss.getPURL());
										adds.setIMGPATH(ss.getIMGPATH());
										adds.setPRICE(ss.getPRICE());
										
										adds.setIP(keyIp);
										adds.setUSERID(scfg.getUserid());
										adds.setSITE_CODE(tmpFg.getSite_code());
										//adds.setPARTID(tmpSd.getPARTID());
										adds.setRDATE(ymd);
										adds.setRTIME(hm);
										adds.setMCGB("B");
										adds.setRF("");
										adds.setGB("02");
										
										dumpObj.getShopLogData().add(adds);
										
										addsList.add(adds);
									}
									
								}
								
								newVal=manageCookie.shopLogManage(newVal,addsList,"append");
								logger.debug("newVal="+newVal);
							}
						}
						debug="sadbn[222] 3 2";
					}
					debug="sadbn[222] 4";
					
				}
				
				newVal=java.net.URLEncoder.encode(newVal,"utf-8");
				manageCookie.makeCookie("shop_log",newVal,cookieDay*24*60*60, response);
			}
		} catch (Exception e) {
//			Map<String, String[]> parameters = request.getParameterMap();
//			for(String parameter : parameters.keySet()) {
//			    if(parameter.toLowerCase().startsWith("question")) {
//			        String[] values= parameters.get(parameter);
//			        logger.error(values);
//			    }
//			}
			
			logger.error("SetCharge:"+e+",debug="+debug); 
		}
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response){
		doGet(request,response);
	}
	public void init() throws ServletException {
		logger.info("SetChargeServlet started");
		if (appContext == null) {
			RFServlet.instance.init();
			appContext=RFServlet.appContext;
		}
		manageCookie=(ManagementCookie)appContext.getBean("ManagementCookie");
		dumpObj=(DumpObject)appContext.getBean("DumpObject");
		dumpDb=(DumpDataToDB)RFServlet.instance.appContext.getBean("DumpDataToDB");
	}
	protected String[] getFileList() { 
        return new String[]{getServletConfig().getInitParameter("pathToContext")};
    }
}