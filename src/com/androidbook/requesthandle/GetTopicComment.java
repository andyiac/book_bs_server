package com.androidbook.requesthandle;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.androidbook.databasesinterface.DataBaseManager;
import com.androidbook.databasesinterface.Topic;
import com.androidbook.databasesinterface.TopicComment;
import com.androidbook.utils.Utils;

public class GetTopicComment implements HandleRequest{

	private String responseParam = null;
	
	public void setResponseParam(String responseParam) {
		this.responseParam = responseParam;
	}

	public int handleRequest(long uid, String password, String... param) {
		Connection conn = null;
		int result = -1;
		long Topic_ID = Long.parseLong(param[0]);
		
		JSONArray jsonArray = new JSONArray();
		try {
			conn = DataBaseManager.getInstance().getConnection();
			if(conn != null){
				Statement stat = conn.createStatement();
				String sql = "select Topic_Com_ID from topic where Topic_ID=" + Topic_ID;
				ResultSet rs = stat.executeQuery(sql);
				if(rs.next()){
					String commemt = rs.getString(Topic.Topic_Com_ID);
					if(!Utils.IsEmpty(commemt)){
						String[] comments = commemt.split("#");
						jsonArray.addAll(handleResquest(stat, comments));
					}
				}
				this.setResponseParam(jsonArray.toString());
				stat.close();
				rs.close();
			}
			result = 0;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("获取话题评论出错： " + e.toString());
		} finally {
			try {
				conn.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		return result;
		
	}

	public JSONArray handleResquest(Statement stat, String[] comments) throws SQLException {
		JSONArray jsonArray = new JSONArray();
		if(comments.length > 0){
			for(String str : comments){
				long cid = Long.parseLong(str);
				String sqll = "select * from topiccomment where Topic_Com_ID=" + cid;
				ResultSet rset = stat.executeQuery(sqll);
				if(rset.next()){
					JSONObject j = buildParam(rset);
					jsonArray.add(j);
				}
				rset.close();
			}
		}
		return jsonArray;
	}

	private JSONObject buildParam(ResultSet rs) throws SQLException {
		long Topic_Com_ID = rs.getLong(TopicComment.Topic_Com_ID); 
		String topicComContent = rs.getString(TopicComment.Topic_Com_Content);
		String topicComPhoto = rs.getString(TopicComment.Topic_Com_Photo);
		int topicComTime = rs.getInt(TopicComment.Topic_Com_Time);
		long topicComFrom = rs.getLong(TopicComment.Topic_Com_From);
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put(TopicComment.Topic_Com_ID, Topic_Com_ID);
		jsonObject.put(TopicComment.Topic_Com_Content, topicComContent);
		jsonObject.put(TopicComment.Topic_Com_Photo, topicComPhoto);
		jsonObject.put(TopicComment.Topic_Com_Time, topicComTime);
		jsonObject.put(TopicComment.Topic_Com_From, topicComFrom);
		
		return jsonObject;
		
		
	}

	public String getRequestType() {
		return "GetNewTopic";
	}

	public String getResponseParam() {
		return responseParam==null ? "" : responseParam;
	}

}
