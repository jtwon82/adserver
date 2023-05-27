package com.adgather.reportmodel;

import java.io.Serializable;

public class DrcData implements Serializable{
	private String keyIp;
	private String u;
	private String gb;
	private String sc;
	private String s;
	private String mc;
	private String no;
	private String pCode;
	private String kno;
	private String mcgb;
	private String product;
	private int point;
	private long actDate;

	@Override
	public String toString() {
		return "DrcData [keyIp=" + keyIp + ", u=" + u + ", gb=" + gb + ", sc="
				+ sc + ", s=" + s + ", mc=" + mc + ", no=" + no + ", pCode="
				+ pCode + ", kno=" + kno + ", mcgb=" + mcgb + ", product="
				+ product + ", point=" + point + "]";
	}
	public int getPoint() {
		return point;
	}
	public void setPoint(int point) {
		this.point = point;
	}
	public String getInfo(String st){
		return st + toString();
	}
	public String getKeyIp() {
		return keyIp;
	}
	public void setKeyIp(String keyIp) {
		this.keyIp = keyIp;
	}
	public String getU() {
		return u;
	}
	public void setU(String u) {
		this.u = u;
	}
	public String getGb() {
		return gb;
	}
	public void setGb(String gb) {
		this.gb = gb;
	}
	public String getSc() {
		return sc;
	}
	public void setSc(String sc) {
		this.sc = sc;
	}
	public String getS() {
		return s;
	}
	public void setS(String s) {
		this.s = s;
	}
	public String getMc() {
		return mc;
	}
	public void setMc(String mc) {
		this.mc = mc;
	}
	public String getKno() {
		return kno;
	}
	public void setKno(String kno) {
		this.kno = kno;
	}
	public String getMcgb() {
		return mcgb;
	}
	public void setMcgb(String mcgb) {
		this.mcgb = mcgb;
	}
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	public String getpCode() {
		return pCode;
	}
	public void setpCode(String pCode) {
		this.pCode = pCode;
	}
	public long getActDate() {
		return actDate;
	}
	public void setActDate(long actDate) {
		this.actDate = actDate;
	}
}
