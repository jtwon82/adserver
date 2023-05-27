package com.adgather.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.jsoup.select.Elements;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.ObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
 
public class StringUtils{
	private static Logger logger = Logger.getLogger(StringUtils.class);
	public static void main(String[] args){

		StringBuffer a= new StringBuffer("1");
		StringUtils.test(a);
		System.out.println( a );
	}
	public static ArrayList getDocToArray(Elements e){
		ArrayList list=null;
		try{
			for(int i=0; i<e.size(); i++){
				if(list==null) list = new ArrayList();
				list.add(e.get(i).html());
			}
		}catch(Exception x){}
		return list;
	}
	public static String getFreeMakerData(String dir, String file, Map map,String charset) throws Exception {
		charset = org.apache.commons.lang3.StringUtils.defaultIfEmpty(charset, "utf-8");
		String content = "";
		try	{
			Configuration cfg = new Configuration();
			cfg.setDirectoryForTemplateLoading(new File(dir));
			cfg.setObjectWrapper(new DefaultObjectWrapper());
			cfg.setDefaultEncoding(charset);
			cfg.setEncoding(Locale.KOREAN, charset);
			cfg.setSetting(Configuration.CACHE_STORAGE_KEY, "strong:50, soft:250"); 
			cfg.setObjectWrapper(ObjectWrapper.BEANS_WRAPPER); 
			Template temp = cfg.getTemplate(file);
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			Writer output = new OutputStreamWriter(os, charset);
			try {
				temp.process(map, output);
			} catch (TemplateException e) {
				logger.error("getFreeMakerData1:"+e);
			}
			output.flush();
			output.close();
			os.close();
			map.clear();
			content = os.toString(charset);
		}catch (Exception e) {
			logger.error("getFreeMakerData2:"+e);
		}
		return content;
	}
	public static String getFreeMakerData(Configuration cfg,String file, Map map,String charset) throws Exception {
		charset = org.apache.commons.lang3.StringUtils.defaultIfEmpty(charset, "utf-8");
		String content = "";
		try	{
			cfg.setDefaultEncoding(charset);
			cfg.setEncoding(Locale.KOREAN,charset);
			Template temp = cfg.getTemplate(file);
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			Writer output = new OutputStreamWriter(os, charset);
			try {
				temp.process(map, output);
			} catch (TemplateException e) {
				logger.error("getFreeMakerData3:"+e);
			}
			output.flush();
			output.close();
			os.close();
			map.clear();
			content = os.toString(charset);
		}catch (Exception e) {
			logger.error("getFreeMakerData4:"+e);
		}
		return content;
	}
	public static void test(StringBuffer in){
		in.setLength(0);
		in.append("2");
		System.out.println( in );
	}
	public static String getConvertString(HashMap<String,String> h, String div){
		String result="";
		
		for(String a :h.keySet() )
			result += a + div;
		
		return result;		
	}
	public static HashMap<String,String> getConvertHashMap(String str, String div){
		String chk_result= str;
		
		HashMap<String,String> addpq=new HashMap<String,String>();
		if(chk_result!=null && chk_result.length()>0){
			StringTokenizer st=new StringTokenizer(chk_result, div, false);
			while(st.hasMoreElements()){
				String stCode=st.nextElement().toString();
				addpq.put(stCode, stCode);
			}
		}
		return addpq;		
	}
	public static String getReg(String reg, String str_in){
		String a = str_in;
		Pattern p = Pattern.compile(reg);
		Matcher m = p.matcher(a);
		String f = "";
		int breakchk=0;
		while( m.find() ){
			f = ( m.group(0) );
			breakchk++;
			if(breakchk>10000) break;
		}
		return f;
	}
    public static String strCut(String szText, String szKey, int nLength, int nPrev, boolean isNotag, boolean isAdddot) { // 문자열 자르기
        String r_val = szText;
        int oF = 0, oL = 0, rF = 0, rL = 0;
        int nLengthPrev = 0;
        Pattern p = Pattern.compile("<(/?)([^<>]*)?>", Pattern.CASE_INSENSITIVE); // 태그제거 패턴
        if (isNotag) {
            r_val = p.matcher(r_val).replaceAll("");
        } // 태그 제거
        r_val = r_val.replaceAll("&amp;", "&");
        r_val = r_val.replaceAll("(!/|\r|\n|&nbsp;)", ""); // 공백제거
        try {
            byte[] bytes = r_val.getBytes("UTF-8"); // 바이트로 보관
            if (szKey != null && !szKey.equals("")) {
                nLengthPrev = (r_val.indexOf(szKey) == -1) ? 0 : r_val.indexOf(szKey); // 일단 위치찾고
                nLengthPrev = r_val.substring(0, nLengthPrev).getBytes("MS949").length; // 위치까지길이를 byte로 다시 구한다
                nLengthPrev = (nLengthPrev - nPrev >= 0) ? nLengthPrev - nPrev : 0; // 좀 앞부분부터 가져오도록한다.
            }
            // x부터 y길이만큼 잘라낸다. 한글안깨지게.
            int j = 0;
            if (nLengthPrev > 0)
                while (j < bytes.length) {
                    if ((bytes[j] & 0x80) != 0) {
                        oF += 2;
                        rF += 3;
                        if (oF + 2 > nLengthPrev) {
                            break;
                        }
                        j += 3;
                    } else {
                        if (oF + 1 > nLengthPrev) {
                            break;
                        }
                        ++oF;
                        ++rF;
                        ++j;
                    }
                }
            j = rF;
            while (j < bytes.length) {
                if ((bytes[j] & 0x80) != 0) {
                    if (oL + 2 > nLength) {
                        break;
                    }
                    oL += 2;
                    rL += 3;
                    j += 3;
                } else {
                    if (oL + 1 > nLength) {
                        break;
                    }
                    ++oL;
                    ++rL;
                    ++j;
                }
            }
            r_val = new String(bytes, rF, rL, "UTF-8"); // charset 옵션
            if (isAdddot && rF + rL + 3 <= bytes.length) {
                r_val += "...";
            } // ...을 붙일지말지 옵션
        } catch (Exception e) {

        }
        return r_val;
    }
	public static Stack<String> gListStack(String o_list){
		Stack<String> stack=new Stack<String>();
		StringTokenizer st=new StringTokenizer(o_list,"|||",false);
		while(st.hasMoreElements()){
			String sel_o_list=st.nextElement().toString();
			stack.push(sel_o_list);
		}
		return stack;
	}
	public static int gAtCnt(String str1, String ar){
		StringTokenizer st= new StringTokenizer(str1, ar);
		int id= -1;
		while( st.hasMoreElements() ){
			st.nextToken();
			id++;
		}
		return id;
	}
	public static String gAtFirst1(String tgt_list, int id, String ar){
		StringTokenizer st= new StringTokenizer(tgt_list, ar);
		int a=0;
		String tmp= "set";
		String rt1= "";
		logger.debug("gAtFirst1:"+tgt_list+","+id+","+ar);
		while( st.hasMoreElements() ){
			String b= st.nextToken();
			if( b.indexOf("set") == -1 ) b = "set";
			if( a==id ){
				//if( b.length()>5){
					tmp = "set";
				//}else{
				//	tmp = b;
				//}
			}else{
				rt1 = ar + b + rt1;
			}
			a++;
		}
		rt1 = tmp + rt1;
		return rt1;
	}
	public static String gAtFirst(String tgt_list, int id, String ar){
		StringTokenizer st= new StringTokenizer(tgt_list, ar);
		int a=0;
		String tmp= "";
		String rt1= "";
		while( st.hasMoreElements() ){
			String b= st.nextToken();
			if( a==id ){
				tmp = b;
			}else{
				rt1 = ar + b + rt1;
			}
			a++;
		}
		rt1 = tmp + rt1;
		return rt1;
	}
	public static String gAtFirst(String url_list, String item, String ar){
		String rtval = "";
		String rt1 = "";
		if( url_list.indexOf(item)>-1 ){
			StringTokenizer st= new StringTokenizer(url_list, ar);
			while( st.hasMoreElements() ){
				String b= st.nextToken();
				if( b.equals(item) ){
				}else{
					rt1 += ar + b;
				}
			}
			rt1 = item + rt1;
			url_list = rt1;
		}else{			
			rt1 = item + ar + url_list;
		}
		
		rtval= rt1;
		return rtval;
	}
	public static String gAtData(String list, String cmp, String at, String divi, String dfval){
		String rt= "";

		int t = StringUtils.gAtId(cmp, at, divi);
		
		if( t>-1 ){ 
			rt = StringUtils.gAt1(list, t, divi);
		}
		if( rt.equals("") ) rt = dfval;
		
		return rt;
	}
	public static String gAtData(String list, String cmp, String at, String divi){
		String rt= "";

		int t = StringUtils.gAtId(cmp, at, divi);
		
		if( t>-1 ){ 
			rt = StringUtils.gAt1(list, t, divi);
		}
		
		return rt;
	}
	public static int gAtId(String list, String item, String ar){
		StringTokenizer st= new StringTokenizer(list, ar);
		int id= -1;
		int a= id;
		while( st.hasMoreElements() ){
			id++;
			String tmp= st.nextToken();
			if(tmp.equals(item)){
				a= id;
			}
		}
		return a;
	}
	public static String gAt1(String str1, int a, String ar){
		StringTokenizer st= new StringTokenizer(str1, ar);
		String rtval= "";
		int id= 0;
		while( st.hasMoreElements() ){
			rtval= st.nextToken();
			if( id++ == a)	break;
		}
		return rtval;
	}
	public static String gAtSet(String str1, int a, String target, String ar){
		StringTokenizer st= new StringTokenizer(str1, ar);
		String rtval= "";
		int id= 0;
		while( st.hasMoreElements() ){
			if( id++ == a){
				st.nextToken();
				rtval += ar + target;
			}else{
				rtval += ar + st.nextToken();
			}
		}
		if( str1.length() >0 )
			rtval= rtval.substring(ar.length());
		return rtval;
	}
	public static String getURLDecodeStrR(String str, String type, int depth){
		
		if( depth<1 ){
			
		} else if( str.indexOf("%")>-1 ){
			str = getURLDecodeStr(str, type);
			str = getURLDecodeStrR(str, type, --depth);
		}
		return str;
	}
	public static String getURLDecodeStr(String str,String type){
		String rtn="";
		try{
			if(str!=null && str.length()>0){
				try{
					if(str.endsWith("%")) str=str+"25";
					str=URLDecoder.decode(str,type);
				}catch(Exception e){
					str=str+"25";
					try{
						str=URLDecoder.decode(str,type);
					}catch(Exception ex){
						str="";
					}
				}
			}else{
				str="";
			}
			rtn=str;
		}catch(Exception e){
			logger.error("getURLDecodeStr:"+e);
		}
		return rtn;
	}
	public static int getRangeVal(int s, int e){
		ArrayList a = StringUtils.getRandList(e);
		int r = (Integer) a.get(0);
		return r+s;
	}
	
