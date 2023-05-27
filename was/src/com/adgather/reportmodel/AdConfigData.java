package com.adgather.reportmodel;

import java.io.Serializable;

public class AdConfigData implements Serializable,Cloneable{
	public AdConfigData clone(){
		Object obj = null;
		try {
			obj = super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		AdConfigData copy=(AdConfigData)obj;
		return copy;
	}
	private String site_code;
	private String site_code_tmp;
	private String site_url;
	private String userid;
	private String site_etc;
	private String media_code;
	private String url_style;
	private String pop;
	private String kno;
	private String scriptno;
	private String mediano;
	private String adtxt;
	private String no;
	private String svc_type;
	private String recom_ad;
	private String dispo_sex;
	private String dispo_age;

	private String site_title;
	private String site_name;
	private String site_desc;
	private String purl;
	private String imgname;
	private String imgname2;
	private String imgname3;
	private String imgname4;
	private String imgname5;
	private String imgname6;
	private String imgname7;
	private String imgname_logo;
	private String imgpath;
	private String pnm;
	private String price;
	private String mallnm;
	private String pcode;
	private String gubun;
	
	private String banner_path1;
	private String banner_path2;
	private String site_descm;
	private String site_urlm;
	private String stateM;
	private String stateW;
	private String site_etcm;
	private String homepi;
	
	/* ?????? a??? */
	private String mcgb;
	private String cate1;
	private String gb;
	
	/* url??????? ??????? ???? */
	private String ismain;
	private String flag;
	
	private boolean adnew=false;
	private String adnew_html;
	
	//쇼콘로고 생성
	private String schonlogo;

	@Override
	public String toString() {
		return "AdConfigData [site_code=" + site_code + ", site_code_tmp="
				+ site_code_tmp + ", site_url=" + site_url + ", userid="
				+ userid + ", site_etc=" + site_etc + ", media_code="
				+ media_code + ", url_style=" + url_style + ", pop=" + pop
				+ ", kno=" + kno + ", scriptno=" + scriptno + ", mediano="
				+ mediano + ", adtxt=" + adtxt + ", no=" + no + ", svc_type="
				+ svc_type + ", recom_ad=" + recom_ad + ", dispo_sex="
				+ dispo_sex + ", dispo_age=" + dispo_age + ", site_title="
				+ site_title + ", site_name=" + site_name + ", site_desc="
				+ site_desc + ", purl=" + purl + ", imgname=" + imgname
				+ ", imgname2=" + imgname2 + ", imgname3=" + imgname3
				+ ", imgname4=" + imgname4 + ", imgname5=" + imgname5
				+ ", imgname6=" + imgname6 + ", imgname7=" + imgname7
				+ ", imgname_logo=" + imgname_logo + ", imgpath=" + imgpath
				+ ", pnm=" + pnm + ", price=" + price + ", mallnm=" + mallnm
				+ ", pcode=" + pcode + ", gubun=" + gubun + ", banner_path1="
				+ banner_path1 + ", banner_path2=" + banner_path2
				+ ", site_descm=" + site_descm + ", site_urlm=" + site_urlm
				+ ", stateM=" + stateM + ", stateW=" + stateW + ", site_etcm="
				+ site_etcm + ", homepi=" + homepi + ", mcgb=" + mcgb
				+ ", cate1=" + cate1 + ", gb=" + gb + ", ismain=" + ismain
				+ ", flag=" + flag + ", adnew=" + adnew + ", adnew_html="
				+ adnew_html + ",schonlogo="+ schonlogo +"]";
	}

	public String getInfo(String s){
		try{
			return s +toString();
		}catch(Exception e){
			return "getInfo:"+e;
		}
	}

	public String getSite_etcm() {
		return site_etcm;
	}
	public void setSite_etcm(String site_etcm) {
		this.site_etcm = site_etcm;
	}
	public String getSvc_type() {
		return svc_type;
	}
	public void setSvc_type(String svc_type) {
		this.svc_type = svc_type;
	}
	public String getSite_code() {
		return site_code;
	}
	public void setSite_code(String siteCode) {
		site_code = siteCode;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getSite_etc() {
		if( site_etc==null)
			return "";
		else
			return site_etc;
	}
	public void setSite_etc(String siteEtc) {
		site_etc = siteEtc;
	}
	public String getMedia_code() {
		return media_code;
	}
	public void setMedia_code(String mediaCode) {
		media_code = mediaCode;
	}
	public String getUrl_style() {
		return url_style;
	}
	public void setUrl_style(String urlStyle) {
		url_style = urlStyle;
	}
	public String getPop() {
		return pop;
	}
	public void setPop(String pop) {
		this.pop = pop;
	}
	public String getSite_url() {
		return site_url;
	}
	public void setSite_url(String siteUrl) {
		site_url = siteUrl;
	}
	public String getKno() {
		return kno;
	}
	public void setKno(String kno) {
		this.kno = kno;
	}
	public String getScriptno() {
		return scriptno;
	}
	public void setScriptno(String scriptno) {
		this.scriptno = scriptno;
	}
	public String getAdtxt() {
		return adtxt;
	}
	public void setAdtxt(String adtxt) {
		this.adtxt = adtxt;
	}
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public String getSite_title() {
		return site_title;
	}
	public void setSite_title(String siteTitle) {
		site_title = siteTitle;
	}
	public String getSite_desc() {
		return site_desc;
	}
	public void setSite_desc(String siteDesc) {
		site_desc = siteDesc;
	}
	public String getImgname() {
		return imgname;
	}
	public void setImgname(String imgname) {
		this.imgname = imgname;
	}
	public String getImgname2() {
		return imgname2;
	}
	public void setImgname2(String imgname2) {
		this.imgname2 = imgname2;
	}
	public String getImgname3() {
		return imgname3;
	}
	public void setImgname3(String imgname3) {
		this.imgname3 = imgname3;
	}
	public String getImgname4() {
		return imgname4;
	}
	public void setImgname4(String imgname4) {
		this.imgname4 = imgname4;
	}
	public String getImgname5() {
		return imgname5;
	}
	public void setImgname5(String imgname5) {
		this.imgname5 = imgname5;
	}
	public String getPurl() {
		return purl;
	}
	public void setPurl(String purl) {
		this.purl = purl;
	}
	public String getImgpath() {
		return imgpath;
	}
	public void setImgpath(String imgpath) {
		this.imgpath = imgpath;
	}
	public String getPnm() {
		return pnm;
	}
	public void setPnm(String pnm) {
		this.pnm = pnm;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getMallnm() {
		return mallnm;
	}
	public void setMallnm(String mallnm) {
		this.mallnm = mallnm;
	}
	public String getMediano() {
		return mediano;
	}
	public void setMediano(String mediano) {
		this.mediano = mediano;
	}
	public String getPcode() {
		return pcode;
	}
	public void setPcode(String pcode) {
		this.pcode = pcode;
	}
	public String getRecom_ad() {
		return recom_ad;
	}
	public void setRecom_ad(String recom_ad) {
		this.recom_ad = recom_ad;
	}
	public String getMcgb() {
		return mcgb;
	}
	public void setMcgb(String mcgb) {
		this.mcgb = mcgb;
	}
	public String getIsmain() {
		return ismain;
	}
	public void setIsmain(String ismain) {
		this.ismain = ismain;
	}
	public String getCate1() {
		return cate1;
	}
	public void setCate1(String cate1) {
		this.cate1 = cate1;
	}
	public String getGb() {
		return gb;
	}
	public void setGb(String gb) {
		this.gb = gb;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getDispo_sex() {
		return dispo_sex;
	}
	public void setDispo_sex(String dispo_sex) {
		this.dispo_sex = dispo_sex;
	}
	public String getDispo_age() {
		return dispo_age;
	}
	public void setDispo_age(String dispo_age) {
		this.dispo_age = dispo_age;
	}
	public String getImgname_logo() {
		return imgname_logo;
	}
	public void setImgname_logo(String imgname_logo) {
		this.imgname_logo = imgname_logo;
	}
	public String getSite_name() {
		return site_name==null?"":site_name;
	}
	public void setSite_name(String site_name) {
		this.site_name = site_name;
	}
	
	public String getGubun() {
		return gubun;
	}

	public void setGubun(String gubun) {
		this.gubun = gubun;
	}
	public String getBanner_path1() {
		return banner_path1;
	}
	public void setBanner_path1(String banner_path1) {
		this.banner_path1 = banner_path1;
	}
	public String getBanner_path2() {
		return banner_path2;
	}
	public void setBanner_path2(String banner_path2) {
		this.banner_path2 = banner_path2;
	}
	public String getSite_descm() {
		return site_descm;
	}
	public void setSite_descm(String site_descm) {
		this.site_descm = site_descm;
	}
	public String getSite_urlm() {
		return site_urlm;
	}
	public void setSite_urlm(String site_urlm) {
		this.site_urlm = site_urlm;
	}
	public String getImgname6() {
		return imgname6;
	}
	public void setImgname6(String imgname6) {
		this.imgname6 = imgname6;
	}
	public String getImgname7() {
		return imgname7;
	}
	public void setImgname7(String imgname7) {
		this.imgname7 = imgname7;
	}
	public String getAdnew_html() {
		return adnew_html;
	}
	public void setAdnew_html(String adnew_html) {
		this.adnew_html = adnew_html;
	}
	public boolean isAdnew() {
		return adnew;
	}
	public void setAdnew(boolean adnew) {
		this.adnew = adnew;
	}

	public String getStateM() {
		return stateM;
	}

	public void setStateM(String stateM) {
		this.stateM = stateM;
	}

	public String getStateW() {
		return stateW;
	}

	public void setStateW(String stateW) {
		this.stateW = stateW;
	}

	public String getHomepi() {
		return homepi;
	}

	public void setHomepi(String homepi) {
		this.homepi = homepi;
	}

	public String getSite_code_tmp() {
		return site_code_tmp;
	}

	public void setSite_code_tmp(String site_code_tmp) {
		this.site_code_tmp = site_code_tmp;
	}

	public String getSchonlogo() {
		return schonlogo;
	}

	public void setSchonlogo(String schonlogo) {
		this.schonlogo = schonlogo;
	}
}