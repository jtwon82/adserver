package com.adgather.schedulejob;


import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.adgather.beans.DumpDataToDB;
import com.adgather.reportmodel.DumpObject;
import com.adgather.servlet.RFServlet;
public class DataPushJob implements Job{
	static Logger logger = Logger.getLogger(DataPushJob.class);
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		try{
			init();
			DataPushJobThread dj=new DataPushJobThread();
			dj.start();
		}catch(Exception e){
			logger.error("DataPushJob-execute:"+e);
		}
	}
	private void init(){
		
	}
	class DataPushJobThread extends Thread{
		private DumpObject robj;
		private DumpObject tempRobj;
		private DumpDataToDB dumpDb;
		private String target="db";
		protected boolean stop;
		DataPushJobThread() {
			super();
			try{
				//dumpDb=(DumpDataToDB)RFServlet.instance.appContext.getBean("DumpDataToDB");
				//robj=(DumpObject)RFServlet.instance.appContext.getBean("DumpObject");
				dumpDb=RFServlet.instance.dumpDb;
				robj=RFServlet.instance.dumpObj;
				
				synchronized(robj){
					tempRobj= (DumpObject) robj.clone();
					robj.clearAll();
				}
			}catch(Exception e){
				logger.error("DataPushJob-init:"+e);
			}
		} 

		public void stop(boolean b) {
			stop = b;
		}

		public void run() {
			RFServlet.instance.setDumpThreadCounter(1);
			if(RFServlet.instance.getDumpThreadCounter()>5){
				target="file";
			}else{
				target="db";
			}
			logger.info("-------- DUMPTARGET= "+target +",ThreadCnt= "+RFServlet.instance.getDumpThreadCounter()+" ----------");
			dumpDb.dumpExecute(tempRobj,target);
			RFServlet.instance.setDumpThreadCounter(-1);
		}
	}
	
}