	public static ArrayList<Integer> getRandList(int cnt){
		ArrayList<Integer> list=new ArrayList<Integer>();
		ArrayList<Integer> rtnList=new ArrayList<Integer>();
		for(int i=0;i<cnt;i++){
			list.add(i);
		}
		Random trandom= new Random();
		while(list.size()>0){
			int tx=trandom.nextInt(list.size());
			rtnList.add(list.get(tx));
			list.remove(tx);
		}
		return rtnList;
	}
	public static String getPartIdKey(String ipKey){
		String result="";
		try{
			if( ipKey.length()<2 ){
				ipKey = "000"+ipKey; 
			}
			//System.out.println(ipKey);
			String ipStr=ipKey.replace(".","");
			//System.out.println(ipStr);
			ipStr=ipStr.substring(ipStr.length()-2,ipStr.length());
			//System.out.println(ipStr);
			SimpleDateFormat mm = new SimpleDateFormat("MM");
			java.util.Date date=new java.util.Date();
			String m=mm.format(date);
			result=((Integer.parseInt(ipStr)%30)+10)+m;
		}catch(Exception e){
			System.out.println( e );
			logger.error("getPartIdKey:"+e);
		}
		return result;
	}
	public static String getPrevPartIdKey(String ipKey){
		String result="";
		try{
			if( ipKey.length()<2 ){
				ipKey = "000"+ipKey; 
			}
			//System.out.println(ipKey);
			String ipStr=ipKey.replace(".","");
			//System.out.println(ipStr);
			ipStr=ipStr.substring(ipStr.length()-2,ipStr.length());
			//System.out.println(ipStr);
			SimpleDateFormat mm = new SimpleDateFormat("MM");
			java.util.Date date=new java.util.Date();
			Calendar cal=Calendar.getInstance();
			cal.add(Calendar.MONTH,-1);
			date.setTime(cal.getTimeInMillis());
			String m=mm.format(date);
			result=((Integer.parseInt(ipStr)%30)+10)+m;
		}catch(Exception e){
			logger.error("getPrevPartIdKey:"+e);
		}
		return result;
	}
	public static String unescape(String inp) { 
	     StringBuffer rtnStr = new StringBuffer();
	     char [] arrInp = inp.toCharArray();
	     int i;
	     for(i=0;i<arrInp.length;i++) {
	         if(arrInp[i] == '%') {
	             String hex; 
	             if(arrInp[i+1] == 'u') {    //유니코드.
	                 hex = inp.substring(i+2, i+6);
	                 i += 5;
	             } else {    //ascii
	                 hex = inp.substring(i+1, i+3);
	                 i += 2;
	             }
	             try{
	                 rtnStr.append(Character.toChars(Integer.parseInt(hex, 16)));
	             } catch(NumberFormatException e) {
	              rtnStr.append("%");
	                 i -= (hex.length()>2 ? 5 : 2);
	             }
	         } else {
	          rtnStr.append(arrInp[i]);
	         }
	     } 
	  
	     return rtnStr.toString();
	 }
	
