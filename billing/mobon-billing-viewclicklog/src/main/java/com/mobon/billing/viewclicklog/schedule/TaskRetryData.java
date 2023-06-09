package com.mobon.billing.viewclicklog.schedule;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.adgather.constants.G;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobon.billing.model.v20.ConversionVo;
import com.mobon.billing.model.v20.PollingData;
import com.mobon.billing.model.v20.ViewClickVo;
import com.mobon.billing.viewclicklog.handler.ContextClosedHandler;
import com.mobon.billing.viewclicklog.service.SumObjectManager;
import com.mobon.billing.viewclicklog.service.WorkQueue;
import com.mobon.billing.viewclicklog.util.ConsumerFileUtils;
import com.mobon.billing.viewclicklog.util.DateUtils;
import com.mobon.billing.viewclicklog.util.FileUtils;

import net.sf.json.JSONObject;

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

	public static int getThreadCnt() {
		return TaskRetryData.threadCnt;
	}

	public static void setThreadCnt(int threadCnt) {
		TaskRetryData.threadCnt = threadCnt;
	}

	public static synchronized void increaseThreadCnt() {
		TaskRetryData.threadCnt++;
	}

	public static synchronized void decreaseThreadCnt() {
		TaskRetryData.threadCnt--;
	}

	/**
	 * retry 디렉토리의 파일 처리
	 */
	public void consumerRetry() {
		logger.info(">> START consumerRetry");

		// 종료되면 재처리 안함
		if (contextClosedHandler.closed) {
			logger.info("ContextClosedHandler closed");
			return;
		}

		workQueue.execute(new RetryTaskerV3());

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
						int result = ((File) f1).getName().compareTo(((File) f2).getName());
						if (result > 0) {
							return 1;
						} else if (result < 0) {
							return -1;
						} else {
							return 0;
						}
					}
				});
				
				logger.info("retry files.length - {}", files.length);

				String fail_folder = String.format("%s%s", logPath, "retryfail/" );
				FileUtils.mkFolder(fail_folder);
				
				boolean exceptionYN = false;
				int j=0;

				if (files.length>0) {
					
					if (!files[j].isFile())		return;
					
					logger.info("files[j] - {}", files[j].getName());
					
					if (!files[j].getName().contains("insertIntoError"))	return ;
					if (files[j].getName().contains("_ing"))				return ;
					
					String tmp_name = files[j].getAbsolutePath() + "_ing";
					File tmp_file = new File( tmp_name );
					files[j].renameTo( tmp_file );
					
					try {
						fileinput = new FileInputStream( tmp_name );
						reader = new BufferedReader(new InputStreamReader(fileinput));
						String line = null;
						String topic = null;
						String msg = null;
						
						while ((line = reader.readLine()) != null){
							String []lines = line.split("\t");
							
							try {
								topic = lines[1];
								msg = lines[2];
							} catch (Exception e) {
								logger.error("err {}", msg);
								continue;
							}
							
							
							long millis = Calendar.getInstance().getTimeInMillis();
							String writeFileName = String.format("%s_%s", "insertIntoError", DateUtils.getDate("yyyyMMdd_HHmm"), millis);
							
							JSONObject jJSONObject = null;
							try {
								jJSONObject = JSONObject.fromObject(msg);
							} catch (Exception e) {
								continue;
							}
							
							logger.debug("jJSONObject - {}", jJSONObject);
							
							if (contextClosedHandler.closed) {
								// 종료되면 바로 파일로 저장.
								ConsumerFileUtils.writeLine( logPath +"retry/", writeFileName, topic, msg);
								
								exceptionYN = false;
								
							} else {
								// 토픽별 분기처리 (insertError 로 떨어진 Object 는 다시 PollingData 로 맵핑 안됨)
								if (G.ViewClickVo.equals( topic )) {
									try {
										ViewClickVo data = new ObjectMapper().readValue(msg, ViewClickVo.class);
										sumObjectManager.appendViewClickVo(data);
										
									} catch (Exception e) {
										logger.error("err data - {}", jJSONObject.toString().substring(0,  200), e);
										ConsumerFileUtils.writeLine( logPath +"retryfail/", writeFileName, G.ViewClickVo, jJSONObject);
									}
								} else if (G.ConversionVo.equals(topic)) {
									try {
										ConversionVo data= new ObjectMapper().readValue(msg, ConversionVo.class);
										sumObjectManager.appendConversionVo(data);
										
									} catch (Exception e) {
										logger.error("err data - {}", jJSONObject.toString().substring(0,  200), e);
										ConsumerFileUtils.writeLine( logPath +"retryfail/", writeFileName, G.ConversionVo, jJSONObject);
									}
								}
							}
						}
						 
						exceptionYN = false;
						
					} catch (Exception e) {
						logger.error("err ", e);
						exceptionYN = true;
						
					} finally {
						reader.close();
						fileinput.close();
						
						if (!exceptionYN) {
							File fileD = new File(tmp_name);
							if (fileD.exists()&&fileD.isFile()) {
								fileD.delete();
							}
						} else {
							String filePathName = tmp_file.getAbsolutePath();

							if (filePathName.indexOf("_ing")>0) {
								File fileT  = new File (filePathName.replace("_ing",""));								
								tmp_file.renameTo(fileT);
							}
						}
					}
				}
				logger.info("retryOk files.length - {}", files.length);
				
			} catch (Exception e) {
				logger.error("err", e);
			} finally {
			}
		}
	}

}
