package com.adgather.management;


import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class  ManagementProgram{

	public static void main(String[] args){
		//ManagementProgram.sendMail("dddddddddddddddddd", "l2ms@nate.com");
		//ManagementProgram.sendMail("l2ms@researchad.com", "l2ms@nate.com", "테스트 메일입니다만..");
		ManagementProgram.msgAlert("테스트 메시지..");
		
	}

	public static String[] getDate()
	 {

		 DecimalFormat df = new DecimalFormat("00");

		 Calendar currentCalendar = Calendar.getInstance();
		 Calendar currentCalendar2 = Calendar.getInstance();
		  currentCalendar.add(currentCalendar2.DATE, -1);
		  currentCalendar2.add(currentCalendar2.DATE, -8);
		 
		 String strYear   = Integer.toString(currentCalendar.get(Calendar.YEAR));
		 String strMonth  = df.format(currentCalendar.get(Calendar.MONTH) + 1);
		 String strDay  = df.format(currentCalendar.get(Calendar.DATE));

		 String strYear2   = Integer.toString(currentCalendar2.get(Calendar.YEAR));
		 String strMonth2  = df.format(currentCalendar2.get(Calendar.MONTH) + 1);
		 String strDay2  = df.format(currentCalendar2.get(Calendar.DATE));

		 String[] strDate = new String[2];
		 strDate[0] =strYear + strMonth + strDay;
		 strDate[1] = strYear2 + strMonth2 + strDay2;
			
		 return strDate;
	  }
	public static void msgAlert(String msg){
		ManagementProgram.sendMail("l2ms@researchad.com", "l2ms@nate.com", msg);
	}
	private static String sendMail(String from,String to,String html){
		String  server   = "mail.researchad.com";
		String  name   = "adgather";
		String  subject   = "Adgather 오류!";
		Properties props = new Properties();
		props.put("mail.smtp.host", server);
		props.put("mail.smtp.port", 25);
		props.put("mail.smtp.auth", "true");
		Authenticator auth=new MailAuthentication("resear","chad.com");
		Session  sess = Session.getInstance(  props,auth );
		Message msg   = new MimeMessage( sess);
		  try{
			  Address  fromAddr = new InternetAddress( from,"이명석" );
			  System.out.println(from);
			  msg.setFrom( fromAddr );
			  Address  toAddr   = new InternetAddress( to );
			  msg.setRecipient( Message.RecipientType.TO ,  toAddr );
			  msg.setSubject( subject );
			  msg.setContent( html,  "text/html;charset=euc-kr" );
			  //Transport.send(msg);
			  Transport  tran = sess.getTransport("smtp");
			  tran.connect( server, "resear", "chad.com" );
			  tran.sendMessage( msg, msg.getAllRecipients() );
			  tran.close();
			  return "true";
		  } catch( Exception e ){
			e.printStackTrace();
			return "false";
		  }
	}
}
