package com.adgather.reportmodel;

import java.io.Serializable;

public class SiteCodeData implements Serializable,Cloneable{
	public SiteCodeData clone(){
		Object obj = null;
		try {
			obj = super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		SiteCodeData copy=(SiteCodeData)obj;
		return copy;
	}
	private String site_code;
	private String userid;
	private String state;
	private String site_code_s; 
	private String sales_url; 
	private String sales_prdno;
	private String site_etc;
	private String dispo_age;
	private String dispo_sex;
	private String cookieday;
	private int usemoney;
	private int usedmoney;
	private String ad_rhour;
	
	
	
	
	public int getUsemoney() {
		return usemoney;
	}

	public void setUsemoney(int usemoney) {
		this.usemoney = usemoney;
	}

	public String getAd_rhour() {
		return ad_rhour;
	}

	public void setAd_rhour(String ad_rhour) {
		this.ad_rhour = ad_rhour;
	}
	private int point;

	@Override
	public String toString() {
		return "SiteCodeData [site_code=" + site_code + ", userid=" + userid
				+ ", state=" + state + ", site_code_s=" + site_code_s
				+ ", sales_url=" + sales_url + ", sales_prdno=" + sales_prdno
				+ ", site_etc=" + site_etc + ", dispo_age=" + dispo_age
				+ ", dispo_sex=" + dispo_sex + ", cookieday=" + cookieday
				+ ", usemoney=" + usemoney + ", usedmoney=" + usedmoney
				+ ", ad_rhour=" + ad_rhour + ", point=" + point + "]";
	}

	public int getUsedmoney() {
		return usedmoney;
	}

	public void setUsedmoney(int usedmoney) {
		this.usedmoney = usedmoney;
	}

	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}

	public String getInfo(String str){
		try{
			return str +" site_code="+ this.site_code +", userid="+ this.userid 
				+", status="+ this.state +", dispo_age="+ this.dispo_age +", dispo_sex="+ this.dispo_sex
				+", sales_prdno="+ this.sales_prdno +", site_etc="+ this.site_etc +", cookieday="+ this.cookieday
				+", sales_url="+ this.sales_url;
		}catch(Exception e){
			return "";
		}
	}
	
	public String getSales_url() {
		return sales_url;
	}
	public void setSales_url(String sales_url) {
		this.sales_url = sales_url;
	}
	public String getSales_prdno() {
		return sales_prdno;
	}
	public void setSales_prdno(String sales_prdno) {
		this.sales_prdno = sales_prdno;
	}
	public String getSite_code() {
		return site_code;
	}
	public void setSite_code(String siteCode) {
		site_code = siteCode;
	}
	public String getSite_code_s() {
		return site_code_s;
	}
	public void setSite_code_s(String siteCodeS) {
		site_code_s = siteCodeS;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getSite_etc() {
		return site_etc;
	}

	public void setSite_etc(String site_etc) {
		this.site_etc = site_etc;
	}

	public String getDispo_age() {
		return dispo_age;
	}

	public void setDispo_age(String dispo_age) {
		this.dispo_age = dispo_age;
	}

	public String getDispo_sex() {
		return dispo_sex;
	}

	public void setDispo_sex(String dispo_sex) {
		this.dispo_sex = dispo_sex;
	}

	public String getCookieday() {
		return cookieday;
	}

	public void setCookieday(String cookieday) {
		this.cookieday = cookieday;
	}
}