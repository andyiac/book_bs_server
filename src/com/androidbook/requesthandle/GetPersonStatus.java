package com.androidbook.requesthandle;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.androidbook.databasesinterface.DataBaseManager;
import com.androidbook.databasesinterface.PersonInfo;

public class GetPersonStatus implements HandleRequest{

	private String responseParam = null;
	
	public int handleRequest(long uid, String password, String... param) {
		Connection conn = null;
		int result = -1;
		JSONArray jsonArray = new JSONArray();
		try {
			conn = DataBaseManager.getInstance().getConnection();
			if(conn != null){
				Statement stat = conn.createStatement();
				ResultSet rs = PersonInfo.query(stat, PersonInfo.TABLE_NAME, PersonInfo.UID, PersonInfo.STATUS);
				while(rs.next()){
					jsonArray.add(buildParam(rs));
					result = 0;
				}
				this.setResponseParam(jsonArray.toString());
				rs.close();
				stat.close();
			}
		} catch (Exception e) {
			System.out.println("获取用户状态出错： " + e.toString());
		} finally {
			try {
				conn.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		return result;
	}
	
	public void setResponseParam(String responseParam) {
		this.responseParam = responseParam;
	}
	
	private JSONObject buildParam(ResultSet rs) throws SQLException {
		long personUID = rs.getLong(PersonInfo.UID);
		int personStatus = rs.getInt(PersonInfo.STATUS);
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("UID", personUID);
		jsonObject.put("personStatus", personStatus);
		
		return jsonObject;
		
		
	}
	
	public String getRequestType() {
		return "GetPersonInfo";
	}

	public String getResponseParam() {
		return responseParam==null ? "" : responseParam;
	}

}
