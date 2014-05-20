package com.androidbook.requesthandle;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.androidbook.databasesinterface.DataBaseManager;

public class Logout implements HandleRequest{

	public int handleRequest(long uid, String password, String... param) {
		
		Connection conn = null;
		int result = -1;
		try {
			conn = DataBaseManager.getInstance().getConnection();
			if(conn != null){
				Statement stat = conn.createStatement();
				String sql = "select * from person where " + 
							 "UID=" + uid + " and " +
							 "person_password=" + password;
				ResultSet rs = stat.executeQuery(sql);
				if(rs.next()){
					sql = "update personinfo set status=-1 where UID=" + uid;
					stat.execute(sql);
					result = 0;
				}
				rs.close();
				stat.close();
			}
		} catch (Exception e) {
			System.out.println("注销出错： "+e.toString());
		} finally {
			try {
				conn.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		return result;
		
	}

	public String getRequestType() {
		
		return null;
	}

	public String getResponseParam() {
		// TODO Auto-generated method stub
		return "";
	}

}
