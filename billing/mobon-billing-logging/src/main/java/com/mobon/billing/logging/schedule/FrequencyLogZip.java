package com.mobon.billing.logging.schedule;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.adgather.util.old.DateUtils;

@Component
public class FrequencyLogZip {

	private static final Logger logger = LoggerFactory.getLogger(FrequencyLogZip.class);

	@Value("${log.path.frequency}")
	private String logPathFrequency;

	private String logFilePreFix = "collect_freq";
	private final int bufferSize = 2048;
	private String zipLogPath = "";

	public void logFileZip() {

		boolean orgFileDelete = true;
		try {
			// /home/dreamsearch/logs/collect/frequency/
			
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			cal.add(Calendar.MINUTE, -40);	// 40분전
			String zipFileName = DateUtils.getDate("yyyy-MM-dd_HH", new Date(cal.getTimeInMillis()));
			logger.info("zipFileName==>{}", zipFileName);

			File file1 = new File(logPathFrequency);
			if (file1.exists() && file1.isDirectory()) {
				File[] targetFileArr = file1.listFiles();
				if ((targetFileArr == null) || (targetFileArr.length == 0)) {
					return;
				}

				String fileName = "";
				String exeStartTime = "";

				for (File targetFile : targetFileArr) {
					orgFileDelete = true;
					fileName = targetFile.getName();
					if (fileName.indexOf(".zip") > 0) {
						continue;
					}
					if (fileName.indexOf(logFilePreFix) < 0) {
						continue;
					}
					if (fileName.indexOf(zipFileName) < 0) {
						continue;
					}
					logger.info("fileName==>{}", fileName);

					ZipOutputStream zipOut = null;
					BufferedInputStream instream = null;
					try {
						File mkFile = new File(logPathFrequency + "/logZip/");
						if (!mkFile.exists()) {
							mkFile.mkdirs();
						}

						exeStartTime = DateUtils.getDate("yyyy-MM-dd HH:mm:ss");
						ZipEntry entry = new ZipEntry(fileName);
						zipLogPath = logPathFrequency + "logZip/";
						zipOut = new ZipOutputStream(new BufferedOutputStream(
								new FileOutputStream(zipLogPath + logFilePreFix +"_"+  zipFileName + ".zip"), bufferSize));
						zipOut.putNextEntry(entry);
						instream = new BufferedInputStream(new FileInputStream(targetFile));

						int read;
						byte[] data = new byte[bufferSize];
						while ((read = instream.read(data, 0, bufferSize)) != -1) {
							zipOut.write(data, 0, read);
						}
						zipOut.flush();

					} catch (Exception e) {
						orgFileDelete = false;
						logger.error("zip file Excepiton ==>{}", e);
					} finally {
						try {
							if (zipOut != null) zipOut.close();
							if (instream != null) instream.close();
						} catch (IOException e) {
							logger.error("zip file Excepiton ==>{}", e);
						}

						if (orgFileDelete) {
							if (targetFile.exists()) {
								logger.info("delete targetFile - {}", targetFile.getName());
								targetFile.delete();
							}
						}

						String exeEndTime = DateUtils.getDate("yyyy-MM-dd HH:mm:ss");
						SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						long diff = 0;
						long sec = 0;
						try {
							Date startT = f.parse(exeStartTime);
							Date endT = f.parse(exeEndTime);

							diff = endT.getTime() - startT.getTime();
							sec = diff / 1000;

						} catch (ParseException e) {
							logger.error("error log => {}", e);
						}

						logger.info("파일 하나 걸린 시간 : {}", sec);

					}
				}
			}

		} catch (Exception e) {
			logger.error("111error log => {}", e);
		} finally {
			// 파일 삭제
			deleteLogFile();
		}
	}

	public void deleteLogFile() {
		try {
			Date beforDate = new Date();
			beforDate.setTime((new Date().getTime() + (1000L * 60 * 60 * 50 * -1)));// 1초*1분*1시간*24시간*이전
			String strBeforDate = DateUtils.getDate("yyyy-MM-dd_HH", beforDate);
			String deleteFileName = zipLogPath + logFilePreFix + "_" + strBeforDate + ".zip";
			File deleteFile = new File(deleteFileName);
			if (deleteFile.exists()) {
				logger.info("delete deleteFileName - {}", deleteFileName);
				deleteFile.delete();
			}

		} catch (Exception e) {
			logger.error("deleteLogFile==> {}", e);
		}

	}// end deleteLogFile

	public static void main(String[] args) {

		FrequencyLogZip clz = new FrequencyLogZip();
		clz.logFileZip();
	}

}// end class
