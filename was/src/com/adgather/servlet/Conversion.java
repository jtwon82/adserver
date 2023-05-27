package com.adgather.servlet;

import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import com.adgather.beans.AdInfoCache;
import com.adgather.beans.DataMapper;
import com.adgather.beans.DumpDataToDB;
import com.adgather.beans.ManagementCookie;
import com.adgather.reportmodel.AdChargeData;
import com.adgather.reportmodel.DumpObject;
import com.adgather.util.DateUtils;
import com.adgather.util.StringUtils;

public class Conversion extends HttpServlet{
	private DumpObject dumpObj;
	public AdInfoCache adInfoCache;
	public DataMapper dataMapper;
	public ManagementCookie manageCookie;
	private DumpDataToDB dumpDb;
	public static RFServlet instance;
	public static ApplicationContext appContext;
	static Logger logger = Logger.getLogger(Conversion.class);
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		int debug=0;
		try {
			request.setCharacterEncoding("8859_1");
			response.setContentType("text/html;charset=euc-kr");
			response.setHeader("Pragma", "no-cache");			
			response.setHeader("Cache-Control", "no-cache"); 
			//response.setHeader("P3P","CP='IDC DSP COR ADM DEVi TAIi PSA PSD IVAi IVDi CONi HIS OUR IND CNT'");
			response.setHeader("P3P","CP='CAO PSA CONi OTR OUR DEM ONL'");
			PrintWriter out=response.getWriter();
			debug++;			
			String ordRFUrl=request.getHeader("Referer")==null ? "" :request.getHeader("Referer");
			String userId=request.getParameter("uid")==null ? "" : request.getParameter("uid");
			String ordCode=request.getParameter("ordcode")==null ? "" : request.getParameter("ordcode");
			String pCode=request.getParameter("pcode")==null ? "" : request.getParameter("pcode");
			String pNm=request.getParameter("pnm")==null ? "" : request.getParameter("pnm");
			String qty=request.getParameter("qty")==null ? "1" : request.getParameter("qty");
			String price=request.getParameter("price")==null ? "0" : request.getParameter("price");
			String pnm=request.getParameter("pnm")==null ? "0" : request.getParameter("pnm");
			
			String uname=request.getParameter("uname")==null ? "0" : request.getParameter("uname");
			String usex=request.getParameter("usex")==null ? "0" : request.getParameter("usex");
			String upno=request.getParameter("upno")==null ? "0" : request.getParameter("upno");
			
			String shop_log=manageCookie.getCookieInfo(request,"shop_log");
			
			
			if( ordCode.equals("") || userId.equals("") ){
				return;
			}
			
			if( pCode.equals("") ){
				if( shop_log.indexOf("_")>-1 ){
					pCode= StringUtils.gAt1(shop_log, 1, "_");
				}
				if( pCode.equals("") ){
					pCode="shop";
				}
			}

			if( uname.length()>-1 ){
				uname = java.net.URLDecoder.decode(uname,"utf-8");
			}
			if( pnm.length()>-1 ){
				pnm = java.net.URLDecoder.decode(pnm,"utf-8");
			}
			if(pnm.length()>200) pnm=pnm.substring(0,199);
			
			String dsck= manageCookie.getCookieInfo(request,"dsck");
			String site_codedirect=manageCookie.getCookieInfo(request,"site_code");
			
			if( dsck.equals("") && !site_codedirect.equals("") && site_codedirect.indexOf("_")>-1 ){
				dsck= site_codedirect;
				logger.debug("실시간이지만 24시간지났다.");
			}
			
			if(ordRFUrl.length()>200){
				ordRFUrl=ordRFUrl.substring(0,200);
			}
			String keyIp=manageCookie.makeKeyCookie(request, response);
			if(keyIp!=null && keyIp.length()>0){
				
				String shopcon=manageCookie.getCookieInfo(request, "shopcon");
				
				AdChargeData ard=(AdChargeData)appContext.getBean("AdChargeData");
				ard.setRegdate( new Timestamp(new Date().getTime()) );
				ard.setOrdRFUrl(ordRFUrl);
				ard.setUSERID(userId);
				ard.setOrdCode(ordCode);
				ard.setPCODE(pCode);
				ard.setPNm(pNm);
				try{int tmp=Integer.parseInt(qty);}catch(Exception e){qty="1";}
				try{int tmp=Integer.parseInt(price);}catch(Exception e){price="0";}
				ard.setOrdQty(qty);
				ard.setPrice(price);
				ard.setIP(keyIp);
				ard.setPNm(pnm);
				
				ard.setS(StringUtils.gAt1(dsck, 0, "_"));
				ard.setSITE_CODE(StringUtils.gAt1(dsck, 1, "_"));
				ard.setADGUBUN(StringUtils.gAt1(dsck, 2, "_"));
				ard.setTYPE(StringUtils.gAt1(dsck, 3, "_"));
				
				ard.setUname(uname);
				ard.setUsex(usex);
				ard.setUpno(upno);
				
				//shopcon
				ard.setShopcon_sdate( DateUtils.getDate("yyyyMMdd") );
				ard.setShopcon_sereal_no( StringUtils.gAt1(shopcon, 0, "_") );
				ard.setShopcon_weight( StringUtils.gAt1(shopcon, 1, "_") );
				
//				if( dsck.equals("") ){
//					ard.setDirect( "0" );
//				}else{
//					ard.setDirect( site_codedirect.equals("") ? "0":"1" );
//				}
				ard.setDirect( site_codedirect.equals("") ? "0":"1" );
				
				SimpleDateFormat yyyymmdd = new SimpleDateFormat("yyyyMMdd");
				java.util.Date date=new java.util.Date();
				String ymd=yyyymmdd.format(date);
				ard.setYYYYMMDD(ymd);
				
				dumpObj.getConvLogData().add(ard);
				
				logger.debug(ard.getInfo("conv ard "));
				
				//manageCookie.makeCookie("dsck","",0, response);
			}
			out.print("OK");
		} catch (Exception e) {
			System.out.println(e);
			logger.error("Conversion2:"+e+",debug="+debug); 
		}
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response){
		doGet(request,response);
	}
	public void init() throws ServletException {
		try{
			logger.info("ConversionServlet started");
			if (appContext == null) {
				RFServlet.instance.init();
				appContext=RFServlet.appContext;
			}
			manageCookie=(ManagementCookie)appContext.getBean("ManagementCookie");
			dumpObj=(DumpObject)appContext.getBean("DumpObject");
		}catch(Exception e){
			logger.error("RF init error : "+e);
		}
	}
	public void destroy(){
		try{
			dumpDb.dumpExecute(dumpObj,"file");
		}catch(Exception e){
			logger.error(e.getMessage());
		}
		
		System.out.println(" ConversionServlet destroy execute.");
	}
}
