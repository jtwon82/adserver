package com.adgather.util;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
public class SecurityUtil
{
	public static String secEncode(String text)throws Exception{
		String rtnStr="";
        BASE64Encoder encoder = new BASE64Encoder();
		//키 생성기 생성 - 대칭암호화 알고리즘(DESede)사용
        byte[] keydata ="g1ncom;research;solution".getBytes();
        DESedeKeySpec keySpec = new DESedeKeySpec(keydata);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
        SecretKey desKey = keyFactory.generateSecret(keySpec);
		//사이퍼 생성
		Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
		//암호화를 하기 위한 사이퍼 초기화
		cipher.init(Cipher.ENCRYPT_MODE, desKey); 
		
		//암호화 할 Text를 바이트 배열로 만든다.
		byte[] plainText = text.getBytes("UTF-8");
//		for(int i = 0 ; i < plainText.length ; i++)
//			System.out.print(plainText[i]+" ");
		
		//암호화
		byte[] cipherText = cipher.doFinal(plainText);
//		for(int i = 0 ; i < cipherText.length ; i++)
//			System.out.print(cipherText[i]+" ");
		rtnStr = java.net.URLEncoder.encode(encoder.encode(cipherText), "UTF-8");
		return rtnStr;	
	}
	public static String secDecode(String text)throws Exception{
		String rtnStr="";

        byte[] keydata ="g1ncom;research;solution".getBytes();

        DESedeKeySpec keySpec = new DESedeKeySpec(keydata);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
        SecretKey desKey = keyFactory.generateSecret(keySpec);

		//사이퍼 생성
		Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");

		//복호화를 하기 위한 사이퍼 초기화
        cipher.init(Cipher.DECRYPT_MODE, desKey);

        //복호화 수행
        BASE64Decoder decoder = new BASE64Decoder();
        byte [] decryptedText = cipher.doFinal(decoder.decodeBuffer(java.net.URLDecoder.decode(text,"UTF-8")));
        rtnStr =  new String(decryptedText, "UTF-8");
//		System.out.println("");
//        System.out.println("\nDecrypted Text : " + output);

		return rtnStr;
	}
}
