package com.androidbook.requesthandle;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import com.androidbook.databasesinterface.DataBaseManager;
import com.androidbook.databasesinterface.Topic;
import com.androidbook.databasesinterface.TopicComment;
import com.androidbook.utils.Utils;

public class AddTopicComment implements HandleRequest{

	private String responseParam = null;
	
	public int handleRequest(long uid, String password, String... param) {
		Connection conn = null;
		int result = -1;
		//为要添加评论的TOPIC_ID
		long topic_ID = Long.parseLong(param[0]);
		String content = param[1];
		String photo = param[2];
		String time = param[3];
		//生成评论的ID
		String topic_Com_ID = param[4];
		
		try {
			conn = DataBaseManager.getInstance().getConnection();
			if(conn != null){
				Statement stat = conn.createStatement();
				String comments = null;
				// 1.更新表Topic中的字段Topic_Com_ID
				String sql = "select * from topic where Topic_ID=" + topic_ID;
				ResultSet rs = stat.executeQuery(sql);
				if(rs.next()){
					comments = rs.getString(8);
				}
				updateString(topic_ID, topic_Com_ID, stat, comments);
				HashMap<String, Object> values = new HashMap<String, Object>();
				values.put(TopicComment.ID, Topic.getMaxID(stat, TopicComment.TABLE_NAME));
				values.put(TopicComment.Topic_Com_ID, topic_Com_ID);
				values.put(TopicComment.Topic_Com_Content, Utils.pad(content));
				values.put(TopicComment.Topic_Com_Time, time);
				values.put(TopicComment.Topic_Com_Photo, Utils.pad(photo));
				values.put(TopicComment.Topic_Com_From, uid);
				TopicComment.insert(stat, TopicComment.TABLE_NAME, values);
				
				rs.close();
				stat.close();
				responseParam = String.valueOf(topic_Com_ID);
				result = 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("插入话题出错： " + e.toString());
		} finally {
			try {
				conn.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		return result;
		
	}

	public void updateString(long topic_ID, String topic_Com_ID, Statement stat, String comments) throws SQLException {
		StringBuffer sb = new StringBuffer();
		System.out.println("comments   " + comments);
		if(!Utils.IsEmpty(comments)){
			sb.append(comments);
			sb.append("#").append(topic_Com_ID);
		}else{
			sb.append(topic_Com_ID);
		}
		HashMap<String, Object> values = new HashMap<String, Object>();
		values.put(Topic.Topic_Com_ID, Utils.pad(sb.toString()));
		Topic.update2(stat, Topic.TABLE_NAME, topic_ID, values, Topic.Topic_Com_ID);
	}

	public String getRequestType() {
		return "AddTopicComment";
	}

	public String getResponseParam() {
		return responseParam==null?"":responseParam;
	}

}
