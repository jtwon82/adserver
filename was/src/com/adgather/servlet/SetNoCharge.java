package com.adgather.servlet;


import java.text.SimpleDateFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import com.adgather.beans.ManagementCookie;
import com.adgather.reportmodel.DumpObject;
import com.adgather.reportmodel.MediaLogData;

public class SetNoCharge extends HttpServlet {
	private DumpObject dumpObj;
	private ApplicationContext appContext;
	public ManagementCookie manageCookie;
	static Logger logger = Logger.getLogger(SetNoCharge.class);
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		int debug=0;
		try {
			request.setCharacterEncoding("8859_1");
			response.setHeader("Pragma", "no-cache");			
			response.setHeader("Cache-Control", "no-cache"); 
			//response.setHeader("P3P","CP='IDC DSP COR ADM DEVi TAIi PSA PSD IVAi IVDi CONi HIS OUR IND CNT'");
			response.setHeader("P3P","CP='CAO PSA CONi OTR OUR DEM ONL'");
			debug++;
			String keyIp=manageCookie.makeKeyCookie(request, response);
			String s=request.getParameter("s")==null ? "" : request.getParameter("s");
			if(s.equals(""))	return;
			SimpleDateFormat yyyymmdd = new SimpleDateFormat("yyyyMMdd");
			java.util.Date date=new java.util.Date();
			String ymd=yyyymmdd.format(date);
			MediaLogData mLog5=(MediaLogData)appContext.getBean("MediaLogData");
			mLog5.setS(s);
			mLog5.setIP(keyIp);
			mLog5.setSDATE(ymd);
			mLog5.setVIEWCNT(1);
			mLog5.setLogId(5);
			
			logger.debug("getS="+mLog5.getS());
			logger.debug("getIP="+mLog5.getIP());
			logger.debug("getSDATE="+mLog5.getSDATE());
			logger.debug("getVIEWCNT="+mLog5.getVIEWCNT());
			
			dumpObj.getMediaLogData().add(mLog5);
		} catch (Exception e) {
			System.out.println(e);
			logger.error(e+",debug="+debug); 
		}
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response){
		doGet(request,response);
	}
	public void init() throws ServletException {
		logger.info("SetNoChargeServlet started");
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