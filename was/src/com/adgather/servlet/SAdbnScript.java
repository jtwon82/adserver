package com.adgather.servlet;


import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import com.adgather.beans.ManagementCookie;
import com.adgather.reportmodel.DumpObject;
import com.adgather.util.FileUtil;

public class SAdbnScript extends HttpServlet {
	private DumpObject dumpObj;
	private ApplicationContext appContext;
	public ManagementCookie manageCookie;
	private RFServlet servlet;
	static Logger logger = Logger.getLogger(SAdbnScript.class);
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		int debug=0;
		try {
			PrintWriter out=response.getWriter();
			request.setCharacterEncoding("8859_1");
			response.setContentType("text/html");
			response.setHeader("Pragma", "no-cache");			
			response.setHeader("Cache-Control", "no-cache"); 
			//response.setHeader("P3P","CP='IDC DSP COR ADM DEVi TAIi PSA PSD IVAi IVDi CONi HIS OUR IND CNT'");
			response.setHeader("P3P","CP='CAO PSA CONi OTR OUR DEM ONL'");
			
			String us=request.getParameter("us")==null ? "" : request.getParameter("us");
			String s=request.getParameter("s")==null ? "" : request.getParameter("s");
			String from=request.getParameter("from")==null ? "" : request.getParameter("from");
			
			String txt_file= (String) RFServlet.instance.xml.get("skyScript_js");
//			txt_file= FileUtil.readFile("/home/dreamsearch/public_html/ad/skyScript.js");
//			if( txt_file.equals("") ){
//				txt_file= FileUtil.readFile("d:/work/enliple/www/webapp/public_html/ad/skyScript.js");
//			}
			
			out.println("		var param = 'from='+escape(escape('"+from+"'))+'&us="+us+"&s="+s+"&chk=1';");	
			out.println("		var imgs = new Array();");	
			out.println( txt_file );
			
//			StringBuffer html= new StringBuffer();
//
//			html.append("		function rc_ad_display(posi){ \n");
//			html.append("			var rcAdStr=''; \n");
//			html.append("			try{ \n");
//			html.append("				//if(rcHead!='') 				rcAdStr+=rcHead; \n");
//			html.append("				if(rcBody!='') 				rcAdStr+=rcBody; \n");
//			html.append("				//if(rcFoot) 				rcAdStr+=rcFoot; \n");
//			html.append("				if(rcAdStr!=''){ \n");
//			html.append("					oldscrollHeight= document.compatMode == 'CSS1Compat' ? document.documentElement.scrollHeight : document.body.scrollHeight; \n");
//			html.append("					document.getElementById(posi).innerHTML=rcAdStr; \n");
//			html.append("					scrollBottom(); \n");
//			html.append("				} \n");
//			html.append("			}catch(e){			} \n");
//			html.append("		} \n");
//			html.append("		function loadJS(url,posi,charset){ \n");
//			html.append("			var hd=document.getElementsByTagName('head')[0]; \n");
//			html.append("			var sc=document.createElement('script'); \n");
//			html.append("			sc.type='text/javascript'; \n");
//			html.append("			if(charset!=null){sc.charset='euc-kr';} \n");
//			html.append("			var ld=false; \n");
//			html.append("			sc.onreadystatechange=function(){ \n");
//			html.append("				try{ \n");
//			html.append("					if(this.readyState=='loaded'||this.readyState=='complete'){ \n");
//			html.append("						if(ld)return; \n");
//			html.append("						ld=true; \n");
//			html.append("						rc_ad_display(posi); \n");
//			html.append("					} \n");
//			html.append("				}catch(e){} \n");
//			html.append("			}; \n");
//			html.append("			sc.onload=function(){ \n");
//			html.append("				rc_ad_display(posi); \n");
//			html.append("			}; \n");
//			html.append("			sc.src=url; \n");
//			html.append("			hd.appendChild(sc); \n");
//			html.append("		} \n");
//			html.append("		function load_rc_ad(sevn,objf,posi){ \n");
//			html.append("			var adSrc= objf; \n");
//			html.append("			loadJS(adSrc,posi,'euc-kr'); \n");
//			html.append("		} \n");
//			html.append("		function addEvent(obj, type, listener) { \n");
//			html.append("			if (window.addEventListener) obj.addEventListener(type, listener, false); \n");
//			html.append("			else obj.attachEvent('on'+type, listener); \n");
//			html.append("		} \n");
//			html.append("		function scrollBottom() { \n");
//			html.append("			var clientHeight = document.compatMode == 'CSS1Compat' ? document.documentElement.clientHeight : document.body.clientHeight; \n");
//			html.append("			var scrollHeight = document.compatMode == 'CSS1Compat' ? document.documentElement.scrollHeight : document.body.scrollHeight; \n");
//			html.append("			var imgHeight= document.getElementById('widthPang').height; \n");
//			html.append("			document.getElementById('widthPang').width= '100%'; \n");
//			html.append("			if (imgHeight >= clientHeight) { \n");
//			html.append("				document.getElementById('skyBanner_view').style.paddingBottom = '0px'; \n");
//			html.append("				scrollTo(0, scrollHeight); \n");
//			html.append("			} \n");
//			html.append("			else { \n");
//			html.append("				var paddingSize = parseInt((clientHeight - imgHeight) / 2, 10); \n");
//			html.append("				document.getElementById('skyBanner_view').style.paddingBottom = paddingSize + 'px'; \n");
//			html.append("				scrollTo(0, scrollHeight); \n");
//			html.append("				//var tsh= scrollHeight-imgHeight; \n");
//			html.append("				//var tosh= oldscrollHeight-imgHeight; \n");
//			html.append("				//var aaa= setInterval(function(){ \n");
//			html.append("				//	if( tsh <= tosh ) { \n");
//			html.append("				//		clearInterval(aaa); \n");
//			html.append("				//	}else{ \n");
//			html.append("				//		tosh +=100; \n");
//			html.append("				//	} \n");
//			html.append("				//	scrollTo(0, tosh); \n");
//			html.append("				//},30); \n");
//			html.append("			} \n");
//			html.append("		} \n");
//			html.append("		function cElement(tname, tid){ \n");
//			html.append("			var a= document.createElement(tname); \n");
//			html.append("			a.id= tid; \n");
//			html.append("			return a; \n");
//			html.append("		} \n");
//			html.append("		var oldscrollHeight= 0; \n");
//			html.append("		var rcBody= ''; \n");			
//			html.append("		var open=false;  \n");
//			html.append("		var open=false;  \n");
//			html.append("		document.getElementsByTagName('body')[0].appendChild(cElement('span','skyBanner_view'));  \n");
//			html.append("		addEvent(window, 'scroll', function() {  \n");
//			html.append("			var scrollHeight = document.compatMode=='CSS1Compat'? document.documentElement.scrollHeight : document.body.scrollHeight;  \n");
//			html.append("			var clientHeight = document.compatMode=='CSS1Compat'? document.documentElement.clientHeight : document.body.clientHeight;  \n");
//			html.append("			var ScrollTop = document.compatMode == 'CSS1Compat'? document.documentElement.scrollTop : document.body.scrollTop;  \n");
//			html.append("			var scrollPos1 = parseInt(scrollHeight) - parseInt(ScrollTop);  \n");
//			html.append("			//alert('scrollHeight='+ scrollHeight +' clientHeight='+ clientHeight +' ScrollTop='+ ScrollTop +' scrollPos1='+ scrollPos1);  \n");
//			html.append("			if (clientHeight == scrollPos1){  \n");
//			html.append("				if(open=='0'){  \n");
//			html.append("					open=true;  \n");
//			html.append("					load_rc_ad('dev','http://www.dreamsearch.or.kr/servlet/sadbnData?from="+ from +"&us="+ us+"&s="+ s+"&chk=1','skyBanner_view');  \n");
//			html.append("				}  \n");
//			html.append("			}  \n");
//			html.append("		}); \n");
			
			//out.print(html.toString());
		} catch (Exception e) { 
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
}