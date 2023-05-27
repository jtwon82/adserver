package com.adgather.management;

import java.util.Properties;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.net.SMTPAppender;

public class AuthSMTPAppender extends SMTPAppender{
	public void activateOptions(){
		Properties props=new Properties(System.getProperties());
		if(getSMTPHost()!=null){
			props.put("mail.smtp.host",getSMTPHost());
		}
		props.put("mail.smtp.port", 25);
		props.put("mail.smtp.auth", "true");
		Authenticator auth=new MailAuthentication("resear","chad.com");
		
		Session  session = Session.getInstance(  props,auth );
		Message msg   = new MimeMessage( session);
		try{
			if(getFrom()!=null){
				Address  fromAddr = new InternetAddress( getFrom()); 
				msg.setFrom(fromAddr);
			}else{
				msg.setFrom();
			}
			Address  toAddr = new InternetAddress( getTo());
			msg.setRecipient( Message.RecipientType.TO ,toAddr);
			if(getSubject()!=null){
				msg.setSubject(getSubject());
			}
		}catch(MessagingException e){
			LogLog.error("Could not activate SMTPAppender options.",e);
		}
	}
}
