package com.adgather.servlet;


import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import com.adgather.beans.ManagementCookie;
import com.adgather.reportmodel.DumpObject;
import com.adgather.util.FileUtil;
import com.adgather.util.StringUtils;

public class AdbnFloating extends HttpServlet {
	private DumpObject dumpObj;
	private ApplicationContext appContext;
	public ManagementCookie manageCookie;
	private RFServlet servlet;
	static Logger logger = Logger.getLogger(AdbnFloating.class);
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

			String from=request.getParameter("from")==null ? "" : request.getParameter("from");
			String us=request.getParameter("us")==null ? "" : request.getParameter("us");
			String u=request.getParameter("u")==null ? "" : request.getParameter("u");
			String pCode=request.getParameter("pCode")==null ? "" : request.getParameter("pCode");
			String s=request.getParameter("s")==null ? "" : request.getParameter("s");
			String igb=request.getParameter("igb")==null ? "" : request.getParameter("igb");
			String t=request.getParameter("t")==null ? "" : request.getParameter("t");
			String l=request.getParameter("l")==null ? "" : request.getParameter("l");
			String iwh=request.getParameter("iwh")==null ? "" : request.getParameter("iwh");
			String w=request.getParameter("w")==null ? "" : request.getParameter("w");
			String h=request.getParameter("h")==null ? "" : request.getParameter("h");
			String types=request.getParameter("types")==null ? "" : request.getParameter("types");
			String align=request.getParameter("align")==null ? "" : request.getParameter("align");
			String bntype=request.getParameter("bntype")==null ? "" : request.getParameter("bntype"); if( !bntype.equals("") )types="mbw";
			String cntad=request.getParameter("cntad")==null ? "" : request.getParameter("cntad");
			String cntsr=request.getParameter("cntsr")==null ? "" : request.getParameter("cntsr");
			String imgtype=request.getParameter("imgtype")==null ? "" : request.getParameter("imgtype");
			
			if( !iwh.equals("") ){
				w = (StringUtils.gAt1(iwh, 0, "_"));
				h = (StringUtils.gAt1(iwh, 1, "_"));
			}
			
			
			String txt_file= "";
			
