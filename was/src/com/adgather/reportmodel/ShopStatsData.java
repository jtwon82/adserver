package com.adgather.reportmodel;

import java.io.Serializable;
import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;

public class ShopStatsData implements Serializable{
	private static Logger logger = Logger.getLogger(ShopStatsData.class);
	private String sDate;
	private String userId;
	private String pCode;
	private int viewCnt;
	private int adViewCnt;
	private int adClickCnt;
	private int adConvCnt;
	private int adConvPrice;
	public ShopStatsData(){
		SimpleDateFormat yyyymmdd = new SimpleDateFormat("yyyyMMdd");
		java.util.Date date=new java.util.Date();
		sDate=yyyymmdd.format(date);
		viewCnt=0;
		adViewCnt=0;
		adClickCnt=0;
		adConvCnt=0;
		adConvPrice=0;
	}
	public String getsDate() {
		return sDate;
	}
	public void setsDate(String sDate) {
		this.sDate = sDate;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getpCode() {
		return pCode;
	}
	public void setpCode(String pCode) {
		this.pCode = pCode;
	}
	public int getViewCnt() {
		return viewCnt;
	}
	public void setViewCnt(int viewCnt) {
		this.viewCnt = viewCnt;
	}
	public int getAdViewCnt() {
		return adViewCnt;
	}
	public void setAdViewCnt(int adViewCnt) {
		this.adViewCnt = adViewCnt;
	}
	public int getAdClickCnt() {
		return adClickCnt;
	}
	public void setAdClickCnt(int adClickCnt) {
		this.adClickCnt = adClickCnt;
	}
	public int getAdConvCnt() {
		return adConvCnt;
	}
	public void setAdConvCnt(int adConvCnt) {
		this.adConvCnt = adConvCnt;
	}
	public int getAdConvPrice() {
		return adConvPrice;
	}
	public void setAdConvPrice(int adConvPrice) {
		this.adConvPrice = adConvPrice;
	}
}