package com.adgather.beans;

import java.io.File;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;
import com.adgather.util.*;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.adgather.reportmodel.AdChargeData;
import com.adgather.reportmodel.DayMediaLogData;
import com.adgather.reportmodel.DrcData;
import com.adgather.reportmodel.DumpObject;
import com.adgather.reportmodel.MediaLogData;
import com.adgather.reportmodel.MediaScriptData;
import com.adgather.reportmodel.PointData;
import com.adgather.reportmodel.RFData;
import com.adgather.reportmodel.ShopData;
import com.adgather.reportmodel.ShopStatsData;
import com.adgather.reportmodel.SiteCodeData;
import com.adgather.resource.db.DBManager;
import com.adgather.servlet.RFServlet;
import com.adgather.util.DateUtils;
import com.adgather.util.PrintFile;
import com.adgather.util.StringUtils;

public class DumpDataToDB{
	private static Logger logger = Logger.getLogger(DumpDataToDB.class);
	private RFServlet servlet;
	public void dumpExecute(DumpObject tempRobj,String target){
		int debug=0;
		Connection conn=null;
		String destDir="";
		try {
			//Random r=new Random();
			//int delay=r.nextInt(40000);
			//logger.debug("강제 지연시켜..:"+delay);
			//Thread.sleep(delay+10000);
			//logger.debug("강제 지연해제:"+delay);
			if(servlet.instance==null){
				logger.debug("servlet 초기화 안됨!!");
				return;
			}
			debug++;
			Calendar cal=Calendar.getInstance();
			try{
				conn=DBManager.getConnection("dreamdb");
			}catch(SQLException e){
				logger.error("DBManager.getConnection error : "+e);
				servlet.instance.dbStatus=false;
			}

			//////////////////////// shop_log ////////////////////////////////////////////
			logger.info(tempRobj.getShopLogData().size() + " 개 SHOP_LOG 데이타 덤프 시작");
			Vector<ShopData> shop = new Vector<ShopData>();
			for(int i=0;i<tempRobj.getShopLogData().size();i++){
				ShopData data=(ShopData) tempRobj.getShopLogData().get(i);
				if(servlet.instance.adInfoCache.shopLogCache.isKeyInCache(data.getIP())){
					logger.info("ShopLog Dump : ShopLog Cache Matched : remove "+data.getIP());
					servlet.instance.adInfoCache.shopLogCache.remove(data.getIP());
				}
				if(servlet.instance.dbStatus==false || target.equals("file") || dumpShopData(conn, data)==1){
					shop.add(data);
				}
				if(data.getPCODE()!=null && data.getPCODE().length()>0){
					setShopStatsData(data.getUSERID(),data.getPCODE(),data,"view");
				}
				try{
					if(data.getPCODE()!=null && data.getPCODE().length()>0 &&
						data.getPNM()!=null && data.getPNM().length()>0 &&
						data.getPRICE()!=null && data.getPRICE().length()>0 &&
						data.getIMGPATH()!=null && data.getIMGPATH().length()>0){
						//실시간 수집의 경
						//data.getPURL()!=null && data.getPURL().length()>0)
						if(servlet.instance.dbStatus==true && target.equals("db") && servlet.instance.getDumpThreadCounter()<=3){
							String pKey=data.getUSERID()+"_"+data.getPCODE();
							ShopData tmp=servlet.instance.adInfoCache.getShopPCodeData(data.getUSERID(),data.getPCODE());
							if(tmp==null){	// 데이타 입력,캐시 저장..
								insertShopData(conn,data);
								logger.debug(pKey+" 신규 입력됨");
							}else{ //하루 이전거인지 점검해서 이전이면.. 갱신
								logger.debug(tmp.getInfo("tmp.getInfo[222] "));
								if(!data.getPRICE().equals(tmp.getPRICE()) 
										|| !data.getPNM().equals(tmp.getPNM())
										|| !data.getIMGPATH().equals(tmp.getIMGPATH())){
									insertShopData(conn,data);
								}
								/*
								}else if(!data.getPURL().equals(tmp.getPURL())){
									Calendar calTmp=Calendar.getInstance();
									calTmp.add(Calendar.DAY_OF_MONTH,-1);
									if(tmp.getLASTUPDATE().getTime()<calTmp.getTimeInMillis()){
										insertShopData(conn,data);
										servlet.instance.adInfoCache.shopPCodeDataCache.remove(pKey);
										logger.debug(pKey+" 갱신됨");
									}
								}
								*/
							}
						}
					}
				}catch(Exception e){
					logger.error("실시간 상품 코드 수집 오류:"+data.getUSERID()+"_"+data.getPCODE()+","+e);
				}
			}
			debug++;
			logger.info(tempRobj.getShopLogData().size()-shop.size() + " 개 SHOP_LOG 데이타 덤프 완료");
			if(shop.size()>0){
				logger.info(shop.size() + " 개 SHOP_LOG 덤프 에러 데이타 파일 작업 시작");
				try{
					destDir=servlet.DumpFailList+"/DATA/SHOP_LOG_"+cal.getTimeInMillis();
					saveObjectToFile(destDir,shop);
				}catch(Exception ioe){
					logger.error("dumpExecute3:"+ioe);
				}
				logger.info(shop.size() + " 개 SHOP_LOG 덤프 에러 데이타 파일 작업 완료");
			}
			////////////////////////shoplog초기화 ////////////////////////////////////////////
			logger.info(tempRobj.getDelShopLogData().size() + " 개 shopLog 데이타 초기화 덤프 시작");
			Vector<ShopData> delshop = new Vector<ShopData>();
			for(int i=0;i<tempRobj.getDelShopLogData().size();i++){
				ShopData data=(ShopData) tempRobj.getDelShopLogData().get(i);
				if(servlet.instance.dbStatus==false || servlet.instance.getDumpThreadCounter()>5 || delShopLogData(conn, data)==1){
					delshop.add(data);
				}
			}
			logger.info(tempRobj.getDelShopLogData().size()-delshop.size() + " 개 shopLog 데이타 초기화 덤프 완료");
			
			if(delshop.size()>0){
				logger.info(delshop.size() + " 개 shopLog 초기화 덤프 에러 데이타 파일 작업 시작");
				try{
					destDir=servlet.DumpFailList+"/DATA/SHOPLOG_INIT_"+cal.getTimeInMillis();
					saveObjectToFile(destDir,delshop);
				}catch(Exception ioe){
					logger.error("dumpExecute10:"+ioe);
				}
				logger.info(delshop.size() + " 개 shopLog 초기화 덤프 에러 데이타 파일 작업 완료");
			}
			
			////////////////////////icover charge_log ////////////////////////////////////////////
			logger.info(tempRobj.getChargeLogData().size() + " 개 ICOVERCHARGE_LOG 데이타 덤프 시작");
			Vector<AdChargeData> charge = new Vector<AdChargeData>();
			for(int i=0;i<tempRobj.getChargeLogData().size();i++){
				AdChargeData data=(AdChargeData) tempRobj.getChargeLogData().get(i);
				//if(target.equals("file") || dumpChargeLogData(conn, data)==1){
				if(servlet.instance.dbStatus==false || servlet.instance.getDumpThreadCounter()>5 || dumpChargeLogData(conn, data)==1){
					charge.add(data);
				}   
			}
			debug++;
			logger.info(tempRobj.getChargeLogData().size()-charge.size() + " 개 ICOVERCHARGE_LOG 데이타 덤프 완료");
			if(charge.size()>0){
				logger.info(charge.size() + " 개 ICOVERCHARGE_LOG 덤프 에러 데이타 파일 작업 시작");
				try{
					destDir=servlet.DumpFailList+"/DATA/ICOVERCHARGE_LOG_"+cal.getTimeInMillis();
					saveObjectToFile(destDir,charge);
				}catch(Exception ioe){
					logger.error("dumpExecute6:"+ioe);
				}
				logger.info(charge.size() + " 개 ICOVERCHARGE_LOG 덤프 에러 데이타 파일 작업 완료");
			}

			////////////////////////normal charge_log ////////////////////////////////////////////
			logger.info(tempRobj.getNormalChargeLogData().size() + " 개 NormalChargeLog 데이타 덤프 시작");
			Vector<AdChargeData> normalcharge = new Vector<AdChargeData>();
			for(int i=0;i<tempRobj.getNormalChargeLogData().size();i++){
				AdChargeData data=(AdChargeData) tempRobj.getNormalChargeLogData().get(i);
				//if(target.equals("file") || dumpNormalChargeLogData(conn, data)==1){
				if(servlet.instance.dbStatus==false || servlet.instance.getDumpThreadCounter()>5 || dumpNormalChargeLogData(conn, data)==1){
					normalcharge.add(data);
				}   
			}
			debug++;
			logger.info(tempRobj.getNormalChargeLogData().size()-normalcharge.size() + " 개 NormalChargeLog 데이타 덤프 완료");
			if(normalcharge.size()>0){
				logger.info(normalcharge.size() + " 개 NormalChargeLog 덤프 에러 데이타 파일 작업 시작");
				try{
					destDir=servlet.DumpFailList+"/DATA/NORMALCHARGE_LOG_"+cal.getTimeInMillis();
					saveObjectToFile(destDir,normalcharge);
				}catch(Exception ioe){
					logger.error("dumpExecute7:"+ioe);
				}
				logger.info(normalcharge.size() + " 개 NormalChargeLog 덤프 에러 데이타 파일 작업 완료");
			}
			////////////////////////normal charge_log Shopcon ////////////////////////////////////////////쇼콘 추가
			logger.info(tempRobj.getNormalChargeLogDataShopcon().size() + " 개 NormalChargeLogShopcon 데이타 덤프 시작");
			Vector<AdChargeData> normalchargeShopcon = new Vector<AdChargeData>();
			for(int i=0;i<tempRobj.getNormalChargeLogDataShopcon().size();i++){
				AdChargeData data=(AdChargeData) tempRobj.getNormalChargeLogDataShopcon().get(i);
				//if(target.equals("file") || dumpNormalChargeLogData(conn, data)==1){
				if(servlet.instance.dbStatus==false || servlet.instance.getDumpThreadCounter()>5 || dumpNormalChargeLogDataShopcon(conn, data)==1){
					normalchargeShopcon.add(data);
				}   
			}
			debug++;
			logger.info(tempRobj.getNormalChargeLogDataShopcon().size()-normalchargeShopcon.size() + " 개 NormalChargeLogShopcon 데이타 덤프 완료");
			if(normalchargeShopcon.size()>0){
				logger.info(normalchargeShopcon.size() + " 개 NormalChargeLogShopcon 덤프 에러 데이타 파일 작업 시작");
				try{
					destDir=servlet.DumpFailList+"/DATA/NORMALCHARGEShopcon_LOG_"+cal.getTimeInMillis();
					saveObjectToFile(destDir,normalchargeShopcon);
				}catch(Exception ioe){
					logger.error("dumpExecute7:"+ioe);
				}
				logger.info(normalchargeShopcon.size() + " 개 NormalChargeLogShopcon 덤프 에러 데이타 파일 작업 완료");
			}
			////////////////////////mobile charge_log ////////////////////////////////////////////
			logger.info(tempRobj.getMobileChargeLogData().size() + " 개 MobileChargeLog 데이타 덤프 시작");
			Vector<AdChargeData> mobile_charge = new Vector<AdChargeData>();
			for(int i=0;i<tempRobj.getMobileChargeLogData().size();i++){
				AdChargeData data=(AdChargeData) tempRobj.getMobileChargeLogData().get(i);
				//if(target.equals("file") || dumpNormalChargeLogData(conn, data)==1){
				if(servlet.instance.dbStatus==false || servlet.instance.getDumpThreadCounter()>5 || dumpMobileChargeLogData(conn, data)==1){
					mobile_charge.add(data);
				}   
			}
			debug++;
			logger.info(tempRobj.getMobileChargeLogData().size()-mobile_charge.size() + " 개 MobileChargeLog 데이타 덤프 완료");
			if(mobile_charge.size()>0){
				logger.info(mobile_charge.size() + " 개 MobileChargeLog 덤프 에러 데이타 파일 작업 시작");
				try{
					destDir=servlet.DumpFailList+"/DATA/MOBILECHARGE_LOG_"+cal.getTimeInMillis();
					saveObjectToFile(destDir,mobile_charge);
				}catch(Exception ioe){
					logger.error("dumpExecute7:"+ioe);
				}
				logger.info(mobile_charge.size() + " 개 MobileChargeLog 덤프 에러 데이타 파일 작업 완료");
			}
			////////////////////////rf ////////////////////////////////////////////
			logger.info(tempRobj.getRfData().size() + " 개 RF 덤프 시작");
			Vector<RFData> rf = new Vector<RFData>();
			for(int i=0;i<tempRobj.getRfData().size();i++){
				RFData data=(RFData) tempRobj.getRfData().get(i);
				if(servlet.instance.dbStatus==false || target.equals("file") || dumpRfData(conn, data)==1){
					rf.add(data);
				} 
			}
			debug++;
			logger.info(tempRobj.getRfData().size()-rf.size() + " 개 RF 데이타 덤프 완료");
			if(rf.size()>0){
				logger.info(rf.size() + " 개 RF덤프 에러 데이타 파일 작업 시작");
				try{
					destDir=servlet.DumpFailList+"/DATA/RF_DATA_"+cal.getTimeInMillis();
					saveObjectToFile(destDir,rf);
				}catch(Exception ioe){
					logger.error("dumpExecute1:"+ioe);
				}
				logger.info(rf.size() + " 개 RF덤프 에러 데이타 파일 작업 완료");
			}
			debug++;
			////////////////////////rf_keyword ////////////////////////////////////////////
			logger.info(tempRobj.getRf_KeywordData().size() + " 개 RF_KEYWORD 데이타 덤프 시작");
			Vector<RFData> rf_keyword = new Vector<RFData>();
			for(int i=0;i<tempRobj.getRf_KeywordData().size();i++){
				RFData data=(RFData) tempRobj.getRf_KeywordData().get(i);
				if(RFServlet.instance.adInfoCache.rfKeywordLogCache.isKeyInCache(data.getIP())){
					RFServlet.instance.dataMapper.getRF_KeywordDataFromIp(data.getIP());
				}
				if(servlet.instance.dbStatus==false || target.equals("file") || dumpRf_KeywordData(conn, data)==1){
					rf_keyword.add(data);
				}   
			}
			debug++;
			logger.info(tempRobj.getRf_KeywordData().size()-rf_keyword.size() + " 개 RF_KEYWORD 데이타 덤프 완료");
			if(rf_keyword.size()>0){
				logger.info(rf_keyword.size() + " 개 RF_KEYWORD덤프 에러 데이타 파일 작업 시작");
				try{
					destDir=servlet.DumpFailList+"/DATA/RF_KEYWORD_"+cal.getTimeInMillis();
					saveObjectToFile(destDir,rf_keyword);
				}catch(Exception ioe){
					logger.error("dumpExecute2:"+ioe);
				}
				logger.info(rf_keyword.size() + " 개 RF_KEYWORD덤프 에러 데이타 파일 작업 완료");
			}
			////////////////////////media_log ////////////////////////////////////////////
			logger.info(tempRobj.getMediaLogData().size() + " 개 MEDIA_LOG 데이타 덤프 시작");
			Vector<MediaLogData> media = new Vector<MediaLogData>();
			for(int i=0;i<tempRobj.getMediaLogData().size();i++){
				MediaLogData data=(MediaLogData) tempRobj.getMediaLogData().get(i);
				if(servlet.instance.dbStatus==false || target.equals("file") || dumpMediaLogData(conn, data)==1){
					media.add(data);
				}   
			}
			debug++;
			logger.info(tempRobj.getMediaLogData().size()-media.size() + " 개 MEDIA_LOG 데이타 덤프 완료");
			if(media.size()>0){
				logger.info(media.size() + " 개 MEDIA_LOG 덤프 에러 데이타 파일 작업 시작");
				try{
					destDir=servlet.DumpFailList+"/DATA/MEDIA_LOG_"+cal.getTimeInMillis();
					saveObjectToFile(destDir,media);
				}catch(Exception ioe){
					logger.error("dumpExecute4:"+ioe);
				}
				logger.info(media.size() + " 개 MEDIA_LOG 덤프 에러 데이타 파일 작업 완료");
			}
			////////////////////////keyworddata_log ////////////////////////////////////////////
			logger.info(tempRobj.getKeywordLogData().size() + " 개 keyworddata_log 데이타 덤프 시작");
			Vector<RFData> ikeyword = new Vector<RFData>();
			for(int i=0;i<tempRobj.getKeywordLogData().size();i++){
				RFData data=(RFData) tempRobj.getKeywordLogData().get(i);
				if(servlet.instance.dbStatus==false || target.equals("file") || dumpKeywordLogData(conn, data)==1){
					ikeyword.add(data);
				}   
			}
			debug++;
			logger.info(tempRobj.getKeywordLogData().size()-ikeyword.size() + " 개 keyworddata_log 데이타 덤프 완료");
			if(ikeyword.size()>0){
				logger.info(ikeyword.size() + " 개 keyworddata_log 덤프 에러 데이타 파일 작업 시작");
				try{
					destDir=servlet.DumpFailList+"/DATA/KEYWORD_LOG_"+cal.getTimeInMillis();
					saveObjectToFile(destDir,ikeyword);
				}catch(Exception ioe){
					logger.error("dumpExecute5:"+ioe);
				}
				logger.info(ikeyword.size() + " 개 keyworddata_log 덤프 에러 데이타 파일 작업 완료");
			}
			////////////////////////status_day_media(매체 노출 통계) ////////////////////////////////////////////
			/*
			logger.info(tempRobj.getNormalDayMediaLogData().size() + " 개 Status_day_media 데이타 덤프 시작");
			Vector<DayMediaLogData> dayMediaLogData = new Vector<DayMediaLogData>();
			for(int i=0;i<tempRobj.getNormalDayMediaLogData().size();i++){
				DayMediaLogData data=(DayMediaLogData) tempRobj.getNormalDayMediaLogData().get(i);
				if(target.equals("file") || dumpStatusDayMediaData(conn, data)==1){
					dayMediaLogData.add(data);
				}   
			}
			debug++;
			logger.info(tempRobj.getNormalDayMediaLogData().size()-dayMediaLogData.size() + " 개 Status_day_media 데이타 덤프 완료");
			if(dayMediaLogData.size()>0){
				logger.info(dayMediaLogData.size() + " 개 status_day_media 덤프 에러 데이타 파일 작업 시작");
				try{
					destDir=servlet.DumpFailList+"/DATA/STATUS_DAY_MEDIA_LOG_"+cal.getTimeInMillis();
					saveObjectToFile(destDir,dayMediaLogData);
				}catch(Exception ioe){
					logger.error("dumpExecute8:"+ioe);
				}
				logger.info(dayMediaLogData.size() + " 개 status_day_media 덤프 에러 데이타 파일 작업 완료");
			}
			*/
			////////////////////////DrcData ////////////////////////////////////////////
			logger.info(tempRobj.getDrcData().size() + " 개 DrcData 데이타 덤프 시작");
			Vector<DrcData> drcdata = new Vector<DrcData>();
			for(int i=0;i<tempRobj.getDrcData().size();i++){
				DrcData data=(DrcData) tempRobj.getDrcData().get(i);
				if(servlet.instance.dbStatus==false || target.equals("file") || dumpDrcData(conn, data)==1){
					drcdata.add(data);
				}   
			}
			debug++;
			logger.info(tempRobj.getDrcData().size()-drcdata.size() + " 개 DrcData 데이타 덤프 완료");
			if(drcdata.size()>0){
				logger.info(drcdata.size() + " 개 DrcData 덤프 에러 데이타 파일 작업 시작");
				try{
					destDir=servlet.DumpFailList+"/DATA/DRCDATA_"+cal.getTimeInMillis();
					saveObjectToFile(destDir,drcdata);
				}catch(Exception ioe){
					logger.error("dumpExecute9:"+ioe);
				}
				logger.info(drcdata.size() + " 개 DrcData 덤프 에러 데이타 파일 작업 완료");
			}

			
			//////////////////////// sky charge ////////////////////////////////////////////
			logger.info(tempRobj.getSkyChargeData().size() + " 개 SKYCHARGE 데이타 덤프 시작");
			Vector<AdChargeData> scharge = new Vector<AdChargeData>();
			for(int i=0;i<tempRobj.getSkyChargeData().size();i++){
				AdChargeData data=(AdChargeData) tempRobj.getSkyChargeData().get(i);
				if(servlet.instance.dbStatus==false || target.equals("file") || dumpSkyChargeData(conn, data)==1){
					scharge.add(data);
				}   
			}
			debug++;
			logger.info(tempRobj.getSkyChargeData().size()-scharge.size() + " 개 SKYCHARGE 데이타 덤프 완료");
			if(scharge.size()>0){
				logger.info(scharge.size() + " 개 SKYCHARGE 덤프 에러 데이타 파일 작업 시작");
				try{
					destDir=servlet.DumpFailList+"/DATA/SKYCHARGE_"+cal.getTimeInMillis();
					saveObjectToFile(destDir,scharge);
				}catch(Exception ioe){
					logger.error("dumpExecute11:"+ioe);
				}
				logger.info(scharge.size() + " 개 SKYCHARGE 덤프 에러 데이타 파일 작업 완료");
			}


			////////////////////////ShopConData ////////////////////////////////////////////
			logger.info(tempRobj.getShopConData().size() + " 개 ShopConData 데이타 덤프 시작");
			Vector<DrcData> shopcon = new Vector<DrcData>();
			for(int i=0;i<tempRobj.getShopConData().size();i++){
				DrcData data=(DrcData) tempRobj.getShopConData().get(i);
				if(servlet.instance.dbStatus==false || target.equals("file") || dumpShopConData(conn, data)==1){
					shopcon.add(data);
				}   
			}
			debug++;
			logger.info(tempRobj.getShopConData().size()-shopcon.size() + " 개 ShopConData 데이타 덤프 완료");
			if(shopcon.size()>0){
				logger.info(shopcon.size() + " 개 ShopConData 덤프 에러 데이타 파일 작업 시작");
				try{
					destDir=servlet.DumpFailList+"/DATA/SHOPCONDATA_"+cal.getTimeInMillis();
					saveObjectToFile(destDir,shopcon);
				}catch(Exception ioe){
					logger.error("dumpExecute12:"+ioe);
				}
				logger.info(shopcon.size() + " 개 ShopConData 덤프 에러 데이타 파일 작업 완료");
			}
			
			////////////////////////conversion_log ////////////////////////////////////////////
			logger.info(tempRobj.getConvLogData().size() + " 개 CONVERSION_LOG 데이타 덤프 시작");
			Vector<AdChargeData> conv = new Vector<AdChargeData>();
			for(int i=0;i<tempRobj.getConvLogData().size();i++){
				AdChargeData data=(AdChargeData) tempRobj.getConvLogData().get(i);
				//if(target.equals("file") || dumpChargeLogData(conn, data)==1){
				if(servlet.instance.dbStatus==false || servlet.instance.getDumpThreadCounter()>2 || dumpConvLogData(conn, data)==1){
					conv.add(data);
				}   
			}
			debug++;
			logger.info(tempRobj.getConvLogData().size()-conv.size() + " 개 CONVERSION_LOG 데이타 덤프 완료");
			if(conv.size()>0){
				logger.info(conv.size() + " 개 CONVERSION_LOG 덤프 에러 데이타 파일 작업 시작");
				try{
					destDir=servlet.DumpFailList+"/DATA/CONVERSION_LOG_"+cal.getTimeInMillis();
					saveObjectToFile(destDir,conv);
				}catch(Exception ioe){
					logger.error("dumpExecute6:"+ioe);
				}
				logger.info(conv.size() + " 개 CONVERSION_LOG 덤프 에러 데이타 파일 작업 완료");
			}
			
			////////////////////////상품 통계 덤프 ////////////////////////////////////////////
			logger.info(tempRobj.getShopStatsData().size() + " 개 shopstats 데이타 초기화 덤프 시작");
			servlet.instance.dbStatus=dumpCheckProc(conn);
			Vector<ShopStatsData> shopStat = new Vector<ShopStatsData>();
			Iterator it = tempRobj.getShopStatsData().keySet().iterator();
			while(it.hasNext()){
				ShopStatsData data=(ShopStatsData) tempRobj.getShopStatsData().get(it.next());
				if(servlet.instance.dbStatus==false || servlet.instance.getDumpThreadCounter()>5 || dumpShopStatsData(conn, data)==1){
					shopStat.add(data);
				}
			}
			logger.info(tempRobj.getShopStatsData().size()-shopStat.size() + " 개 shopstats 데이타 초기화 덤프 완료");
			if(shopStat.size()>0){
				logger.info(shopStat.size() + " 개 shopstats 초기화 덤프 에러 데이타 파일 작업 시작");
				try{
					destDir=servlet.DumpFailList+"/DATA/SHOPSTATS_INIT_"+cal.getTimeInMillis();
					saveObjectToFile(destDir,shopStat);
				}catch(Exception ioe){
					logger.error("dumpExecute10:"+ioe);
				}
				logger.info(shopStat.size() + " 개 shopstats 초기화 덤프 에러 데이타 파일 작업 완료");
			}
			
			/*
			////////////////////////SDrcData ////////////////////////////////////////////
			logger.info(tempRobj.getSkyDrcData().size() + " 개 SDrcData 데이타 덤프 시작");
			Vector<DrcData> sdrcdata = new Vector<DrcData>();
			for(int i=0;i<tempRobj.getSkyDrcData().size();i++){
				DrcData data=(DrcData) tempRobj.getSkyDrcData().get(i);
				if(servlet.instance.dbStatus==false || target.equals("file") || dumpSkyDrcData(conn, data)==1){
					sdrcdata.add(data);
				}   
			}
			debug++;
			logger.info(tempRobj.getSkyDrcData().size()-sdrcdata.size() + " 개 SDrcData 데이타 덤프 완료");
			if(sdrcdata.size()>0){
				logger.info(sdrcdata.size() + " 개 SDrcData 덤프 에러 데이타 파일 작업 시작");
				try{
					destDir=servlet.DumpFailList+"/DATA/SDRCDATA_"+cal.getTimeInMillis();
					saveObjectToFile(destDir,sdrcdata);
				}catch(Exception ioe){
					logger.error("dumpExecute9:"+ioe);
				}
				logger.info(sdrcdata.size() + " 개 SDrcData 덤프 에러 데이타 파일 작업 완료");
			}*/
			FileUtil.reWriteFile("/home/dreamsearch/public_html/sysmon/resetChk.jsp","true");
		} catch (SQLException e) {
			logger.error("dumpExecute9:"+e+"debug="+debug);
		}catch(Exception e){
			logger.error("dumpExecute10:"+e+"debug="+debug);
			FileUtil.reWriteFile("/home/dreamsearch/public_html/sysmon/resetChk.jsp","false");
		}finally{
			try {	if(conn !=null)	conn.close();	} catch (SQLException e) {}
		}
	}
	public void failDataDump() throws ClassNotFoundException{
		File dir=new File(servlet.DumpFailList+"/DATA");
		File[] files=dir.listFiles();
		if( files==null ) {
			logger.error("failDataDump() default path notfound ");
			return;
		}
		Connection conn=null;
		try{
			logger.info("failDataDump 실행 :"+ files.length + " 개 작업 시작.");
			conn=DBManager.getConnection("dreamdb");
			for(int i=0;i<files.length;i++){
				//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				//if( !sdf.format( files[i].lastModified() ).equals(DateUtils.getDate("yyyy-MM-dd")) ){
				//	FileUtil.fileCopy( files[i].getAbsolutePath(), servlet.DumpFailList+"/DUMPFAIL/"+files[i].getName() );
				//	files[i].delete();
				//	logger.info(files[i].getAbsolutePath() +" 날짜지남");
				//	continue;
				//}
				FileInputStream fileinput = null;
				ObjectInputStream in=null;
				try{
					fileinput = new FileInputStream(files[i].getAbsolutePath());
					logger.info(files[i].getAbsolutePath() + " 에러 파일 처리 시작");
					in = new ObjectInputStream(fileinput); 
					Vector obj=(Vector)in.readObject();

					if(files[i].getName().indexOf("SHOP_LOG_")>-1){
						logger.info("SHOP_LOG FailDump Start:"+files[i].getName());
						StringBuffer dumpFailTxtData=new StringBuffer();
						Vector<ShopData> sv = new Vector<ShopData>();
						Vector<ShopData> fv = new Vector<ShopData>();
						for(int cnt=0;cnt<obj.size();cnt++){
							ShopData data=(ShopData)obj.get(cnt);
							if(data==null) continue;
							if(dumpShopData(conn,data)==0){
								sv.add((ShopData)data);
							}else{
								fv.add((ShopData)data);
								dumpFailTxtData.append(ReflectionToStringBuilder.toString(data)+"\n");
							}
						}
						if(sv.size()>0){
							saveObjectToFile(servlet.DumpFailList+"/DUMPOK/"+files[i].getName(),sv);
							logger.info("SHOP_LOG "+ sv.size()+" 개 재처리 완료.");
						}
						if(fv.size()>0){
							saveObjectToFile(servlet.DumpFailList+"/DUMPFAIL/"+files[i].getName(),fv);
							PrintFile.write(servlet.DumpFailList+"/DUMPFAIL/"+files[i].getName()+".txt", dumpFailTxtData.toString());
							logger.info("SHOP_LOG "+ fv.size()+" 개 재처리 실패.");
						}
						logger.debug("SHOP_LOG FailDump End:"+files[i].getName());
						
					}else if(files[i].getName().indexOf("RF_DATA_")>-1){
						logger.info("RF FailDump Start:"+files[i].getName());
						StringBuffer dumpFailTxtData=new StringBuffer();
						Vector<RFData> sv = new Vector<RFData>();
						Vector<RFData> fv = new Vector<RFData>();
						for(int cnt=0;cnt<obj.size();cnt++){
							RFData data=(RFData)obj.get(cnt);
							if(data==null) continue;
							if(dumpRfData(conn,data)==0){
								sv.add((RFData)data);
							}else{
								fv.add((RFData)data);
								dumpFailTxtData.append(ReflectionToStringBuilder.toString(data)+"\n");
							}
						}
						if(sv.size()>0){
							saveObjectToFile(servlet.DumpFailList+"/DUMPOK/"+files[i].getName(),sv);
							logger.info("RF_DATA_"+ sv.size()+" 개 재처리 완료.");
						}
						if(fv.size()>0){
							saveObjectToFile(servlet.DumpFailList+"/DUMPFAIL/"+files[i].getName(),fv);
							PrintFile.write(servlet.DumpFailList+"/DUMPFAIL/"+files[i].getName()+".txt", dumpFailTxtData.toString());
							logger.info("RF_DATA_"+ fv.size()+" 개 재처리 실패.");
						} 
						logger.debug("RF FailDump End:"+files[i].getName());
						
					}else if(files[i].getName().indexOf("RF_KEYWORD_")>-1){
						logger.info("RF_KEYWORD FailDump Start:"+files[i].getName());
						StringBuffer dumpFailTxtData=new StringBuffer();
						Vector<RFData> sv = new Vector<RFData>();
						Vector<RFData> fv = new Vector<RFData>();
						for(int cnt=0;cnt<obj.size();cnt++){
							RFData data=(RFData)obj.get(cnt);
							if(data==null) continue;
							if(dumpRf_KeywordData(conn,data)==0){
								sv.add((RFData)data);
							}else{
								fv.add((RFData)data);
								dumpFailTxtData.append(ReflectionToStringBuilder.toString(data)+"\n");
							}
						}
						if(sv.size()>0){
							saveObjectToFile(servlet.DumpFailList+"/DUMPOK/"+files[i].getName(),sv);
							logger.info("RF_KEYWORD_"+ sv.size()+" 개 재처리 완료.");
						}
						if(fv.size()>0){
							saveObjectToFile(servlet.DumpFailList+"/DUMPFAIL/"+files[i].getName(),fv);
							PrintFile.write(servlet.DumpFailList+"/DUMPFAIL/"+files[i].getName()+".txt", dumpFailTxtData.toString());
							logger.info("RF_KEYWORD_"+ fv.size()+" 개 재처리 실패.");
						}
						logger.debug("RF_KEYWORD FailDump End:"+files[i].getName());
						
					}else if(files[i].getName().indexOf("MEDIA_LOG_")==0){
						logger.debug("MEDIA_LOG FailDump Start:"+files[i].getName());
						StringBuffer dumpFailTxtData=new StringBuffer();
						Vector<MediaLogData> sv = new Vector<MediaLogData>();
						Vector<MediaLogData> fv = new Vector<MediaLogData>();
						for(int cnt=0;cnt<obj.size();cnt++){
							MediaLogData data=(MediaLogData)obj.get(cnt);
							if(data==null) continue;
							if(dumpMediaLogData(conn,data)==0){
								sv.add((MediaLogData)data);
							}else{
								fv.add((MediaLogData)data);
								dumpFailTxtData.append(ReflectionToStringBuilder.toString(data)+"\n");
							}
						}
						if(sv.size()>0)	saveObjectToFile(servlet.DumpFailList+"/DUMPOK/"+files[i].getName(),sv);
						if(fv.size()>0){
							saveObjectToFile(servlet.DumpFailList+"/DUMPFAIL/"+files[i].getName(),fv);
							PrintFile.write(servlet.DumpFailList+"/DUMPFAIL/"+files[i].getName()+".txt", dumpFailTxtData.toString());
						}
						logger.debug("MEDIA_LOG FailDump End:"+files[i].getName());
						
					}else if(files[i].getName().indexOf("KEYWORD_LOG_")>-1){
						logger.info("KEYWORD_LOG FailDump Start:"+files[i].getName());
						StringBuffer dumpFailTxtData=new StringBuffer();
						Vector<RFData> sv = new Vector<RFData>();
						Vector<RFData> fv = new Vector<RFData>();
						for(int cnt=0;cnt<obj.size();cnt++){
							RFData data=(RFData)obj.get(cnt);
							if(data==null) continue;
							if(dumpKeywordLogData(conn,data)==0){
								sv.add((RFData)data);
							}else{
								fv.add((RFData)data);
								dumpFailTxtData.append(ReflectionToStringBuilder.toString(data)+"\n");
							}
						}
						if(sv.size()>0){
							saveObjectToFile(servlet.DumpFailList+"/DUMPOK/"+files[i].getName(),sv);
							logger.info("KEYWORD_LOG "+ sv.size()+" 개 재처리 완료.");
						}
						if(fv.size()>0){
							saveObjectToFile(servlet.DumpFailList+"/DUMPFAIL/"+files[i].getName(),fv);
							PrintFile.write(servlet.DumpFailList+"/DUMPFAIL/"+files[i].getName()+".txt", dumpFailTxtData.toString());
							logger.info("KEYWORD_LOG "+ fv.size()+" 개 재처리 완료.");
						}
						logger.debug("KEYWORD_LOG FailDump End:"+files[i].getName());
						
					}else if(files[i].getName().indexOf("CONVERSION_LOG_")>-1){
						logger.info("CONVERSION_LOG_ FailDump Start:"+files[i].getName());
						StringBuffer dumpFailTxtData=new StringBuffer();
						Vector<AdChargeData> sv = new Vector<AdChargeData>();
						Vector<AdChargeData> fv = new Vector<AdChargeData>();
						for(int cnt=0;cnt<obj.size();cnt++){
							AdChargeData data=(AdChargeData)obj.get(cnt);
							if(data==null) continue;
							if(dumpConvLogData(conn,data)==0){
								sv.add((AdChargeData)data);
							}else{
								fv.add((AdChargeData)data);
								dumpFailTxtData.append(ReflectionToStringBuilder.toString(data)+"\n");
							}
						}
						if(sv.size()>0){
							saveObjectToFile(servlet.DumpFailList+"/DUMPOK/"+files[i].getName(),sv);
							logger.info("CONVERSION_LOG_ "+ sv.size()+" 개 재처리 완료.");
						}
						if(fv.size()>0){
							saveObjectToFile(servlet.DumpFailList+"/DUMPFAIL/"+files[i].getName(),fv);
							PrintFile.write(servlet.DumpFailList+"/DUMPFAIL/"+files[i].getName()+".txt", dumpFailTxtData.toString());
							logger.info("CONVERSION_LOG_ "+ fv.size()+" 개 재처리 실패.");
						}
						logger.debug("CHARGE_LOG FailDump End:"+files[i].getName());
						
					}else if(files[i].getName().indexOf("ICOVERCHARGE_LOG_")>-1){
						logger.info("ICOVERCHARGE_LOG_ FailDump Start:"+files[i].getName());
						StringBuffer dumpFailTxtData=new StringBuffer();
						Vector<AdChargeData> sv = new Vector<AdChargeData>();
						Vector<AdChargeData> fv = new Vector<AdChargeData>();
						for(int cnt=0;cnt<obj.size();cnt++){
							AdChargeData data=(AdChargeData)obj.get(cnt);
							if(data==null) continue;
							if(dumpChargeLogData(conn,data)==0){
								sv.add((AdChargeData)data);
							}else{
								fv.add((AdChargeData)data);
								dumpFailTxtData.append(ReflectionToStringBuilder.toString(data)+"\n");						
							}
						}
						if(sv.size()>0){
							saveObjectToFile(servlet.DumpFailList+"/DUMPOK/"+files[i].getName(),sv);
							logger.info("ICOVERCHARGE_LOG_ "+ sv.size()+" 개 재처리 완료.");
						}
						if(fv.size()>0){
							saveObjectToFile(servlet.DumpFailList+"/DUMPFAIL/"+files[i].getName(),fv);
							PrintFile.write(servlet.DumpFailList+"/DUMPFAIL/"+files[i].getName()+".txt", dumpFailTxtData.toString());
							logger.info("ICOVERCHARGE_LOG_ "+ fv.size()+" 개 재처리 실패.");
						}
						logger.debug("CHARGE_LOG FailDump End:"+files[i].getName());
						
					}else if(files[i].getName().indexOf("MOBILECHARGE_LOG_")>-1){
						logger.info("MOBILECHARGE_LOG FailDump Start:"+files[i].getName());
						StringBuffer dumpFailTxtData=new StringBuffer();
						Vector<AdChargeData> sv = new Vector<AdChargeData>();
						Vector<AdChargeData> fv = new Vector<AdChargeData>();
						for(int cnt=0;cnt<obj.size();cnt++){
							AdChargeData data=(AdChargeData)obj.get(cnt);
							if(data==null) continue;
							if(dumpMobileChargeLogData(conn,data)==0){
								sv.add((AdChargeData)data);
							}else{
								fv.add((AdChargeData)data);
								dumpFailTxtData.append(ReflectionToStringBuilder.toString(data)+"\n");							
							}
						}
						if(sv.size()>0){
							saveObjectToFile(servlet.DumpFailList+"/DUMPOK/"+files[i].getName(),sv);
							logger.info("MOBILECHARGE_LOG "+ sv.size()+" 개 재처리 완료.");
						}
						if(fv.size()>0){
							saveObjectToFile(servlet.DumpFailList+"/DUMPFAIL/"+files[i].getName(),fv);
							PrintFile.write(servlet.DumpFailList+"/DUMPFAIL/"+files[i].getName()+".txt", dumpFailTxtData.toString());
							logger.info("MOBILECHARGE_LOG "+ fv.size()+" 개 재처리 실패.");
						}
						logger.debug("MOBILECHARGE_LOG FailDump End:"+files[i].getName());
						
					}else if(files[i].getName().indexOf("NORMALCHARGE_LOG_")>-1){
							logger.info("NORMALCHARGE_LOG FailDump Start:"+files[i].getName());
							StringBuffer dumpFailTxtData=new StringBuffer();
							Vector<AdChargeData> sv = new Vector<AdChargeData>();
							Vector<AdChargeData> fv = new Vector<AdChargeData>();
							for(int cnt=0;cnt<obj.size();cnt++){
								AdChargeData data=(AdChargeData)obj.get(cnt);
								if(data==null) continue;
								if(dumpNormalChargeLogData(conn,data)==0){
									sv.add((AdChargeData)data);
								}else{
									fv.add((AdChargeData)data);
									dumpFailTxtData.append(ReflectionToStringBuilder.toString(data)+"\n");							
								}
							}
							if(sv.size()>0){
								saveObjectToFile(servlet.DumpFailList+"/DUMPOK/"+files[i].getName(),sv);
								logger.info("NORMALCHARGE_LOG "+ sv.size()+" 개 재처리 완료.");
							}
							if(fv.size()>0){
								saveObjectToFile(servlet.DumpFailList+"/DUMPFAIL/"+files[i].getName(),fv);
								PrintFile.write(servlet.DumpFailList+"/DUMPFAIL/"+files[i].getName()+".txt", dumpFailTxtData.toString());
								logger.info("NORMALCHARGE_LOG "+ fv.size()+" 개 재처리 실패.");
							}
							logger.debug("NORMALCHARGE_LOG FailDump End:"+files[i].getName());
							
					}/*else if(files[i].getName().indexOf("STATUS_DAY_MEDIA_LOG_")==0){
						logger.info("STATUS_DAY_MEDIA_LOG_ FailDump Start:"+files[i].getName());
						StringBuffer dumpFailTxtData=new StringBuffer();
						Vector<DayMediaLogData> sv = new Vector<DayMediaLogData>();
						Vector<DayMediaLogData> fv = new Vector<DayMediaLogData>();
						for(int cnt=0;cnt<obj.size();cnt++){
							DayMediaLogData data=(DayMediaLogData)obj.get(cnt);
							if(data==null) continue;
							if(dumpStatusDayMediaData(conn,data)==0){
								sv.add((DayMediaLogData)data);
							}else{
								fv.add((DayMediaLogData)data);
								dumpFailTxtData.append(ReflectionToStringBuilder.toString(data)+"\n");
								logger.debug("data.getMC()="+data.getMC());
								logger.debug("data.getMCODE()="+data.getMCODE());
								logger.debug("data.getSDATE()="+data.getSDATE());
								logger.debug("data.getVIEWCNT()="+data.getVIEWCNT());
								logger.debug("data.getCLICKCNT()="+data.getCLICKCNT());
								logger.debug("data.getPOINT()="+data.getPOINT());
								logger.debug("data.getGUBUN()="+data.getGUBUN());
								logger.debug("data.getMEDIA_ID()="+data.getMEDIA_ID());
							}
						}
						if(sv.size()>0){
							saveObjectToFile(servlet.DumpFailList+"/DUMPOK/"+files[i].getName(),sv);
							logger.info("STATUS_DAY_MEDIA_LOG_ "+ sv.size()+" 개 재처리 완료.");
						}
						if(fv.size()>0){
							saveObjectToFile(servlet.DumpFailList+"/DUMPFAIL/"+files[i].getName(),fv);
							PrintFile.write(servlet.DumpFailList+"/DUMPFAIL/"+files[i].getName()+".txt", dumpFailTxtData.toString());
							logger.info("STATUS_DAY_MEDIA_LOG_ "+ fv.size()+" 개 재처리 실패.");
						}
						logger.debug("NORMALCHARGE_LOG FailDump End:"+files[i].getName());
					
					}*/else if(files[i].getName().indexOf("DRCDATA_")>-1){
						logger.info("DRCDATA FailDump Start:"+files[i].getName());
						StringBuffer dumpFailTxtData=new StringBuffer();
						Vector<DrcData> sv = new Vector<DrcData>();
						Vector<DrcData> fv = new Vector<DrcData>();
						for(int cnt=0;cnt<obj.size();cnt++){
							DrcData data=(DrcData)obj.get(cnt);
							if(data==null) continue;
							if(dumpDrcData(conn,data)==0){
								sv.add((DrcData)data);
							}else{
								fv.add((DrcData)data);
								dumpFailTxtData.append(ReflectionToStringBuilder.toString(data)+"\n");
								logger.debug("data.getKeyIp="+data.getKeyIp());
								logger.debug("getU="+data.getU());
								logger.debug("data.getGb="+data.getGb());
								logger.debug("data.getSc="+data.getSc());
								logger.debug("data.getS="+data.getS());
								logger.debug("data.getMc="+data.getMc());
								logger.debug("data.getKno="+data.getKno());
							}
						}
						if(sv.size()>0){
							saveObjectToFile(servlet.DumpFailList+"/DUMPOK/"+files[i].getName(),sv);
							logger.info("DRCDATA "+ sv.size()+" 개 재처리 완료.");
						}
						if(fv.size()>0){
							saveObjectToFile(servlet.DumpFailList+"/DUMPFAIL/"+files[i].getName(),fv);
							PrintFile.write(servlet.DumpFailList+"/DUMPFAIL/"+files[i].getName()+".txt", dumpFailTxtData.toString());
							logger.info("DRCDATA "+ fv.size()+" 개 재처리 실패.");
						}
						logger.debug("DRCDATA FailDump End:"+files[i].getName());
					
					}else if(files[i].getName().indexOf("SHOPLOG_INIT_")>-1){
						logger.info("SHOPLOG_INIT FailDump Start:"+files[i].getName());
						StringBuffer dumpFailTxtData=new StringBuffer();
						Vector<ShopData> sv = new Vector<ShopData>();
						Vector<ShopData> fv = new Vector<ShopData>();
						for(int cnt=0;cnt<obj.size();cnt++){
							ShopData data=(ShopData)obj.get(cnt);
							if(data==null) continue;
							if(delShopLogData(conn,data)==0){
								sv.add((ShopData)data);
							}else{
								fv.add((ShopData)data);
								dumpFailTxtData.append(data.getIP()+",");
								dumpFailTxtData.append(data.getGB()+",");
								dumpFailTxtData.append(data.getNO());
								logger.debug("data.getIP()="+data.getIP());
								logger.debug("data.getGB()="+data.getGB());
								logger.debug("data.getNO()="+data.getNO());
							}
						}
						if(sv.size()>0){
							saveObjectToFile(servlet.DumpFailList+"/DUMPOK/"+files[i].getName(),sv);
							logger.info("SHOPLOG_INIT "+ sv.size()+" 개 재처리 완료.");
						}
						if(fv.size()>0){
							saveObjectToFile(servlet.DumpFailList+"/DUMPFAIL/"+files[i].getName(),fv);
							PrintFile.write(servlet.DumpFailList+"/DUMPFAIL/"+files[i].getName()+".txt", dumpFailTxtData.toString());
							logger.info("SHOPLOG_INIT "+ fv.size()+" 개 재처리 실패.");
						}
						logger.debug("SHOPLOG_INIT FailDump End:"+files[i].getName());
						
					}else if(files[i].getName().indexOf("SKYCHARGE_")>-1){
						logger.info("SKYCHARGE_LOG FailDump Start:"+files[i].getName());
						StringBuffer dumpFailTxtData=new StringBuffer();
						Vector<AdChargeData> sv = new Vector<AdChargeData>();
						Vector<AdChargeData> fv = new Vector<AdChargeData>();
						for(int cnt=0;cnt<obj.size();cnt++){
							AdChargeData data=(AdChargeData)obj.get(cnt);
							if(data==null) continue;
							if(dumpSkyChargeData(conn,data)==0){
								sv.add((AdChargeData)data);
							}else{
								fv.add((AdChargeData)data);
								dumpFailTxtData.append(ReflectionToStringBuilder.toString(data)+"\n");						
							}
						}
						if(sv.size()>0){
							saveObjectToFile(servlet.DumpFailList+"/DUMPOK/"+files[i].getName(),sv);
							logger.info("SKYCHARGE_LOG "+ sv.size()+" 개 재처리 완료.");
						}
						if(fv.size()>0){
							saveObjectToFile(servlet.DumpFailList+"/DUMPFAIL/"+files[i].getName(),fv);
							PrintFile.write(servlet.DumpFailList+"/DUMPFAIL/"+files[i].getName()+".txt", dumpFailTxtData.toString());
							logger.info("SKYCHARGE_LOG "+ fv.size()+" 개 재처리 실패.");
						}
						logger.debug("SKYCHARGE_LOG FailDump End:"+files[i].getName());
						
					}else if(files[i].getName().indexOf("SHOPSTATS_INIT_")>-1){
						logger.info("SHOPSTATS FailDump Start:"+files[i].getName());
						StringBuffer dumpFailTxtData=new StringBuffer();
						Vector<ShopStatsData> sv = new Vector<ShopStatsData>();
						Vector<ShopStatsData> fv = new Vector<ShopStatsData>();
						for(int cnt=0;cnt<obj.size();cnt++){
							ShopStatsData data=(ShopStatsData)obj.get(cnt);
							if(data==null) continue;
							if(dumpShopStatsData(conn,data)==0){
								sv.add((ShopStatsData)data);
							}else{
								fv.add((ShopStatsData)data);
								dumpFailTxtData.append(ReflectionToStringBuilder.toString(data)+"\n");
							}
						}
						if(sv.size()>0){
							saveObjectToFile(servlet.DumpFailList+"/DUMPOK/"+files[i].getName(),sv);
							logger.info("SHOPSTATS "+ sv.size()+" 개 재처리 완료.");
						}
						if(fv.size()>0){
							saveObjectToFile(servlet.DumpFailList+"/DUMPFAIL/"+files[i].getName(),fv);
							PrintFile.write(servlet.DumpFailList+"/DUMPFAIL/"+files[i].getName()+".txt", dumpFailTxtData.toString());
							logger.info("SHOPSTATS "+ fv.size()+" 개 재처리 실패.");
						}
						logger.debug("SKYCHARGE_LOG FailDump End:"+files[i].getName());
						
					}else if(files[i].getName().indexOf("SHOPCONDATA_")>-1){
						logger.info("SHOPCONATA FailDump Start:"+files[i].getName());
						StringBuffer dumpFailTxtData=new StringBuffer();
						Vector<DrcData> sv = new Vector<DrcData>();
						Vector<DrcData> fv = new Vector<DrcData>();
						for(int cnt=0;cnt<obj.size();cnt++){
							DrcData data=(DrcData)obj.get(cnt);
							if(data==null) continue;
							if(dumpShopConData(conn,data)==0){
								sv.add((DrcData)data);
							}else{
								fv.add((DrcData)data);
								dumpFailTxtData.append(ReflectionToStringBuilder.toString(data)+"\n");
								logger.debug(data.toString());
							}
						}
						if(sv.size()>0){
							saveObjectToFile(servlet.DumpFailList+"/DUMPOK/"+files[i].getName(),sv);
							logger.info("SHOPCONDATA "+ sv.size()+" 개 재처리 완료.");
						}
						if(fv.size()>0){
							saveObjectToFile(servlet.DumpFailList+"/DUMPFAIL/"+files[i].getName(),fv);
							PrintFile.write(servlet.DumpFailList+"/DUMPFAIL/"+files[i].getName()+".txt", dumpFailTxtData.toString());
							logger.info("SHOPCONDATA "+ fv.size()+" 개 재처리 실패.");
						}
						logger.debug("SHOPCONATA FailDump End:"+files[i].getName());
					}
				}catch(IOException e){
					logger.error("failDataDump1:"+e);
				}catch(Exception e){
					logger.error("failDataDump2:"+e);
				}finally{
					try{	if(in !=null) in.close();
					}catch(Exception ex){}
					try{	if(fileinput !=null) fileinput.close();
					}catch(Exception ex){}
				}
				files[i].delete();
			}
			logger.info("failDataDump 실행 :"+ files.length + " 개 작업 완료.");
		}catch(Exception e){
			logger.error("failDataDump3:"+e);
		}finally{
			try {	if(conn !=null)	conn.close();	} catch (SQLException e) {}
		}
	}
	private void saveObjectToFile(String destDir,Vector v){
		FileOutputStream fileout = null;
		ObjectOutputStream out=null;
		try{
			fileout=new FileOutputStream(destDir);
			out = new ObjectOutputStream(fileout);
			out.writeObject(v);
		}catch(IOException e){
			logger.error("saveObjectToFile:"+e);
		}finally{
			try{	if(out !=null) out.close();
			}catch(Exception ex){}
			try{	if(fileout !=null) fileout.close();
			}catch(Exception ex){}
		}
	}
	private boolean dumpCheckProc(Connection conn){
		StringBuffer sql=new StringBuffer();
		Statement stmt=null;
		try{
			InetAddress addr=InetAddress.getLocalHost();
			String host=addr.getHostAddress();
			Calendar cal=Calendar.getInstance();
			String minute=cal.get(Calendar.MINUTE)<10 ? "0"+cal.get(Calendar.MINUTE) : cal.get(Calendar.MINUTE)+"";
			String second=cal.get(Calendar.SECOND)<10 ? "0"+cal.get(Calendar.SECOND) : cal.get(Calendar.SECOND)+""; 
			String miliSecond="";
			if(cal.get(Calendar.MILLISECOND)<10){
				miliSecond="00"+cal.get(Calendar.MILLISECOND);
			}else if(cal.get(Calendar.MILLISECOND)<100){
				miliSecond="0"+cal.get(Calendar.MILLISECOND);
			}else{
				miliSecond=cal.get(Calendar.MILLISECOND)+"";
			}
			
			int chkNum=Integer.parseInt(minute+""+second+""+miliSecond);
			stmt=conn.createStatement();
			sql.append(" update tb_autoappchk ");
			sql.append(" set chknum="+chkNum+",lastupdate=now() ");
			sql.append(" where appname='"+host+"'");
			logger.debug(sql.toString());
			stmt.executeUpdate(sql.toString());
			return true;
		}catch(Exception e){
			logger.error("dumpCheckProc:"+e);
			return false;
		}finally{
			try{	if(stmt !=null) stmt.close();	}catch(Exception e){}
		}
	}
	private int dumpRfData(Connection conn,RFData data) throws IOException{
		CallableStatement cstmt=null;
		try{
			logger.debug(data.getURL());
			logger.debug(data.getCURL());
			logger.debug(data.getMCGB());
			logger.debug(data.getRF());
			logger.debug(data.getSC_TXT());
			logger.debug(data.getIP());
			logger.debug(data.getRDATE());
			logger.debug(data.getRTIME());
			logger.debug(StringUtils.getPartIdKey(data.getIP()));
			logger.debug("call proc_RfDataDump");
			
			cstmt =conn.prepareCall("{	 call proc_RfDataDump(?,?,?,?,?,?,?,?,?)}");
			cstmt.setString(1,data.getURL()+"");
			cstmt.setString(2,data.getCURL());
			cstmt.setString(3,data.getMCGB());    
			cstmt.setString(4,data.getRF());
			cstmt.setString(5,data.getSC_TXT());
			cstmt.setString(6,data.getIP());
			cstmt.setString(7,data.getRDATE());
			cstmt.setString(8,data.getRTIME());
			cstmt.setString(9,StringUtils.getPartIdKey(data.getIP()));
			cstmt.executeUpdate();
			return 0;
		}catch(Exception e){
			logger.error(data.getURL());
			logger.error(data.getCURL());
			logger.error(data.getMCGB());
			logger.error(data.getRF());
			logger.error(data.getSC_TXT());
			logger.error(data.getIP());
			logger.error(data.getRDATE());
			logger.error(data.getRTIME());
			logger.error(StringUtils.getPartIdKey(data.getIP()));
			logger.info("call proc_RfDataDump");
			logger.error("dumpRfData:"+e);
			e.printStackTrace();
			return 1;
		}finally{
			try{
				if(cstmt !=null) cstmt.close();
			}catch(Exception e){}
		}
	}
	private int dumpRf_KeywordData(Connection conn,RFData data) throws IOException{
		CallableStatement cstmt=null;
		try{
			logger.debug(data.toString() +" "+ StringUtils.getPartIdKey(data.getIP()));
			logger.debug(StringUtils.getPartIdKey(data.getIP()));
			logger.debug("call proc_Rf_KeywordDataDump");
			
			cstmt =conn.prepareCall("{	 call proc_Rf_KeywordDataDump(?,?,?,?,?,?,?,?,?)}");
			cstmt.setString(1,data.getURL()+"");
			cstmt.setString(2,data.getCURL());
			cstmt.setString(3,data.getMCGB());    
			cstmt.setString(4,data.getRF());
			cstmt.setString(5,data.getSC_TXT());
			cstmt.setString(6,data.getIP());
			cstmt.setString(7,data.getRDATE());
			cstmt.setString(8,data.getRTIME());
			cstmt.setString(9,StringUtils.getPartIdKey(data.getIP()));
			cstmt.executeUpdate();
			return 0;
		}catch(Exception e){
			logger.error(data.getURL());
			logger.error(data.getCURL());
			logger.error(data.getMCGB());
			logger.error(data.getRF());
			logger.error(data.getSC_TXT());
			logger.error(data.getIP());
			logger.error(data.getRDATE());
			logger.error(data.getRTIME());
			logger.error(StringUtils.getPartIdKey(data.getIP()));
			logger.info("call proc_Rf_KeywordDataDump");
			logger.error("dumpRf_KeywordData:"+e);
			e.printStackTrace();
			return 1;
		}finally{
			try{
				if(cstmt !=null) cstmt.close();
			}catch(Exception e){
				logger.fatal(e.getMessage());
			}
		}
	}
	public int dumpShopData(Connection conn,ShopData data) throws IOException{
		CallableStatement cstmt=null;
		try{
			logger.debug(data.toString()+" "+ StringUtils.getPartIdKey(data.getIP()));
			logger.debug("call proc_Rf_KeywordDataDump");
			
			//data.setRF(java.net.URLEncoder.encode(data.getRF(),"euc-kr"));
			String url = data.getURL(); if( url.length()>200 ) url = url.substring(0, 199);
			String purl = data.getPURL(); if( purl.length()>200 ) purl=purl.substring(0, 199);
			String rf= data.getRF(); if( rf.length()>200 ) rf=rf.substring(0, 199);
			
			cstmt =conn.prepareCall("{	 call proc_ShopLogDataDump(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
			cstmt.setString(1,StringUtils.getPartIdKey(data.getIP()));
			cstmt.setString(2,url );
			cstmt.setString(3,data.getMCGB());
			cstmt.setString(4,rf);
			cstmt.setString(5,data.getSC_TXT());
			cstmt.setString(6,data.getPNM());
			cstmt.setString(7,data.getPCODE());
			cstmt.setString(8,data.getPRICE());
			cstmt.setString(9,data.getIMGPATH());
			cstmt.setString(10,purl);
			cstmt.setString(11,data.getGB());
			cstmt.setString(12,data.getSITE_CODE());
			cstmt.setString(13,data.getIP());
			cstmt.setString(14,data.getRDATE());
			cstmt.setString(15,data.getRTIME());
			cstmt.executeUpdate();
			return 0;
		}catch(Exception e){
			logger.info("call proc_Rf_KeywordDataDump");
			logger.error("dumpShopData:"+e);
			e.printStackTrace();
			return 1;
		}finally{
			try{
				if(cstmt !=null) cstmt.close();
			}catch(Exception e){
				logger.fatal(e);
			}
		}
	}
	private int dumpActData(Connection conn,AdChargeData data) throws IOException{
		CallableStatement cstmt=null;
		try{
			logger.debug(data.getInfo("dumpActData "));
			logger.debug("call proc_ActLogDataDump");
			
			cstmt =conn.prepareCall("{	 call proc_ActLogDataDump(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
			cstmt.setString(1,data.getYYYYMMDD());
			cstmt.setString(2,data.getIP());
			cstmt.setString(3,data.getPCODE());
			cstmt.setString(4,data.getNO());
			cstmt.setString(5,data.getSITE_CODE());
			cstmt.setString(6,data.getUSERID());
			cstmt.setString(7,data.getS());
			cstmt.setString(8,data.getSCRIPTUSERID());
			cstmt.setString(9,data.getKNO()+"");
			if(data.getTYPE().equals("V")){
				cstmt.setInt(10,data.getSRView2()+1);
				cstmt.setInt(11,data.getSRView2());
				cstmt.setInt(12,0);
			}else if(data.getTYPE().equals("C")){
				cstmt.setInt(10,0);
				cstmt.setInt(11,0);
				cstmt.setInt(12,1);
				if(data.getPCODE()!=null && data.getPCODE().length()>0){
					setShopStatsData(data.getUSERID(),data.getPCODE(),data,"adclick");
				}
			}
			cstmt.setString(13,data.getPOINT());
			cstmt.setString(14,data.getTYPE());
			cstmt.setString(15,data.getADGUBUN());
			cstmt.setString(16,data.getPRODUCT());
			cstmt.setString(17, data.getMCGB());
			cstmt.executeUpdate();
			return 0;
		}catch(Exception e){
			logger.info("call proc_ActLogDataDump");
			logger.error("dumpActData:"+e+" data="+data.toString());
			e.printStackTrace();
			return 1;
		}finally{
			try{
				if(cstmt !=null) cstmt.close();
			}catch(Exception e){
				logger.fatal(e);
			}
		}
	}
	private int dumpMediaLogData(Connection conn,MediaLogData data) throws IOException{
		CallableStatement cstmt=null;
		try{
			
//			logger.debug("getS="+data.getS());
//			logger.debug("getIP="+data.getIP());
//			logger.debug("getSDATE="+data.getSDATE());
//			logger.debug("getVIEWCNT="+data.getVIEWCNT());
//			logger.debug("getLogId="+data.getLogId());
//			logger.debug("call proc_Rf_KeywordDataDump");
//			
//			cstmt =conn.prepareCall("{	 call proc_MediaLogDataDump(?,?,?,?)}");
//			cstmt.setString(1,data.getS());
//			cstmt.setString(2,data.getIP());
//			cstmt.setString(3,data.getSDATE());
//			cstmt.setInt(4,data.getLogId());
//			cstmt.executeUpdate();
			return 0;
		}catch(Exception e){
			logger.error("dumpMediaLogData:"+e);
			e.printStackTrace();
			return 1;
		}finally{
			try{
				if(cstmt !=null) cstmt.close();
			}catch(Exception e){
				logger.fatal(e.getMessage());
			}
		}
	}
	private int dumpKeywordLogData(Connection conn,RFData data) throws IOException{
		CallableStatement cstmt=null;
		Statement stmt=null;
		StringBuffer sql=new StringBuffer();
		try{
			logger.debug(data.getRDATE());
			logger.debug(data.getRTIME());
			logger.debug(data.getMCGB());
			logger.debug(data.getSC_TXT());
			logger.debug(data.getRURL());
			logger.debug(data.getDPOINT());
			logger.debug(data.getIP());
			logger.debug(data.getMC());
			logger.debug(data.getK_GUBUN());
			logger.debug("call proc_KeywordDataDump");
			String rurl= data.getRURL();
			if( rurl.length()>500 )rurl= rurl.substring(0, 500);
			logger.debug(ReflectionToStringBuilder.toString(data));
			if(data.getTbName()==null || data.getTbName().length()==0){
				cstmt =conn.prepareCall("{	 call proc_KeywordDataDump(?,?,?,?,?,?,?,?)}");
				cstmt.setString(1,data.getRDATE()+"");
				cstmt.setString(2,data.getRTIME()+"");
				cstmt.setString(3,data.getMCGB()+"");
				cstmt.setString(4,data.getSC_TXT()+"");
				cstmt.setString(5,rurl+"");
				cstmt.setString(6,data.getDPOINT()+"");
				cstmt.setString(7,data.getIP()+"");
				cstmt.setString(8,data.getK_GUBUN()+"");
				cstmt.executeUpdate();
			}else{
				sql.append(" insert into "+data.getTbName()+" (sdate,gbn,stime,kwd,ipaddr,cntrst) values ( \n");
				sql.append("'"+data.getRDATE()+"', \n");
				sql.append("'"+data.getMCGB()+"', \n");
				sql.append("'"+data.getRTIME()+"', \n");
				sql.append("'"+data.getSC_TXT()+"', \n");
				sql.append("'"+data.getIP()+"', \n");
				sql.append(data.getSetCnt()+") \n");
				logger.debug(sql.toString());
				stmt=conn.createStatement();
				stmt.executeUpdate(sql.toString());
			}
			return 0;
		}catch(Exception e){
			logger.error(data.getTbName());
			logger.error(data.getRDATE());
			logger.error(data.getRTIME());
			logger.error(data.getMCGB());
			logger.error(data.getSC_TXT());
			logger.error(data.getRURL());
			logger.error(data.getDPOINT());
			logger.error(data.getIP());
			logger.error(data.getMC());
			logger.error(data.getK_GUBUN());
			logger.info("call proc_KeywordDataDump");
			logger.error("dumpKeywordLogData:"+e);
			e.printStackTrace();
			return 1;
		}finally{
			try{
				if(cstmt !=null) cstmt.close();
				if(stmt !=null) stmt.close();
			}catch(Exception e){
				logger.fatal(e.getMessage());
			}
		}
	}
	private int dumpChargeLogData(Connection conn,AdChargeData data) throws IOException{
		CallableStatement cstmt=null;
		try{
			
			cstmt =conn.prepareCall("{	 call sp_status_copy(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
			cstmt.setString(1,data.getSITE_CODE());
			cstmt.setString(2,data.getADGUBUN());
			cstmt.setString(3,data.getTYPE());
			cstmt.setString(4,data.getYYYYMMDD());
			cstmt.setString(5,data.getHHMM());
			cstmt.setString(6,data.getPC());
			cstmt.setString(7,data.getUSERID());
			cstmt.setInt(8,Integer.parseInt(data.getPOINT().trim()));
			cstmt.setInt(9,Integer.parseInt(data.getPPOINT().trim()));
			cstmt.setString(10,data.getYM());
			cstmt.setString(11,data.getNO());
			cstmt.setString(12,data.getIP());
			cstmt.setInt(13,Integer.parseInt(data.getKNO().trim()));
			cstmt.setInt(14,Integer.parseInt(data.getS().trim()));
			cstmt.setString(15,data.getSCRIPTUSERID());
			cstmt.setString(16, data.getMCGB());
			//logger.info("dumpChargeLogDataDegug1:"+ReflectionToStringBuilder.toString(data));
			cstmt.executeUpdate();
			//logger.info("dumpChargeLogDataDegug2:"+ReflectionToStringBuilder.toString(data));
			dumpActData(conn,data);
			//logger.info("dumpChargeLogDataDegug3:"+ReflectionToStringBuilder.toString(data));
			logger.debug(data.getInfo("dumpChargeLogData:"));
			
			return 0;
		}catch(Exception e){
			logger.error("dumpChargeLogData:"+e);
			e.printStackTrace();
			return 1;
		}finally{
			try{
				if(cstmt !=null) cstmt.close();
			}catch(Exception e){
				logger.fatal(e.getMessage());
			}
		}
	}
	/*
	private int dumpShopStatsData(Connection conn,ShopStatsData data) throws IOException{
		Statement stmt=null;
		StringBuffer sql=new StringBuffer();
		try{
			sql.append(" update stats_shopdata_new \n");
			sql.append(" set viewcnt=viewcnt+"+data.getViewCnt()+",");
			sql.append(" adviewcnt=adviewcnt+"+data.getAdViewCnt()+", \n");
			sql.append(" adclickcnt=adclickcnt+"+data.getAdClickCnt()+", \n");
			sql.append(" adconvcnt=adconvcnt+"+data.getAdConvCnt()+", \n");
			sql.append(" adconvprice=adconvprice+"+data.getAdConvPrice()+" \n");
			sql.append(" where userid='"+data.getUserId()+"' \n");
			sql.append(" and pcode='"+data.getpCode()+"' \n");
			sql.append(" and sdate='"+data.getsDate()+"' \n");
			stmt=conn.createStatement();
			int ex=stmt.executeUpdate(sql.toString());
			if(ex==0){
				ShopData result=RFServlet.instance.adInfoCache.getShopPCodeData(data.getUserId(),data.getpCode());
				if(result!=null){
					sql.delete(0,sql.length());
					sql.append(" INSERT INTO stats_shopdata_new(sdate,userid,cate1,pcode,viewcnt,adviewcnt,adclickcnt,adconvcnt,adconvprice) values( \n");
					sql.append("'"+data.getsDate()+"', \n");
					sql.append("'"+data.getUserId()+"', \n");
					sql.append("'"+result.getCATE1()+"', \n");
					sql.append("'"+data.getpCode()+"', \n");
					sql.append(data.getViewCnt()+", \n");
					sql.append(data.getAdViewCnt()+", \n");
					sql.append(data.getAdClickCnt()+", \n");
					sql.append(data.getAdConvCnt()+", \n");
					sql.append(data.getAdConvPrice()+") \n");
					if(stmt!=null) stmt.close();
					stmt=conn.createStatement();
					stmt.executeUpdate(sql.toString());
				}
			}
			return 0;
		}catch(Exception e){
			logger.error("dumpShopStatsData:"+e+","+sql.toString());
			e.printStackTrace();
			return 1;
		}finally{
			try{
				if(stmt !=null) stmt.close();
			}catch(Exception e){
				logger.error(e.getMessage());
			}
		}
	}
	*/
	private int dumpShopStatsData(Connection conn,ShopStatsData data) throws IOException{
		PreparedStatement cstmt=null;
		try{
			ShopData result=RFServlet.instance.adInfoCache.getShopPCodeData(data.getUserId(),data.getpCode());
			if(result!=null){
				cstmt =conn.prepareCall("{	 call proc_ShopStatsDataDump(?,?,?,?,?,?,?,?,?)}");
				cstmt.setString(1,data.getsDate());
				cstmt.setString(2,data.getUserId());
				cstmt.setString(3,data.getpCode().replaceAll("'",""));
				cstmt.setString(4,result.getCATE1());
				cstmt.setInt(5,data.getViewCnt());
				cstmt.setInt(6,data.getAdViewCnt());
				cstmt.setInt(7,data.getAdClickCnt());
				cstmt.setInt(8,data.getAdConvCnt());
				cstmt.setInt(9,data.getAdConvPrice());
				cstmt.executeUpdate();
			}
			return 0;
		}catch(Exception e){
			logger.error("dumpShopStatsData:"+e);
			e.printStackTrace();
			return 1;
		}finally{
			try{
				if(cstmt !=null) cstmt.close();
			}catch(Exception e){
				logger.error(e.getMessage());
			}
		}
	}
	private int dumpConvLogData(Connection conn,AdChargeData data) throws IOException{
		CallableStatement cstmt=null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try{
			logger.debug("dumpConvLogData 1502 1 ");
			
			String NO, ADPRODUCT, ADGUBUN, SITECODE, MEDIA_ID, MEDIA_CODE, ACTGUBUN, MCGB, IN_HOUR, ORDCODE, DIRECT;
			NO=ADPRODUCT=ADGUBUN=SITECODE=MEDIA_ID=MEDIA_CODE=ACTGUBUN=MCGB=IN_HOUR=ORDCODE=DIRECT="";

			PointData p = servlet.instance.adInfoCache.getAdCacheData(data.getUSERID());
			int indirect, direct;
			indirect=7;
			direct=24;
			if( p!=null ){
				try{
					indirect = p.getCookie_indirsales();
					direct = p.getCookie_dirsales();
				}catch(Exception e){
				}
			}
			logger.debug("dumpConvLogData 1502 2 ");
			
			Date d = new Date();
			Date d1 = new Date();
			d1.setTime( ( d.getTime() + (1000*60*60*24* (indirect * -1)) ) );
			
			SimpleDateFormat dFormat = new SimpleDateFormat ( "yyyyMMdd" );
			String yyyymmdd = dFormat.format ( d1.getTime ( ) ).toString();
			
			
			StringBuffer sql1= new StringBuffer();
			if( data.getS().equals("") ){//indirect conversion
				sql1.append("SELECT a.NO, a.ADPRODUCT, a.ADGUBUN, a.SITECODE, a.MEDIA_ID, a.MEDIA_CODE, a.ACTGUBUN, a.MCGB, 0 in_hour \n");
				sql1.append("FROM ACTION_LOG a \n");
				sql1.append("WHERE a.IP='"+data.getIP()+"' AND a.MCODE='"+data.getUSERID()+"' AND a.ACTGUBUN='V' \n"); 
				sql1.append("AND partdt > "+ yyyymmdd +" \n"); 
				sql1.append("ORDER BY NO DESC \n");
				sql1.append("LIMIT 1; \n");
			}else{// direct conversion
				sql1.append("SELECT a.NO, a.ADPRODUCT, a.ADGUBUN, a.SITECODE, a.MEDIA_ID, a.MEDIA_CODE, ACTGUBUN, MCGB \n");
				sql1.append(", CASE WHEN TIMESTAMPDIFF(HOUR, a.regdate, NOW())<="+direct+" THEN 24 ELSE 0 END in_hour \n");
				sql1.append("FROM ACTION_LOG a \n");
				sql1.append("WHERE ( a.IP='"+data.getIP()+"' AND a.MCODE='"+data.getUSERID()+"' AND a.MEDIA_CODE='"+data.getS()+"' AND a.SITECODE='"+data.getSITE_CODE()+"' \n"); 
				sql1.append("AND a.ADGUBUN='"+data.getADGUBUN()+"' AND a.ADPRODUCT='"+data.getTYPE()+"' AND ACTGUBUN='C' ) \n");
				sql1.append("ORDER BY a.NO DESC \n");
				sql1.append("LIMIT 1; \n");
			}
			
			logger.debug("dumpConvLogData 1502 3 "+sql1);
			
			try{
				stmt=conn.createStatement();
				rs=stmt.executeQuery( sql1.toString());
				logger.debug("dumpConvLogData[1225] "+ sql1.toString());
				while( rs.next() ){
					NO= rs.getString("NO");
					ADPRODUCT= rs.getString("ADPRODUCT");
					ADGUBUN= rs.getString("ADGUBUN");
					SITECODE= rs.getString("SITECODE");
					MEDIA_ID= rs.getString("MEDIA_ID");
					MEDIA_CODE= rs.getString("MEDIA_CODE");
					ACTGUBUN= rs.getString("ACTGUBUN");
					MCGB= rs.getString("MCGB");
					IN_HOUR= rs.getString("IN_HOUR");
					
					if( IN_HOUR.equals("0") ) data.setDirect("0");
					if( ACTGUBUN.equals("ico") ) ACTGUBUN="C";
				}
			}catch(Exception e){
				logger.error("dumpConvLogData[1241 err:"+ e);
				return 0;
			}finally{
				try{if(rs !=null) rs.close();} catch(Exception e){}
				try{if(stmt !=null) stmt.close();} catch(Exception e){}
			}
			
			logger.debug("dumpConvLogData 1502 4 no="+ NO );
			
			if( !NO.equals("") ){
				try{
					stmt=conn.createStatement();
					rs=stmt.executeQuery( "SELECT ORDCODE FROM CONVERSION_LOG WHERE ordcode='"
								+ data.getOrdCode() +"' and userid='"+ data.getUSERID() +"' LIMIT 1 " );
					while( rs.next() ){
						ORDCODE= rs.getString("ORDCODE");
					}
				}catch(Exception e){
					logger.error("dumpConvLogData[1260 err:"+ e);
				}finally{
					try{if(rs !=null) rs.close();} catch(Exception e){}
					try{if(stmt !=null) stmt.close();} catch(Exception e){}
				}
				
				logger.debug("dumpConvLogData 1502 5 ");
				
				if( ORDCODE.equals("") ){
					logger.debug(data.getInfo("dumpConvLogData="));
					String pnm = data.getPNm(); if(pnm.length()>200) pnm=pnm.substring(0,199);
					
					int shopconNo=0;
					int weight=0;
					try{
						shopconNo=(data.getShopcon_sereal_no().equals("")?0:Integer.parseInt(data.getShopcon_sereal_no()));
						weight=Integer.parseInt(data.getShopcon_weight());
					}catch(Exception e){}
					
					cstmt =conn.prepareCall("{	CALL proc_ConvLogDataDump( ?,?,?,?,?,?,?,?,?,?, ?,?,?,?,?,?,?,?,?,?, ?,?,?,?,? ) }");
					cstmt.setString(1, NO );
					cstmt.setString(2, data.getYYYYMMDD());
					cstmt.setString(3, data.getIP());
					cstmt.setString(4, data.getOrdCode());
					cstmt.setString(5, data.getOrdRFUrl());
					cstmt.setString(6, data.getPCODE());
					cstmt.setInt(7, Integer.parseInt(data.getOrdQty()));
					cstmt.setInt(8, Integer.parseInt(data.getPrice()));
					cstmt.setString(9, data.getUSERID());
					cstmt.setString(10, MEDIA_ID);
					cstmt.setString(11, MEDIA_CODE);
					cstmt.setString(12, SITECODE);
					cstmt.setString(13, ADGUBUN);
					cstmt.setString(14, ADPRODUCT);
					cstmt.setString(15, ACTGUBUN);
					cstmt.setString(16, MCGB);
					cstmt.setString(17, pnm);
					cstmt.setString(18, data.getUname());
					cstmt.setString(19, data.getUsex());
					cstmt.setString(20, data.getUpno());
					cstmt.setString(21, IN_HOUR);
					cstmt.setString(22, data.getDirect());
					cstmt.setTimestamp(23, data.getRegdate() );
					cstmt.setInt(24, shopconNo );
					cstmt.setInt(25, weight );
					cstmt.executeUpdate();
					
					logger.debug("dumpConvLogData 1502 data "+data.toString());
					if(data.getPCODE()!=null && data.getPCODE().length()>0){
						RFServlet.instance.dumpDb.setShopStatsData(data.getUSERID(),data.getPCODE(),data,"conv");
					}
				}
			}
			
			return 0;
		}catch(Exception e){
			logger.debug(data.getInfo("dumpConvLogData="));
			logger.error("dumpConvLogData:"+e);
			e.printStackTrace();
			return 1;
		}finally{
			try{
				if(cstmt !=null) cstmt.close();
			}catch(Exception e){
				logger.error(e.getMessage());
			}
		}
	}
	private void insertShopData(Connection conn,ShopData data){
		Statement stmt=null;
		StringBuffer sql=new StringBuffer();
		try{
			String url = data.getURL(); if( url.length()>200 ) url = url.substring(0, 199);
			String purl = data.getPURL(); if( purl.length()>200 ) purl = purl.substring(0, 199);
			/*
			if(data.getPNM().indexOf("\\x")==0){
				try{
					String pnm=data.getPNM().replace("\\x","");
					byte[] hex2=org.bouncycastle.util.encoders.Hex.decode(pnm);
					data.setPNM(new String(hex2,"utf-8"));
					logger.debug("kwd6="+data.getPNM());
				}catch(Exception e){
					data.setPNM("");
					logger.error("insertShopData,bouncycastle:"+e);
				}
			}
			*/
			sql.append("insert into SHOP_DATA(userid, url, pnm, pcode, price, imgpath,purl,gb,rdate,rtime \n");
			sql.append(", cate1, cate2, cate3, cate4, caid1, caid2, caid3, caid4,LOADINFO,REGDATE,LASTUPDATE) \n");
			sql.append("values('"+ data.getUSERID() +"', '"+ url +"', '"+ data.getPNM().replaceAll("\\<.*?\\>","") +"', '"+ data.getPCODE() +"', '"+ data.getPRICE() +"' \n");
			sql.append(", '"+ data.getIMGPATH() +"', '"+ purl +"', '', replace(left(now(),10),'-',''), '0101', '', '', '', '', '', '', '', '','U',now(),now()) \n");
			sql.append("ON DUPLICATE KEY update pnm='"+data.getPNM().replaceAll("\\<.*?\\>","") +"', price='"+data.getPRICE()+"', imgpath='"+data.getIMGPATH()+"',purl='"+purl+"' \n");
			sql.append(", cate1='', status='Y', LOADINFO='U', LASTUPDATE=NOW() ; \n");
			stmt=conn.createStatement();
			stmt.executeUpdate(sql.toString());
			logger.debug("insertShopData[1345] "+ sql.toString());
		}catch(Exception e){
			logger.error("insertShopData:"+e+" data:"+ data.getURL()+","+data.getPNM());
		}finally{
			try{	if(stmt !=null) stmt.close();	}catch(Exception e){	logger.error(e);}
		}
	}
	private int dumpSkyChargeData(Connection conn,AdChargeData data) throws IOException{
		CallableStatement cstmt=null;
		Statement stmt2=null;
		try{
			logger.debug(data.getInfo("dmpSkyCharge="));

			MediaScriptData ms=RFServlet.instance.adInfoCache.getMediaScriptData(data.getS());
			if( ms==null ){
				return 0;
			}
			
			PointData getpoint=RFServlet.instance.adInfoCache.getAdCacheData(data.getUSERID());
			if(getpoint==null)	return 1;
			double weight_pct, tmp_point;
			int point  = 0;
			int ppoint = 0;
			
			weight_pct = tmp_point = point = 0;
			
			weight_pct = (double)ms.getWeight_pct();
			
			if(data.getADGUBUN().equals("AD")){
				if(getpoint.getSAD()>0){
					tmp_point = (double)getpoint.getSAD();
				}
			}else if(data.getADGUBUN().equals("KL")){
				if(getpoint.getSKL()>0){
					tmp_point = (double)getpoint.getSKL();
				}
			}else if(data.getADGUBUN().equals("SR")){
				if(getpoint.getSSR()>0){
					tmp_point = (double)getpoint.getSSR();
				}
			}else if(data.getADGUBUN().equals("UM")){
				if(getpoint.getSUM()>0){
					tmp_point = (double)getpoint.getSUM();
				}
			}else if(data.getADGUBUN().equals("ST")){
				if(getpoint.getSST()>0){
					tmp_point = (double)getpoint.getSST();
				}
			}else if(data.getADGUBUN().equals("SP")){
				if(getpoint.getSSP()>0){
					tmp_point = (double)getpoint.getSSP();
				}
			}
			
			try{
				tmp_point *= ( weight_pct / (double)100 );
			}catch(Exception e ){
				logger.error("sky drcpct[1352] ");
			}
			point = (int)tmp_point;
			
			logger.debug("sky drcpct[1555] point="+ point);
			
			logger.debug(data.getInfo("sadbn dump charge.info"));

			if( data.getTYPE().equals("C")){
				point=0;
				ppoint=0;
				String kno = "sky_"+ data.getADGUBUN() +"_"+ data.getS() +"_"+ data.getKNO();
				
				String chkData=RFServlet.instance.dataMapper.getClickChkDataInfo( kno, data.getIP(), "sky", data.getADGUBUN());
				if( chkData==null || chkData.length()<1 ){
					try{
						String sql="insert into chksite_new (ukey, site_code, sdatetime, ip, svc_type, gubun) values ("
								+ "'"+ kno +"', '"+data.getSITE_CODE()+"', LEFT(REPLACE(REPLACE(REPLACE(NOW(),'-',''),':',''),' ',''),12), '"
								+ data.getIP()+"','sky','"
								+ data.getADGUBUN() +"')";
						logger.debug(sql);
						stmt2=conn.createStatement();
						stmt2.executeUpdate(sql);
					}catch(Exception e){
						logger.error("dumpSkyChargeData[1351 e:"+e);
					}
				}else{
					return 0;
				}
			}
			
			cstmt =conn.prepareCall("{	CALL sp_status_sky(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)	}");
			cstmt.setString(1,data.getSITE_CODE());
			cstmt.setString(2,data.getADGUBUN());
			cstmt.setString(3,data.getTYPE());
			cstmt.setString(4,data.getYYYYMMDD());
			cstmt.setString(5,data.getHHMM());
			cstmt.setString(6,data.getPC());
			cstmt.setString(7,data.getUSERID());
			cstmt.setInt(8,point);
			cstmt.setInt(9,ppoint);
			cstmt.setString(10,data.getYM());
			cstmt.setString(11,data.getS());
			cstmt.setString(12,data.getIP());
			cstmt.setString(13,data.getKNO());
			cstmt.setString(14, data.getMCGB());
			cstmt.executeUpdate();
			MediaScriptData msd= RFServlet.instance.adInfoCache.getMediaScriptData(data.getS());
			DayMediaLogData mld=(DayMediaLogData)RFServlet.instance.appContext.getBean("DayMediaLogData");
			data.setSCRIPTUSERID(msd.getUSERID());
			dumpActData(conn,data);
			
			
			mld.setMC(data.getS());
			mld.setMCODE(data.getUSERID());
			mld.setSDATE(data.getYYYYMMDD());
			mld.setVIEWCNT(1);
			mld.setCLICKCNT(0);
			mld.setPOINT(point);
			mld.setGUBUN(data.getADGUBUN());
			mld.setMEDIA_ID(msd.getUSERID());
			mld.setActGubun(data.getTYPE());
			dumpSStatusDayMediaData(conn,mld);
			
			return 0;
		}catch(Exception e){
			logger.error(data.getInfo("dmpSkyCharge="));
			logger.error("dumpSkyChargeData:"+e);
			e.printStackTrace();
			return 1;
		}finally{
			try{
				if(cstmt !=null) cstmt.close();
				if(stmt2 !=null) stmt2.close();
			}catch(Exception e){
				logger.fatal(e.getMessage());
			}
		}
	}
	private int dumpSStatusDayMediaData(Connection conn,DayMediaLogData data) throws IOException{
		Statement stmt=null;
		try{
			logger.debug(data.getInfo("sky media_data.info="));
			
			
			stmt =conn.createStatement();
			String sql="";
			if(data.getActGubun().equals("V")){
				sql="insert into sstatus_day_media (mc, mcode, sdate,viewcnt,clickcnt,point,gubun,media_id) values("
					+"'"+data.getMC()+"',"
					+"'"+data.getMCODE()+"',"
					+"'"+data.getSDATE()+"',"
					+"'"+data.getVIEWCNT()+"',"
					+"'"+data.getCLICKCNT()+"',"
					+"'"+data.getPOINT()+"',"
					+"'"+data.getGUBUN()+"',"
					+"'"+data.getMEDIA_ID()+"')"
					+" ON DUPLICATE KEY	update viewcnt = viewcnt+1,point=point+"+data.getPOINT();
				logger.debug(sql);
			}else if(data.getActGubun().equals("C")){
				sql="insert into sstatus_day_media (mc, mcode, sdate,viewcnt,clickcnt,point,gubun,media_id) values("
					+"'"+data.getMC()+"',"
					+"'"+data.getMCODE()+"',"
					+"'"+data.getSDATE()+"',"
					+"'"+data.getVIEWCNT()+"',"
					+"'"+data.getCLICKCNT()+"',"
					+"'"+data.getPOINT()+"',"
					+"'"+data.getGUBUN()+"',"
					+"'"+data.getMEDIA_ID()+"')"
					+" ON DUPLICATE KEY	update clickcnt = clickcnt+1,point=point+"+data.getPOINT();
				logger.debug(sql);
			}
			stmt.executeUpdate(sql);
			return 0;
		}catch(Exception e){
			logger.error(data.getInfo("sky media_data.info="));
			logger.error("dumpStatusDayMediaData:"+e);
			return 1;
		}finally{
			try{
				if(stmt !=null) stmt.close();
			}catch(Exception e){
			}
		}
	}
	private int dumpNormalChargeLogData(Connection conn,AdChargeData data) throws IOException{
		CallableStatement cstmt=null;
		try{
			cstmt =conn.prepareCall("{	 call sp_status(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) }");
			cstmt.setString(1,data.getSITE_CODE());
			cstmt.setString(2,data.getADGUBUN());
			cstmt.setString(3,data.getTYPE());
			cstmt.setString(4,data.getYYYYMMDD());
			cstmt.setString(5,data.getHHMM());
			cstmt.setString(6,data.getPC());
			cstmt.setString(7,data.getUSERID());
			cstmt.setInt(8,Integer.parseInt(data.getPOINT().trim()));
			cstmt.setInt(9,Integer.parseInt(data.getPPOINT().trim()));
			cstmt.setString(10,data.getYM());
			cstmt.setString(11,data.getS());
			cstmt.setString(12,data.getIP());
			cstmt.setString(13,data.getKNO());
			cstmt.setString(14,data.getSCRIPTUSERID());
			cstmt.setInt(15,data.getSRView2());
			cstmt.setString(16,data.getMCGB());
			cstmt.setInt(17, data.getViewcnt3());

			if( data.getADGUBUN().endsWith("PE") ){
			}else if( data.getADGUBUN().endsWith("CA") ){
			}else{
				cstmt.executeUpdate();
			}
			
			logger.debug(data.getInfo("dumpNormalChargeLogData:"));
			dumpActData(conn,data);
			return 0;
		}catch(Exception e){
			logger.info("call dumpNormalChargeLogData");
			logger.error("dumpNormalChargeLogData:"+e+" "+data.toString());
			e.printStackTrace();
			return 1;
		}finally{
			try{
				if(cstmt !=null) cstmt.close();
			}catch(Exception e){
				logger.fatal(e.getMessage());
			}
		}
	}
	
	// 쇼콘 용 프로시져 추가 sp_status_scn 
		private int dumpNormalChargeLogDataShopcon(Connection conn,AdChargeData data) throws IOException{
			CallableStatement cstmt=null;
			try{
				cstmt =conn.prepareCall("{	 call sp_status_scn(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) }");
				cstmt.setString(1,data.getSITE_CODE());
				cstmt.setString(2,data.getADGUBUN());
				cstmt.setString(3,data.getTYPE());
				cstmt.setString(4,data.getYYYYMMDD());
				cstmt.setString(5,data.getHHMM());
				cstmt.setString(6,data.getPC());
				cstmt.setString(7,data.getUSERID());
				cstmt.setInt(8,Integer.parseInt(data.getPOINT().trim()));
				cstmt.setInt(9,Integer.parseInt(data.getPPOINT().trim()));
				cstmt.setString(10,data.getYM());
				cstmt.setString(11,data.getS());
				cstmt.setString(12,data.getIP());
				cstmt.setString(13,data.getKNO());
				cstmt.setString(14,data.getSCRIPTUSERID());
				cstmt.setInt(15,data.getSRView2());
				cstmt.setString(16,data.getMCGB());
				cstmt.setInt(17, data.getViewcnt3());
				logger.info("");
				logger.info("Integer.parseInt(data.getPOINT().trim()) ="+Integer.parseInt(data.getPOINT().trim()));
				logger.info(" Integer.parseInt(data.getPPOINT().trim() ="+Integer.parseInt(data.getPPOINT().trim()));
				
				cstmt.executeUpdate();
				
				logger.debug(data.getInfo("dumpNormalChargeLogDataShopcon:"));
				dumpActData(conn,data);
				return 0;
			}catch(Exception e){
				logger.info("call dumpNormalChargeLogDataShopcon");
				logger.error("dumpNormalChargeLogDataShopcon:"+e+" "+data.toString());
				e.printStackTrace();
				return 1;
			}finally{
				try{
					if(cstmt !=null) cstmt.close();
				}catch(Exception e){
					logger.fatal(e.getMessage());
				}
			}
		}
	private int dumpMobileChargeLogData(Connection conn,AdChargeData data) throws IOException{
		CallableStatement cstmt=null;
		try{
			cstmt =conn.prepareCall("{	 call sp_status_mbw(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) }");
			cstmt.setString(1,data.getSITE_CODE());
			cstmt.setString(2,data.getADGUBUN());
			cstmt.setString(3,data.getTYPE());
			cstmt.setString(4,data.getYYYYMMDD());
			cstmt.setString(5,data.getHHMM());
			cstmt.setString(6,data.getPC());
			cstmt.setString(7,data.getUSERID());
			cstmt.setInt(8,Integer.parseInt(data.getPOINT().trim()));
			cstmt.setInt(9,Integer.parseInt(data.getPPOINT().trim()));
			cstmt.setString(10,data.getYM());
			cstmt.setString(11,data.getS());
			cstmt.setString(12,data.getIP());
			cstmt.setString(13,data.getKNO());
			cstmt.setString(14,data.getSCRIPTUSERID());
			cstmt.setInt(15,data.getSRView2());
			cstmt.setString(16,data.getMCGB());
			cstmt.setInt(17, data.getViewcnt3());
			
			if( data.getADGUBUN().endsWith("PE") ){
			}else if( data.getADGUBUN().endsWith("CA") ){
			}else{
				cstmt.executeUpdate();
			}
			
			logger.debug(data.getInfo("dumpMobileChargeLogData:"));
			dumpActData(conn,data);
			return 0;
		}catch(Exception e){
			logger.info("call dumpMobileChargeLogData");
			logger.error("dumpMobileChargeLogData:"+e);
			e.printStackTrace();
			return 1;
		}finally{
			try{
				if(cstmt !=null) cstmt.close();
			}catch(Exception e){
				logger.fatal(e.getMessage());
			}
		}
	}
	private int dumpShopConData(Connection conn,DrcData data)throws IOException{
		try{
			logger.debug("dumpShopConData data "+ data.toString());
			
			SimpleDateFormat yyyymmddHHmm = new SimpleDateFormat("yyyyMMddHHmm");
			SimpleDateFormat yyyymmdd = new SimpleDateFormat("yyyyMMdd");
			SimpleDateFormat yyyymm = new SimpleDateFormat("yyyyMM");
			SimpleDateFormat kk = new SimpleDateFormat("kk");
			java.util.Date date=new java.util.Date();
			//date.setTime(data.getActDate());
			String yyyymdhm=yyyymmddHHmm.format(date);
			String ymd=yyyymmdd.format(date);
			String ym=yyyymm.format(date);
			String kh=kk.format(date);

			MediaScriptData ms=RFServlet.instance.adInfoCache.getMediaScriptData(data.getS());
			if( ms==null ){
				return 0;
			}
			
			AdChargeData crd=(AdChargeData)RFServlet.instance.appContext.getBean("AdChargeData");
			crd.setSITE_CODE(data.getSc());
			crd.setADGUBUN(data.getGb());
			crd.setTYPE("C");
			crd.setYYYYMMDD(ymd);
			crd.setHHMM(kh);
			crd.setPC("");
			crd.setUSERID(data.getU());
			crd.setPCODE(data.getpCode());
			crd.setPOINT(Integer.toString(data.getPoint()));
			crd.setPPOINT("0");
			crd.setYM(ym);
			crd.setNO(data.getNo());
			crd.setS(data.getS());
			crd.setSCRIPTUSERID(ms.getUSERID());
			crd.setIP(data.getKeyIp());
			crd.setKNO(data.getKno());
			crd.setPRODUCT( data.getProduct() );
			crd.setMCGB(data.getMcgb());
			
			if( data.getProduct().equals("mbw") ){
				dumpMobileChargeLogData(conn,crd);
			}else{
				dumpNormalChargeLogDataShopcon(conn,crd);
			}
			return 0;
		}catch(Exception e){
			logger.error("dumpShopConData[1799] err "+ e);
			return 1;
		}
	}
	
	private int dumpDrcData(Connection conn,DrcData data) throws IOException{
		Statement stmt=null;
		Statement stmt2=null;
		Statement stmt3=null;
		try{
			SimpleDateFormat yyyymmddHHmm = new SimpleDateFormat("yyyyMMddHHmm");
			SimpleDateFormat yyyymmdd = new SimpleDateFormat("yyyyMMdd");
			SimpleDateFormat yyyymm = new SimpleDateFormat("yyyyMM");
			SimpleDateFormat kk = new SimpleDateFormat("kk");
			java.util.Date date=new java.util.Date();
			//date.setTime(data.getActDate());
			String yyyymdhm=yyyymmddHHmm.format(date);
			String ymd=yyyymmdd.format(date);
			String ym=yyyymm.format(date);
			String kh=kk.format(date);

			if( data.getNo().equals("") ) data.setNo("0");
			
			logger.debug(data.getInfo("dumpDrcData:"));
			
			MediaScriptData ms=RFServlet.instance.adInfoCache.getMediaScriptData(data.getS());
			if( ms==null ){
				return 0;
			}
			
			if( data.getS().equals("1304") ){ //only v3
				SiteCodeData s=RFServlet.instance.adInfoCache.getAdSiteStatus(data.getSc());
				logger.debug("dumpDrc s="+s.toString());
				if( s!=null ){
					if( s.getPoint()<1000 ){
						logger.debug("drc 1");
						return 0;
					}
					if( DateUtils.getAdTimeZone(s.getUsemoney(), s.getUsedmoney(), s.getAd_rhour())==0 ){
						logger.debug("drc 2");
						return 0;
					}
				}else{
					logger.debug("drc 3");
					return 0;
				}
			}
			
			int chk=RFServlet.instance.dataMapper.getKEYWORD_CLICK_SCCnt(data.getKeyIp());
			//PointData getpoint=RFServlet.instance.dataMapper.getPointInfo(data.getU());
			PointData getpoint=RFServlet.instance.adInfoCache.getAdCacheData(data.getU());
			if(getpoint==null)	return 1;
			logger.debug("getpoint "+ getpoint.toString() );
			double weight_pct, tmp_point;
			int point  = 0;
			int ppoint = 0;
			
			weight_pct = tmp_point = point = 0;
			
			weight_pct = (double)ms.getWeight_pct();
			
			if(data.getGb().equals("AD")){
				if(getpoint.getAD()>0){
					tmp_point = (double)getpoint.getAD();
				}
			}else if(data.getGb().equals("KL")){
				if(getpoint.getKL()>0){
					tmp_point = (double)getpoint.getKL();
				}
			}else if(data.getGb().equals("SR")){
				if(getpoint.getSR()>0){
					tmp_point = (double)getpoint.getSR();
				}
			}else if(data.getGb().equals("UM")){
				if(getpoint.getUM()>0){
					tmp_point = (double)getpoint.getUM();
				}
			}else if(data.getGb().equals("ST")){
				if(getpoint.getST()>0){
					tmp_point = (double)getpoint.getST();
				}
			}else if(data.getGb().equals("SP")){
				if(getpoint.getSP()>0){
					tmp_point = (double)getpoint.getSP();
				}
			}
			
			try{
				tmp_point *= ( weight_pct / (double)100 );
			}catch(Exception e ){
				logger.error("drcpct[1553] ");
			}
			if( data.getPoint()>0 ){
				point = data.getPoint();
			}else{
				point = (int)tmp_point;
			}
			
			Document doc= (Document) RFServlet.instance.xml.get("adbn_partner");
			String _point= doc.select("partner config point "+data.getU()).html();
			if(!_point.equals("")){
				try{
					point = Integer.parseInt(_point);
				}catch(Exception e){}
			}
			
			logger.debug("drcpct[1555] point="+ point);
			
			String chk_key = "banner_"+ data.getGb() +"_"+ data.getMcgb() +"_"+ data.getNo() +"_"+ data.getKno() +"_"+ data.getS();
			String chkData=RFServlet.instance.dataMapper.getClickChkDataInfo(chk_key,data.getKeyIp(), "banner", "");
			
			if(data.getGb().equals("KL")){
				stmt=conn.createStatement();
				//String sql="update KEYWORD_LOG set clickyn='Y',dpoint="+point+" where no="+data.getKno();
				StringBuffer sql=new StringBuffer();
				sql.append(" UPDATE KEYWORD_LOG k               \n");
				sql.append(" INNER JOIN (SELECT MAX(NO) NO      \n");
				sql.append(" 	FROM KEYWORD_LOG                \n");
				sql.append(" 	WHERE ip='"+data.getKeyIp()+"' \n");
				sql.append(" 	AND rdate='"+ymd+"') k2        \n");
				sql.append(" ON k.no=k2.no                      \n");
				sql.append(" SET clickyn='N',dpoint="+point+"                   \n");
				logger.debug(sql.toString());
				stmt.executeUpdate(sql.toString());
			}
			if((chkData==null || chkData.length()<1) && chk<10){
				
				AdChargeData crd=(AdChargeData)RFServlet.instance.appContext.getBean("AdChargeData");
				crd.setSITE_CODE(data.getSc());
				crd.setADGUBUN(data.getGb());
				crd.setTYPE("C");
				crd.setYYYYMMDD(ymd);
				crd.setHHMM(kh);
				crd.setPC("");
				crd.setUSERID(data.getU());
				crd.setPCODE(data.getpCode());
				crd.setPOINT(point+"");
				crd.setPPOINT("0");
				crd.setYM(ym);
				crd.setNO(data.getNo());
				crd.setS(data.getS());
				crd.setSCRIPTUSERID(ms.getUSERID());
				crd.setIP(data.getKeyIp());
				crd.setKNO(data.getKno());
				crd.setPRODUCT( data.getProduct() );
				crd.setMCGB(data.getMcgb());
				if(crd.getS().length()>0 && ms.getUSERID()!=null && ms.getUSERID().length()>0){
					if( data.getProduct().equals("mbw") ){
						dumpMobileChargeLogData(conn,crd);
					}else{
						dumpNormalChargeLogData(conn,crd);
					}
				}
				
				/*
				DayMediaLogData mld=(DayMediaLogData)RFServlet.instance.appContext.getBean("DayMediaLogData");
				mld.setMC(data.getMc());
				mld.setMCODE(data.getU());
				mld.setSDATE(ymd);
				mld.setVIEWCNT(0);
				mld.setCLICKCNT(1);
				mld.setPOINT(point);
				mld.setGUBUN(data.getGb());
				mld.setMEDIA_ID(ms.getUSERID());
				mld.setActGubun("C");
				dumpStatusDayMediaData(conn,mld);
				*/
				try{
					String sql="insert into chksite_new (site_code, ukey, sdatetime, ip, svc_type, gubun) values ('"
							+data.getSc()+"','"+chk_key+"','"+yyyymdhm+"', '"+data.getKeyIp()+"', 'banner', '"+ data.getGb() +"')";
						logger.debug(sql);
						stmt2=conn.createStatement();
						stmt2.executeUpdate(sql);
				}catch(Exception e){
					logger.error("dumpDrcData[1589 :"+ e);
				}
				if(data.getGb().equals("KL")){
					String sql="insert into KEYWORD_CLICK_SC (ip,site_code, rdate) values ("
						+"'"+data.getKeyIp()+"','"+data.getSc()+"', '"+ymd+"')";
					logger.debug(sql);
					stmt3=conn.createStatement();
					stmt3.executeUpdate(sql);
				}
			}
			return 0;
		}catch(Exception e){
			logger.error(data.getInfo("drcerr[1604] data.info "));
			logger.info("call dumpNormalChargeLogData");
			logger.error("dumpDrcData:"+e);
			return 1;
		}finally{
			try{
				if(stmt !=null) stmt.close();
				if(stmt2 !=null) stmt2.close();
				if(stmt3 !=null) stmt3.close();
			}catch(Exception e){
			}
		}
	}
	/*
	private int dumpStatusDayMediaData(Connection conn,DayMediaLogData data) throws IOException{
		Statement stmt=null;
		try{
			logger.debug("data.getMC()="+data.getMC());
			logger.debug("data.getMCODE()="+data.getMCODE());
			logger.debug("data.getSDATE()="+data.getSDATE());
			logger.debug("data.getVIEWCNT()="+data.getVIEWCNT());
			logger.debug("data.getCLICKCNT()="+data.getCLICKCNT());
			logger.debug("data.getPOINT()="+data.getPOINT());
			logger.debug("data.getGUBUN()="+data.getGUBUN());
			logger.debug("data.getMEDIA_ID()="+data.getMEDIA_ID());
			logger.debug("data.getActGubun()="+data.getActGubun());
			
			
			stmt =conn.createStatement();
			String sql="";
			if(data.getActGubun().equals("V")){
				sql="insert into status_day_media (mc, mcode, sdate,viewcnt,clickcnt,point,gubun,media_id) values("
					+"'"+data.getMC()+"',"
					+"'"+data.getMCODE()+"',"
					+"'"+data.getSDATE()+"',"
					+"'"+data.getVIEWCNT()+"',"
					+"'"+data.getCLICKCNT()+"',"
					+"'"+data.getPOINT()+"',"
					+"'"+data.getGUBUN()+"',"
					+"'"+data.getMEDIA_ID()+"')"
					+" ON DUPLICATE KEY	update viewcnt = viewcnt+1";
				logger.debug(sql);
			}else if(data.getActGubun().equals("C")){
				sql="insert into status_day_media (mc, mcode, sdate,viewcnt,clickcnt,point,gubun,media_id) values("
					+"'"+data.getMC()+"',"
					+"'"+data.getMCODE()+"',"
					+"'"+data.getSDATE()+"',"
					+"'"+data.getVIEWCNT()+"',"
					+"'"+data.getCLICKCNT()+"',"
					+"'"+data.getPOINT()+"',"
					+"'"+data.getGUBUN()+"',"
					+"'"+data.getMEDIA_ID()+"')"
					+" ON DUPLICATE KEY	update clickcnt = clickcnt+1,point=point+"+data.getPOINT();
				logger.debug(sql);
			}
			stmt.executeUpdate(sql);
			return 0;
		}catch(Exception e){
			logger.error("data.getMC()="+data.getMC());
			logger.error("data.getMCODE()="+data.getMCODE());
			logger.error("data.getSDATE()="+data.getSDATE());
			logger.error("data.getVIEWCNT()="+data.getVIEWCNT());
			logger.error("data.getCLICKCNT()="+data.getCLICKCNT());
			logger.error("data.getPOINT()="+data.getPOINT());
			logger.error("data.getGUBUN()="+data.getGUBUN());
			logger.error("data.getMEDIA_ID()="+data.getMEDIA_ID());
			logger.error("data.getActGubun()="+data.getActGubun());
			logger.error("dumpStatusDayMediaData:"+e);
			return 1;
		}finally{
			try{
				if(stmt !=null) stmt.close();
			}catch(Exception e){
			}
		}
	}
	private int dumpStatusDayMediaData(Connection conn,DayMediaLogData data) throws IOException{
		CallableStatement cstmt=null;
		try{
			logger.debug("data.getMC()="+data.getMC());
			logger.debug("data.getMCODE()="+data.getMCODE());
			logger.debug("data.getSDATE()="+data.getSDATE());
			logger.debug("data.getVIEWCNT()="+data.getVIEWCNT());
			logger.debug("data.getCLICKCNT()="+data.getCLICKCNT());
			logger.debug("data.getPOINT()="+data.getPOINT());
			logger.debug("data.getGUBUN()="+data.getGUBUN());
			logger.debug("data.getMEDIA_ID()="+data.getMEDIA_ID());
			logger.debug("data.getActGubun()="+data.getActGubun());
			
			
			cstmt =conn.prepareCall("{	 call proc_StatusDayMediaDump(?,?,?,?,?,?,?,?,?)}");
			cstmt.setString(1,data.getMC());
			cstmt.setString(2,data.getMCODE());
			cstmt.setString(3,data.getSDATE());
			cstmt.setInt(4,data.getVIEWCNT());
			cstmt.setInt(5,data.getCLICKCNT());
			cstmt.setInt(6,data.getPOINT());
			cstmt.setString(7,data.getGUBUN());
			cstmt.setString(8,data.getMEDIA_ID());
			cstmt.setString(9,data.getActGubun());
			cstmt.executeUpdate();
			return 0;
		}catch(Exception e){
			logger.error("data.getMC()="+data.getMC());
			logger.error("data.getMCODE()="+data.getMCODE());
			logger.error("data.getSDATE()="+data.getSDATE());
			logger.error("data.getVIEWCNT()="+data.getVIEWCNT());
			logger.error("data.getCLICKCNT()="+data.getCLICKCNT());
			logger.error("data.getPOINT()="+data.getPOINT());
			logger.error("data.getGUBUN()="+data.getGUBUN());
			logger.error("data.getMEDIA_ID()="+data.getMEDIA_ID());
			logger.error("data.getActGubun()="+data.getActGubun());
			logger.error("dumpStatusDayMediaData:"+e);
			return 1;
		}finally{
			try{
				if(cstmt !=null) cstmt.close();
			}catch(Exception e){
				logger.fatal(e.getMessage());
			}
		}
	} 
	 */
	public int delShopLogData(Connection conn,ShopData data) throws IOException{
		Statement stmt=null;
		StringBuffer sql=new StringBuffer();
		try{
			logger.debug(data.getInfo("shop_log del="));
			boolean chk=false;
			if(data.getSvc_type().equals("sky")){
				sql.append(" update SHOP_LOG set TARGETGUBUN=CONCAT(TARGETGUBUN,'C'),lastupdate=now() \n");
				sql.append(" where INSTR(TARGETGUBUN,'C')=0 \n");
				sql.append(" and ip='"+data.getIP()+"' \n");
				sql.append(" and partid in('"+StringUtils.getPartIdKey(data.getIP())+"','"+StringUtils.getPrevPartIdKey(data.getIP())+"') \n");
				if(data.getNO()>0){
					sql.append(" and no="+ data.getNO()+" \n");
					chk=true;
				}else if(data.getSITE_CODE()!=null && data.getPCODE()!=null && data.getSITE_CODE().length()>0 && data.getPCODE().length()>0){
					sql.append(" and site_code='"+ data.getSITE_CODE()+"' \n");
					sql.append(" and pcode='"+ data.getPCODE()+"' \n");
					chk=true;
				}
			}else if(data.getSvc_type().equals("normal")){
				sql.append(" update SHOP_LOG set TARGETGUBUN=CONCAT(TARGETGUBUN,'A'),lastupdate=now() \n");
				sql.append(" where INSTR(TARGETGUBUN,'A')=0 \n");
				sql.append(" and ip='"+data.getIP()+"' \n");
				sql.append(" and partid in('"+StringUtils.getPartIdKey(data.getIP())+"','"+StringUtils.getPrevPartIdKey(data.getIP())+"') \n");
				if(data.getNO()>0){
					chk=true;
					sql.append(" and no="+ data.getNO()+" \n");
				}else if(data.getSITE_CODE()!=null && data.getPCODE()!=null && data.getSITE_CODE().length()>0 && data.getPCODE().length()>0){
					chk=true;
					sql.append(" and site_code='"+ data.getSITE_CODE()+"' \n");
					sql.append(" and pcode='"+ data.getPCODE()+"' \n");
				}	
			}else if(data.getNO()>0){
				chk=true;
				sql.append(" update SHOP_LOG set TARGETGUBUN=CONCAT(TARGETGUBUN,'B'),lastupdate=now() \n");
				sql.append(" where INSTR(TARGETGUBUN,'B')=0 and no="+data.getNO()+" \n");
				sql.append(" and ip='"+data.getIP()+"' \n");
				sql.append(" and partid in('"+StringUtils.getPartIdKey(data.getIP())+"','"+StringUtils.getPrevPartIdKey(data.getIP())+"') \n");
			}else if(data.getSITE_CODE()!=null && data.getPCODE()!=null && data.getSITE_CODE().length()>0 && data.getPCODE().length()>0){
				chk=true;
				sql.append(" update SHOP_LOG set TARGETGUBUN=CONCAT(TARGETGUBUN,'B'),lastupdate=now() \n");
				sql.append(" where INSTR(TARGETGUBUN,'B')=0 \n");
				sql.append(" and ip='"+data.getIP()+"' \n");
				sql.append(" and site_code='"+ data.getSITE_CODE()+"' \n");
				sql.append(" and pcode='"+ data.getPCODE()+"' \n");
				sql.append(" and partid in('"+StringUtils.getPartIdKey(data.getIP())+"','"+StringUtils.getPrevPartIdKey(data.getIP())+"') \n");
				
			} 
			logger.debug("del 1 "+sql);
			if(data.getNO()==0){
				logger.error(data.getInfo("delShopLogData[1674] no none ") );
			}
			if(sql.length()>0 && chk==true){
				stmt=conn.createStatement();
				stmt.executeUpdate(sql.toString());
				logger.debug("del 2 "+sql);
				if(servlet.instance.adInfoCache.shopLogCache.isKeyInCache(data.getIP())){
					logger.info("charge_log Dump : ShopLog Cache Matched : remove "+data.getIP());
					servlet.instance.adInfoCache.shopLogCache.remove(data.getIP());
				}
			}
//			}
			return 0;
		}catch(Exception e){
			logger.error(""+data.getIP());
			logger.error(""+data.getInfo("★★ => "));
			logger.info("call delShopData");
			logger.error("delShopLogData:"+e);
			e.printStackTrace();
			return 1;
		}finally{
			try{
				if(stmt !=null) stmt.close();
			}catch(Exception e){
				logger.fatal(e.getMessage());
			}
		}
	}
	public void setShopStatsData(String userId,String pCode,Object obj,String type){
		try{
			ShopStatsData ssData=null;
			String key=userId+"_"+pCode;
			if(servlet.instance.dumpObj.getShopStatsData().containsKey(key)){
				ssData=(ShopStatsData) servlet.instance.dumpObj.getShopStatsData().get(key);
			}else{
				ssData=new ShopStatsData();
				ssData.setUserId(userId);
				ssData.setpCode(pCode);
			}
			if(type.equals("conv")){
				AdChargeData data=(AdChargeData) obj;
				ssData.setAdConvCnt(ssData.getAdConvCnt()+1);
				ssData.setAdConvPrice(ssData.getAdConvPrice()+Integer.parseInt(data.getPrice()));
			}else if(type.equals("adclick")){
				ssData.setAdClickCnt(ssData.getAdClickCnt()+1);
			}else if(type.equals("adview")){
				ssData.setAdViewCnt(ssData.getAdViewCnt()+1);
			}else if(type.equals("view")){	
				ssData.setViewCnt(ssData.getViewCnt()+1);
			}
			servlet.instance.dumpObj.getShopStatsData().put(key,ssData);
		}catch(Exception e){
			logger.error("setShopStatsData:"+e);
		}
	}
	public void init(){
	}
	public static void main(String[] args){
		DumpDataToDB d=new DumpDataToDB();
		String a=StringUtils.getPartIdKey("112.161.52.833317.31");
		System.out.println(a);
	}
}