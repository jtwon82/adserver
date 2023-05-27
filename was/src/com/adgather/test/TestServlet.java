package com.adgather.test;

import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

public class TestServlet extends HttpServlet {
	static Logger logger = Logger.getLogger(TestServlet.class);	
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		try {
			PrintWriter out = response.getWriter();
			out.println("cookieValue=aa");	
		} catch (Exception e) {
			logger.error(e.getMessage());
			System.out.println(e);
		}
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response){
		doGet(request,response);
	}
	public void init() throws ServletException {
		System.out.println(" Test Servlet start.");
	}	
	public void destroy(){
		try{
			
		}catch(Exception e){
			
		}
		System.out.println(" Test Servlet destroy execute.");
	}
}