package com.adgather.batchjob;

import java.io.File;
import java.net.InetAddress;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;

import com.adgather.resource.db.DBManager;
import com.adgather.util.FileUtil;

public class ShopImgDown {
	public static void main(String[] args) throws Exception{
		// 
		
		//String base_dir= "D:/work/enliple/www/webapp/public_html/ad/sr/";
		String base_dir= "/home/dreamsearch/public_html/ad/sr/";
		
		boolean chk_main= false;
		InetAddress[] local = InetAddress.getAllByName(InetAddress.getLocalHost().getHostName());
        for(int i=0; i<local.length; i++) {
        	if( local[i].getHostAddress().indexOf("183.111.148.51")>-1 ){
        		chk_main= true;
        	}
        	if( local[i].getHostAddress().indexOf("211.191.174.5")>-1 ){
        		chk_main= true;
        	}
		}
        
		if( args.length>0 ){
			System.out.println( "db="+ args[0] +" act="+ args[1] );
			
			if( args[1].equals("down") ){
				ShopImgDown.imgProcess(args[0], "down", base_dir);
			}else if( args[1].equals("chk") ){
				ShopImgDown.imgProcess(args[0], "chk", base_dir);
			}
		}else{
			System.out.println("none");
		}
	}
	public static void imgProcess(String strdb, String act, String base_dir){
		Connection conn = null;
		Statement stmt=null;
		ResultSet rs=null;
		try{
			StringBuffer sql= new StringBuffer();
			sql.append(" SELECT userid                        \n");
			sql.append(" FROM admember                        \n");
			sql.append(" WHERE userid IN(SELECT userid        \n");
			sql.append(" 				FROM (SELECT userid   \n");
			sql.append(" 					  FROM iadsite    \n");
			sql.append(" 					  WHERE state='Y' \n");
			sql.append(" 					  GROUP BY userid \n");
			sql.append(" 					  UNION ALL       \n");
			sql.append(" 					  SELECT userid   \n");
			sql.append(" 					  FROM adsite     \n");
			sql.append(" 					  WHERE state='Y' \n");
			sql.append(" 					  GROUP BY userid \n");
			sql.append(" 					  ) t             \n");
			sql.append(" 				GROUP BY USERID)      \n");
			sql.append(" AND POINT>0                          \n");
			
			conn=DBManager.getConnection(strdb, "dreamsearch");
			stmt=conn.createStatement();
			rs= stmt.executeQuery(sql.toString());
			while(rs.next()){
				System.out.println(rs.getString("userid"));
				
				if( act.equals("down") ){
					ShopImgDown.runSaveImg(conn,base_dir,rs.getString("userid") );
				}else if ( act.equals("chk") ){
					ShopImgDown.chkRealImg2(conn, base_dir, rs.getString("userid"));
				}
			}
		}catch(Exception e){
			System.out.println(e );
		}finally{
			try{if(rs !=null) rs.close();} catch(Exception e){};
			try{if(stmt !=null) stmt.close();} catch(Exception e){};
			try{if(conn !=null) conn.close();} catch(Exception e){};
		}
	}
	public static void runSaveImg(Connection conn,String base_dir, String userid){
		StringBuffer sql=new StringBuffer();
		Statement stmt=null;
		Statement stmt2=null;
		ResultSet rs=null;
		try {
			sql.append(" SELECT NO, IMGPATH FROM SHOP_DATA WHERE down_ok='N' AND userid='"+userid+"' \n");
			stmt=conn.createStatement();
			rs=stmt.executeQuery(sql.toString());
			while(rs.next()){
				String imgfile = rs.getString("IMGPATH");
				int no = rs.getInt("NO");
				System.out.println(userid+":"+imgfile);
				try{
			        try{
			        	File a= new File( base_dir +"/"+ userid );
			        	if( !a.exists() ){
			        		a.mkdirs();
			        	}
			        }catch(Exception e){
			        	System.out.println( e );
			        }
			        String save_file= base_dir +"/"+ userid +"/"+ new File(imgfile).getName();
					int rt= FileUtil.saveUrlData(save_file, imgfile);
					File f= new File(save_file);
					if( f.length()<100 ) rt=1;
					
					stmt2=conn.createStatement();
			        if( rt==0 ){
				        stmt2.executeUpdate("update SHOP_DATA set down_ok='Y' WHERE NO="+rs.getInt("no"));
			        }else{
			        	System.out.print( "--e\n" );
				        stmt2.executeUpdate("update SHOP_DATA set down_ok='E',status='N' WHERE NO="+rs.getInt("no"));			        	
			        }
			        if(stmt2!=null) stmt2.close();
				}catch(Exception e){
					System.out.println( e );
				}finally{
				}
			}
		} catch(Exception e) {
			System.out.println( e );
		} finally {
			try{if(rs !=null) rs.close();} catch(Exception e){};
			try{if(stmt !=null) stmt.close();} catch(Exception e){};
			try{if(stmt2 !=null) stmt2.close();} catch(Exception e){};
		}
	}
	public static void chkRealImg2(Connection conn, String base_dir, String userid){
		Statement stmt1=null;
		ResultSet rs1=null;
		try{
			stmt1=conn.createStatement();
			rs1=stmt1.executeQuery( " SELECT no, imgpath FROM SHOP_DATA WHERE down_ok='Y' and userid='"+ userid +"' \n" );
			
			while(rs1.next()){
				
				String imgpath= rs1.getString("imgpath");
				
				String filename= imgpath.split("/")[ imgpath.split("/").length-1 ];
				System.out.println( "filepath /home/dreamsearch/public_html/ad/sr/"+ userid +"/"+ filename );
				File a= new File( "/home/dreamsearch/public_html/ad/sr/"+ userid +"/"+ filename);
				if( !a.exists() ){
					Statement stmt2=null;
					try{
						stmt2=conn.createStatement();
						String sql2="UPDATE SHOP_DATA SET down_ok='X' WHERE NO="+rs1.getString("no")+" ";
						System.out.println( sql2 );
						stmt2.executeUpdate(sql2);
					}catch(Exception e){
						System.out.println( "2 "+ e );
					}finally{
						if(stmt2!=null) stmt2.close();
					}
				}
			}
		}catch(Exception e){
			System.out.println( "3 "+ e );
		}finally{
			try{if(rs1 !=null) rs1.close();} catch(Exception e){};
			try{if(stmt1 !=null) stmt1.close();} catch(Exception e){};
		}
	}
	public static void chkRealImg(String base_dir){
		try{
        	File a= new File( base_dir );
        	String []list= a.list();
        	
        	if( list.length>0 ){
        		for(int i=0; i<list.length; i++){
        			File b= new File( base_dir +"/"+ list[i] );
		        	if( b.isDirectory() ){
		        		System.out.println( base_dir + list[i] );
		        		ShopImgDown.chkRealImg( base_dir + list[i] );
		        	}else if( b.length()>100 ){
		        		String userid= base_dir.split("/")[ base_dir.split("/").length-1 ];
		        		String sql= "update SHOP_DATA set down_ok='Y' where userid='"+ userid 
		        				+"' and imgpath like '%"+ b.getName() +"%' ";
		        		System.out.println( sql );
		        	}
        		}
        	}
        }catch(Exception e){
        	System.out.println( e );
        }
	}
	private void fileTest(){
		File f=new File("D:/work/enliple/www/rfdata/DATA/DUMPFAIL/aaaa.txt");
		f.renameTo(new File("D:/work/enliple/www/rfdata/DATA/DUMPFAIL/aaaa.txt"));
		Calendar now=Calendar.getInstance();
		java.util.Date udate=now.getTime();
		java.sql.Date date=new java.sql.Date(udate.getTime());
		System.out.println(now.getTimeInMillis());
	}
}
