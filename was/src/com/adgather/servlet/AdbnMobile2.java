package com.adgather.servlet;


import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.context.ApplicationContext;

import com.adgather.beans.DataMapper;
import com.adgather.beans.ManagementCookie;
import com.adgather.reportmodel.AdConfigData;
import com.adgather.reportmodel.DumpObject;
import com.adgather.reportmodel.MediaScriptData;
import com.adgather.reportmodel.RFData;
import com.adgather.reportmodel.ShopData;
import com.adgather.util.StringUtils;

public class AdbnMobile2 extends HttpServlet {
	
	private DumpObject dumpObj;
	private ApplicationContext appContext;
	public ManagementCookie manageCookie;
	private static JSONArray resultJson;
	private static String mc = "";
	private static String adgubun = "";
	private static String uid = "";
	private static int max = 1;
	private static int dataLength = 0;
	private static String kwd = "";
	
	static Logger logger = Logger.getLogger(AdbnMobile2.class);
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		try {
			mc = "";
			adgubun = "";
			uid = "";
			max = 1;
			dataLength = 0;
			kwd = "";
			resultJson = new JSONArray();
			request.setCharacterEncoding("utf-8");
			response.setContentType("application/json; charset=utf-8");
			response.setHeader("Pragma", "no-cache");			
			response.setHeader("Cache-Control", "no-cache"); 
			//response.setHeader("P3P","CP='IDC DSP COR ADM DEVi TAIi PSA PSD IVAi IVDi CONi HIS OUR IND CNT'");
			response.setHeader("P3P","CP='CAO PSA CONi OTR OUR DEM ONL'");
			PrintWriter out=response.getWriter();
			//us는 그냥 시퀀스인가??? 
			//상품타게팅에서는 media_no
			String us=request.getParameter("us")==null ? "" : request.getParameter("us");
			mc = request.getParameter("mc")==null ? "" : request.getParameter("mc");//s = mc
			//광고주아이디
			uid=request.getParameter("uid")==null ? "" : request.getParameter("uid");
			//상품코드
			String pcode=request.getParameter("pcode")==null ? "" : request.getParameter("pcode");
			//키워드
			kwd=request.getParameter("kwd")==null ? "" : request.getParameter("kwd");
			//방문도메인
			String url=request.getParameter("url")==null ? "" : request.getParameter("url");
			//광고갯수
			String adCnt=request.getParameter("adCnt")==null ? "" : request.getParameter("adCnt");
			
			String test=request.getParameter("test")==null ? "" : request.getParameter("test");
			if(StringUtils.isNotEmpty(test)){
				String testString = "";
				int testlength = 0;
				if(pcode!=null&&pcode!=""){
					testlength = 2;
					testString = "SR";
				}else{
					testlength = 1;
					if(kwd!=null&&kwd!=""){
						testString = "KL";
					}else if(url!=null&&url!=""){
						testString = "UM";
					}else{
						testString = "AD";
					}
				}
				
				JSONObject test1 = new JSONObject();
				JSONObject obj = new JSONObject();
				JSONObject data = new JSONObject();
				JSONArray arr = new JSONArray();
				obj.put("gb", testString);
				for(int i = 0 ; i < testlength ; i++){
					data = new JSONObject();
					data.put("sc", "005fdf40ff814d644b5cb02e8bb2af55");
					data.put("snm", "위반코");
					data.put("price", "50000");
					data.put("logo", "http://www.dreamsearch.or.kr/ad/imgfile/cocolota-1164622052000.png");
					data.put("bnpath1", "http://www.dreamsearch.or.kr/ad/imgfile/00010370afad75d3369ac42a4ef4f5e5.jpg");
					data.put("bnpath2", "");
					data.put("link", "http://www.dreamsearch.or.kr/servlet/drc?no=1463643453&kno=&s=1483&gb=SR&sc=e2e66718331e022e61d015812409d7f2&mc=1483&mcgb=&u=styleberry&product=video&clk_param=&pCode=031005000051&slink=http%3A%2F%2Fwww.styleberry.co.kr%2Findex.html%3Fbrandcode%3D031005000051%26ACE_REF%3Denuri_pi%26ref%3Dwithp");
					arr.add(data);
				}
				obj.put("data", arr);
				test1.put("mobadbn", obj);
				PrintWriter printout = response.getWriter();
				printout.print(test1.toJSONString());
				/*test*/
				return;
			}
			
			/*
			 * 상품타게팅 데이터 갯수
			 * PCODE가 있을 경우 2개 리턴
			 * 없을경우 1개 리턴
			*/
			if(StringUtils.isNotEmpty(adCnt)){
				if(StringUtils.isNumeric(adCnt)){
					max = Integer.parseInt(adCnt);
				}
			}else{
				if(StringUtils.isNotEmpty(pcode)){
					max = 2;
				}else{
					max = 1;
				}
			}
			
			//타게팅 목록
			ArrayList<AdConfigData> adList = null;
			//광고주 아이디,상품코드 가 있을 경우에만 데이터 조회
			if(StringUtils.isNotEmpty(uid)&&StringUtils.isNotEmpty(pcode)&&chechLength()){
				adList = new ArrayList<AdConfigData>();
				adgubun = "SR";
				adList = RFServlet.instance.dataMapper.getApiList(uid,pcode,"SR",mc,1);
				parsingJson(adList,"SR");
				//추천상품
				if(chechLength()){
					logger.debug("::::::::추천상품조회::::::::::");
					LinkedHashMap ldList= RFServlet.instance.adInfoCache.getShopStatsData(uid,"");
					if(ldList!=null && ldList.size()>0){
						List<String> sdlist = new ArrayList<String>(ldList.keySet());
						ArrayList<Integer> rndList = StringUtils.getRandList( sdlist.size() );
						logger.debug("adbnMobile[306] sdlist.size"+ sdlist.size());
						ArrayList<AdConfigData> addsList = new ArrayList<AdConfigData>();
						for( int i=0; i<rndList.size() && i<2; i++){
							AdConfigData adds = new AdConfigData();
							ShopData ss =RFServlet.instance.adInfoCache.getShopPCodeData(uid,sdlist.get(rndList.get(i)).toString());
							if(ss!=null){
								adds.setUserid(uid);
								adds.setSite_code("");
								adds.setSite_desc("");
								adds.setPrice(ss.getPRICE());
								adds.setBanner_path1(ss.getIMGPATH());
								adds.setBanner_path2("");
								adds.setPurl(ss.getPURL());
								addsList.add(adds);
							}
						}
						parsingJson(addsList,"RP");
					}
				}
			}
			//키워드 가 있을 경우에만 데이터 조회
			if(StringUtils.isNotEmpty(kwd)&&chechLength()){
				adList = new ArrayList<AdConfigData>();
				if(!StringUtils.isNotEmpty(adgubun)){adgubun = "KL";}
				adList = RFServlet.instance.dataMapper.getApiList(kwd,"","KL",mc,1);
				parsingJson(adList,"KL");
			}
			//url 이 있을 경우에만 데이터 조회
			if(StringUtils.isNotEmpty(url)&&chechLength()){
				adList = new ArrayList<AdConfigData>();
				if(!StringUtils.isNotEmpty(adgubun)){adgubun = "UM";}
				adList = RFServlet.instance.dataMapper.getApiList(url,"","UM",mc,1);
				parsingJson(adList,"UM");
			}
			/*
			 * 베이스조회
			 * 베이스가 1개만 나올경우가 있나??
			 * 혹시나!!
			 */
			int checkCnt = 0;
			logger.debug("::::::::chechLength()::::::::::"+chechLength());
			logger.debug("::::::::chechLength()::::::::::"+max+":::::::"+dataLength);
			while (chechLength()) {
				adList = new ArrayList<AdConfigData>();
				if(!StringUtils.isNotEmpty(adgubun)){adgubun = "AD";}
				adList = RFServlet.instance.dataMapper.getApiList("","","AD",mc,max-dataLength);
				parsingJson(adList,"AD");
				checkCnt++;
				if(checkCnt>max){
					break;
				}
			}
			System.out.println(":::::::end:");
			response.setContentType("application/json; charset=euc-kr");
			JSONObject root = new JSONObject();
			JSONObject parent = new JSONObject();
			parent.put("gb", adgubun);
			parent.put("data", resultJson);
			root.put("mobadbn", parent);
			out.print(root.toJSONString());
			
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	//json파싱
	public void parsingJson(ArrayList<AdConfigData> list,String div) throws UnsupportedEncodingException{
		try {
			if(list!=null&&list.size()>0){
				//상품,추천상품
				if(StringUtils.isNotEmpty(div)&&(div.equals("RP")||div.equals("SR"))){
					String imgstr ="";
					String imgdomain = "www.dreamsearch.or.kr";
					String slink  = "";
					String site_url = "";
					String site_etc = "";
					String purl = "";
					JSONObject data = new JSONObject();
					JSONArray arr = new JSONArray();
					for(AdConfigData result : list){
						purl = result.getPurl();
						//클릭 링크생성
						slink = "http://www.dreamsearch.or.kr/servlet/drc?no=0&kno=0&s="+mc+"&gb="+adgubun+"&sc="+result.getSite_code()+"&mc="+mc
								+"&mcgb="+result.getMcgb()+"&u="+result.getUserid()+"&product=mbw&";
						imgstr = "http://"+imgdomain+"/ad/imgfile/"+result.getBanner_path1();
						if( result.getSite_etc()!=null && result.getSite_etc().length()>0 ){
							purl += site_etc.substring(0, 1).equals("&")? site_etc: "&"+ site_etc ;
						}
						slink+="&pCode="+result.getPcode()+"&slink="+URLEncoder.encode(purl,"UTF-8");
						data = new JSONObject();
						data.put("sc", "");
						data.put("snm", result.getPnm());
						data.put("sdsc", "");
						data.put("price", result.getPrice());
						data.put("logo", result.getImgname_logo());
						data.put("bnpath1", result.getBanner_path1());
						data.put("bnpath2", "");
						data.put("link", slink);
						resultJson.add(data);
						dataLength++;
					}
				}else{
					//이미지경로
					String logo ="";
					String bnpath1 ="";
					String bnpath2 ="";
					String imgdomain = "www.dreamsearch.or.kr";
					String slink  = "";
					String site_url = "";
					String site_etc = "";
					String rurl = "";
					String tempUrl = "";
					JSONObject data = new JSONObject();
					JSONArray arr = new JSONArray();
					for(AdConfigData result : list){
						//클릭 링크생성
						slink = "http://www.dreamsearch.or.kr/servlet/drc?no=0&kno=0&s="+mc+"&gb="+adgubun+"&sc="+result.getSite_code()+"&mc="+mc
								+"&mcgb="+result.getMcgb()+"&u="+result.getUserid()+"&product=mbw&";
						if(adgubun.equals("KL")){
							logo = "http://"+imgdomain+"/ad/imgfile/"+result.getImgname_logo();
							bnpath1 = "http://"+imgdomain+"/ad/imgfile/"+result.getBanner_path1();
							bnpath2 = "http://"+imgdomain+"/ad/imgfile/"+result.getBanner_path2();
							slink+="&slink="+URLEncoder.encode(rurl,"UTF-8");
						}
						if(adgubun.equals("AD")){
							if(StringUtils.isNotEmpty(result.getSite_urlm())){
								tempUrl = result.getSite_urlm();
							}else{
								tempUrl = result.getSite_url();
							}
							site_url = (tempUrl.substring(0,4).equals("http")?tempUrl:"http://"+tempUrl);
							rurl=URLEncoder.encode(site_url,"UTF-8");
							bnpath1 = "http://"+imgdomain+"/ad/efile/"+result.getAdtxt();
							slink +="&slink="+rurl;
						}
						if(adgubun.equals("UM")){
							if(StringUtils.isNotEmpty(result.getSite_urlm())){
								tempUrl = result.getSite_urlm();
							}else{
								tempUrl = result.getSite_url();
							}
							site_url = (tempUrl.substring(0,4).equals("http")?tempUrl:"http://"+tempUrl);
							rurl	=URLEncoder.encode(site_url,"UTF-8");
							bnpath1 = "http://"+imgdomain+"/ad/imgfile/"+result.getBanner_path1();
							slink+="&slink="+rurl;
						}
						
						data = new JSONObject();
						data.put("sc", result.getSite_code());
						data.put("snm", result.getSite_name());
						data.put("sdsc", result.getSite_desc());
						data.put("price", result.getPrice());
						data.put("logo", logo);
						data.put("bnpath1", bnpath1);
						data.put("bnpath2", bnpath2);
						data.put("link", slink);
						resultJson.add(data);
						dataLength++;
					}
				}
			}
		} catch (Exception e) {
			System.out.println(":::::::::::test:"+e.getMessage());
		}
	}
	//데이터 갯수 체크
	public boolean chechLength(){
		return max == dataLength ? false : true;
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response){
		doGet(request,response);
	}
	public void init() throws ServletException {
		logger.info("AdbnServlet started");
		if (appContext == null) {
			RFServlet.instance.init();
			appContext=RFServlet.appContext;
		}
		manageCookie=(ManagementCookie)appContext.getBean("ManagementCookie");
		dumpObj=(DumpObject)appContext.getBean("DumpObject");
	}
	protected String[] getFileList() { 
        return new String[]{getServletConfig().getInitParameter("pathToContext")};
    }
}