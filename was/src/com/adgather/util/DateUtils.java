package com.adgather.util;

import java.util.Calendar;

public class DateUtils{
	public static String getDate(String strtype){
		java.util.Date date = new java.util.Date();
		java.text.DateFormat df = new java.text.SimpleDateFormat(strtype);	///"yyyy-MM-dd"
		return df.format(date).toString();
	}
	public static int getAdTimeZone(int usemoney, int usedmoney, String ad_rhour){
		if( ad_rhour.equals("") ){
			return 0;
		}
		int hour= Integer.parseInt(DateUtils.getDate("HH")); hour= (hour==0?24:hour);
		int ad_rhoura= ad_rhour.indexOf("a")>-1?7:0;
		int ad_rhourb= ad_rhour.indexOf("b")>-1?3:0;
		int ad_rhourc= ad_rhour.indexOf("c")>-1?3:0;
		int ad_rhourd= ad_rhour.indexOf("d")>-1?3:0;
		int ad_rhoure= ad_rhour.indexOf("e")>-1?3:0;
		int ad_rhourf= ad_rhour.indexOf("f")>-1?3:0;
		int ad_rhourg= ad_rhour.indexOf("g")>-1?3:0;
		
		int adlive = ( hour >= 0 && hour <= 6 && ad_rhoura > 0 ? 1 : 0 )
				+ ( hour >= 7 && hour <= 9 && ad_rhourb > 0 ? 1 : 0 )
				+ ( hour >= 10 && hour <= 12 && ad_rhourc > 0 ? 1 : 0 )
				+ ( hour >= 13 && hour <= 15 && ad_rhourd > 0 ? 1 : 0 )
				+ ( hour >= 16 && hour <= 18 && ad_rhoure > 0 ? 1 : 0 )
				+ ( hour >= 19 && hour <= 21 && ad_rhourf > 0 ? 1 : 0 )
				+ ( hour >= 22 && hour <= 24 && ad_rhourg > 0 ? 1 : 0 ) ;
		
		 int point_hour=0;
		 try{
			 point_hour = (usemoney / (ad_rhoura + ad_rhourb + ad_rhourc
				 + ad_rhourd + ad_rhoure + ad_rhourf + ad_rhourg  ) ); 
		 }catch(Exception e){
			 return 0;
		 }
		
		 int during_hour = ( hour > 0 && ad_rhoura > 0 ? 6 : 0 )
				 + ( hour > 6 && ad_rhourb > 0 ? 3 : 0 )
				 + ( hour > 10 && ad_rhourc > 0 ? 3 : 0 )
				 + ( hour > 13 && ad_rhourd > 0 ? 3 : 0 )
				 + ( hour > 16 && ad_rhoure > 0 ? 3 : 0 )
				 + ( hour > 19 && ad_rhourf > 0 ? 3 : 0 )
				 + ( hour > 22 && ad_rhourg > 0 ? 3 : 0 );
		 
		 int return_value=0;
		 try{
			 return_value= (usemoney==0 ? (adlive>0?1:0) : ( adlive > 0 && usedmoney < ( point_hour * during_hour ) ? 1 : 0 ) );
		 }catch(Exception e){
			 return 0;
		 }
		 return return_value;
	}
	public String getTime(){
		Calendar now=Calendar.getInstance();
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
		return  year+"-"+month+"-"+date+" "+hourd+":"+minute+":"+second+".000";
	}
	public String getSQLTime(long time){
		Calendar now=Calendar.getInstance();
		now.setTimeInMillis(time);
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
	public String getYYYYMMDDHH(long time,String sep){
		Calendar now=Calendar.getInstance();
		now.setTimeInMillis(time);
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
		return  year+sep+month+sep+date+sep+hourd;
	}
}