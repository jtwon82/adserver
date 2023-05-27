package com.adgather.reportmodel;

import java.io.Serializable;
import java.sql.Timestamp;

import org.apache.log4j.Logger;

public class ShopData implements Serializable{
	private static Logger logger = Logger.getLogger(ShopData.class);
	
	private long NO;
	private String PARTID;
	private String TARGETGUBUN;
	private String SITE_CODE;
	private String IP;
	private String svc_type;
	private String flag;
	private String PCODE;
	private String GB;
	private String MCGB;
	private String SC_TXT;
	private String PNM;
	private String PURL;
	private String RDATE;
	private String RTIME;
	private String IMGPATH;
	private String URL;
	private String RF;
	private String PRICE;
	private String USERID;
	private String SITE_URL;
	private String CATE1;
	private String site_etc;
	private String etc_type;
	private Timestamp REGDATE;
	private Timestamp LASTUPDATE;

	@Override
	public String toString() {
		return "ShopData [NO=" + NO + ", PARTID=" + PARTID + ", TARGETGUBUN="
				+ TARGETGUBUN + ", SITE_CODE=" + SITE_CODE + ", IP=" + IP
				+ ", svc_type=" + svc_type + ", flag=" + flag + ", PCODE="
				+ PCODE + ", GB=" + GB + ", MCGB=" + MCGB + ", SC_TXT="
				+ SC_TXT + ", PNM=" + PNM + ", PURL=" + PURL + ", RDATE="
				+ RDATE + ", RTIME=" + RTIME + ", IMGPATH=" + IMGPATH
				+ ", URL=" + URL + ", RF=" + RF + ", PRICE=" + PRICE
				+ ", USERID=" + USERID + ", SITE_URL=" + SITE_URL + ", CATE1="
				+ CATE1 + ", site_etc=" + site_etc + ", etc_type=" + etc_type
				+ ", REGDATE=" + REGDATE + ", LASTUPDATE=" + LASTUPDATE + "]";
	}
	public String getInfo(String st){
		String rt="";
		try{
			rt= st + toString();
		}catch(Exception e){
		}
		return rt;
	}
	public void debug(String st){
		logger.debug( getInfo(st) );
	}
	public String getTARGETGUBUN() {
		if(TARGETGUBUN==null) TARGETGUBUN="";
		return TARGETGUBUN;
	}

	public void setTARGETGUBUN(String tARGETGUBUN) {
		TARGETGUBUN = tARGETGUBUN;
	}
	
	public String getCATE1() {
		return CATE1;
	}
	public void setCATE1(String cATE1) {
		CATE1 = cATE1;
	}
	public String getPARTID() {
		return PARTID;
	}
	public void setPARTID(String pARTID) {
		PARTID = pARTID;
	}
	public String getURL() {
		return URL;
	}
	public void setURL(String uRL) {
		URL = uRL;
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
	public String getIP() {
		return IP;
	}
	public void setIP(String iP) {
		IP = iP;
	}
	public String getMCGB() {
		if(MCGB==null) MCGB="";
		return MCGB;
	}
	public void setMCGB(String mCGB) {
		MCGB = mCGB;
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
	public String getPNM() {
		if(PNM!=null && PNM.length()>=50){
			PNM=PNM.substring(0,50);
		}
		return PNM;
	}
	public void setPNM(String pNM) {
		PNM = pNM;
	}
	public String getPCODE() {
		if(PCODE==null) PCODE="";
		return PCODE;
	}
	public void setPCODE(String pCODE) {
		PCODE = pCODE;
	}
	public String getPURL() {
		if(PURL==null) PURL="";
		return PURL;
	}
	public void setPURL(String pURL) {
		PURL = pURL;
	}
	public String getGB() {
		if(GB==null) GB="";
		return GB;
	}
	public void setGB(String gB) {
		GB = gB;
	}
	public String getSITE_CODE() {
		if(SITE_CODE==null) SITE_CODE="";
		return SITE_CODE;
	}
	public void setSITE_CODE(String sITECODE) {
		SITE_CODE = sITECODE;
	}
	public String getIMGPATH() {
		return IMGPATH;
	}
	public void setIMGPATH(String iMGPATH) {
		IMGPATH = iMGPATH;
	}
	public String getPRICE() {
		return PRICE;
	}
	public void setPRICE(String pRICE) {
		PRICE = pRICE;
	}
	public long getNO() {
		return NO;
	}
	public void setNO(long nO) {
		NO = nO;
	}
	public String getUSERID() {
		return USERID;
	}
	public void setUSERID(String uSERID) {
		USERID = uSERID;
	}
	public String getSITE_URL() {
		return SITE_URL;
	}
	public void setSITE_URL(String sITEURL) {
		SITE_URL = sITEURL;
	}
	
	public String getSvc_type() {
		if(svc_type==null){
			return "";
		}else{
			return svc_type;
		}
	}

	public void setSvc_type(String svc_type) {
		this.svc_type = svc_type;
	}
	public String getEtc_type() {
		return etc_type;
	}
	public void setEtc_type(String etc_type) {
		this.etc_type = etc_type;
	}
	public String getSite_etc() {
		return site_etc;
	}
	public void setSite_etc(String site_etc) {
		this.site_etc = site_etc;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public Timestamp getLASTUPDATE() {
		return LASTUPDATE;
	}
	public void setLASTUPDATE(Timestamp lASTUPDATE) {
		LASTUPDATE = lASTUPDATE;
	}
	public Timestamp getREGDATE() {
		return REGDATE;
	}
	public void setREGDATE(Timestamp rEGDATE) {
		REGDATE = rEGDATE;
	}

}