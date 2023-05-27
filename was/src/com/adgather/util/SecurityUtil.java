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
		//Ű ������ ���� - ��Ī��ȣȭ �˰���(DESede)���
        byte[] keydata ="g1ncom;research;solution".getBytes();
        DESedeKeySpec keySpec = new DESedeKeySpec(keydata);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
        SecretKey desKey = keyFactory.generateSecret(keySpec);
		//������ ����
		Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
		//��ȣȭ�� �ϱ� ���� ������ �ʱ�ȭ
		cipher.init(Cipher.ENCRYPT_MODE, desKey); 
		
		//��ȣȭ �� Text�� ����Ʈ �迭�� �����.
		byte[] plainText = text.getBytes("UTF-8");
//		for(int i = 0 ; i < plainText.length ; i++)
//			System.out.print(plainText[i]+" ");
		
		//��ȣȭ
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

		//������ ����
		Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");

		//��ȣȭ�� �ϱ� ���� ������ �ʱ�ȭ
        cipher.init(Cipher.DECRYPT_MODE, desKey);

        //��ȣȭ ����
        BASE64Decoder decoder = new BASE64Decoder();
        byte [] decryptedText = cipher.doFinal(decoder.decodeBuffer(java.net.URLDecoder.decode(text,"UTF-8")));
        rtnStr =  new String(decryptedText, "UTF-8");
//		System.out.println("");
//        System.out.println("\nDecrypted Text : " + output);

		return rtnStr;
	}
}
