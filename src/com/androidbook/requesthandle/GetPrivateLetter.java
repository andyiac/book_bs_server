package com.androidbook.requesthandle;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.androidbook.databasesinterface.DataBaseManager;

public class GetPrivateLetter implements HandleRequest{

	private String responseParam = null;

	public int handleRequest(long uid, String password, String... param) {
		Connection conn = null;
		int result = -1;
		int letterLength = param.length;
		
		JSONArray jsonArray = new JSONArray();
		try {
			conn = DataBaseManager.getInstance().getConnection();
			if(conn != null){
				Statement stat = conn.createStatement();
				for(int i=0; i<letterLength; i++){
					System.out.println("=====  " + letterLength);
					jsonArray.addAll(getPrivateLetter(stat, uid, "UID", param[i]));
					jsonArray.addAll(getPrivateLetter(stat, uid, "privateLetter_UID", param[i]));
				}
				this.setResponseParam(jsonArray.toString());
				result = 0;
				stat.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("获取私信出错： " + e.toString());
		} finally {
			try {
				conn.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		return result;
	}
	
	public JSONArray getPrivateLetter(Statement stat, long uid, String field, String privateLetterID) throws SQLException{
		JSONArray jsonArray = new JSONArray();
		String sql = "select * from letter where " + field + "=" + uid + " and " + 
		 "PrivateLetter_ID=" + (IsEmpty(privateLetterID)?"0" : privateLetterID);
		ResultSet rs = stat.executeQuery(sql);
		while(rs.next()){
			jsonArray.add(buildParam(rs, uid));
		}
		rs.close();
		return jsonArray;
	}
	
	public boolean IsEmpty(String friends) {
		if(friends==null || friends.equals("")){
			return true;
		}
		return false;
	}
	
	public void setResponseParam(String responseParam) {
		this.responseParam = responseParam;
	}
	
	private JSONObject buildParam(ResultSet rs, long uid) throws SQLException {
		long UID = rs.getLong(2);
		long privateLetterUID = rs.getLong(3);
		long privateLetterID = rs.getLong(4);
		String privateLetterContent = rs.getString(5);
		int privateLetterTime = rs.getInt(6);
		String privateLetterName = rs.getString(7);
		String privateLetterPhoto = rs.getString(8);
		int privateLetterIsSend = rs.getInt(9);
		privateLetterIsSend = uid == UID ? 1 : 0; 
		
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("UID", UID);
		jsonObject.put("privateLetterUID", privateLetterUID);
		jsonObject.put("privateLetterID", privateLetterID);
		jsonObject.put("privateLetterContent", privateLetterContent);
		jsonObject.put("privateLetterTime", privateLetterTime);
		jsonObject.put("privateLetterName", privateLetterName);
		jsonObject.put("privateLetterPhoto", privateLetterPhoto);
		jsonObject.put("privateLetterIsSend", privateLetterIsSend);
		
		return jsonObject;
		
		
	}
	
	public String getRequestType() {
		return "GetPrivateLetter";
	}

	public String getResponseParam() {
		return responseParam==null ? "" : responseParam;
	}

}
