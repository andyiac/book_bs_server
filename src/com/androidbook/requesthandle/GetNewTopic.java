package com.androidbook.requesthandle;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.androidbook.databasesinterface.DataBaseManager;
import com.androidbook.utils.Utils;

public class GetNewTopic implements HandleRequest{

	private String responseParam = null;
	
	public void setResponseParam(String responseParam) {
		this.responseParam = responseParam;
	}

	public int handleRequest(long uid, String password, String... param) {
		Connection conn = null;
		int result = -1;
		int insideCount = 0;
		int outsideCount = Integer.parseInt(Utils.IsEmpty(param[0]) ? "0" : param[0]);
		JSONArray jsonArray = new JSONArray();
		try {
			conn = DataBaseManager.getInstance().getConnection();
			if(conn != null){
				Statement stat = conn.createStatement();
				
				String maxCount = "select max(id) from topic";
				ResultSet rs2 = stat.executeQuery(maxCount);
				if(rs2.next()){
					insideCount = rs2.getInt(1);
					rs2.close();
				}
				
				if(insideCount > outsideCount){
					String sql = "select * from topic where id>" + outsideCount;
					ResultSet rs = stat.executeQuery(sql);
					while(rs.next()){
						jsonArray.add(buildParam(rs));
					}
					this.setResponseParam(jsonArray.toString());
					stat.close();
					rs.close();
				}
				result = 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("获取话题出错： " + e.toString());
		} finally {
			try {
				conn.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		return result;
		
	}

	private JSONObject buildParam(ResultSet rs) throws SQLException {
		long topicUID = rs.getLong("UID"); 
		long topicID = rs.getLong("topic_ID");
		String topicContent = rs.getString("topic_Content");
		String topicName = rs.getString("topic_Name");
		String topicPhoto = rs.getString("topic_Photo");
		int topicTime = rs.getInt("topic_Time");
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("topicUID", topicUID);
		jsonObject.put("topicID", topicID);
		jsonObject.put("topicContent", topicContent);
		jsonObject.put("topicName", topicName);
		jsonObject.put("topicPhoto", topicPhoto);
		jsonObject.put("topicTime", topicTime);
		
		return jsonObject;
		
		
	}

	public String getRequestType() {
		return "GetNewTopic";
	}

	public String getResponseParam() {
		return responseParam==null ? "" : responseParam;
	}

}
