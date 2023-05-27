package com.adgather.servlet;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import com.adgather.beans.ManagementCookie;
import com.adgather.reportmodel.DumpObject;
import com.adgather.reportmodel.PointData;
import com.adgather.util.StringUtils;

public class Idrc extends HttpServlet {
	private DumpObject dumpObj;
	private ApplicationContext appContext;
	public ManagementCookie manageCookie;
	static Logger logger = Logger.getLogger(Idrc.class);
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
			String pop=manageCookie.getCookieInfo(request,"pop");
			String rurl="";
			String tlink=request.getParameter("tlink")==null ? "" : request.getParameter("tlink");
			String slink=request.getParameter("slink")==null ? "" : request.getParameter("slink");

			String u=request.getParameter("u")==null ? "" : request.getParameter("u");
			String gb=request.getParameter("gb")==null ? "" : request.getParameter("gb");
			String sc=request.getParameter("sc")==null ? "" : request.getParameter("sc");
			String s=request.getParameter("s")==null ? "" : request.getParameter("s");
			String mcgb=request.getParameter("mcgb")==null ? "" : request.getParameter("mcgb");
			String mc=request.getParameter("mc")==null ? "" : request.getParameter("mc");
			String fq=request.getParameter("fq")==null ? "" : request.getParameter("fq");
			String clk_param=request.getParameter("clk_param")==null ? "" : request.getParameter("clk_param");
			
			PointData pd= RFServlet.instance.adInfoCache.getAdCacheData(u);
			int cookie_dirsales = 24;
			if(pd!=null){
				logger.debug(pd.getInfo("idrc[61 "));
				cookie_dirsales=pd.getCookie_dirsales();
			}

			if( !clk_param.equals("") ){
				
				URL url =null;
				HttpURLConnection connection =null;
				InputStream input=null;	
				BufferedReader reader=null;
				String line="";
				try{
					url = new URL(clk_param);
				    HttpURLConnection connect = (HttpURLConnection)url.openConnection();
				    connect.connect();
				    
				    Object content = connect.getContent();
				    input = (InputStream)content;
				    reader = new BufferedReader(new InputStreamReader(input));
				    
					StringBuffer fullBuf =new StringBuffer();
					while((line = reader.readLine()) != null){
						fullBuf.append(line+"\r\n");
					}
					
					logger.debug("clk_param con succ "+fullBuf.toString());
					
				}catch(Exception e){
					logger.error("err "+e);
				}finally{
					try{	connection.disconnect();	}catch(Exception e){}
				}
			}
			if(tlink==null || tlink.length()==0){
				//rurl=StringUtils.getURLDecodeStr(slink,"UTF-8");
				
				if( slink.indexOf("%")>-1 ){
					rurl = slink;
				}else{
					rurl = StringUtils.getURLDecodeStr(slink,"UTF-8");
				}
				
				int fq1 = 300;
				
				if( !fq.equals("") ){
					try{
						fq1 = Integer.parseInt(fq);
					}catch(Exception e){
						fq1 = 300;
					}
				}
				
				if( request.getRemoteAddr().indexOf("211.36.33.")>=0 || request.getRemoteAddr().indexOf("123.142.220.")>=0
						|| request.getRemoteAddr().indexOf("114.199.147.")>=0 || request.getRemoteAddr().indexOf("112.220.97.")>=0 ){
					manageCookie.makeCookie("send_chk","1",1, response);
					manageCookie.makeCookie("send_chk2","1",1, response);
				}else{
					manageCookie.makeCookie("send_chk","1",fq1, response);		// 60 * 5
					manageCookie.makeCookie("send_chk2","1",21600, response);
				}
			}else{
				pop="1";
			}
			
			if(pop==null ||  pop.length()==0){
				html.append(" <script language=\"javascript\"> \n");
				//html.append(" opener.focus(); opener.window.focus(); self.blur();  \n");
				html.append(" setTimeout(function(){  \n");
				html.append(" 	window.location = \""+rurl+"\";  \n");
				html.append(" },300);  \n");
				html.append(" </script> \n");
				//manageCookie.makeCookie("dsck",mc+"_"+sc+"_"+gb+"_ico",(60*24*31), response);
				
				
				manageCookie.makeCookie("dsck",mc+"_"+sc+"_"+gb+"_ico",(60*60*24* cookie_dirsales ), response);
				String site_codedirect=manageCookie.getCookieInfo(request,"site_code");
				if( !site_codedirect.equals("")) {
					manageCookie.makeCookie("site_code", mc+"_"+sc+"_"+gb+"_ico", -1, response);
				}
				
				//response.sendRedirect( rurl);
			}else{
				html.append(" <script type=\"text/javascript\">                          \n");
				html.append(" //<![CDATA[                                              \n");
				html.append(" var url = \""+tlink+"\";                                 \n");
				html.append(" var chk = \""+slink+"\";                                 \n");
				html.append(" if(chk){                                                 \n");
				html.append(" 	window.onload = function() {                           \n");
				html.append(" 		document.gogo.method = \"post\";                     \n");
				html.append(" 		document.gogo.action = \"/servlet/idrc\";                 \n");
				html.append(" 		document.gogo.submit();                            \n");
				html.append(" 	}                                                      \n");
				html.append(" } else {                                                 \n");
				html.append(" 	window.onload = function() {                           \n");
				html.append(" 		document.gogo.action = url;                        \n");
				html.append(" 		document.gogo.submit();                            \n");
				html.append(" 	}                                                      \n");
				html.append(" }                                                        \n");
				html.append(" //]]>                                                    \n");
				html.append(" </script>                                                \n");
				html.append(" <form name=\"gogo\" method=\"post\">                         \n");
				html.append(" <input type=\"hidden\" name=\"tlink\" value=\""+slink+"\" /> \n");
				html.append(" </form>                                                  \n");
			}
			out.print(html.toString());
			
		} catch (Exception e) {
			System.out.println(e);
			logger.error("Idrc:"+e+",debug="+debug); 
		}
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response){
		doGet(request,response);
	}
	public void init() throws ServletException {
		logger.info("IdrcServlet started");
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