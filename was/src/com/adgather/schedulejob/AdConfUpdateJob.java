package com.adgather.schedulejob;

import java.net.InetAddress;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Random;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.adgather.resource.db.DBManager;
import com.adgather.servlet.RFServlet;

public class AdConfUpdateJob implements Job{
	static Logger logger = Logger.getLogger(AdConfUpdateJob.class);
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		try{
			logger.debug("AdConfUpdateJob Thread is started.");
			Thread exec=new Thread(new AdConfUpdateJobThread());
			exec.start();
		}catch(Exception e){
			logger.error("AdConfUpdateJob-execute"+e);
		}
	}
	public class AdConfUpdateJobThread implements Runnable{
		public void run() {
			try {
				if(RFServlet.instance==null){
					logger.debug("servlet 초기화 안됨.");
					return;
				}
				if(RFServlet.instance.adInfoCache==null){
					logger.debug("servlet.instance.adInfoCache 초기화 안됨.");
					return;
				} 
				if(RFServlet.instance.currDay==0){	// 최초 기동시 1회..실행..
					RFServlet.instance.currDay=Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
					logger.info("AdConfUpdateJob All job start.");
					RFServlet.instance.CacheReload("all","");
					logger.info("AdConfUpdateJob All job end.");
				}else{
					if(RFServlet.instance.currDay!=Calendar.getInstance().get(Calendar.DAY_OF_MONTH)){
						RFServlet.instance.currDay=Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
						RFServlet.instance.scheduleStatus=false;
					}
					if(RFServlet.instance.scheduleStatus==false){
						RFServlet.instance.scheduleStatus=true;
						loadCacheChangeChk();
					}
				}	
			} catch (Exception e) {
				logger.error("AdConfUpdateJobThread : "+e);
			}
		}
	}
	private void loadCacheChangeChk(){
		Connection conn = null;
		Statement stmt = null;
		Statement upstmt = null;
		ResultSet rs = null;
		StringBuffer sql=new StringBuffer();
		try {
			/*int i=0;
			while(true){
				i++;
				logger.info("loadCacheChangeChk:"+RFServlet.instance.scheduleStatus);
				if(RFServlet.instance.scheduleStatus==false){
					break;
				}
				if(i>10) break;
				Thread.sleep(5000);
			}*/
			InetAddress addr=InetAddress.getLocalHost();
			String hostIp=addr.getHostAddress();
			sql.append(" select cachename,IFNULL(chkey,'') chkey                     \n");
			sql.append(" from Tb_CacheReloadChk \n");
			sql.append(" where workstate='R' \n");
			sql.append(" and serverip='"+hostIp+"'");
			sql.append(" group by cachename,chkey \n");
			logger.debug(sql.toString());
			conn=DBManager.getConnection("dreamdb");
			stmt=conn.createStatement();
			rs=stmt.executeQuery(sql.toString());
			while(rs.next()){
				try{
					logger.debug(rs.getString("cachename")+" reload start.");
					RFServlet.instance.CacheReload(rs.getString("cachename"),rs.getString("chkey"));
					String upSql="update Tb_CacheReloadChk set workstate='Y' "
						+" where serverip='"+hostIp+"' and workstate='R' and cachename='"+rs.getString("cachename")+"'";
					if(rs.getString("chkey").length()>0){
						upSql+=" and chkey='"+rs.getString("chkey")+"'";
					}
					//String upSql="delete from Tb_CacheReloadChk"
					//	+" where serverip='"+hostIp+"' and cachename='"+rs.getString("cachename")+"' and chkey='"+rs.getString("chkey")+"'";
					logger.debug(upSql);
					upstmt=conn.createStatement();
					upstmt.executeUpdate(upSql);
					logger.debug(rs.getString("cachename")+" reload end.");
				}catch(Exception ex){
					logger.error("loadCacheChangeChk1:"+ex);
				}finally{
					try{if(upstmt !=null) upstmt.close();} catch(Exception e){};
				}
			}
			Random trandom= new Random();
			int tx=trandom.nextInt(10);
			if(tx==1){
				String delsql="delete from Tb_CacheReloadChk WHERE workstate='Y' or regdate<NOW()- INTERVAL 15 MINUTE";
				logger.debug(delsql);
				upstmt=conn.createStatement();
				upstmt.executeUpdate(delsql);
			}
		} catch(Exception e) {
			logger.error("loadCacheChangeChk2:"+e);
			RFServlet.instance.dbStatus=false;
		} finally {
			RFServlet.instance.scheduleStatus=false;
			try{if(rs !=null) rs.close();} catch(Exception e){};
			try{if(stmt !=null) stmt.close();} catch(Exception e){};
			try{if(upstmt !=null) upstmt.close();} catch(Exception e){};
			try{if(conn !=null) conn.close();} catch(Exception e){};
		}
	}
}