package com.adgather.reportmodel;

import java.io.Serializable;

public class AdData implements Serializable,Cloneable {
	private String siteCode;
	private String size;
	private String userid;
	private int point;
	private String adtag;

	public AdData clone(){
		Object obj = null;
		try {
			obj = super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		AdData copy=(AdData)obj;
		return copy;
	}
	@Override
	public String toString() {
		return "AdData [siteCode=" + siteCode + ", size=" + size + ", userid="
				+ userid + ", point=" + point + ", adtag=" + adtag + "]";
	}

	public String getSiteCode() {
		return siteCode;
	}

	public void setSiteCode(String siteCode) {
		this.siteCode = siteCode;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}

	public String getAdtag() {
		return adtag;
	}

	public void setAdtag(String adtag) {
		this.adtag = adtag;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
