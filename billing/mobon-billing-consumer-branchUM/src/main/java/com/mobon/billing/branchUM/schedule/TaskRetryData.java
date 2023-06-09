package com.mobon.billing.branchUM.schedule;

import com.adgather.constants.G;
import com.adgather.util.old.DateUtils;
import com.mobon.billing.branchUM.handler.ContextClosedHandler;
import com.mobon.billing.branchUM.schedule.TaskRetryData;
import com.mobon.billing.branchUM.service.SumObjectManager;
import com.mobon.billing.branchUM.service.WorkQueue;
import com.mobon.billing.model.v15.ActionLogData;
import com.mobon.billing.model.v15.AppTargetData;
import com.mobon.billing.model.v15.BaseCVData;
import com.mobon.billing.model.v15.ConvData;
import com.mobon.billing.model.v15.ExternalInfoData;
import com.mobon.billing.model.v15.NearData;
import com.mobon.billing.model.v15.ShopInfoData;
import com.mobon.billing.model.v15.ShopStatsInfoData;
import com.mobon.billing.util.ConsumerFileUtils;
import com.mobon.billing.util.FileUtils;

import net.sf.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TaskRetryData {
	
	private static final Logger logger = LoggerFactory.getLogger(TaskRetryData.class);

	@Autowired
	private SumObjectManager sumObjectManager;

	@Autowired
	@Qualifier("RetryWorkQueue")
	private WorkQueue workQueue;

	@Autowired
	private ContextClosedHandler contextClosedHandler;

	@Value("${log.path}")
	private String logPath;

	private static int threadCnt = 0;

	public static void setThreadCnt(int threadCnt) {
		TaskRetryData.threadCnt = threadCnt;
	}

	public static synchronized void increaseThreadCnt() {
		threadCnt++;
	}

	public static synchronized void decreaseThreadCnt() {
		threadCnt--;
	}

	public static int getThreadCnt() {
		return threadCnt;
	}

	public void consumerRetry() {
		logger.info(">> START consumerRetry");

		if (this.contextClosedHandler.closed) {
			logger.info("ContextClosedHandler closed");

			return;
		}
		this.workQueue.execute((Runnable) new RetryTaskerV3());
	}
	private class RetryTaskerV3 implements Runnable {

		private RetryTaskerV3() {
		}

		public void run() {
			try {
				FileInputStream fileinput = null;
				BufferedReader reader = null;

				FileUtils.mkFolder( logPath +"retry/" );
				File []files = new File( logPath +"retry/" ).listFiles();

				Arrays.sort(files, new Comparator() {
					@Override
					public int compare(Object f1, Object f2) {
						File f11 = (File) f1;
						File f22 = (File) f2;
						if (f11.lastModified() > f22.lastModified()) {
							return 1;
						} else if (f11.lastModified() < f22.lastModified()) {
							return -1;
						} else {
							return 0;
						}
					}
				});
				
				logger.info("retry files.length - {}", files.length);

				if(files.length>0) {
					for( File f : files ) {
						logger.info("retry_files {}	{}	{}", f.lastModified(), new Date(f.lastModified()), f.getName() );
					}
				}

				String fail_folder = String.format("%s%s", logPath, "retryfail/" );
				FileUtils.mkFolder( fail_folder );
				
				boolean exceptionYN = false;
				int j=0;
//				for( int j=0; j<files.length; j++ ) {
				if ( files.length>0 ) {
					
					if(!files[j].isFile()){
						return ;
					}
					
					logger.info("files[j] - {}", files[j].getName());
					
					if( files[j].getName().indexOf("insertIntoError")<0 ){
						return ;
					}
					
					if( files[j].getName().indexOf("_ing")>-1 ) {
						return ;
					}
					
					String tmp_name = files[j].getAbsolutePath() + "_ing";
					File tmp_file = new File( tmp_name );
					files[j].renameTo( tmp_file );
					
					try {
						fileinput = new FileInputStream( tmp_name );
						reader = new BufferedReader(new InputStreamReader(fileinput));
						String line = null;
						String topic = null;
						String msg = null;
						
						while( (line = reader.readLine()) != null){
							String []lines = line.split("\t");
							
							try {
								topic = lines[1];
								msg = lines[2];
							}catch(Exception e) {
								logger.error("err {}", msg);
								continue;
							}
							
							
							long millis = Calendar.getInstance().getTimeInMillis();
							String writeFileName = String.format("%s_%s", "insertIntoError", DateUtils.getDate("yyyyMMdd_HHmm"), millis);
							
							JSONObject jJSONObject = null;
							try {
								jJSONObject = JSONObject.fromObject(msg);
							}catch(Exception e) {
								continue;
							}
							
							//logger.debug("jJSONObject - {}", jJSONObject);
							
							if( contextClosedHandler.closed ) {
								
								// 종료되면 바로 파일로 저장.
								ConsumerFileUtils.writeLine( logPath +"retry/", writeFileName, topic, msg);
								
								exceptionYN = false;
								
							} else {
									BaseCVData item = null;
									try {
										
										item = BaseCVData.fromHashMap(jJSONObject);
										
										item.setPlatform(G.convertPLATFORM_CODE_BACK(item.getPlatform()));
										item.setAdGubun(G.convertTP_CODE_BACK(item.getAdGubun()));
									} catch (Exception e) {
										logger.error("err data - {}", jJSONObject.toString().substring(0,  200), e);
										ConsumerFileUtils.writeLine( logPath +"retryfail/", writeFileName, G.click_view, jJSONObject);
										return;
									}
									
								if( G.KnoUMSiteCodeData.equals( topic ) ) {
									try {
										sumObjectManager.appendKnoUMSiteCodeData(item);	
									} catch ( Exception e ) {
										writeFileName = String.format("%s_%s", "insertIntoErrorStiecode", DateUtils.getDate("yyyyMMdd_HHmm"), millis);
										logger.error("err data - {}", jJSONObject.toString().substring(0,  200), e);
										ConsumerFileUtils.writeLine( logPath +"retryfail/", writeFileName, G.click_view, jJSONObject);
									}
								} else if (G.KnoUMScriptNoData.equals(topic)) {
									try {
																				
										sumObjectManager.appendKnoUMScriptNoData(item);
										
									}catch(Exception e) {
										writeFileName = String.format("%s_%s", "insertIntoErrorScriptNo", DateUtils.getDate("yyyyMMdd_HHmm"), millis);
										logger.error("err data - {}", jJSONObject.toString().substring(0,  200), e);
										ConsumerFileUtils.writeLine( logPath +"retryfail/", writeFileName, G.click_view, jJSONObject);
									}
								} else if (G.KnoKpiData.equals(topic)) {
									try {
										
										sumObjectManager.appendKnoKpiData(item);
										
									}catch(Exception e) {
										writeFileName = String.format("%s_%s", "insertIntoErrorKpiNo", DateUtils.getDate("yyyyMMdd_HHmm"), millis);
										logger.error("err data - {}", jJSONObject.toString().substring(0,  200), e);
										ConsumerFileUtils.writeLine( logPath +"retryfail/", writeFileName, G.click_view, jJSONObject);
									}
								}
							}
						}
						 
						exceptionYN = false;
						
					}catch(Exception e) {
						
						logger.error("err ", e);
						exceptionYN = true;
						
					}finally {
						 
						reader.close();
						
						fileinput.close();
						
						
						if(!exceptionYN) {
						
							File fileD = new File(tmp_name);
							
							if(fileD.exists()&&fileD.isFile()) {
								fileD.delete();
							}
							
						} 
						
						
						if(exceptionYN) {
							
							String filePathName = tmp_file.getAbsolutePath();
							
							if(filePathName.indexOf("_ing")>0) {								
								
								File fileT  = new File (filePathName.replace("_ing",""));								
								tmp_file.renameTo(fileT);
								
							}
							
						}
						
					}
				}
				logger.info("retryOk files.length - {}", files.length);
				
			}catch(Exception e) {
				logger.error("err", e);
			}finally {
			}
		}
	}
}
