package com.adgather.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.context.ApplicationContext;

import com.adgather.beans.AdInfoCache;
import com.adgather.beans.ManagementCookie;
import com.adgather.parsing.PasingMember;
import com.adgather.reportmodel.DumpObject;

import de.odysseus.staxon.json.JsonXMLConfig;
import de.odysseus.staxon.json.JsonXMLConfigBuilder;
import de.odysseus.staxon.json.JsonXMLInputFactory;
import de.odysseus.staxon.xml.util.PrettyXMLEventWriter;

/**
 * 멤버출력 API 
 * @author mobon069
 
 */
@SuppressWarnings("serial")
public class ADMemberParsing extends HttpServlet{
	
	private DumpObject dumpObj; 
	private ApplicationContext appContext;
	public ManagementCookie manageCookie;
	public AdInfoCache adInfoCache;
	static Logger logger = Logger.getLogger(ADMemberParsing.class);
	
	@SuppressWarnings("unchecked")
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException{
		String mc = req.getParameter("mc") == null ? "" : req.getParameter("mc");
		//데이터 조회
		JSONObject obj = PasingMember.MemberData();
		PrintWriter printout = resp.getWriter();
		String xml = "";
		manageCookie.makeCookie("push", "1", 60*6, resp);
		
        //mc가 1111인 경우 xml 데이터 출력
        if(mc.equals("1111")){
			try {
				JSONObject out = new JSONObject();
//				out.put("element", list);
				//json을 InputStream 으로 변환 
				InputStream in = IOUtils.toInputStream(out.toJSONString());
				//json to xml 파싱
				StringWriter stringWriter = new StringWriter();
				JsonXMLConfig config = new JsonXMLConfigBuilder().multiplePI(false).prettyPrint(false).build();
				XMLEventReader reader = new JsonXMLInputFactory(config).createXMLEventReader(in);
				XMLEventWriter writer = XMLOutputFactory.newInstance().createXMLEventWriter(stringWriter);
				writer = new PrettyXMLEventWriter(writer);
				writer.add(reader);
				xml = stringWriter.getBuffer().toString();
				printout.print(xml);
			} catch (XMLStreamException e) {
				e.printStackTrace();
			} catch (FactoryConfigurationError e) {
				e.printStackTrace();
			}
		}
		//json 데이터 출력
		else{
			resp.setContentType("application/json; charset=UTF-8");
			printout.print(obj.toJSONString());
		}
        
        printout.flush();
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{
		doGet(request,response);
	}
	
	public void init() throws ServletException {
		logger.info("ADMemberParsing started");
		if (appContext == null) {
			RFServlet.instance.init();
			appContext=RFServlet.appContext;
		}
		manageCookie=(ManagementCookie)appContext.getBean("ManagementCookie");
		dumpObj=(DumpObject)appContext.getBean("DumpObject");
	}

}
