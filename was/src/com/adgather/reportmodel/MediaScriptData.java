package com.adgather.reportmodel;
import java.io.Serializable;
public class MediaScriptData implements Serializable {
	private String USERID;
	private String AD_TYPE;
	private String NO;
	private String H_TYPE;
	private String H_BANNER;
	private String mediasite_no;
	private String accept_sr;
	private String accept_um;
	private String accept_kl;
	private String accept_ad;
	private String accept_st;
	private String accept_sp;

	private String limit_domains;
	private int weight_pct;
	
	@Override
	public String toString() {
		return "MediaScriptData [USERID=" + USERID + ", AD_TYPE=" + AD_TYPE
				+ ", NO=" + NO + ", H_TYPE=" + H_TYPE + ", H_BANNER="
				+ H_BANNER + ", mediasite_no=" + mediasite_no + ", accept_sr="
				+ accept_sr + ", accept_um=" + accept_um + ", accept_kl="
				+ accept_kl + ", accept_ad=" + accept_ad + ", accept_st="
				+ accept_st + ", accept_sp=" + accept_sp + ", limit_domains="
				+ limit_domains + ", weight_pct=" + weight_pct + "]";
	}

	
	public String getInfo(String a){
		try{
			return a + toString();
		}catch(Exception e){
			return "";
		}
	}
	
	public String getUSERID() {
		return USERID;
	}
	public void setUSERID(String uSERID) {
		USERID = uSERID;
	}
	public String getAD_TYPE() {
		return AD_TYPE;
	}
	public void setAD_TYPE(String aDTYPE) {
		AD_TYPE = aDTYPE;
	}
	public String getNO() {
		return NO;
	}
	public void setNO(String nO) {
		NO = nO;
	}
	public String getH_BANNER() {
		return H_BANNER;
	}
	public void setH_BANNER(String hBANNER) {
		H_BANNER = hBANNER;
	}
	public String getMediasite_no() {
		return mediasite_no;
	}
	public void setMediasite_no(String mediasite_no) {
		this.mediasite_no = mediasite_no;
	}

	public String getAccept_sr() {
		return accept_sr;
	}

	public void setAccept_sr(String accept_sr) {
		this.accept_sr = accept_sr;
	}

	public String getAccept_um() {
		return accept_um;
	}

	public void setAccept_um(String accept_um) {
		this.accept_um = accept_um;
	}

	public String getAccept_kl() {
		return accept_kl;
	}

	public void setAccept_kl(String accept_kl) {
		this.accept_kl = accept_kl;
	}

	public String getAccept_ad() {
		return accept_ad;
	}

	public void setAccept_ad(String accept_ad) {
		this.accept_ad = accept_ad;
	}

	public String getLimit_domains() {
		return limit_domains;
	}

	public void setLimit_domains(String limit_domains) {
		this.limit_domains = limit_domains;
	}

	public String getH_TYPE() {
		return H_TYPE;
	}

	public void setH_TYPE(String h_TYPE) {
		H_TYPE = h_TYPE;
	}

	public int getWeight_pct() {
		return weight_pct;
	}

	public void setWeight_pct(int weight_pct) {
		this.weight_pct = weight_pct;
	}
	public String getAccept_st() {
		return accept_st;
	}

	public void setAccept_st(String accept_st) {
		this.accept_st = accept_st;
	}


	public String getAccept_sp() {
		return accept_sp;
	}


	public void setAccept_sp(String accept_sp) {
		this.accept_sp = accept_sp;
	}
}
