package com.adgather.reportmodel;
import java.io.Serializable;
import java.sql.Date;

public class PointData implements Serializable{
	private String USERID;
	private int AD;
	private int KL;
	private int SR;
	private int UM;
	private int ST;
	private int SP;
	
	private int IAD;
	private int IKL;
	private int ISR;
	private int IUM;
	private int IST;
	private int ISP;
	
	private int SAD;
	private int SKL;
	private int SSR;
	private int SUM;
	private int SST;
	private int SSP;
	private int point;
	
	@Override
	public String toString() {
		return "PointData [USERID=" + USERID + ", AD=" + AD + ", KL=" + KL
				+ ", SR=" + SR + ", UM=" + UM + ", ST=" + ST + ", SP=" + SP
				+ ", IAD=" + IAD + ", IKL=" + IKL + ", ISR=" + ISR + ", IUM="
				+ IUM + ", IST=" + IST + ", ISP=" + ISP + ", SAD=" + SAD
				+ ", SKL=" + SKL + ", SSR=" + SSR + ", SUM=" + SUM + ", SST="
				+ SST + ", SSP=" + SSP + ", cookieDay=" + cookieDay
				+ ", cookie_dirsales=" + cookie_dirsales
				+ ", cookie_indirsales=" + cookie_indirsales + ", adWeight="
				+ adWeight + ", adWeightRate=" + adWeightRate + ", svcType="
				+ svcType + "]";
	}

	private int cookieDay;
	private int cookie_dirsales;
	private int cookie_indirsales;
	
	private float adWeight;
	private int adWeightRate;
	private String svcType;
	
	public String getInfo(String str){
		String rt="";
			try{
				rt= str + toString();
			}catch(Exception e){}
		return rt;
	}
	
	public String getUSERID() {
		return USERID;
	}
	public void setUSERID(String uSERID) {
		USERID = uSERID;
	}
	public int getAD() {
		return AD;
	}
	public void setAD(int aD) {
		AD = aD;
	}
	public int getKL() {
		return KL;
	}
	public void setKL(int kL) {
		KL = kL;
	}
	public int getSR() {
		return SR;
	}
	public void setSR(int sR) {
		SR = sR;
	}
	public int getUM() {
		return UM;
	}
	public void setUM(int uM) {
		UM = uM;
	}
	public int getIAD() {
		return IAD;
	}
	public void setIAD(int iAD) {
		IAD = iAD;
	}
	public int getIKL() {
		return IKL;
	}
	public void setIKL(int iKL) {
		IKL = iKL;
	}
	public int getISR() {
		return ISR;
	}
	public void setISR(int iSR) {
		ISR = iSR;
	}
	public int getIUM() {
		return IUM;
	}
	public void setIUM(int iUM) {
		IUM = iUM;
	}
	public int getSAD() {
		return SAD;
	}
	public void setSAD(int sAD) {
		SAD = sAD;
	}
	public int getSKL() {
		return SKL;
	}
	public void setSKL(int sKL) {
		SKL = sKL;
	}
	public int getSSR() {
		return SSR;
	}
	public void setSSR(int sSR) {
		SSR = sSR;
	}
	public int getSUM() {
		return SUM;
	}
	public void setSUM(int sUM) {
		SUM = sUM;
	}
	public int getCookieDay() {
		return cookieDay;
	}
	public void setCookieDay(int cookieDay) {
		this.cookieDay = cookieDay;
	}

	public int getCookie_dirsales() {
		return cookie_dirsales;
	}

	public void setCookie_dirsales(int cookie_dirsales) {
		this.cookie_dirsales = cookie_dirsales;
	}

	public int getCookie_indirsales() {
		return cookie_indirsales;
	}

	public void setCookie_indirsales(int cookie_indirsales) {
		this.cookie_indirsales = cookie_indirsales;
	}

	public int getST() {
		return ST;
	}

	public void setST(int sT) {
		ST = sT;
	}

	public int getIST() {
		return IST;
	}

	public void setIST(int iST) {
		IST = iST;
	}

	public int getSST() {
		return SST;
	}

	public void setSST(int sST) {
		SST = sST;
	}

	public int getSP() {
		return SP;
	}

	public void setSP(int sP) {
		SP = sP;
	}

	public int getISP() {
		return ISP;
	}

	public void setISP(int iSP) {
		ISP = iSP;
	}

	public int getSSP() {
		return SSP;
	}

	public void setSSP(int sSP) {
		SSP = sSP;
	}
	public String getSvcType() {
		return svcType;
	}

	public void setSvcType(String svcType) {
		this.svcType = svcType;
	}

	public float getAdWeight() {
		return adWeight;
	}

	public void setAdWeight(float adWeight) {
		this.adWeight = adWeight;
	}

	public int getAdWeightRate() {
		return adWeightRate;
	}

	public void setAdWeightRate(int adWeightRate) {
		this.adWeightRate = adWeightRate;
	}

	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}
}
