package com.adgather.test;

import java.io.DataOutputStream;
import java.io.FileOutputStream;

public class DataOutputStreamTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		FileOutputStream fos = null;
		DataOutputStream dos = null;
		try{
			fos= new FileOutputStream("data.bin");
			dos = new DataOutputStream(fos);
			dos.writeBoolean(false);
			dos.writeByte((byte)5);
			dos.writeInt(1000);
			dos.writeUTF("ggg");
		}catch(Exception e){
			System.out.println( e );
		}
		
	}

}
