package com.adgather.reportmodel;

import java.io.Serializable;
import java.sql.Timestamp;

public class AdChargeData implements Serializable{
	private String SITE_CODE;
	private String ADGUBUN;
	private String YYYYMMDD;
	private String HHMM;
	private String PC;
	private String USERID;
	private String POINT;
	private String PPOINT;
	private String YM;
	private String NO;
	//private long NO2;
	private String IP;
	private String KNO;
	private String S;
	private String SCRIPTUSERID;
	private String TYPE;
	private int SRView2;
	private String PCODE;
	private String PRODUCT;
	private String ymdhms;
	private Timestamp regdate;
	/* use um, kl, ad */
	private int viewcnt3;
	
	/* use conversion */
	private String ordRFUrl;
	private String ordQty;
	private String PNm;
	private String price;
	private String ordCode;
	private String uname;
	private String usex;
	private String upno;
	private String direct;
	
	private String MCGB;
	private String shopcon_sdate;
	private String shopcon_sitecode;
	private String shopcon_sereal_no;
	private String shopcon_weight;
	
	public String getInfo(String st){
		try{
			return st + toString();
		}catch(Exception e){
			return "getInfo : error";
		}
	}
	@Override
	public String toString() {
		return "AdChargeData [SITE_CODE=" + SITE_CODE + ", ADGUBUN=" + ADGUBUN
				+ ", YYYYMMDD=" + YYYYMMDD + ", HHMM=" + HHMM + ", PC=" + PC
				+ ", USERID=" + USERID + ", POINT=" + POINT + ", PPOINT="
				+ PPOINT + ", YM=" + YM + ", NO=" + NO + ", IP=" + IP
				+ ", KNO=" + KNO + ", S=" + S + ", SCRIPTUSERID="
				+ SCRIPTUSERID + ", TYPE=" + TYPE + ", SRView2=" + SRView2
				+ ", PCODE=" + PCODE + ", PRODUCT=" + PRODUCT + ", ymdhms="
				+ ymdhms + ", regdate=" + regdate + ", viewcnt3=" + viewcnt3
				+ ", ordRFUrl=" + ordRFUrl + ", ordQty=" + ordQty + ", PNm="
				+ PNm + ", price=" + price + ", ordCode=" + ordCode
				+ ", uname=" + uname + ", usex=" + usex + ", upno=" + upno
				+ ", direct=" + direct + ", MCGB=" + MCGB + ", shopcon_sdate="
				+ shopcon_sdate + ", shopcon_sitecode=" + shopcon_sitecode
				+ ", shopcon_sereal_no=" + shopcon_sereal_no
				+ ", shopcon_weight=" + shopcon_weight + "]";
	}

