package com.adgather.schedulejob;

import java.sql.CallableStatement;
import java.sql.Connection;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.adgather.resource.db.DBManager;
import com.adgather.servlet.RFServlet;

public class AdPointConfUpdateJob implements Job{
	static Logger logger = Logger.getLogger(AdPointConfUpdateJob.class);
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		try{
			logger.info("AdPointConfUpdateJob Thread is started.");
			Thread exec=new Thread(new AdPointConfUpdateJobThread());
			exec.start();
			logger.info("AdPointConfUpdateJob Thread is ended.");
		}catch(Exception e){
			logger.error("AdPointConfUpdateJob-execute"+e);
		}
	}
	public class AdPointConfUpdateJobThread implements Runnable{
		public void run() {
			Connection conn = null;
			CallableStatement cstmt=null;
			try{
				logger.info("AdPointConfUpdateJob 관련 캐시 리로드 시작.");
				conn=DBManager.getConnection("dreamdb");
				cstmt =conn.prepareCall("{	 call proc_CacheUpdateExec(?,?) }");
				String[] cList={"adCashCache","normalShopAdConfigCache","normalKeywordMatchConfigCache",
						"normalUrlMatchConfigCache","shopAdConfigCache","urlMatchConfigCache","iKeywordMatchConfigCache","mediaCodeCache"
						,"baseAdConfigCache","normalBaseAdConfigCache"};
				for(int i=0;i<cList.length;i++){
					cstmt.setString(1,cList[i]);
					cstmt.setString(2,"");
					cstmt.addBatch();
				}
				cstmt.executeBatch();

				//"iAdSiteCache","adsiteCache",
				RFServlet.instance.CacheReload("iAdSiteCache","");
				RFServlet.instance.CacheReload("adsiteCache","");
				
				// reset RFServlet xmldata init
				RFServlet.instance.loadXmlData();
				
				logger.info("AdPointConfUpdateJob 관련 캐시 리로드 종료.");
			}catch(Exception e){
				logger.error("loadMemberPointChangeChk : "+e);
			} finally {
				try{if(cstmt !=null) cstmt.close();} catch(Exception e){};
				try{if(conn !=null) conn.close();} catch(Exception e){};
			}
		}
	}
	/*
	
	private void loadMemberPointChangeChk(){
		
		Connection conn = null;
		CallableStatement cstmt=null;
		try{
			logger.info("AdPointConfUpdateJob 관련 캐시 리로드 시작.");
			cstmt =conn.prepareCall("{	 call proc_CacheUpdateExec(?,?) }");
			String[] cList={"adCashCache","normalBaseAdConfigCache","normalShopAdConfigCache","normalKeywordMatchConfigCache",
					"normalUrlMatchConfigCache","shopAdConfigCache","urlMatchConfigCache","iKeywordMatchConfigCache","baseAdConfigCache","mediaCodeCache"};
			for(int i=0;i<cList.length;i++){
				cstmt.setString(1,cList[i]);
				cstmt.setString(2,"");
				cstmt.addBatch();
			}
			cstmt.executeBatch();
			logger.info("AdPointConfUpdateJob 관련 캐시 리로드 종료.");
		}catch(Exception e){
			logger.error("loadMemberPointChangeChk : "+e);
		} finally {
			try{if(cstmt !=null) cstmt.close();} catch(Exception e){};
			try{if(conn !=null) conn.close();} catch(Exception e){};
		}
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		StringBuffer sql=new StringBuffer();
		
		try {
			sql.append(" SELECT userid,POINT,lastupdate,TIME_TO_SEC(TIMEDIFF(lastupdate,NOW())/60) AS prevmin                     \n");
			sql.append(" from admember \n");
			sql.append(" WHERE TIME_TO_SEC(TIMEDIFF(NOW(),lastupdate)/60)<=10 \n");
			sql.append(" AND POINT<=2000 ");
			logger.debug(sql.toString());
			conn=DBManager.getConnection("dreamdb");
			stmt=conn.createStatement();
			rs=stmt.executeQuery(sql.toString());
			if(rs.next()){
				logger.info("AdPointConfUpdateJob 관련 캐시 리로드 시작.");
				RFServlet.instance.CacheReload("adCashCache");
				RFServlet.instance.CacheReload("normalBaseAdConfigCache");
				RFServlet.instance.CacheReload("normalShopAdConfigCache");
				RFServlet.instance.CacheReload("normalKeywordMatchConfigCache");
				RFServlet.instance.CacheReload("normalUrlMatchConfigCache");
				RFServlet.instance.CacheReload("shopAdConfigCache");
				RFServlet.instance.CacheReload("urlMatchConfigCache");
				RFServlet.instance.CacheReload("iKeywordMatchConfigCache");
				RFServlet.instance.CacheReload("baseAdConfigCache");
				RFServlet.instance.CacheReload("mediaCodeCache");	
				logger.info("AdPointConfUpdateJob 관련 캐시 리로드 종료.");
			}
		} catch(Exception e) {
			logger.error("loadMemberPointChangeChk2:"+e);
		} finally {
			try{if(rs !=null) rs.close();} catch(Exception e){};
			try{if(stmt !=null) stmt.close();} catch(Exception e){};
			try{if(conn !=null) conn.close();} catch(Exception e){};
		}
	}
	*/
}