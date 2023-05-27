package com.adgather.reportmodel;

import java.util.ArrayList;
import java.util.HashMap;
 
public class DumpObject implements Cloneable{
	private ArrayList rfData;
	private ArrayList rf_KeywordData;
	private ArrayList shopLogData;
	private ArrayList mediaLogData;
	private ArrayList keywordLogData;
	private ArrayList chargeLogData;
	private ArrayList mobileChargeLogData;
	private ArrayList normalChargeLogData;
	private ArrayList normalDayMediaLogData;
	private ArrayList drcData;
	private ArrayList delShopLogData;
	private ArrayList skyChargeData;
	private ArrayList skyDrcData;
	private ArrayList convLogData;
	private ArrayList ShopConData;
	private HashMap shopStatsData;
	//¼îÄÜ¿ë Ãß°¡
	private ArrayList normalChargeLogDataShopcon;
	
	public DumpObject(){
		rfData=new ArrayList();
		rf_KeywordData=new ArrayList();
		shopLogData=new ArrayList();
		mediaLogData=new ArrayList();
		keywordLogData=new ArrayList();
		chargeLogData=new ArrayList();
		mobileChargeLogData=new ArrayList();
		normalChargeLogData=new ArrayList();
		normalDayMediaLogData=new ArrayList();
		drcData=new ArrayList();
		delShopLogData=new ArrayList();
		skyChargeData=new ArrayList();
		skyDrcData=new ArrayList();
		convLogData=new ArrayList();
		ShopConData=new ArrayList();
		shopStatsData=new HashMap();
		//¼îÄÜ¿ë Ãß°¡
		normalChargeLogDataShopcon=new ArrayList();
	}
	public Object clone(){          
		try{
			//return super.clone();                      
			 Object obj = super.clone();  
		     Object rfData = this.rfData.clone();
		     Object rf_KeywordData = this.rf_KeywordData.clone();
		     Object shopLogData = this.shopLogData.clone();
		     Object mediaLogData = this.mediaLogData.clone();
		     Object keywordLogData = this.keywordLogData.clone();
		     Object chargeLogData = this.chargeLogData.clone();
		     Object mobileChargeLogData = this.mobileChargeLogData.clone();
		     Object normalChargeLogData = this.normalChargeLogData.clone();
		     Object normalDayMediaLogData = this.normalDayMediaLogData.clone();
		     Object drcData = this.drcData.clone();
		     Object delShopLogData = this.delShopLogData.clone();
		     Object skyChargeData = this.skyChargeData.clone();
		     Object skyDrcData = this.skyDrcData.clone();
		     Object convLogData = this.convLogData.clone();
		     Object ShopConData = this.ShopConData.clone();
		     Object shopStatsData = this.shopStatsData.clone();
		     //¼îÄÜ¿ë Ãß°¡
		     Object normalChargeLogDataShopcon = this.normalChargeLogDataShopcon.clone();
			 
		     DumpObject copy=(DumpObject)obj;
		     copy.rfData=(ArrayList) rfData;
		     copy.rf_KeywordData=(ArrayList) rf_KeywordData;
		     copy.shopLogData=(ArrayList) shopLogData;
		     copy.mediaLogData=(ArrayList) mediaLogData;
		     copy.keywordLogData=(ArrayList) keywordLogData;
		     copy.chargeLogData=(ArrayList) chargeLogData;
		     copy.mobileChargeLogData=(ArrayList) mobileChargeLogData;
		     copy.normalChargeLogData=(ArrayList) normalChargeLogData;
		     copy.normalDayMediaLogData=(ArrayList) normalDayMediaLogData;
		     copy.drcData=(ArrayList) drcData;
		     copy.delShopLogData=(ArrayList) delShopLogData;
		     copy.skyChargeData=(ArrayList) skyChargeData;
		     copy.skyDrcData=(ArrayList) skyDrcData;
		     copy.convLogData=(ArrayList) convLogData;
		     copy.ShopConData=(ArrayList) ShopConData;
		     copy.shopStatsData=(HashMap) shopStatsData;
		     //¼îÄÜ¿ë Ãß°¡
		     copy.normalChargeLogDataShopcon=(ArrayList) normalChargeLogDataShopcon;
		     
		     return copy;
		}catch(CloneNotSupportedException cse){}
			return null;
		}
	public void clearAll(){
		this.rfData.clear();
		this.rf_KeywordData.clear();
		this.shopLogData.clear();
		this.mediaLogData.clear();
		this.keywordLogData.clear();
		this.chargeLogData.clear();
		this.mobileChargeLogData.clear();
		this.normalChargeLogData.clear();
		this.normalDayMediaLogData.clear();
		this.drcData.clear();
		this.delShopLogData.clear();
		this.skyChargeData.clear();
		this.skyDrcData.clear();
		this.convLogData.clear();
		this.ShopConData.clear();
		this.shopStatsData.clear();
		//¼îÄÜ¿ë Ãß°¡
		this.normalChargeLogDataShopcon.clear();
	}
	public ArrayList getShopConData() {
		return ShopConData;
	}
	public void setShopConData(ArrayList shopConData) {
		ShopConData = shopConData;
	}
	public ArrayList getConvLogData() {
		return convLogData;
	}
	public void setConvLogData(ArrayList convLogData) {
		this.convLogData = convLogData;
	}
	public ArrayList getRfData() {
		return rfData;
	}
	public void setClickData(ArrayList rfData) {
		this.rfData = rfData;
	}
	public ArrayList getRf_KeywordData() {
		return rf_KeywordData;
	}
	public void setRf_KeywordData(ArrayList rf_KeywordData) {
		this.rf_KeywordData = rf_KeywordData;
	}
	public ArrayList getShopLogData() {
		return shopLogData;
	}
	public void setShopLogData(ArrayList shopLogData) {
		this.shopLogData = shopLogData;
	}
	public ArrayList getMediaLogData() {
		return mediaLogData;
	}
	public void setMediaLogData(ArrayList mediaLogData) {
		this.mediaLogData = mediaLogData;
	}
	public ArrayList getKeywordLogData() {
		return keywordLogData;
	}
	public void setKeywordLogData(ArrayList keywordLogData) {
		this.keywordLogData = keywordLogData;
	}
	public ArrayList getChargeLogData() {
		return chargeLogData;
	}
	public void setChargeLogData(ArrayList chargeLogData) {
		this.chargeLogData = chargeLogData;
	}
	public ArrayList getMobileChargeLogData() {
		return mobileChargeLogData;
	}
	public void setMobileChargeLogData(ArrayList mobileChargeLogData) {
		this.mobileChargeLogData = mobileChargeLogData;
	}
	public ArrayList getNormalChargeLogData() {
		return normalChargeLogData;
	}
	public void setNormalChargeLogData(ArrayList normalChargeLogData) {
		this.normalChargeLogData = normalChargeLogData;
	}
	public ArrayList getNormalDayMediaLogData() {
		return normalDayMediaLogData;
	}
	public void setNormalDayMediaLogData(ArrayList normalDayMediaLogData) {
		this.normalDayMediaLogData = normalDayMediaLogData;
	}
	public ArrayList getDrcData() {
		return drcData;
	}
	public void setDrcData(ArrayList drcData) {
		this.drcData = drcData;
	}
	public ArrayList getDelShopLogData() {
		return delShopLogData;
	}
	public void setDelShopLogData(ArrayList delShopLogData) {
		this.delShopLogData = delShopLogData;
	}
	public ArrayList getSkyChargeData() {
		return skyChargeData;
	}
	public void setSkyChargeData(ArrayList skyChargeData) {
		this.skyChargeData = skyChargeData;
	}
	public ArrayList getSkyDrcData() {
		return skyDrcData;
	}
	public void setSkyDrcData(ArrayList skyDrcData) {
		this.skyDrcData = skyDrcData;
	}
	public HashMap getShopStatsData() {
		return shopStatsData;
	}
	public void setShopStatsData(HashMap shopStatsData) {
		this.shopStatsData = shopStatsData;
	}
	public ArrayList getNormalChargeLogDataShopcon() {
		return normalChargeLogDataShopcon;
	}
	public void setNormalChargeLogDataShopcon(ArrayList normalChargeLogDataShopcon) {
		this.normalChargeLogDataShopcon = normalChargeLogDataShopcon;
	}
}