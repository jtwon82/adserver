package com.adgather.servlet;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
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
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.context.ApplicationContext;

import com.adgather.beans.ManagementCookie;
import com.adgather.reportmodel.AdChargeData;
import com.adgather.reportmodel.AdData;
import com.adgather.reportmodel.DrcData;
import com.adgather.reportmodel.DumpObject;
import com.adgather.reportmodel.MediaScriptData;
import com.adgather.reportmodel.PointData;
import com.adgather.util.NetworkUtils;
import com.adgather.util.StringUtils;

public class DrcNew extends HttpServlet {
	private DumpObject dumpObj;
	private ApplicationContext appContext;
	public ManagementCookie manageCookie;
	static Logger logger = Logger.getLogger(DrcNew.class);
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		String debug="";
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
			String no=request.getParameter("no")==null ? "" : request.getParameter("no");	if( no.equals("") )no="0";
			String kno=request.getParameter("kno")==null ? "" : request.getParameter("kno");if( kno.equals("") )kno="0";
			String product=request.getParameter("product")==null ? "" : request.getParameter("product");
			String slink=request.getParameter("slink")==null ? "" : request.getParameter("slink");
			
			String type=request.getParameter("type")==null ? "" : request.getParameter("type");
			String chk_redirect=request.getParameter("chk_redirect")==null ? "" : request.getParameter("chk_redirect");
			String clk_param=request.getParameter("clk_param")==null ? "" : request.getParameter("clk_param");
			String curl=request.getParameter("curl")==null ? "" : request.getParameter("curl");
			
			if( keyIp.indexOf("5.10.83")>-1 || ( u.equals("") || gb.equals("") || sc.equals("") || s.equals("") || mc.equals("") 
					|| product.equals("") ) ){
				logger.error("drcnew param err keyIp="+keyIp+", u="+u+", mcgb="+mcgb+", gb="+gb+", sc="+sc+", s="+s+", mc="+mc+", no="+no+", kno="+kno+", product="+product+", slink="+slink+", ");
				return;
			}
			debug="1";
			
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
			
			MediaScriptData ms=RFServlet.instance.adInfoCache.getMediaScriptData(s);
			
			if( ms==null) return;

			String rurl = "";
			
			debug="2";
			if( product.equals("nor") ){

				Document doc= (Document) RFServlet.instance.xml.get("adbn_partner");
				int tmp_point=0;
				String _point= doc.select("partner config point "+u).html();
				if(!_point.equals("")){
					try{
						tmp_point= Integer.parseInt(_point);
					}catch(Exception e){}
				}
				
				debug="3";
				if( type.equals("V") ){
					try{
						AdChargeData crd=(AdChargeData)appContext.getBean("AdChargeData");
						crd.setSITE_CODE(sc);
						crd.setADGUBUN(gb);
						crd.setTYPE(type);
						crd.setYYYYMMDD(ymd);
						crd.setHHMM(kh);
						crd.setPC("");
						crd.setUSERID(u);
						crd.setPOINT( "0" );
						crd.setPPOINT("0");
						crd.setYM(ym);
						crd.setNO("0");
						crd.setPCODE("");
						crd.setS(s);
						crd.setSCRIPTUSERID(ms.getUSERID());
						crd.setIP(keyIp);
						crd.setKNO("");
						crd.setSRView2(0);
						crd.setMCGB("");
						dumpObj.getNormalChargeLogData().add(crd);
					}catch(Exception e){
						logger.error("drcNew[152] err "+e);
					}
					return;
				}else{
					try{
						DrcData drcData=(DrcData)appContext.getBean("DrcData");
						drcData.setKeyIp(keyIp);
						drcData.setU(u);
						drcData.setGb(gb);
						drcData.setSc(sc);
						drcData.setS(s);
						drcData.setMc(mc);
						drcData.setNo(no);
						drcData.setKno(kno);
						drcData.setMcgb(mcgb);
						drcData.setProduct(product);
						drcData.setpCode(pCode);
						drcData.setPoint( tmp_point );
						dumpObj.getDrcData().add(drcData);
						PointData pd= RFServlet.instance.adInfoCache.getAdCacheData(u);
						int cookie_dirsales = 24;
						if(pd!=null){
							cookie_dirsales=pd.getCookie_dirsales();
						}
						
						manageCookie.makeCookie("push", "1", 60*6, response);
						manageCookie.makeCookie("dsck",mc+"_"+sc+"_"+gb+"_"+product,(60*60*24* cookie_dirsales), response);
						String site_codedirect=manageCookie.getCookieInfo(request,"site_code");
						if( !site_codedirect.equals("")) {
							manageCookie.makeCookie("site_code", mc+"_"+sc+"_"+gb+"_"+product, -1, response);
						}
					}catch(Exception e){
						logger.error("drcNew[185] err "+e);
					}
					
				}
				
				debug="4";

				if( chk_redirect.equals("x") ){
					
				}else{
					rurl= StringUtils.getURLDecodeStrR(slink,"UTF-8",5);
					if( !curl.equals("") ){
						rurl=curl;
					}
					if( rurl.indexOf("?")>-1 ){
						rurl+="&ip="+ NetworkUtils.getClientIP(request);
					}
					response.sendRedirect( rurl );
				}
				
			}else if( product.equals("ico") ){
				
				debug="5";
				Document doc =null;
				try{
					doc = Jsoup.parse(new File("/home/dreamsearch/public_html/adnet/iadbn_ebay.xml"), "UTF-8", "http://www.dreamsearch.or.kr/");
				}catch(Exception e){
					doc= Jsoup.parse(new File("D:/work/enliple/www/webapp/public_html/adnet/iadbn_ebay.xml"), "UTF-8", "http://www.dreamsearch.or.kr/");
				}
				Elements ad= doc.select("site ad");
				
				String tmp_sitecode="";
				int tmp_point=0;
				//AdData adata = new AdData();
				for(int i=0; i<ad.size(); i++){
					String tmpA= ad.get(i).select("sitecode").text();
					if( tmpA.equals(sc) ){
						tmp_sitecode = ad.get(i).select("sitecode").text();
						try{
							tmp_point = Integer.parseInt(ad.get(i).select("point").text());
						}catch(Exception e){
							tmp_point=6;
							logger.error("drcNew[215] err "+e);
						}
						break;
					}
				}
				debug="6";
				AdChargeData crd=(AdChargeData)appContext.getBean("AdChargeData");
				crd.setSITE_CODE(sc);
				crd.setADGUBUN(gb);
				crd.setYYYYMMDD(ymd);
				crd.setHHMM(kh);
				crd.setPC("");
				crd.setUSERID(u);
				crd.setPOINT( tmp_point+"");
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
				crd.setPCODE("");
				dumpObj.getChargeLogData().add(crd);
				
				debug="7";
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
			}
			
			
			
			
			
		} catch (Exception e) {
			logger.error("DrcNew:"+e+",debug="+debug); 
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