	public static String getRefMass(String refURL){
		if(refURL==null) refURL = "";
		refURL = refURL.toLowerCase();
		String refMass = "";		
		try{
			refMass = refURL.replace("http://", "");
			refMass = refMass.replace("https://", "");
			if(refMass.indexOf("/") > -1) {
				refMass = refMass.substring(0, refMass.indexOf("/"));
			} else if(refURL.indexOf("?") > -1) {
				refMass = refMass.substring(0, refMass.indexOf("?"));
			} else if(refMass.equals("")) {
				refMass = "bookmark";
			}
		}catch(Exception e){			
			logger.error("getRefMass:"+e);
			return refMass;
		}
		return refMass;
	}
	public static String getDestDomain(String url){
		String destDomain=url;
		url=url.toLowerCase();		
		try{
			if(url !=null && url.trim().length() !=0){
				if(url.indexOf("http://") != -1 ){
					destDomain= url.substring(7,url.length());
				}else if(url.indexOf("https://") != -1 ){
					destDomain= url.substring(8,url.length());
				}
				if(url.indexOf("www.") != -1 ){
					destDomain= destDomain.substring(4,destDomain.length());
				}
				if(destDomain.indexOf("/")>-1){
					destDomain=destDomain.substring(0,destDomain.indexOf("/"));
				}
				if(destDomain.indexOf("?")>-1){
					destDomain=destDomain.substring(0,destDomain.indexOf("?"));
				}
				
			} 
		}catch(Exception e){
			logger.error("getDestDomain:"+e);
			return destDomain;
		}
		return destDomain;
	}
	
