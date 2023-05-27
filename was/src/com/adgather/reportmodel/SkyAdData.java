package com.adgather.reportmodel;

import java.util.ArrayList;

public class SkyAdData {

	private long no;
	private String us;
	private String s;
	private String sorts;
	private String site_code;
	private String h_types;
	private String target;
	private String pcode;
	private String logo;
	private String purl;
	private String usrid;
	private String kno;
	private String id;
	private String mcgb; 
	private String adgubun;
	
	public ArrayList<ShopData> data;
	
	public String getInfo(String s1){
		return s1 + toString();
	}
	@Override
	public String toString() {
		return "SkyAdData [no=" + no + ", us=" + us + ", s=" + s + ", sorts="
				+ sorts + ", site_code=" + site_code + ", h_types=" + h_types
				+ ", target=" + target + ", pcode=" + pcode + ", logo=" + logo
				+ ", purl=" + purl + ", usrid=" + usrid + ", kno=" + kno
				+ ", id=" + id + ", mcgb=" + mcgb + ", adgubun=" + adgubun
				+ ", data=" + data + "]";
	}

	public int getDataSize(){
		return data.size();
	}
	public boolean eqSite_code(String a){
		if( site_code.equals(a) )
			return true;
		else
			return false;
	}
	public String getSite_code() {
		return site_code;
	}
	public void setSite_code(String site_code) {
		this.site_code = site_code;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public String getPurl() {
		return purl;
	}
	public void setPurl(String purl) {
		this.purl = purl;
	}
	public ArrayList<ShopData> getData() {
		return this.data;
	}
	public void setData(ArrayList<ShopData> data) {
		this.data= null;
		this.data = data;
	}
	public SkyAdData(){
		this.data= new ArrayList<ShopData>();
	}
	public String getUserid() {
		return usrid;
	}
	public void setUserid(String usrid) {
		this.usrid = usrid;
	}
	public String getSorts() {
		return sorts;
	}
	public void setSorts(String sorts) {
		this.sorts = sorts;
	}
	public long getNo() {
		return no;
	}
	public void setNo(long no) {
		this.no = no;
	}
	public String getS() {
		return s;
	}
	public void setS(String s) {
		this.s = s;
	}
	public String getKno() {
		return kno;
	}
	public void setKno(String kno) {
		this.kno = kno;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUs() {
		return us;
	}

	public void setUs(String us) {
		this.us = us;
	}

	public String getMcgb() {
		return mcgb;
	}

	public void setMcgb(String mcgb) {
		this.mcgb = mcgb;
	}

	public String getH_types() {
		return h_types;
	}

	public void setH_types(String h_types) {
		this.h_types = h_types;
	}
	public String getPcode() {
		return pcode;
	}
	public void setPcode(String pcode) {
		this.pcode = pcode;
	}
	public String getAdgubun() {
		return adgubun;
	}
	public void setAdgubun(String adgubun) {
		this.adgubun = adgubun;
	}
}
