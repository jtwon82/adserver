package com.adgather.management;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class MailAuthentication extends Authenticator{
	public PasswordAuthentication pa;
	public MailAuthentication(String id,String pwd){
		super();
		pa=new PasswordAuthentication(id,pwd);
	}
	public PasswordAuthentication getPasswordAuthentication(){
		return pa;
	}
}
