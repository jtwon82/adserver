package com.adgather.parsing;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.adgather.batchjob.ShopDataItem;
import com.adgather.resource.db.DBManager;
import com.adgather.util.NetworkUtils;
import com.adgather.util.StringUtils;

/**
 * 멤버데이터
 * 
 * @author mobon069
 *
 */
public class PasingMember {
	// 결과리스트
	public static JSONObject MemberData() {
		ShopDataItem.dbip = NetworkUtils.getServerType().equals("real") ? "54" : "5";

		JSONObject root = new JSONObject();
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			StringBuffer sql = new StringBuffer();
			sql.append(" SELECT userid, homepi,imgname_logo \n");
			sql.append(" FROM admember  \n");
			sql.append(" WHERE grade < 2 AND gubun = 11 AND delflag = 'N' AND point > 1000\n");
			sql.append(" AND homepi<> ''	\n");
			sql.append(" GROUP BY homepi	\n");
			sql.append(" ORDER BY regdate DESC	\n");

			conn = DBManager.getConnection(ShopDataItem.dbip, "dreamsearch");

			stmt = conn.createStatement();

			rs = stmt.executeQuery(sql.toString());

			JSONObject child = null;
			JSONArray arr = new JSONArray();
			while (rs.next()) {
				child = new JSONObject();
				String logo = StringUtils.isNotEmpty(rs.getString("imgname_logo"))?
						"http://www.dreamsearch.or.kr/ad/imgfile/"+rs.getString("imgname_logo"):"";
				child.put("userid", rs.getString("userid"));
				child.put("domain", rs.getString("homepi"));
				child.put("logo", logo);
				arr.add(child);
			}
			root.put("element", arr);
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
			;
			try {
				if (stmt != null)
					stmt.close();
			} catch (Exception e) {
			}
			;
			try {
				if (conn != null)
					conn.close();
			} catch (Exception e) {
			}
			;
		}
		return root;
	}
}