			if( igb.equals("7") ){
//				txt_file= FileUtil.readFile("/home/dreamsearch/public_html/ad/adbn_edge.js");
//				if( txt_file.equals("") ){
//					txt_file= FileUtil.readFile("D:/work/enliple/www/webapp/public_html/ad/adbn_edge.js");
//				}
				txt_file=(String) RFServlet.instance.xml.get("adbn_edge_js");
				if(txt_file.equals(""))logger.error("adbnfloating adbn_edge none ");
				
				ArrayList a1 = StringUtils.getRandList(100);
				
				txt_file= txt_file.replace("{{wp_rnd_no}}",a1.get(0)+"");
				txt_file= txt_file.replace("{{ad_t}}",t);
				txt_file= txt_file.replace("{{ad_align}}",align);
				txt_file= txt_file.replace("{{ad_w}}",w);
				txt_file= txt_file.replace("{{ad_h}}",h);
				txt_file= txt_file.replace("{{ad_u}}",u);
				txt_file= txt_file.replace("{{ad_pcode}}",pCode);
				txt_file= txt_file.replace("{{ad_s}}",s);
				txt_file= txt_file.replace("{{ad_us}}",us);
				txt_file= txt_file.replace("{{ad_iwh}}",iwh);
				txt_file= txt_file.replace("{{ad_igb}}",igb);
				
				out.println( txt_file );
			}else if( types.equals("video") ){
//				txt_file= FileUtil.readFile("/home/dreamsearch/public_html/ad/adbn_video.js");
//				//logger.debug("adbnfloating[90] "+txt_file);
//				if( txt_file.equals("") ){
//					txt_file= FileUtil.readFile("D:/work/enliple/www/webapp/public_html/ad/adbn_video.js");
//				}
				txt_file=(String) RFServlet.instance.xml.get("adbn_video_js");
				if(txt_file.equals(""))logger.error("adbnfloating adbn_video none ");

				txt_file= txt_file.replace("{{ad_t}}",t);
				txt_file= txt_file.replace("{{ad_align}}",align);
				txt_file= txt_file.replace("{{ad_w}}",w);
				txt_file= txt_file.replace("{{ad_h}}",h);
				txt_file= txt_file.replace("{{ad_tag}}", "<iframe name='ifrad' id='ifrad' width='"+w+"' height='"+h+"' "
						+" src='http://www.dreamsearch.or.kr/servlet/adbn?from=\"+escape(from)+\"&u="+u+"&pCode="+pCode+"&us="+us+"&s="+s+"&iwh="+iwh+"&igb="+igb+"&types=video' "
						+" frameBorder='0' marginWidth='0' marginHeight='0' scrolling='no' ></iframe>");
				
				out.println( txt_file );
				
			}else if( types.equals("mbw") ){
				
				logger.debug("bntype="+bntype+", cntsr="+cntsr+", cntad="+cntad+", imgtype="+imgtype);
				
//				txt_file= FileUtil.readFile("/home/dreamsearch/public_html/ad/adbn_mobile.js");
//				if( txt_file.equals("") ){
//					txt_file= FileUtil.readFile("D:/work/enliple/www/webapp/public_html/ad/adbn_mobile.js");
//				}
				txt_file=(String) RFServlet.instance.xml.get("adbn_mobile_js");
				if(txt_file.equals(""))logger.error("adbnfloating adbn_movile_js none ");
				
				if( !cntsr.equals("") ){
//					txt_file= FileUtil.readFile("/home/dreamsearch/public_html/ad/adbn_mobile1.js");
//					if( txt_file.equals("") ){
//						txt_file= FileUtil.readFile("D:/work/enliple/www/webapp/public_html/ad/adbn_mobile1.js");
//					}
					txt_file=(String) RFServlet.instance.xml.get("adbn_mobile1_js");
					if(txt_file.equals(""))logger.error("adbnfloating adbn_movile1_js none ");
				}
				
				ArrayList a1 = StringUtils.getRandList(100);
				
				txt_file= txt_file.replace("{{wp_rnd_no}}",a1.get(0)+"");
				txt_file= txt_file.replace("{{wp_bntype}}",bntype);
				txt_file= txt_file.replace("{{wp_cntad}}",cntad);
				txt_file= txt_file.replace("{{wp_cntsr}}",cntsr);
				txt_file= txt_file.replace("{{wp_imgtype}}",imgtype);
				txt_file= txt_file.replace("{{wp_us}}",us);
				txt_file= txt_file.replace("{{wp_u}}",u);
				txt_file= txt_file.replace("{{wp_s}}",s);
				txt_file= txt_file.replace("{{wp_w}}",w);
				txt_file= txt_file.replace("{{wp_h}}",h);
				out.println( txt_file );
				
			}else{
//				txt_file= FileUtil.readFile("/home/dreamsearch/public_html/ad/adbn_floating.js");
//				if( txt_file.equals("") ){
//					txt_file= FileUtil.readFile("D:/work/enliple/www/webapp/public_html/ad/adbn_floating.js");
//				}
				txt_file=(String) RFServlet.instance.xml.get("adbn_floating_js");
				if(txt_file.equals(""))logger.error("adbnfloating adbn_floating_js none ");
				
				ArrayList a1 = StringUtils.getRandList(100);
				
				txt_file= txt_file.replace("{{wp_rnd_no}}",a1.get(0)+"");
				txt_file= txt_file.replace("{{wp_igb}}",igb);
				txt_file= txt_file.replace("{{wp_types}}",types);
				txt_file= txt_file.replace("{{wp_us}}",us);
				txt_file= txt_file.replace("{{wp_u}}",u);
				txt_file= txt_file.replace("{{wp_pCode}}",pCode);
				txt_file= txt_file.replace("{{wp_s}}",s);
				txt_file= txt_file.replace("{{wp_t}}",t);
				txt_file= txt_file.replace("{{wp_l}}",l);
				txt_file= txt_file.replace("{{wp_w}}",w);
				txt_file= txt_file.replace("{{wp_h}}",h);
				out.println( txt_file );
			}

		} catch (Exception e) {
			logger.error("adbnFloating err "+e);
		}
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response){
		doGet(request,response);
	}
	public void init() throws ServletException {
		logger.info("AdbnFloatingServlet started");
		if (appContext == null) {
			RFServlet.instance.init();
			appContext=RFServlet.appContext;
		}
		manageCookie=(ManagementCookie)appContext.getBean("ManagementCookie");
		dumpObj=(DumpObject)appContext.getBean("DumpObject");
	}
}