package com.adgather.servlet;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import com.adgather.beans.ManagementCookie;
import com.adgather.reportmodel.DrcData;
import com.adgather.reportmodel.DumpObject;
import com.adgather.reportmodel.PointData;
import com.adgather.reportmodel.ShopData;
import com.adgather.util.StringUtils;

public class Drc extends HttpServlet {
	private DumpObject dumpObj;
	private ApplicationContext appContext;
	public ManagementCookie manageCookie;
	static Logger logger = Logger.getLogger(Drc.class);
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
			String mcgb=request.getParameter("mcgb")==null ? "" : request.getParameter("mcgb");
			String gb=request.getParameter("gb")==null ? "" : request.getParameter("gb");
			String sc=request.getParameter("sc")==null ? "" : request.getParameter("sc");
			String s=request.getParameter("s")==null ? "" : request.getParameter("s");
			String mc=request.getParameter("mc")==null ? "" : request.getParameter("mc");
			String no=request.getParameter("no")==null ? "" : request.getParameter("no");
			String kno=request.getParameter("kno")==null ? "" : request.getParameter("kno");
			String product=request.getParameter("product")==null ? "" : request.getParameter("product");
			String slink=request.getParameter("slink")==null ? "" : request.getParameter("slink");
			String clk_param=request.getParameter("clk_param")==null ? "" : request.getParameter("clk_param");
			
			
			if( no.equals("") ){
				no="0";
			}
			if( kno.equals("") ){
				kno="0";
			}
			
			if( keyIp.indexOf("5.10.83")>-1 && ( u.equals("") || gb.equals("") || sc.equals("") || s.equals("") || mc.equals("") 
					|| product.equals("") || slink.equals("")) ){
				logger.error("drc param err keyIp="+keyIp+", u="+u+", mcgb="+mcgb+", gb="+gb+", sc="+sc+", s="+s+", mc="+mc+", no="+no+", kno="+kno+", product="+product+", slink="+slink+", ");
				return;
			}
			
			PointData pd= RFServlet.instance.adInfoCache.getAdCacheData(u);
			int cookie_dirsales = 24;
			if(pd!=null){
				logger.debug(pd.getInfo("drc[61] "));
				cookie_dirsales=pd.getCookie_dirsales();
			}
			
			SimpleDateFormat yyyymmddHHmm = new SimpleDateFormat("yyyyMMddHHmm");
			SimpleDateFormat yyyymmdd = new SimpleDateFormat("yyyyMMdd");
			SimpleDateFormat yyyymm = new SimpleDateFormat("yyyyMM");
			SimpleDateFormat kk = new SimpleDateFormat("kk");
			java.util.Date date=new java.util.Date();
			String yyyymdhm=yyyymmddHHmm.format(date);
			String ymd=yyyymmdd.format(date);
			String ym=yyyymm.format(date);
			String kh=kk.format(date);
			
			DrcData drcData=(DrcData)appContext.getBean("DrcData");
			drcData.setKeyIp(keyIp);
			drcData.setU(u);
			drcData.setGb(gb);
			drcData.setSc(sc);
			drcData.setActDate(Calendar.getInstance().getTimeInMillis());
			drcData.setS(s);
			drcData.setMc(mc);
			drcData.setNo(no);
			drcData.setKno(kno);
			drcData.setMcgb(mcgb);
			drcData.setProduct(product);
			drcData.setpCode(pCode);
			dumpObj.getDrcData().add(drcData);
			
			String newVal=manageCookie.getCookieInfo(request,"shop_log");
			newVal=java.net.URLDecoder.decode(newVal,"utf-8");
			
			if( gb.equals("SR") ){
				ShopData slog=(ShopData)appContext.getBean("ShopData");
				slog.setGB(gb);
				slog.setIP(keyIp);
				slog.setNO(Long.parseLong(no));
				slog.setSvc_type("normal");
				slog.setSITE_CODE(sc);
				slog.setPCODE(pCode);
				logger.debug(slog.getInfo("drc[110] slog "));
				dumpObj.getDelShopLogData().add(slog);
				//manageCookie.shopLogReSet(u,slog,request, response);
				newVal=manageCookie.shopLogManage(1, newVal,slog,"edit");
			}
			manageCookie.makeCookie("push", "1", 60*6, response);
			manageCookie.makeCookie("dsck",mc+"_"+sc+"_"+gb+"_"+product,(60*60*24* cookie_dirsales), response);
			String site_codedirect=manageCookie.getCookieInfo(request,"site_code");
			if( !site_codedirect.equals("")) {
				manageCookie.makeCookie("site_code", mc+"_"+sc+"_"+gb+"_"+product, -1, response);
			}
			newVal=java.net.URLEncoder.encode(newVal,"utf-8");
			manageCookie.makeCookie("shop_log",newVal,60*24*60*60, response);
			
			/*
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
			*/
			
			String rurl = StringUtils.getURLDecodeStrR(slink,"UTF-8",5);
			
			logger.debug("drc[115] slink = "+ rurl);
			
			html.append("<script> \n");
			html.append("	function go(){ \n");
			html.append("		location.replace(\""+rurl+"\"); \n");
			html.append("	} \n");
			html.append("</script> \n");

			if( !clk_param.equals("") ){
				html.append("<script> \n");
				html.append("	var clk_param=\""+clk_param+"\";\n");
				html.append("	document.write(\"<scr\"+\"ipt src='\"+clk_param+\"' onload='go()'></scr\"+\"ipt>\"); \n");
				html.append("</script> \n");
				
			}else{
				html.append("<script>go();</script> \n");
				response.sendRedirect( rurl );
			}
			
			logger.debug("drc[162] "+html.toString());
			
			out.print(html.toString());
			
			//response.sendRedirect( rurl );
			
		} catch (Exception e) {
			System.out.println(e);
			logger.error("Drc1:"+e+",debug="+debug); 
		}
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response){
		doGet(request,response);
	}
	public void init() throws ServletException {
		logger.info("DrcServlet started");
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