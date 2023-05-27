package com.adgather.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.text.SimpleDateFormat;

import com.adgather.util.DateUtils;
import com.adgather.util.FileUtil;

public class FailLogTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

		FileInputStream fileinput = null;
		ObjectInputStream in=null;
		
		String path = "/work/enliple/www/webapp/log/test/";
		
		File dir=new File(path);
		File[] files=dir.listFiles();
		System.out.println( files.length );
		
		for(int i=0;i<files.length;i++){
			try{
				fileinput = new FileInputStream(files[i].getAbsolutePath());
			}catch(Exception e){
				continue;
			}
			if( files[i].getName().indexOf("SHOP_LOG")==-1 ) continue;

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			
			if( !sdf.format( files[i].lastModified() ).equals(DateUtils.getDate("yyyy-MM-dd")) ){
				FileUtil.fileCopy( files[i].getAbsolutePath(), path+"/ok/"+files[i].getName() );
				System.out.println( files[i].delete() );
				continue;
			}
			
			System.out.println( sdf.format( files[i].lastModified() ) +" "+ DateUtils.getDate("yyyy-MM-dd") );
		}
	}

}
