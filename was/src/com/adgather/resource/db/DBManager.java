package com.adgather.resource.db;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.adgather.batchjob.ShopDataItem;
public class DBManager{
	static Logger logger = Logger.getLogger(DBManager.class);
	

	public static Connection getConnection(String types, String dbname) throws Exception{
		try {
			Class.forName("org.gjt.mm.mysql.Driver");
			Connection conn=null;
			if( types.equals("local")){
				String url= "jdbc:mysql://localhost:3306/"+ dbname +"?characterEncoding=euc-kr";
				conn= DriverManager.getConnection(url,"root","1132");
			}else if( types.equals("5")){
				String url= "jdbc:mysql://211.191.174.5:3306/"+ dbname +"?characterEncoding=utf8";
				conn= DriverManager.getConnection(url,"dreamsearch","dnjvld$%^");
			}else if( types.equals("54")){
				String url= "jdbc:mysql://183.111.148.54:3306/"+ dbname +"?characterEncoding=utf8";
				conn= DriverManager.getConnection(url,"dreamsearch","dnjvld$%^");
			}
			return conn;
		}catch(Exception e) { 
			logger.fatal(e.getMessage());
			System.out.print( e.getMessage());
			return null;
			//System.out.println("Couldn't get a valid DataSource or connection and throwing a EJBException");
			//throw new Exception(e.getMessage());
		}
	} 
	public static Connection getConnection(String jndi) throws Exception{
		try { 
			InitialContext context = new InitialContext(); 
			DataSource ds = (DataSource)context.lookup("java:comp/env/"+jndi);
			//System.out.println("Got a valid DataSource Object");
			Connection conn = ds.getConnection();
			//System.out.println("Got a valid connection from EJB Server");  
			return conn;
		}catch(Exception e) { 
			logger.fatal(e.getMessage());
			String server_type= ShopDataItem.dbip;
			
			if( server_type.equals("real") )
				return getConnection("54","dreamsearch");
			else
				return getConnection("5","dreamsearch");
			//System.out.println("Couldn't get a valid DataSource or connection and throwing a EJBException");
			//throw new Exception(e.getMessage());
		}
	} 
	public static void close(Connection conn){
		try{
			if(conn !=null) conn.close(); 
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public static void close(Statement stmt){  
		try{
			if(stmt !=null) stmt.close(); 
		}catch(Exception e){ 
			e.printStackTrace(); 
		}
	}

	public static void close(PreparedStatement pstmt){
		try{
			if(pstmt !=null) pstmt.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public static void close(ResultSet rs){ 
		try{ 
			if(rs !=null) rs.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public static void close(Connection conn,PreparedStatement pstmt,ResultSet rs){
		try{
			if(conn !=null) conn.close();
			if(pstmt !=null) pstmt.close(); 
			if(rs !=null) rs.close(); 
		}catch(Exception e){
			e.printStackTrace();
		}	
	}
	public static void close(Connection conn,Statement stmt,ResultSet rs){
		try{
			if(conn !=null) conn.close();
			if(stmt !=null) stmt.close(); 
			if(rs !=null) rs.close(); 
		}catch(Exception e){
			e.printStackTrace();
		}	
	}
	public static void close(Connection conn,PreparedStatement pstmt){
		try{
			if(conn !=null) conn.close();
			if(pstmt !=null) pstmt.close(); 
		}catch(Exception e){
			e.printStackTrace();
		}	
	}
	
	public static void close(Connection conn,Statement stmt){
		try{
			if(conn !=null) conn.close();
			if(stmt !=null) stmt.close(); 
		}catch(Exception e){
			e.printStackTrace();
		}	
	}	
}