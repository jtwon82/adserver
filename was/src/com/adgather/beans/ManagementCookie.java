package com.adgather.beans;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.adgather.reportmodel.PointData;
import com.adgather.reportmodel.ShopData;
import com.adgather.servlet.RFServlet;
import com.adgather.util.DateUtils;
import com.adgather.util.StringUtils;

public class ManagementCookie {
	private static Logger logger = Logger.getLogger(ManagementCookie.class);
	public void init(){
	}
	public static void main(String[] args){
		ManagementCookie m=new ManagementCookie();
		//String prevLog="{\"data\":[{\"sc\":\"ebac42aaf0f7e5d161c67b90f6c2a33e\",\"pcd\":\"1403761096\",\"tg\":\"\",\"gb\":\"02\",\"mcgb\":\"B\",\"purl\":\"http://mooltong.co.kr/shop/item.php?it_id=1403761096\"},{\"sc\":\"ebac42aaf0f7e5d161c67b90f6c2a33e\",\"pcd\":\"1378344203\",\"tg\":\"\",\"gb\":\"02\",\"mcgb\":\"B\",\"purl\":\"http://mooltong.co.kr/shop/item.php?it_id=1378344203\"},{\"sc\":\"20b63e719728ed8a96c4100bf2da46a6\",\"pcd\":\"1403761096\",\"tb\":\"B\",\"gb\":\"02\",\"mcgb\":\"\",\"purl\":\"http://mooltong.co.kr/shop/item.php?it_id=1403761096\"}]}";
		String prevLog="%7B%22data%22%3A%5B%7B%22sc%22%3A%22e85a62328955149572c0328b5b63751c%22%2C%22pcd%22%3A%22006003000475%22%2C%22tg%22%3A%22A%22%2C%22gb%22%3A%2202%22%2C%22mcgb%22%3A%22A%22%2C%22purl%22%3A%22http%3A%2F%2Fwww.nurimom.com%2Findex.html%3Fbrandcode%3D006003000475%22%7D%2C%7B%22sc%22%3A%22e85a62328955149572c0328b5b63751c%22%2C%22pcd%22%3A%22010004000633%22%2C%22tg%22%3A%22A%22%2C%22gb%22%3A%2202%22%2C%22mcgb%22%3A%22A%22%2C%22purl%22%3A%22http%3A%2F%2Fwww.nurimom.com%2Findex.html%3Fbrandcode%3D010004000633%22%7D%2C%7B%22sc%22%3A%22f3f1a389eb62e3bc8190a1abbd16b16b%22%2C%22pcd%22%3A%22006002000538%22%2C%22tg%22%3A%22AC%22%2C%22gb%22%3A%2202%22%2C%22mcgb%22%3A%22%22%2C%22purl%22%3A%22http%3A%2F%2Fnurimom.com%2Fshop%2Fshopdetail.html%3Fbranduid%3D1017253%26xcode%3D006%26mcode%3D002%26scode%3D%26special%3D3%26GfDT%3DbGt3UA%3D%3D%22%7D%5D%7D";
		
		prevLog=StringUtils.getURLDecodeStr(prevLog, "UTF-8");
		
		ArrayList list = new ArrayList();
		ShopData crd=new ShopData();
		crd.setSITE_CODE("ebac42aaf0f7e5d161c67b90f6c2a33e");
		crd.setPCODE("1403761096");
		crd.setSvc_type("");
		
		System.out.println(m.shopLogManage(1,prevLog, crd,"edit"));
	}
	public String shopLogReSet(String userId,ShopData slog,HttpServletRequest request, HttpServletResponse response){
		String result="";
		try{
			PointData pd=RFServlet.instance.adInfoCache.getAdCacheData(userId);
			int cookieDay=31;
			if(pd!=null) cookieDay=pd.getCookieDay();
			String tmp=java.net.URLDecoder.decode(getCookieInfo(request,"shop_log"),"utf-8");
			String newVal=shopLogManage(1, tmp,slog,"edit");
			newVal=java.net.URLEncoder.encode(newVal,"utf-8");
			makeCookie("shop_log",newVal,cookieDay*24*60*60, response);
		}catch(Exception e){
			logger.error("shopLogReSet:"+e);
		}
		return result;
	}
	public String shopLogManage(String prevLog, ArrayList<ShopData> list,String mode){
		String result= prevLog;
		for(int i=0; i<list.size(); i++){
			result = shopLogManage(1, result, list.get(i), mode);
		}
		return result;
	}
	public String shopLogManage(int cnt, String prevLog,ShopData crd,String mode){
		String rtn="";
		try{
			if( prevLog.indexOf("\"data\"")>-1 || prevLog.equals("") ){
			}else{
				return prevLog; // = getShopLogToJson(crd);
			}
			if( cnt>3 ) return "";
			Map room=new HashMap();
			JsonBuilderFactory factory = Json.createBuilderFactory(room); 
			JsonObjectBuilder ob1 = factory.createObjectBuilder();
			JsonObjectBuilder ob2 = factory.createObjectBuilder();
			JsonArrayBuilder dataList = factory.createArrayBuilder();
			logger.debug("mode="+mode);
			logger.debug("shopLogManage prevLog:"+prevLog);
			//logger.debug("shopLogManage data:"+ReflectionToStringBuilder.toString(crd));
			if(mode.equals("append")){
				ob2.add("sc",crd.getSITE_CODE());
				ob2.add("pcd",crd.getPCODE());
				ob2.add("tg",crd.getTARGETGUBUN());
				ob2.add("gb",crd.getGB());
				ob2.add("mcgb",crd.getMCGB());
				ob2.add("purl",crd.getPURL());
				if(prevLog==null || prevLog.length()==0){
					dataList.add(ob2);
				}else{
					try{
						InputStream is = new ByteArrayInputStream(prevLog.getBytes());
						JsonReader rdr = Json.createReader(is);
						JsonArray obj=rdr.readObject().getJsonArray("data");
						dataList.add(ob2);
						int limit=0;
						for (JsonObject result : obj.getValuesAs(JsonObject.class)) {
							dataList.add(result);
							if(limit>5) break;
							limit++;
						}
					}catch(Exception e){}
				}
			}else if(mode.equals("edit")){
				try{
					InputStream is = new ByteArrayInputStream(prevLog.getBytes());
					JsonReader rdr = Json.createReader(is);
					JsonArray obj=rdr.readObject().getJsonArray("data");
					for (JsonObject row : obj.getValuesAs(JsonObject.class)) {
						String rowkey=row.getString("sc","")+row.getString("pcd","");
						String ikey=crd.getSITE_CODE()+crd.getPCODE();
						
						if(rowkey.length()>0){
							logger.debug(" key="+ rowkey +" crd="+ ikey);
							if( rowkey.equals( ikey )){
								
								String tg= row.getString("tg","");
								if(crd.getSvc_type().equals("sky")){
									if(tg.indexOf("C")<0)tg+="C";
								}else if(crd.getSvc_type().equals("normal")){
									if(tg.indexOf("A")<0)tg+="A";
								}else if(crd.getSvc_type().equals("")){
									if(tg.indexOf("B")<0)tg+="B";
								}
								
								ob2.add("sc",row.getString("sc",""));
								ob2.add("pcd",row.getString("pcd",""));
								ob2.add("tg", tg );
								ob2.add("gb",row.getString("gb",""));
								ob2.add("mcgb",row.getString("mcgb",""));
								ob2.add("purl",row.getString("purl",""));
								dataList.add(ob2);
								
								logger.debug("edit succ key="+rowkey);
								
							}else{
								dataList.add(row);
							}
						}else{
							dataList.add(row);
						}
					}
				}catch(Exception e){}
			}
			ob1.add("data",dataList);
			rtn= ob1.build().toString();
			if(rtn==null || rtn.equals("")){
				rtn="";
			}
			logger.debug("rtn"+rtn);
		}catch(Exception e){
			//System.out.println(e);
			logger.error("shopLogManage:"+e+" prevLog="+prevLog);
			rtn=shopLogManage(++cnt, "",crd,mode);
		}
		return rtn; 
	}
	public String getShopLogToJson(ShopData crd){
		String prevLog="";
		try{
			HashMap<String,ShopData> shhm=null;
			
			if( RFServlet.instance.adInfoCache.shopLogCache.isKeyInCache(crd.getIP()) ){
				shhm=(HashMap) RFServlet.instance.adInfoCache.shopLogCache.get(crd.getIP()).getObjectValue();
				logger.debug("getShopLogToJson : "+crd.getIP() + ","+shhm.size()+" Cache hit.");
			}else{
				shhm=RFServlet.instance.dataMapper.getShopLogDataFromIp(crd.getIP(),"");
				logger.debug("getShopLogToJson : "+crd.getIP() + ","+shhm.size()+" db hit.");
			}
			
			if( shhm!=null ){
				List<String> it = new ArrayList<String>(shhm.keySet());
			    Collections.sort(it);
			    
			    for(String me:it){
			    	ShopData crd1= shhm.get(me);
	
					Map room=new HashMap();
					JsonBuilderFactory factory = Json.createBuilderFactory(room); 
					JsonObjectBuilder ob1 = factory.createObjectBuilder();
					JsonObjectBuilder ob2 = factory.createObjectBuilder();
					JsonArrayBuilder dataList = factory.createArrayBuilder();
					
			    	ob2.add("sc",crd1.getSITE_CODE());
					ob2.add("pcd",crd1.getPCODE());
					ob2.add("tg",crd1.getTARGETGUBUN());
					ob2.add("gb",crd1.getGB());
					ob2.add("mcgb",crd1.getMCGB());
					ob2.add("purl",crd1.getPURL());
					if(prevLog==null || prevLog.length()==0){
						dataList.add(ob2);
					}else{
						try{
							InputStream is = new ByteArrayInputStream(prevLog.getBytes());
							JsonReader rdr = Json.createReader(is);
							JsonArray obj=rdr.readObject().getJsonArray("data");
							dataList.add(ob2);
							int limit=0;
							for (JsonObject result : obj.getValuesAs(JsonObject.class)) {
								dataList.add(result);
								if(limit>5) break;
								limit++;
							}
						}catch(Exception e){}
					}
					ob1.add("data",dataList);
					prevLog= ob1.build().toString();
					logger.debug("getShopLogToJson prevLog="+prevLog);
			    }
			}
		}catch(Exception e){
			logger.error("getShopLogToJson[223] err "+e);
		}
		return prevLog;
	}
	public String procDuring(String mode, String log, ShopData sd){
		String result="";
		try{
			if(mode.equals("append")){	//append, edit, delete
				
			}else if (mode.equals("edit")){
				
			}
		}catch(Exception e){
			logger.error("procDur err "+e);
		}
		return result;
	}
	public long chkDuringTarget(HttpServletRequest request, String ic_dur){
		
		//String ic_dur= getCookieInfo(request,"ic_dur");
		String n= DateUtils.getDate("yyyy-MM-dd HH:mm:ss");
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		Date d1 = null;
		Date d2 = null;
		try{
			d1 = format.parse(ic_dur);
			d2 = format.parse(n);
		}catch(Exception e){}
		
		long diff = d2.getTime() - d1.getTime();
		long diff_secound= diff / 1000;
	    long diff_minute= diff / (60 * 1000) % 60;
	    //boolean is_diff5= diff_secound>=diff_no ? true : false ;
	    
	    return diff_secound;
	}
	public String makeKeyCookie(HttpServletRequest request, HttpServletResponse response){
		Cookie [] cookies = request.getCookies();
		String cookieName="IP_info";
		String ipInfo="";
		try{
			if(cookies!=null){
				for(int i=0; i < cookies.length; i++){		
					if( cookies[i].getName().equals(cookieName) ){
						ipInfo = cookies[i].getValue();
						break;
					}
				}
			}
			if(ipInfo==null || ipInfo.equals("")){
				String ip=request.getRemoteAddr();
				Random r=new Random();
				int rdNum=r.nextInt(100000000)+1;
				ipInfo=ip+"."+rdNum;
				if(ipInfo.length()>20){
					ipInfo=(ip+"."+rdNum).substring(0,20);
				}
				makeCookie(cookieName,ipInfo,60*24*60*60,response);
				//makeCookie("IPNewBN","new",-1,response);
				//makeCookie("IPNew","new",-1,response);
				//makeCookie("IPNewSky","new",-1,response);
			}else{
				makeCookie(cookieName,ipInfo,60*24*60*60,response);
			}
			
		}catch(Exception e){
			logger.error("makeKeyCookie:"+e);
		}
		return ipInfo; 
	}
	public void makeCookie(String cName,String cValue,int age,HttpServletResponse response){
		Cookie cookie=null;
		try{
			cookie= new Cookie(cName,cValue);
			cookie.setDomain(".dreamsearch.or.kr");
			cookie.setPath("/");
			cookie.setMaxAge(age);
			response.addCookie(cookie);
		}catch(Exception e){
			if( cName.equals("shop_log") ){
				cookie= new Cookie(cName,"shop");
				cookie.setDomain(".dreamsearch.or.kr");
				cookie.setPath("/");
				cookie.setMaxAge(age);
				response.addCookie(cookie);
			}else{
				logger.error("makeCookie:cName="+cName+",cValue="+cValue+","+e);
			}
		} 
	}
	public void nextStep(HttpServletResponse response, String step_data, String step_name, String step, String set){
		if( step.equals("") ){
			step= StringUtils.gAt1(step_data, 0, ",");
		} else {
			int i= StringUtils.gAtId(step_data, step, ",");
			if( i<0 ){
				step= StringUtils.gAt1(step_data, 0, ",");
			}else{
				step= StringUtils.gAt1(step_data, ++i, ",");
				if( step.equals("") ){
					step= StringUtils.gAt1(step_data, 0, ",");
				}
			}
		}
		if( !set.equals("") ){
			step= set;
		}
		logger.debug("nextStep step_name-"+ step_name +" step-"+ step);
		makeCookie(step_name, step, 60*60*24*31, response);
	}
	public void clearChkCk(HttpServletResponse response, String chk_result, String chk_ck, String ic_um, String product,String adGubun){
		try{
			/*
			HashMap<String,ArrayList> um_list=null;
			
			if( product.equals("banner") ){
				um_list=RFServlet.instance.adInfoCache.getNormalUrlMatchAd(ic_um, "|||");
			}else{
				um_list=RFServlet.instance.adInfoCache.getUrlMatchAd(ic_um, "|||");
			}
			
			if(um_list!=null){
				HashMap<String,AdConfigData> eraser= new HashMap();
				
				for( String a: um_list.keySet() ){
					ArrayList list = um_list.get(a);
					for(int i=0; i<list.size(); i++){
						String key = ic_um;
						logger.debug(key);
						eraser.put(key, (AdConfigData)list.get(i));
					}
				}
				logger.debug("iadbn[355] eraser "+ eraser);
			*/
			String chk_cktmp= chk_ck.toString();
			HashMap<String,String> chkresult_hashmap = StringUtils.getConvertHashMap(chk_result, "#");
			HashMap<String,String> chkck_hashmap = StringUtils.getConvertHashMap(chk_ck.toString(), "#");
			for( String item : chkresult_hashmap.keySet() ){
				//if( eraser.containsKey(item) ){
				//if( product.equals("banner") ){
				//	chk_result = chk_result.replace(item, "");
				//}else if( eraser.get(item).getSvc_type().equals(product) ){
				if(item.indexOf(adGubun)==0){
					chk_result = chk_result.replace(item, "");
				}
				//}
				//}
			}
			for( String item : chkck_hashmap.keySet() ){
				logger.debug("test "+ item);
				//String item1 = StringUtils.gAt1(item, 0, "=");
				//if( eraser.containsKey( item1 ) ){
				//if( product.equals("banner")){
				//	chk_cktmp = chk_cktmp.replace(item, "");
				//}else if( eraser.get(item1).getSvc_type().equals(product) ){
				if(item.indexOf(adGubun)==0){
					chk_cktmp = chk_cktmp.replace(item, "");
				}
				//}
				//}
			}
			
			chk_result = chk_result.replace("##", "#");
			chk_cktmp= chk_cktmp.replace("##", "#");
			makeCookie("chk_result", chk_result, 3600, response);
			makeCookie("chk_ck", chk_cktmp, 3600, response);
			logger.debug("clearChkCk:chk_result="+chk_result+",chk_ck="+chk_cktmp);
			//}
		}catch(Exception e ){
			logger.debug("clearChkCk()[168] err "+ e);
		}
	}
	public String makeCookieStr(String userId,HttpServletResponse response, HttpServletRequest request,
			String adgubun, String chk_code, String chk_result, StringBuffer chk_ck, int c, String adchk){
		String resultvalue="";
		String direct="";
		try{
			String rebuild="";
			if( chk_ck.indexOf(chk_code)<0 ){
				//chk_ck += chk_code+"=0#";
				chk_ck.append(chk_code+"=0#");
			}
			StringTokenizer st=new StringTokenizer(chk_ck.toString(),"#",false);
			
			while(st.hasMoreElements()){
				String oneSell=st.nextElement().toString();
				StringTokenizer st2=new StringTokenizer(oneSell,"=",false);
				
				if(st2.countTokens()==2){
					String sCode=st2.nextElement().toString();
					int sCnt=1;
					try{
						sCnt=Integer.parseInt(st2.nextElement().toString());
					}catch(Exception e){
						sCnt=1;
					}
					
					if(sCode.equals(chk_code)){
						if(adchk.equals(""))sCnt++;
						if(sCnt>19 && chk_result.indexOf(chk_code)<0){
							chk_result+=chk_code +"#";
							makeCookie("chk_result",chk_result,3600, response);
							direct="next";
						}
						if(sCnt>20){
							sCnt=20;
							direct="next";
						}
						if(sCnt==20){
							if(adgubun.equals("SR")){
								ShopData slog= new ShopData();
								slog.setIP(getCookieInfo(request,"IP_info"));
								slog.setGB(adgubun);
								String chkNew=StringUtils.gAt1(chk_code, 1, "_");
								long logNo=0;
								String siteNo="";
								String pCode="";
								try{
									logNo=Long.parseLong(chkNew);
								}catch(Exception e){
									siteNo=StringUtils.gAt1(chk_code, 0,"^");
									pCode=StringUtils.gAt1(chk_code, 1,"^");
								}
								slog.setNO(logNo);
								slog.setSvc_type("normal");
								slog.setSITE_CODE(siteNo);
								slog.setPCODE(pCode);
								logger.debug("slog.setGb="+adgubun);
								logger.debug("slog.setNO="+ StringUtils.gAt1(chk_code, 1, "_") );
								RFServlet.instance.dumpObj.getDelShopLogData().add(slog);
								shopLogReSet(userId,slog,request, response);
							}
						}
						rebuild += sCode+"="+sCnt+"#";
					}else{
						rebuild += oneSell +"#";
					}

				}
			}
			resultvalue=rebuild;
			chk_ck.setLength(0);
			chk_ck.append(resultvalue);
			
			makeCookie("chk_ck",resultvalue,3600, response);
		}catch(Exception e){
			logger.error("makeCookieStr:"+e);
		}
		
		if( direct.equals("next") ){
			return direct;
		}else{
			return resultvalue;
		}
	}
	public String makeCookieStrNew(HttpServletResponse response, HttpServletRequest request
			, String chk_result, StringBuffer chk_ck, String chk_code, int c, String adchk){
		String resultvalue="";
		String direct="";
		try{
			String rebuild="";
			if( chk_ck.indexOf(chk_code)<0 ){
				//chk_ck += chk_code+"=0#";
				chk_ck.append(chk_code+"=0#");
			}
			StringTokenizer st=new StringTokenizer(chk_ck.toString(),"#",false);
			
			while(st.hasMoreElements()){
				String oneSell=st.nextElement().toString();
				StringTokenizer st2=new StringTokenizer(oneSell,"=",false);
				
				if(st2.countTokens()==2){
					String sCode=st2.nextElement().toString();
					int sCnt=0;
					try{
						sCnt=Integer.parseInt(st2.nextElement().toString());
					}catch(Exception e){
						sCnt=0;
					}
					
					if(sCode.equals(chk_code)){
						if(adchk.equals(""))++sCnt;
						
						if( sCnt>(c-1) && chk_result.indexOf(chk_code)<0 ){
							chk_result += chk_code +"#";
							makeCookie("chk_result",chk_result,3600, response);
							direct="next";
						}
						if( sCnt>c ){
							sCnt=c;
							direct="next";
						}
						rebuild += sCode+"="+sCnt+"#";
					}else{
						rebuild += oneSell +"#";
					}
				}
			}
			resultvalue=rebuild;
			chk_ck.setLength(0);
			chk_ck.append(resultvalue);
			
			makeCookie("chk_ck",resultvalue,3600, response);
		}catch(Exception e){
			logger.error("makeCookieStr:"+e);
		}
		if( direct.equals("next") ){
			return direct;
		}else{
			return resultvalue;
		}
	}
	public String getCookieInfo(HttpServletRequest request,String cookieName){
		Cookie [] cookies = request.getCookies();
		String cookieInfo="";
		try{
			if(cookies!=null){
				for(int i=0; i < cookies.length; i++){		
					if( cookies[i].getName().equals(cookieName) ){
						cookieInfo = cookies[i].getValue();
						break;
					}
				}
			}
		}catch(Exception e){
			logger.error("getCookieInfo:"+e);
		}
		return cookieInfo; 
	}
	private long getTime(){
		Calendar now=Calendar.getInstance();
		return now.getTimeInMillis();		
	}
	public String getRefMass(String refURL){
		String refMass="direct";
		try{
			if( refURL.trim().length() !=0 && refURL.indexOf("http://") != -1 ){
				refMass= refURL.substring(7,refURL.indexOf("/",7));
			}else if( refURL.trim().length() !=0 && refURL.indexOf("https://") != -1 ){
				refMass= refURL.substring(8,refURL.indexOf("/",8));
			}else{
				if(refURL.trim().length() !=0){	
					refMass = refURL.substring(0,refURL.indexOf("/"));		
				}else{
					refMass = "direct";
				}
			}
		}catch(Exception e){
			logger.error("getRefMass:"+refURL+":"+e);
			return refMass;
		}
		return refMass;
	}
	public String getDestDomain(String url){
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
			logger.error(destDomain+":"+e);
			return destDomain;
		}
		return destDomain;
	}
	private String getCooKieTime(Long longDate){
		Calendar now=Calendar.getInstance();
		now.setTimeInMillis(longDate);
		String year=Integer.toString(now.get(Calendar.YEAR)),
		   month=((now.get(Calendar.MONTH)+1)<10)  ? "0"+(now.get(Calendar.MONTH)+1) : ""+(now.get(Calendar.MONTH)+1),
		   date=(now.get(Calendar.DATE)<10) ? "0"+now.get(Calendar.DATE) : ""+now.get(Calendar.DATE),
		   hourd=(now.get(Calendar.HOUR_OF_DAY)<10) ? "0"+now.get(Calendar.HOUR_OF_DAY) : ""+now.get(Calendar.HOUR_OF_DAY),
		   minute=(now.get(Calendar.MINUTE)<10) ? "0"+now.get(Calendar.MINUTE) : ""+now.get(Calendar.MINUTE),
		   second=(now.get(Calendar.SECOND)<10) ? "0"+now.get(Calendar.SECOND) : ""+now.get(Calendar.SECOND),
		   millisecond = "";			
		if( (now.get(Calendar.MILLISECOND )+"").length() == 3){
			millisecond = ""+(now.get(Calendar.MILLISECOND ));
		}else if( (now.get(Calendar.MILLISECOND )+"").length() == 2){
			millisecond = "0"+(now.get(Calendar.MILLISECOND ));
		}else if( (now.get(Calendar.MILLISECOND )+"").length() == 1){
			millisecond = "00"+(now.get(Calendar.MILLISECOND ));			
		}
		return  year+"-"+month+"-"+date+" "+hourd+":"+minute+":"+second+"."+millisecond;
	}
	
	private String getYYMMDDStr(String format, long tdate) {
		long ldate = tdate;
		String strDate = "";
		try {
			java.util.Date date = new java.util.Date(ldate);
			java.text.SimpleDateFormat df = new java.text.SimpleDateFormat(format);
			strDate = df.format(date);
		} catch(Exception e) {
			logger.error("getYYMMDDStr:"+e);
		}
		
		return strDate;
	}
}
