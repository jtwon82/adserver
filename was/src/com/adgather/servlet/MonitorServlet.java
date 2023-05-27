package com.adgather.servlet;

import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

public class MonitorServlet extends HttpServlet{
	static Logger logger = Logger.getLogger(MonitorServlet.class);
	private ApplicationContext appContext;
	private static long requestCounter;
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		PrintWriter out=null;
		try{
			out = response.getWriter();
		}catch(Exception e){}

		String mode=request.getParameter("mode")==null ? "chk" : request.getParameter("mode");
		String cname=request.getParameter("cname")==null ? "cname" : request.getParameter("cname");
		String keyIp=RFServlet.instance.manageCookie.makeKeyCookie(request, response);
		
		String htmlStr="";
		
		try{
			request.setCharacterEncoding("8859_1");
			response.setContentType("text/html;charset=euc-kr");
			
			if(mode.equals("cacheInfo")){
				htmlStr=htmlStr=RFServlet.instance.adInfoCache.getCacheInfo();
			}
			out.print(htmlStr);
		}catch(Exception e){
			logger.error("MonitorServlet:"+e);
		}finally{
		}
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response){
		doGet(request,response);
	}
	public void init() throws ServletException {
		logger.info("MonitorServlet started");
	}
	public void destroy(){
		
	}
}

