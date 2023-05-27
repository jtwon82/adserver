package com.adgather.reportmodel;

import java.io.Serializable;

public class RFData implements Serializable,Cloneable{
	private String PARTID;
	private String IP;
	private String CURL;
	private String URL; 
	private String MCGB;
	private String RF;
	private String SC_TXT;
	private String RDATE;
	private String RTIME;
	private String RURL;
	private int DPOINT;
	private String MC;
	
	private String MEDIA_CODE;
	private String SITE_CODE;
	private String SITE_URL;
	private String STATE;
	private String K_GUBUN;
	private String kno;
	private String svc_type;
	private int setCnt;
	
	private String tbName;
	
	@Override
	public String toString() {
		return "RFData [PARTID=" + PARTID + ", IP=" + IP + ", CURL=" + CURL
				+ ", URL=" + URL + ", MCGB=" + MCGB + ", RF=" + RF
				+ ", SC_TXT=" + SC_TXT + ", RDATE=" + RDATE + ", RTIME="
				+ RTIME + ", RURL=" + RURL + ", DPOINT=" + DPOINT + ", MC="
				+ MC + ", MEDIA_CODE=" + MEDIA_CODE + ", SITE_CODE="
				+ SITE_CODE + ", SITE_URL=" + SITE_URL + ", STATE=" + STATE
				+ ", K_GUBUN=" + K_GUBUN + ", kno=" + kno + ", svc_type="
				+ svc_type + ", setCnt=" + setCnt + ", tbName=" + tbName + "]";
	}
	public RFData clone(){
		Object obj = null;
		try {
			obj = super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		RFData copy=(RFData)obj;
		return copy;
	}
	public String getInfo(String str){
		String rt="";
		try{
			rt= str + " partid="+ this.PARTID +" ip="+ this.IP +" mcgb="+ this.MCGB
					 +" rf="+ this.RF +" sc_txt="+ this.SC_TXT +" rdate="+ this.RDATE +" rtime="+ this.RTIME
					 +" dpoint="+ this.DPOINT +" mc="+ this.MC +" media_code="+ this.MEDIA_CODE +" site_code="+ this.SITE_CODE
					 +" state="+ this.STATE +" k_gubun="+ this.K_GUBUN +" kno="+ this.kno
					 +" svc_type="+ this.svc_type 
					 +" curl="+ this.CURL +" url="+ this.URL +" rurl="+ this.RURL +" site_url="+ this.SITE_URL ;
		}catch(Exception e){}
		return rt;
	}
	public String getPARTID() {
		return PARTID;
	}
	public void setPARTID(String pARTID) {
		PARTID = pARTID;
	}
	public String getIP() {
		return IP;
	}
	public void setIP(String iP) {
		IP = iP;
	}
	public String getCURL() {
		return CURL;
	}
	public void setCURL(String cURL) {
		CURL = cURL;
	}
	public String getURL() {
		return URL;
	}
	public void setURL(String uRL) {
		URL = uRL;
	}
	public String getMCGB() {
		return MCGB;
	}
	public void setMCGB(String mCGB) {
		MCGB = mCGB;
	}
	public String getRF() {
		return RF;
	}
	public void setRF(String rF) {
		RF = rF;
	}
	public String getSC_TXT() {
		return SC_TXT;
	}
	public void setSC_TXT(String sCTXT) {
		SC_TXT = sCTXT;
	}
	public String getRDATE() {
		return RDATE;
	}
	public void setRDATE(String rDATE) {
		RDATE = rDATE;
	}
	public String getRTIME() {
		return RTIME;
	}
	public void setRTIME(String rTIME) {
		RTIME = rTIME;
	}
	public String getRURL() {
		return RURL;
	}
	public void setRURL(String rURL) {
		RURL = rURL;
	}
	public int getDPOINT() {
		return DPOINT;
	}
	public void setDPOINT(int dPOINT) {
		DPOINT = dPOINT;
	}
	public String getMC() {
		return MC;
	}
	public void setMC(String mC) {
		MC = mC;
	}
	public String getMEDIA_CODE() {
		return MEDIA_CODE;
	}
	public void setMEDIA_CODE(String mEDIACODE) {
		MEDIA_CODE = mEDIACODE;
	}
	public String getSITE_CODE() {
		return SITE_CODE;
	}
	public void setSITE_CODE(String sITECODE) {
		SITE_CODE = sITECODE;
	}
	public String getSITE_URL() {
		return SITE_URL;
	}
	public void setSITE_URL(String sITEURL) {
		SITE_URL = sITEURL;
	}
	public String getSTATE() {
		return STATE;
	}
	public void setSTATE(String sTATE) {
		STATE = sTATE;
	}
	public String getK_GUBUN() {
		return K_GUBUN;
	}
	public void setK_GUBUN(String kGUBUN) {
		K_GUBUN = kGUBUN;
	}
	public String getSvc_type() {
		return svc_type;
	}
	public void setSvc_type(String svc_type) {
		this.svc_type = svc_type;
	}
	public String getKno() {
		return kno;
	}
	public void setKno(String kno) {
		this.kno = kno;
	}
	public String getTbName() {
		return tbName;
	}
	public void setTbName(String tbName) {
		this.tbName = tbName;
	}
	public int getSetCnt() {
		return setCnt;
	}
	public void setSetCnt(int setCnt) {
		this.setCnt = setCnt;
	}
}
