package com.androidbook.requesthandle;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import net.sf.json.JSONArray;

import com.androidbook.databasesinterface.DataBaseManager;
import com.androidbook.databasesinterface.Friend;

public class GetAllFriends implements HandleRequest{

	private String responseParam = null;
	
	public void setResponseParam(String responseParam) {
		this.responseParam = responseParam;
	}

	public int handleRequest(long uid, String password, String... param) {
		Connection conn = null;
		int result = -1;
		JSONArray jsonArray = new JSONArray();
		try {
			conn = DataBaseManager.getInstance().getConnection();
			if(conn != null){
				Statement stat = conn.createStatement();
				ResultSet rs = Friend.query(stat, Friend.TABLE_NAME, uid, Friend.FRIEND_UID);
				if(rs.next()){
					String[] friends = rs.getString(Friend.FRIEND_UID).split("#");
					int fCount = friends.length;
					if(fCount > 0){
						GetPersonInfo gpi = new GetPersonInfo();
						int ret = gpi.handleRequest(uid, password, friends);
						if(ret == 0){
							String personinfojsonarray = gpi.getResponseParam();
							JSONArray j = JSONArray.fromObject(personinfojsonarray);
							jsonArray.addAll(j);
						}
					}
					result = 0;
				}
				this.setResponseParam(jsonArray.toString());
				rs.close();
				stat.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("获取好友出错： " + e.toString());
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
		return "GetAllFriends";
	}

	public String getResponseParam() {
		return responseParam==null ? "" : responseParam;
	}

}