	public static String getDestDomainExwww(String url){
		String destDomain=url;
		url=url.toLowerCase();		
		try{
			if(url !=null && url.trim().length() !=0){
				if(url.indexOf("http://") != -1 ){
					destDomain= url.substring(7,url.length());
				}else if(url.indexOf("https://") != -1 ){
					destDomain= url.substring(8,url.length());
				}				
				if(destDomain.indexOf("/")>-1){
					destDomain=destDomain.substring(0,destDomain.indexOf("/"));
				}
				if(destDomain.indexOf("?")>-1){
					destDomain=destDomain.substring(0,destDomain.indexOf("?"));
				}
				
			} 
		}catch(Exception e){
			logger.error("getDestDomainExwww:"+e);
			return destDomain;
		}
		return destDomain;
	}
	
	public static String getParamName(Map<String, String> list, String refMass) {
		String tempParamName = "";
		for(int i = 0; i < list.size(); i++) {
			Iterator<String> it = list.keySet().iterator();
			while(it.hasNext()) {
				String temp = (String)it.next();
				if(refMass.equals(temp)){
					tempParamName = list.get(temp);
					break;
				}
			}
		}
		return tempParamName;
	}
	
	public static String getParamValue(String url, String chooseName) {
		//String temp = "http://search.naver.com/search.nhn?one=1&two=2&three=3";
		String v = "noKeyword";
		if(!chooseName.equals("")) {			
			StringTokenizer st = new StringTokenizer(url.substring(url.indexOf("?")+1),"&");
			while(st.hasMoreTokens()) {
				String [] pair = st.nextToken().split("=");				
				if(pair[0].equals(chooseName)) {					
					v = pair[1];
				}
			}
		}		
		return v;
	}
	
	/**
	 * null 체크
	 * @param str
	 * @return
	 */
	public static boolean isNotEmpty(String str){
		return org.apache.commons.lang3.StringUtils.isNotEmpty(str);
	}
	
	/**
	 * 숫자인지 체크
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str){
		return org.apache.commons.lang3.StringUtils.isNumeric(str);
	}
	
}