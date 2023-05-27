package com.adgather.util;
import java.util.Hashtable;
public class MassInformation {
	  
	private String Mass = "";
	private String domain = "";
	private String[] DetailList;
	private String refererUrl = "";
	private String pQuery = "";
	public MassInformation(Hashtable<String, String> domainTable,String url) {
		if(!url.equals("bookmark")) {
			if(url.startsWith("http%3A%2F%2F")) {
				try {
					refererUrl = java.net.URLDecoder.decode(url, "euc-kr");
				} catch (Exception e) {}
			} else { 
				refererUrl = url;
			}			
			
			DetailList = refererUrl.split("/");
			//domain = GetDomain(DetailList);		
			if(DetailList!=null && DetailList.length>=3){
				domain=DetailList[2];
				if(domain.length()>0){
					pQuery = domainTable.get(domain);	
				}
			}
		} else {
			refererUrl = "";
		}
		 
	}
	
	public String GetDecodingKeyword() {
		String keyword = "";
		if(!refererUrl.equals("")) {			
			if(pQuery!=null && pQuery.length()>0) {
				keyword = ExtractKeyword(pQuery);
			}
		}
		return keyword;
	}
	
	public String ExtractKeyword(String pQuery) {
		String encoderKeyword = GetKeyword(refererUrl, pQuery);
		KDecoder decoder = new KDecoder();		
		return decoder.decoderkeywordString(encoderKeyword);
	}
	
	public String encoding(String encodingStr, String type) {
		KDecoder encoder = new KDecoder();		
		return encoder.encodingkeywordString(encodingStr, type);	
	}	
	
	public String ExtractMediaName() {
		return domain;
	}
	//public String GetDomain(String[] list) {
	//	return list[2];
	//}
	
	public String GetPath(String url) {
		String path = url.substring(0, url.indexOf("?"));		
		String[] PathList = path.split("/");
		StringBuffer SPath = new StringBuffer();
		for(int i = 3; i < PathList.length; i++) {			
			SPath.append(PathList[i]);
		}
		return SPath.toString();
	}
	
	public String GetParam(String url) {
		String path = url.substring(url.indexOf("?") + 1, url.length());
		return path;		
	}
	
	public String GetKeyword(String url, String param) {
		String[] paramList = GetParam(url).split("&");
		for(int i = 0; i < paramList.length; i++) {		
			String[] params = paramList[i].split("=");
			if(params!=null && params.length>1 && params[0].equals(param)){
				return params[1];
			}
		}
		return "";
	}

	public String getMass() {
		if(this.Mass.equals("")) {
			return "direct";
		} else {
			return this.Mass;
		}
	}

	public void setMass(String mass) {
		Mass = mass;
	}
}
/* 
 	http://search.naver.com/search.naver?where=nexearch&query=%B8%AE%BC%AD%C4%A1%BE%D6%B5%E5&sm=top_hty&fbm=1
*/