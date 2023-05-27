package com.adgather.batchjob;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import com.adgather.resource.db.DBManager;
import com.adgather.util.FileUtil;
import com.adgather.util.NetworkUtils;

public class ShopDataImgChk {
	public static String dbip;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		ShopDataItem.dbip = NetworkUtils.getServerType().equals("real")?"54":"5";
		
		System.out.println( ShopDataItem.dbip );
		
		Connection conn = null;
		Statement stmt=null;
		ResultSet rs=null;
		try{
			StringBuffer sql= new StringBuffer();
			sql.append(" SELECT no, userid, url, imgpath \n");
			sql.append(" FROM SHOP_DATA \n");
			sql.append(" WHERE lastupdate < DATE_ADD(NOW(), INTERVAL -1 DAY) and status='Y' \n");
			
			conn=DBManager.getConnection(ShopDataItem.dbip, "dreamsearch");
			System.out.println( conn );
			
			stmt=conn.createStatement();
			
			rs= stmt.executeQuery( sql.toString() );
			
			while(rs.next()){
				boolean chking= false;
				
				if( FileUtil.imgURLCheck(rs.getString("imgpath")) ){
					chking=true; 
				}else{
					chking=false;
				}
				System.out.println( rs.getString("userid") +" "+ rs.getString("no") +" "+ rs.getString("imgpath") +" "+ chking );
				
				Statement stmt2=null;
				try{
					stmt2=conn.createStatement();
					String sql2="update SHOP_DATA set lastupdate = now(), status='"+(chking?"Y":"N")+"' "
							+" where no="+ rs.getString("no");
					//System.out.println( sql2 );
					stmt2.executeUpdate(sql2);
				}catch(Exception e){
					System.out.println( e );
				}finally{
					if(stmt2!=null) stmt2.close();
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

}