	public Timestamp getRegdate() {
		return regdate;
	}
	public void setRegdate(Timestamp regdate) {
		this.regdate = regdate;
	}
	public String getYmdhms() {
		return ymdhms;
	}
	public void setYmdhms(String ymdhms) {
		this.ymdhms = ymdhms;
	}
	public String getSITE_CODE() {
		return SITE_CODE;
	}
	public void setSITE_CODE(String sITECODE) {
		SITE_CODE = sITECODE;
	}
	public String getADGUBUN() {
		return ADGUBUN;
	}
	public void setADGUBUN(String aDGUBUN) {
		ADGUBUN = aDGUBUN;
	}
	public String getYYYYMMDD() {
		return YYYYMMDD;
	}
	public void setYYYYMMDD(String yYYYMMDD) {
		YYYYMMDD = yYYYMMDD;
	}
	public String getHHMM() {
		return HHMM;
	}
	public void setHHMM(String hHMM) {
		HHMM = hHMM;
	}
	public String getPC() {
		return PC;
	}
	public void setPC(String pC) {
		PC = pC;
	}
	public String getPOINT() {
		return POINT;
	}
	public void setPOINT(String pOINT) {
		POINT = pOINT;
	}
	public String getPPOINT() {
		return PPOINT;
	}
	public void setPPOINT(String pPOINT) {
		PPOINT = pPOINT;
	}
	public String getYM() {
		return YM;
	}
	public void setYM(String yM) {
		YM = yM;
	}
	public String getNO() {
		if(this.NO==null || this.NO.equals("")){
			this.NO="0";
		}
		return NO;
	}
	public void setNO(String nO) {
		NO = nO;
	}
	public String getIP() {
		return IP;
	}
	public void setIP(String iP) {
		IP = iP;
	}
	public String getKNO() {
		return KNO;
	}
	public void setKNO(String kNO) {
		KNO = kNO;
	}
	public String getS() {
		return S;
	}
	public void setS(String s) {
		S = s;
	}
	public String getUSERID() {
		return USERID;
	}
	public void setUSERID(String uSERID) {
		USERID = uSERID;
	}
	public String getSCRIPTUSERID() {
		return SCRIPTUSERID;
	}
	public void setSCRIPTUSERID(String sCRIPTUSERID) {
		SCRIPTUSERID = sCRIPTUSERID;
	}
	public String getTYPE() {
		return TYPE;
	}
	public void setTYPE(String tYPE) {
		TYPE = tYPE;
	}
	public int getSRView2() {
		return SRView2;
	}
	public void setSRView2(int sRView2) {
		SRView2 = sRView2;
	}
	public String getPCODE() {
		return PCODE;
	}
	public void setPCODE(String pCODE) {
		PCODE = pCODE;
	}
	public String getPRODUCT() {
		return PRODUCT;
	}
	public void setPRODUCT(String pRODUCT) {
		PRODUCT = pRODUCT;
	}
	public String getOrdRFUrl() {
		return ordRFUrl;
	}
	public void setOrdRFUrl(String ordRFUrl) {
		this.ordRFUrl = ordRFUrl;
	}
	public String getOrdQty() {
		return ordQty;
	}
	public void setOrdQty(String ordQty) {
		this.ordQty = ordQty;
	}
	public String getPNm() {
		return PNm;
	}
	public void setPNm(String pNm) {
		PNm = pNm;
	}
	public String getOrdCode() {
		return ordCode;
	}
	public void setOrdCode(String ordCode) {
		this.ordCode = ordCode;
	}
	public String getPrice() {
		try{
			int tmp=Integer.parseInt(price);
		}catch(Exception e){
			price="0";
		}
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getUname() {
		return uname;
	}
	public void setUname(String uname) {
		this.uname = uname;
	}
	public String getUsex() {
		return usex;
	}
	public void setUsex(String usex) {
		this.usex = usex;
	}
	public String getUpno() {
		return upno;
	}
	public void setUpno(String upno) {
		this.upno = upno;
	}
	public String getMCGB() {
		if(this.MCGB==null) this.MCGB="";
		return MCGB;
	}
	public void setMCGB(String mCGB) {
		MCGB = mCGB;
	}
	public int getViewcnt3() {
		return viewcnt3;
	}
	public void setViewcnt3(int viewcnt3) {
		this.viewcnt3 = viewcnt3;
	}
	public String getDirect() {
		return direct;
	}
	public void setDirect(String direct) {
		this.direct = direct;
	}
	public String getShopcon_sdate() {
		return shopcon_sdate;
	}
	public void setShopcon_sdate(String shopcon_sdate) {
		this.shopcon_sdate = shopcon_sdate;
	}
	public String getShopcon_sitecode() {
		return shopcon_sitecode;
	}
	public void setShopcon_sitecode(String shopcon_sitecode) {
		this.shopcon_sitecode = shopcon_sitecode;
	}
	public String getShopcon_sereal_no() {
		return shopcon_sereal_no;
	}
	public void setShopcon_sereal_no(String shopcon_sereal_no) {
		this.shopcon_sereal_no = shopcon_sereal_no;
	}
	public String getShopcon_weight() {
		return shopcon_weight;
	}
	public void setShopcon_weight(String shopcon_weight) {
		this.shopcon_weight = shopcon_weight;
	}
}
