package com.adgather.schedulejob;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.adgather.beans.DumpDataToDB;
import com.adgather.servlet.RFServlet;
public class PushFailDummpJob implements Job{
	private RFServlet servlet;
	static Logger logger = Logger.getLogger(PushFailDummpJob.class);
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		init();
		try {
			//dumpDb.failDataDump();
			FailDataPushJobThread dj=new FailDataPushJobThread();
			dj.start();
		} catch (Exception e) {
			logger.error("PushFailDummpJob-execute:"+e);
		}
	}
	private void init(){ 
	}
	class FailDataPushJobThread extends Thread{
		protected boolean stop;
		private DumpDataToDB dumpDb;
		FailDataPushJobThread() {
			super();
			try{
				dumpDb=(DumpDataToDB)servlet.appContext.getBean("DumpDataToDB");
			}catch(Exception e){
				logger.error("PushFailDummpJob-init:"+e);
			}
		}
		public void stop(boolean b) {
			stop = b;
		}
		public void run() {
			try{
				if(servlet.instance.getFailDumpStatus()==false){	// 颇老贸府 吝酶 规瘤..
					logger.info("ReportUpdateJob2: failDataDump execute start.");
					servlet.instance.setFailDumpStatus(true);
					dumpDb.failDataDump();
					servlet.instance.setFailDumpStatus(false);
				}
			}catch(Exception e){
				logger.error("FailDataPushJobThread:"+e);
			}
		}
	}
	
	
}