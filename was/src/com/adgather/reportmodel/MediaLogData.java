package com.adgather.reportmodel;

import java.io.Serializable;

public class MediaLogData implements Serializable{
	@Override
	public String toString() {
		return "MediaLogData [S=" + S + ", IP=" + IP + ", SDATE=" + SDATE
				+ ", VIEWCNT=" + VIEWCNT + ", logId=" + logId + "]";
	}
	private String S;
	private String IP;
	private String SDATE;
	private int VIEWCNT;
	private int logId;
	public String getS() {
		return S;
	}
	public void setS(String s) {
		S = s;
	}
	public String getIP() {
		return IP;
	}
	public void setIP(String iP) {
		IP = iP;
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
	public int getLogId() {
		return logId;
	}
	public void setLogId(int logId) {
		this.logId = logId;
	} 

}

