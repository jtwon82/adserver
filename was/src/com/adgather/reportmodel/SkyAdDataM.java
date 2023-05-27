package com.adgather.reportmodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;


public class SkyAdDataM {
	private String tmp_sitecode= "";
	public HashMap<String,SkyAdData> list;
	public SkyAdDataM(){
		if( list==null ){
			list= new HashMap();
		}
	}
	public String getImgurls(){
		StringBuffer rt_goods= new StringBuffer();
		int i=0;

		Iterator it = list.keySet().iterator();

		while(it.hasNext()){
			String pkey= it.next().toString();
			SkyAdData s= list.get(pkey);
			
			if( true ){
				ArrayList<ShopData> s1= s.getData();
				if( s.getTarget().equals("goods") ){
					if( s1.size()>0 ){
						for(int j=0; j<s1.size(); j++)
							rt_goods.append( "imgs.push('"+ s1.get(j).getIMGPATH() +"'); \n" );
					}
				} else if( s.getTarget().equals("keyword") ){
					rt_goods.append( "imgs.push('"+ s.getLogo() +"'); \n" );
				} else if( s.getTarget().equals("url") ){
					rt_goods.append( "imgs.push('"+ s.getLogo() +"'); \n" );
				} else if( s.getTarget().equals("base") ){
					rt_goods.append( "imgs.push('"+ s.getLogo() +"'); \n" );
				}
			}
		}
		return rt_goods.toString();
	}
	public String getJsonData(String adgubun){
		StringBuffer rt= new StringBuffer();
		StringBuffer rt_goods= new StringBuffer();
		StringBuffer rt_keyword= new StringBuffer();
		StringBuffer rt_url= new StringBuffer();
		StringBuffer rt_base= new StringBuffer();
		StringBuffer rt_hbanner= new StringBuffer();
		int i=0;

		Iterator it = list.keySet().iterator();

		while(it.hasNext()){
			String pkey= it.next().toString();
			SkyAdData s= list.get(pkey);
			
			if( s.getTarget().equals("goods") ){
				rt_goods.append(" { \r\n");
				rt_goods.append(" \"site_code\":\""+ s.getSite_code() +"\" \r\n");
				rt_goods.append(", \"target\":\""+ s.getTarget() +"\" \r\n");
				rt_goods.append(", \"adgubun\":\""+ adgubun +"\" \r\n");
				rt_goods.append(", \"no\":\""+ s.getNo() +"\" \r\n");
				rt_goods.append(", \"userid\":\""+ s.getUserid() +"\" \r\n");
				rt_goods.append(", \"pcode\":\""+ s.getPcode() +"\" \r\n");
				rt_goods.append(", \"us\":\""+ s.getUs() +"\" \r\n");
				rt_goods.append(", \"s\":\""+ s.getS() +"\" \r\n");
				rt_goods.append(", \"mcgb\":\""+ s.getMcgb() +"\" \r\n");
				rt_goods.append(", \"logo\":\""+ s.getLogo() +"\" \r\n");
				
				if( s.getTarget().equals("goods") ){
					ArrayList<ShopData> s1= s.getData();
					if( s1.size()>0 ){
						rt_goods.append(", \"data\":[ \r\n");
						for(int j=0; j<s1.size(); j++){
							rt_goods.append( getArShopData(s1.get(j),j) );
							rt_goods.append( (j<s1.size()-1?",":"") +"\r\n");
						}
						rt_goods.append(" ] \r\n");
					}
				}else{
					rt_goods.append(", \"purl\":\""+ s.getPurl() +"\" \r\n");
				}
				rt_goods.append(" }"+ (i<list.size()-1?",":"") +"\r\n");
				i++;
			}else if( s.getTarget().equals("keyword") ){
				rt_keyword.append(" { \r\n");
				rt_keyword.append(" \"site_code\":\""+ s.getSite_code() +"\" \r\n");
				rt_keyword.append(", \"target\":\""+ s.getTarget() +"\" \r\n");
				rt_keyword.append(", \"adgubun\":\""+ adgubun +"\" \r\n");
				rt_keyword.append(", \"pcode\":\""+ s.getPcode() +"\" \r\n");
				rt_keyword.append(", \"userid\":\""+ s.getUserid() +"\" \r\n");
				rt_keyword.append(", \"us\":\""+ s.getUs() +"\" \r\n");
				rt_keyword.append(", \"s\":\""+ s.getS() +"\" \r\n");
				rt_keyword.append(", \"mcgb\":\""+ s.getMcgb() +"\" \r\n");
				rt_keyword.append(", \"kno\":\""+ s.getKno() +"\" \r\n");
				rt_keyword.append(", \"logo\":\""+ s.getLogo() +"\" \r\n");
				rt_keyword.append(", \"id\":\""+ s.getId() +"\" \r\n");
				
				if( s.getTarget().equals("goods") ){
					ArrayList<ShopData> s1= s.getData();
					if( s1.size()>0 ){
						rt_keyword.append(", \"data\":[ \r\n");
						for(int j=0; j<s1.size(); j++){
							rt_keyword.append( getArShopData(s1.get(j),j) );
							rt_keyword.append( (j<s1.size()-1?",":"") +"\r\n");
						}
						rt_keyword.append(" ] \r\n");
					}
				}else{
					rt_keyword.append(", \"purl\":\""+ s.getPurl() +"\" \r\n");
				}
				rt_keyword.append(" }"+ (i<list.size()-1?",":"") +"\r\n");
				i++;

			}else if( s.getTarget().equals("ST") ){
				rt_url.append(" { \r\n");
				rt_url.append(" \"site_code\":\""+ s.getSite_code() +"\" \r\n");
				rt_url.append(", \"target\":\""+ s.getTarget() +"\" \r\n");
				rt_url.append(", \"adgubun\":\""+ adgubun +"\" \r\n");
				rt_url.append(", \"pcode\":\""+ s.getPcode() +"\" \r\n");
				rt_url.append(", \"userid\":\""+ s.getUserid() +"\" \r\n");
				rt_url.append(", \"us\":\""+ s.getUs() +"\" \r\n");
				rt_url.append(", \"s\":\""+ s.getS() +"\" \r\n");
				rt_url.append(", \"mcgb\":\""+ s.getMcgb() +"\" \r\n");
				rt_url.append(", \"kno\":\""+ s.getKno() +"\" \r\n");
				rt_url.append(", \"logo\":\""+ s.getLogo() +"\" \r\n");
				rt_url.append(", \"id\":\""+ s.getId() +"\" \r\n");
				
				if( s.getTarget().equals("ST") ){
					ArrayList<ShopData> s1= s.getData();
					if( s1.size()>0 ){
						rt_url.append(", \"data\":[ \r\n");
						for(int j=0; j<s1.size(); j++){
							rt_url.append( getArShopData(s1.get(j),j) );
							rt_url.append( (j<s1.size()-1?",":"") +"\r\n");
						}
						rt_url.append(" ] \r\n");
					}
				}else{
					rt_url.append(", \"purl\":\""+ s.getPurl() +"\" \r\n");
				}
				rt_url.append(" }"+ (i<list.size()-1?",":"") +"\r\n");
				i++;
				
			}else if( s.getTarget().equals("url") ){
				rt_url.append(" { \r\n");
				rt_url.append(" \"site_code\":\""+ s.getSite_code() +"\" \r\n");
				rt_url.append(", \"target\":\""+ s.getTarget() +"\" \r\n");
				rt_url.append(", \"adgubun\":\""+ adgubun +"\" \r\n");
				rt_url.append(", \"userid\":\""+ s.getUserid() +"\" \r\n");
				rt_url.append(", \"us\":\""+ s.getUs() +"\" \r\n");
				rt_url.append(", \"s\":\""+ s.getS() +"\" \r\n");
				rt_url.append(", \"mcgb\":\""+ s.getMcgb() +"\" \r\n");
				rt_url.append(", \"kno\":\""+ s.getKno() +"\" \r\n");
				rt_url.append(", \"logo\":\""+ s.getLogo() +"\" \r\n");
				rt_url.append(", \"id\":\""+ s.getId() +"\" \r\n");
				
				if( s.getTarget().equals("goods") ){
					ArrayList<ShopData> s1= s.getData();
					if( s1.size()>0 ){
						rt_url.append(", \"data\":[ \r\n");
						for(int j=0; j<s1.size(); j++){
							rt_url.append( getArShopData(s1.get(j),j) );
							rt_url.append( (j<s1.size()-1?",":"") +"\r\n");
						}
						rt_url.append(" ] \r\n");
					}
				}else{
					rt_url.append(", \"purl\":\""+ s.getPurl() +"\" \r\n");
				}
				rt_url.append(" }"+ (i<list.size()-1?",":"") +"\r\n");
				i++;
			}else if( s.getTarget().equals("base") ){
				rt_base.append(" { \r\n");
				rt_base.append(" \"site_code\":\""+ s.getSite_code() +"\" \r\n");
				rt_base.append(", \"target\":\""+ s.getTarget() +"\" \r\n");
				rt_base.append(", \"adgubun\":\""+ adgubun +"\" \r\n");
				rt_base.append(", \"userid\":\""+ s.getUserid() +"\" \r\n");
				rt_base.append(", \"us\":\""+ s.getUs() +"\" \r\n");
				rt_base.append(", \"s\":\""+ s.getS() +"\" \r\n");
				rt_base.append(", \"kno\":\""+ s.getKno() +"\" \r\n");
				rt_base.append(", \"logo\":\""+ s.getLogo() +"\" \r\n");
				rt_base.append(", \"id\":\""+ s.getId() +"\" \r\n");
				
				if( s.getTarget().equals("goods") ){
					ArrayList<ShopData> s1= s.getData();
					if( s1.size()>0 ){
						rt_base.append(", \"data\":[ \r\n");
						for(int j=0; j<s1.size(); j++){
							rt_base.append( getArShopData(s1.get(j),j) );
							rt_base.append( (j<s1.size()-1?",":"") +"\r\n");
						}
						rt_base.append(" ] \r\n");
					}
				}else{
					rt_base.append(", \"purl\":\""+ s.getPurl() +"\" \r\n");
				}
				rt_base.append(" }"+ (i<list.size()-1?",":"") +"\r\n");
				i++;
			}else if( s.getTarget().equals("hbanner") ){
				rt_hbanner.append(" { \r\n");
				rt_hbanner.append(" \"purl\":\""+ s.getPurl() +"\" \r\n");
				rt_hbanner.append(", \"h_types\":\""+ s.getH_types() +"\" \r\n");
				rt_hbanner.append(", \"target\":\""+ s.getTarget() +"\" \r\n");
				rt_hbanner.append(" }"+ (i<list.size()-1?",":"") +"\r\n");
				i++;
			}
			
		}
		rt.append(" { \"client\":[\r\n");
		rt.append(rt_goods.toString());
		rt.append(rt_keyword.toString());
		rt.append(rt_url.toString());
		rt.append(rt_base.toString());
		rt.append(rt_hbanner.toString());
		rt.append(" ] }\r\n");
		return rt.toString();		
	}
	public String getArShopData(ShopData sd, int id){
		StringBuffer sb= new StringBuffer();		
		sb.append("{\r\n");
		sb.append(" \"no\":\"t"+ id +"\" \r\n");
		sb.append(", \"pcode\":\""+ sd.getPCODE() +"\" \r\n");
		sb.append(", \"pnm\":\""+ sd.getPNM() +"\" \r\n");
		sb.append(", \"price\":\""+ sd.getPRICE() +"\" \r\n");
		sb.append(", \"img\":\""+ sd.getIMGPATH() +"\" \r\n");
		sb.append(", \"purl\":\""+ sd.getPURL() +"\" \r\n");
		sb.append("}");
		return sb.toString();
	}
	public String getInfo(){
		StringBuffer rt= new StringBuffer();
		int i= 0;

		Iterator it = list.keySet().iterator();
	    
		while(it.hasNext()){
			String pkey= it.next().toString();
			SkyAdData s= list.get(pkey);
			
			rt.append( "id="+ i +" sd="+ s.getSite_code() +" userid="+ s.getUserid() +" target="+ s.getTarget() +" purl="+ s.getPurl() +"\r\n");
			ArrayList<ShopData> s1= s.getData();
			for(int j=0; j<s1.size(); j++){
				rt.append( s1.get(j).getInfo("sid="+ j +" " ) );
			}
			i++;
		}
		return rt.toString();	
	}
	public int size(){
		if( list!=null ){
			return list.size();
		}else{
			return -1;
		}
	}
}
