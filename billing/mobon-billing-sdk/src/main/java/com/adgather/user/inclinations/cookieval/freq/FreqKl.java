package com.adgather.user.inclinations.cookieval.freq;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;

import com.adgather.user.inclinations.convert.Base64Converter;
import com.adgather.user.inclinations.cookieval.inter.CookieVal;


/**
 * 도메인 관련 프리퀀시 로우 데이터
 * @author yhlim
 *
 */
public class FreqKl implements CookieVal{
	/** values ****************************************/
	public final String DELIMETER = "^";
	
	private String keyword;				//도메인
	private int freqCnt;				//프리퀀시
	
	/** create method **********************************/
	public FreqKl() {}
	
	public FreqKl(String cookieValue) throws Exception {
		setCookieValue(cookieValue);
	}
	
	public FreqKl(Document doc) throws Exception {
		setMongoValue(doc);
	}
	
	public FreqKl(FreqKl obj) {
		this.keyword = obj.keyword;
		this.freqCnt = obj.freqCnt;
	}
	

	/** value get/set method **********************************/
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public int getFreqCnt() {
		return freqCnt;
	}
	public void setFreqCnt(int freqCnt) {
		this.freqCnt = freqCnt;
	}
	
	/** Implements method  **********************************/
	@Override
	public void setCookieValue(Object cookieValue) throws Exception {
		if(cookieValue instanceof String) {
			setCookieValue((String)cookieValue);
		} else {
			throw new Exception("Unsupported Object.");
		}
	}
	public void setCookieValue(String cookieValue) throws Exception {
		if(StringUtils.isEmpty(cookieValue))		throw new Exception("Cookie Value Is Empty.");
		
		String[] strs = StringUtils.splitPreserveAllTokens(cookieValue, DELIMETER);
		if(strs == null || strs.length != 2)		throw new Exception("Cookie Value Is Not Validate.");
		
		if(StringUtils.isEmpty(strs[0]))			throw new Exception("FreqKl keyword Is Empty.");
		
		this.keyword =  Base64Converter.getInstance().decode(strs[0]);
		this.freqCnt = NumberUtils.toInt(strs[1], 0);
	}
	
	@Override
	public Object getCookieValue() {
		return String.format("%s%s%d", Base64Converter.getInstance().encode(StringUtils.defaultString(keyword)), DELIMETER, freqCnt);
	}
	
	@Override
	public void setMongoValue(Object mongoDoc) throws Exception {
		if(mongoDoc instanceof Document) {
			setMongoValue((Document)mongoDoc);
		} else {
			throw new Exception("Unsupported Object.");
		}
	}
	public void setMongoValue(Document mongoDoc) throws Exception {
		if(mongoDoc == null)				throw new Exception("Mongo Value Is Empty.");
		this.keyword = mongoDoc.getString("kwd");
		this.freqCnt = mongoDoc.getInteger("freqCnt", 0);
	}
	
	
	@Override
	public Object getMongoValue() {
		Document doc = new Document();
		doc.put("kwd", this.keyword);
		doc.put("freqCnt", this.freqCnt);
		return doc;
	}
	
	@Override
	public FreqKl clone() throws CloneNotSupportedException {
		return new FreqKl(this);
	}
	
	@Override
	public String getKey() {
		return StringUtils.defaultString(keyword);
	}
	
	@Override
	public void modValue(Object element, boolean bAppendValue) {
		if(!(element instanceof FreqKl))		return;
		
		FreqKl obj = (FreqKl)element;
		if (bAppendValue) {
			this.freqCnt += obj.freqCnt;
		} else {
			this.freqCnt = obj.freqCnt;
		}
	}
}
