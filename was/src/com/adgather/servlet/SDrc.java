package com.adgather.servlet;


import java.io.PrintWriter;
import java.text.SimpleDateFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import com.adgather.beans.ManagementCookie;
import com.adgather.reportmodel.AdChargeData;
import com.adgather.reportmodel.DumpObject;
import com.adgather.reportmodel.PointData;
import com.adgather.reportmodel.ShopData;
import com.adgather.util.StringUtils;

public class SDrc extends HttpServlet {
	private DumpObject dumpObj;
	private ApplicationContext appContext;
	public ManagementCookie manageCookie;
	static Logger logger = Logger.getLogger(SDrc.class);
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		int debug=0;
		try {
			PrintWriter out=response.getWriter();
			StringBuffer html=new StringBuffer();
			request.setCharacterEncoding("8859_1");
			response.setContentType("text/html");
			response.setHeader("Pragma", "no-cache");			
			response.setHeader("Cache-Control", "no-cache"); 
			//response.setHeader("P3P","CP='IDC DSP COR ADM DEVi TAIi PSA PSD IVAi IVDi CONi HIS OUR IND CNT'");
			response.setHeader("P3P","CP='CAO PSA CONi OTR OUR DEM ONL'");
			String keyIp=manageCookie.getCookieInfo(request,"IP_info");
			if(keyIp==null || keyIp.equals("")){
				keyIp=manageCookie.makeKeyCookie(request, response);
			}
			String u=request.getParameter("u")==null ? "" : request.getParameter("u");
			String pCode=request.getParameter("pCode")==null ? "" : request.getParameter("pCode");
			String gb=request.getParameter("gb")==null ? "" : request.getParameter("gb");
			String sc=request.getParameter("sc")==null ? "" : request.getParameter("sc");
			String s=request.getParameter("s")==null ? "" : request.getParameter("s");
			String mc=request.getParameter("mc")==null ? "" : request.getParameter("mc");
			String mcgb=request.getParameter("mcgb")==null ? "" : request.getParameter("mcgb");
			String kno=request.getParameter("kno")==null ? "" : request.getParameter("kno");
			String slink=request.getParameter("slink")==null ? "" : request.getParameter("slink");
			

			SimpleDateFormat yyyymmdd = new SimpleDateFormat("yyyyMMdd");
			SimpleDateFormat yyyymm = new SimpleDateFormat("yyyyMM");
			SimpleDateFormat kk = new SimpleDateFormat("kk");
			java.util.Date date=new java.util.Date();
			String ymd=yyyymmdd.format(date);
			String ym=yyyymm.format(date);
			String kh=kk.format(date);

			PointData pd= RFServlet.instance.adInfoCache.getAdCacheData(u);
			int cookie_dirsales = 24;
			if(pd!=null){
				logger.debug(pd.getInfo("sdrc[61 "));
				cookie_dirsales=pd.getCookie_dirsales();
			}
			
			AdChargeData crd= (AdChargeData)appContext.getBean("AdChargeData");
			crd.setSITE_CODE(sc);
			crd.setADGUBUN(gb);
			crd.setTYPE("C");
			crd.setYYYYMMDD(ymd);
			crd.setHHMM(kh);
			crd.setPC("");
			crd.setKNO(kno);		// 클릭일경우 shop_log의 no값이 들어오는곳. 첫번째슬록의 상품이 아닌경우는 노출되지 않은 shop_log의 no값이 들어온다. 하지만 노출은 srlink에 따로 저장하지 않아서. 현재로서는 상관없음.
			crd.setS(s);
			crd.setPCODE(pCode);
			crd.setSCRIPTUSERID("");
			crd.setUSERID(u);
			crd.setPOINT("0");
			crd.setPPOINT("0");
			crd.setYM(ym);
			crd.setNO("");
			crd.setIP(keyIp);
			crd.setPRODUCT("sky");
			crd.setMCGB(mcgb);
			dumpObj.getSkyChargeData().add(crd);
			logger.debug(crd.getInfo("Sadbn click info="));
			
			String newVal=manageCookie.getCookieInfo(request,"shop_log");
			newVal=java.net.URLDecoder.decode(newVal,"utf-8");

			if( gb.equals("SR") ){
				ShopData slog=(ShopData)appContext.getBean("ShopData");
				slog.setGB(gb);
				slog.setIP(keyIp);
				slog.setNO(Long.parseLong(kno));
				slog.setSvc_type("sky");
				slog.setSITE_CODE(sc);
				slog.setPCODE(pCode);
				dumpObj.getDelShopLogData().add(slog);
				//manageCookie.shopLogReSet(u,slog,request, response);
				newVal=manageCookie.shopLogManage(1, newVal,slog,"edit");
			}
			//manageCookie.makeCookie("dsck",mc+"_"+sc+"_"+gb+"_sky",(60*24*31), response);

			newVal=java.net.URLEncoder.encode(newVal,"utf-8");
			manageCookie.makeCookie("shop_log",newVal,60*24*60*60, response);
			
			manageCookie.makeCookie("dsck",mc+"_"+sc+"_"+gb+"_sky",(60*60*24*cookie_dirsales), response);
			String site_codedirect=manageCookie.getCookieInfo(request,"site_code");
			if( !site_codedirect.equals("")) {
				manageCookie.makeCookie("site_code", mc+"_"+sc+"_"+gb+"_sky", -1, response);
			}
			
			String rurl=""; //StringUtils.getURLDecodeStr(slink,"UTF-8");
			
			if( slink.indexOf("%")>-1 ){
				rurl = slink;
			}else{
				rurl = StringUtils.getURLDecodeStr(slink,"UTF-8");
			}
			
			//html.append(" <script> \n");
			//html.append(" 	location.href=\""+rurl+"\"; \n");
			//html.append(" </script> \n");
			 
			//out.print(html.toString());
			
			response.sendRedirect( rurl);
			
		} catch (Exception e) {
			System.out.println(e);
			logger.error("sDrc:"+e+",debug="+debug); 
		}
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response){
		doGet(request,response);
	}
	public void init() throws ServletException {
		logger.info("SDrcServlet started");
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