package com.adgather.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

public class SdkCount extends HttpServlet{
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		req.setCharacterEncoding("utf-8");
		resp.setContentType("application/json; charset=utf-8");
		resp.setHeader("Pragma", "no-cache");			
		resp.setHeader("Cache-Control", "no-cache"); 
		
		JSONObject test = new JSONObject();
		test.put("result", "true");
		PrintWriter printout = resp.getWriter();
		printout.print(test.toJSONString());
		return;
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req,resp);
	}
}
