

package com.adgather.reportmodel;

import java.io.Serializable;

public class DayMediaLogData implements Serializable{
	private String MC;
	private String MCODE;
	private String SDATE;
	private int VIEWCNT;
	private int CLICKCNT;
	private int POINT;
	private String GUBUN;
	private String MEDIA_ID;
	private String actGubun;
	
	public String getInfo(String str){
		return str + toString();
	}
	@Override
	public String toString() {
		return "DayMediaLogData [MC=" + MC + ", MCODE=" + MCODE + ", SDATE="
				+ SDATE + ", VIEWCNT=" + VIEWCNT + ", CLICKCNT=" + CLICKCNT
				+ ", POINT=" + POINT + ", GUBUN=" + GUBUN + ", MEDIA_ID="
				+ MEDIA_ID + ", actGubun=" + actGubun + "]";
	}
	public String getMC() {
		return MC;
	}
	public void setMC(String mC) {
		MC = mC;
	}
	public String getMCODE() {
		return MCODE;
	}
	public void setMCODE(String mCODE) {
		MCODE = mCODE;
	}
	public String getSDATE() {
		return SDATE;
	}
	public void setSDATE(String sDATE) {
		SDATE = sDATE;
	}
	public int getVIEWCNT() {
		return VIEWCNT;
	}
	public void setVIEWCNT(int vIEWCNT) {
		VIEWCNT = vIEWCNT;
	}
	public int getCLICKCNT() {
		return CLICKCNT;
	}
	public void setCLICKCNT(int cLICKCNT) {
		CLICKCNT = cLICKCNT;
	}
	public int getPOINT() {
		return POINT;
	}
	public void setPOINT(int pOINT) {
		POINT = pOINT;
	}
	public String getGUBUN() {
		return GUBUN;
	}
	public void setGUBUN(String gUBUN) {
		GUBUN = gUBUN;
	}
	public String getMEDIA_ID() {
		return MEDIA_ID;
	}
	public void setMEDIA_ID(String mEDIAID) {
		MEDIA_ID = mEDIAID;
	}
	public String getActGubun() {
		return actGubun;
	}
	public void setActGubun(String actGubun) {
		this.actGubun = actGubun;
	}
}