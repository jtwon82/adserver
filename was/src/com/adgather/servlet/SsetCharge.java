package com.adgather.servlet;


import java.io.PrintWriter;
import java.sql.Connection;
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
import com.adgather.reportmodel.MediaScriptData;
import com.adgather.reportmodel.PointData;
import com.adgather.reportmodel.ShopData;
import com.adgather.reportmodel.SiteCodeData;
import com.adgather.resource.db.DBManager;
import com.adgather.util.StringUtils;

public class SsetCharge extends HttpServlet {
	private DumpObject dumpObj;
	private DumpDataToDB dumpDb;
	private ApplicationContext appContext;
	public ManagementCookie manageCookie;
	static Logger logger = Logger.getLogger(SsetCharge.class);
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		int debug=0;
		try {
			request.setCharacterEncoding("8859_1");
			response.setHeader("Pragma", "no-cache");			
			response.setHeader("Cache-Control", "no-cache"); 
			//response.setHeader("P3P","CP='IDC DSP COR ADM DEVi TAIi PSA PSD IVAi IVDi CONi HIS OUR IND CNT'");
			response.setHeader("P3P","CP='CAO PSA CONi OTR OUR DEM ONL'");
			PrintWriter out=response.getWriter();
			
			debug++;
			String keyIp=manageCookie.makeKeyCookie(request, response);
			String site_code=request.getParameter("site_code")==null ? "" : request.getParameter("site_code");
			String svc_type=request.getParameter("svc_type")==null ? "" : request.getParameter("svc_type");
			String userid=request.getParameter("userid")==null ? "" : request.getParameter("userid");
			String pcode=request.getParameter("pcode")==null ? "" : request.getParameter("pcode");
			String kno=request.getParameter("kno")==null ? "" : request.getParameter("kno");
			String tid=request.getParameter("id")==null ? "" : request.getParameter("id");
			String adgubun=request.getParameter("adgubun")==null ? "" : request.getParameter("adgubun");
			String gb=request.getParameter("gb")==null ? "" : request.getParameter("gb");
			String no=request.getParameter("no")==null ? "0" : request.getParameter("no");
			//String no2=request.getParameter("no2")==null ? "0" : request.getParameter("no2");
			String us=request.getParameter("us")==null ? "" : request.getParameter("us");
			String s=request.getParameter("s")==null ? "" : request.getParameter("s");
			String mcgb=request.getParameter("mcgb")==null ? "" : request.getParameter("mcgb");
			String shopurl=request.getParameter("shopurl")==null ? "" : request.getParameter("shopurl");
			shopurl=StringUtils.getURLDecodeStr(shopurl, "UTF-8");
			MediaScriptData ms=RFServlet.instance.adInfoCache.getMediaScriptData(s);
			if(ms==null){
				logger.error("SsetCharge mediascript none");
				return;
			}
			if( no.equals("") || kno.equals("")){
				logger.error("SetCharge : no kno is null:"+ms.getUSERID());
				return;
			}
			String ic_um= manageCookie.getCookieInfo(request,"ic_um");
			String ic_ki= manageCookie.getCookieInfo(request,"ic_ki");
			String ic_um_tgt= manageCookie.getCookieInfo(request,"ic_um_tgt");
			String ic_ki_tgt= manageCookie.getCookieInfo(request,"ic_ki_tgt");
			String chk_result=manageCookie.getCookieInfo(request,"chk_result");
			String chk_ck1=manageCookie.getCookieInfo(request,"chk_ck");
			StringBuffer chk_ck= new StringBuffer();
			chk_ck.append(chk_ck1);

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
			
			AdChargeData crd= (AdChargeData)appContext.getBean("AdChargeData");
			crd.setSITE_CODE(site_code);
			crd.setADGUBUN(gb);
			crd.setTYPE("V");
			crd.setYYYYMMDD(ymd);
			crd.setHHMM(kh);
			crd.setPC("");
			crd.setKNO(kno);	// 노출일때는 kno가 안들어오고 no 값이 들어온다. sstatus_srlink에는 no를 따로 저장하지 않고 공중으로 뜸.
			crd.setS(s);
			crd.setSCRIPTUSERID("");
			crd.setUSERID(userid);
			crd.setPOINT("0");
			crd.setPPOINT("0");
			crd.setYM(ym);
			crd.setNO(no);
			crd.setIP(keyIp);
			crd.setPRODUCT("sky");
			crd.setMCGB(mcgb);
			crd.setPCODE(pcode);
			dumpObj.getSkyChargeData().add(crd);
			if(crd.getPCODE()!=null && crd.getPCODE().length()>0){
				RFServlet.instance.dumpDb.setShopStatsData(crd.getUSERID(),crd.getPCODE(),crd,"adview");
			}	
			logger.debug("★ SsetCharge start");
			logger.debug(crd.getInfo("Sadbn view info="));

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
			
			if(adgubun.equals("SR") || adgubun.equals("ST") || adgubun.equals("SP") ){	//shoplog 쿠키 및 데이타 초기화 
				
				String chk_code= adgubun+"_"+site_code+"^"+pcode;
				String resultvalue = manageCookie.makeCookieStrNew(response, request, chk_result, chk_ck, chk_code, 3,"");
				logger.debug("sadbn[168] resultvalue="+ resultvalue);
				
				String newVal=manageCookie.getCookieInfo(request,"shop_log");
				newVal=java.net.URLDecoder.decode(newVal,"utf-8");
				
				if( resultvalue.equals("next") ){
					
					ShopData slog=(ShopData)appContext.getBean("ShopData");
					slog.setGB("02");
					slog.setIP(keyIp);
					slog.setNO(Long.parseLong(no));
					slog.setSvc_type(svc_type);
					slog.setSITE_CODE(site_code);
					slog.setPCODE(pcode);
					logger.debug(slog.getInfo("sadbn after charge="));
					dumpObj.getDelShopLogData().add(slog);
					
					newVal=manageCookie.shopLogManage(1, newVal,slog,"edit");
				}
				
				if( resultvalue.equals("next") && !mcgb.equals("C") ){
					
					AdConfigData scfg= RFServlet.instance.adInfoCache.getShopAdConfig(us,s,site_code +"_sky",adgubun);
					
					String shopLog=manageCookie.getCookieInfo(request,"shop_log");
					shopLog=StringUtils.getURLDecodeStr(shopLog,"UTF-8");
					HashMap<String,ShopData> shhm=RFServlet.instance.adInfoCache.getShopLogData(shopLog,keyIp,"sky");
					
					List<String> it = new ArrayList<String>(shhm.keySet());
					Collections.sort(it);
					
				    for(String me:it){
				    	ShopData shop_log1= shhm.get(me);
				    	logger.debug("ssetcharge 21 "+shop_log1);
				    	if( shop_log1.getPCODE().equals(pcode) ){
				    		scfg.setFlag(shop_log1.getFlag());
				    		scfg.setMcgb(shop_log1.getMCGB());
				    		break;
				    	}
				    }
				    
					if( scfg!=null && scfg.getFlag()!=null && scfg.getFlag().equals("1") && mcgb.equals("") ){
						logger.debug("sadbn setcharge scfg="+scfg);
						AdConfigData tmpFg=RFServlet.instance.adInfoCache.getRecomAdConfig(scfg.getUserid(),us,s,"iadlink","sky");
						
						if(tmpFg!=null && (scfg.getMcgb()!=null && scfg.getMcgb().equals("") || !scfg.getMcgb().equals("C")) ){
							
							logger.debug("ssetcharge "+tmpFg );
							LinkedHashMap ldList= RFServlet.instance.adInfoCache.getShopStatsData(scfg.getUserid(),"");
							if(ldList!=null && ldList.size()>0){
								//ArrayList sdlist=new ArrayList(ldList.values());
								List<String> sdlist = new ArrayList<String>(ldList.keySet());
								ArrayList<Integer> rndList = StringUtils.getRandList( sdlist.size() );
								ArrayList addsList= new ArrayList();
							
								for( int i=0; i<rndList.size() && i<2; i++){
									
									ShopData adds = new ShopData();
									//ShopData ss = (ShopData) sdlist.get( rndList.get(i) );
									ShopData ss =RFServlet.instance.adInfoCache.getShopPCodeData(scfg.getUserid(),sdlist.get(rndList.get(i)).toString());
									
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
									adds.setMCGB("C");
									adds.setRF("");
									adds.setGB("02");
									
									dumpObj.getShopLogData().add(adds);
									addsList.add(adds);
								}
								newVal=manageCookie.shopLogManage(newVal,addsList,"append");
								logger.debug("newVal="+newVal);
							}
						}
					}
					
				}

				
				newVal=java.net.URLEncoder.encode(newVal,"utf-8");
				logger.debug("newVal="+newVal);
				manageCookie.makeCookie("shop_log",newVal,cookieDay*24*60*60, response);
				//AdConfigData acf= RFServlet.instance.adInfoCache.getShopAdConfig(us,s,site_code +"_","SR");
				
			}else if(adgubun.equals("UM")){
				
				int id= Integer.parseInt(tid);

				String sel_ic_um_tgt= StringUtils.gAt1(ic_um_tgt, id, "|||");
				logger.debug("sadbn ic_um_tgt="+ ic_um_tgt +", sel_ic_um_tgt="+ sel_ic_um_tgt );
				if( sel_ic_um_tgt.indexOf("C") >=0 ){
				}else{
					
				}
			}else if(adgubun.equals("KL")){

				int id= Integer.parseInt(tid);

				String sel_ic_ki_tgt= StringUtils.gAt1(ic_ki_tgt, id, "|||");
				if( sel_ic_ki_tgt.indexOf("C") >=0 ){
				}else{
					
				}
				
			}
			out.print("ok");

		} catch (Exception e) {
			logger.error("SetCharge:"+e+",debug="+debug); 
		}
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response){
		doGet(request,response);
	}
	public void init() throws ServletException {
		logger.info("SsetChargeServlet started");
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