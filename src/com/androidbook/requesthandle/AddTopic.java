package com.androidbook.requesthandle;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import com.androidbook.databasesinterface.DataBaseManager;
import com.androidbook.databasesinterface.Topic;
import com.androidbook.utils.Utils;

public class AddTopic implements HandleRequest{

	private String responseParam = null;
	
	//String ... 不定长度的数组
	public int handleRequest(long uid, String password, String... param) {
		
		// 定义连接变量
		Connection conn = null;
		int result = -1;
		
		String UID = String.valueOf(uid);
		String Topic_Content = param[0];
		int Topic_Time = Integer.parseInt(param[1]);
		String Topic_Name = param[2];
		String Topic_photo = param[3];
		
		// 生成话题ID
		long Topic_ID = System.currentTimeMillis();
		
		try {
			//连接到数据库
			DataBaseManager dbm = DataBaseManager.getInstance();
			conn = dbm.getConnection();
			if(conn != null){
				// 建立了到特定数据库的连接之后，就可用该连接发送 SQL 语句。
				Statement stat = conn.createStatement();
				
				//插入的数据
				HashMap<String, Object> values = new HashMap<String, Object>();
				values.put(Topic.ID, Topic.getMaxID(stat, Topic.TABLE_NAME));
				values.put(Topic.UID, UID);
				values.put(Topic.Topic_ID, Topic_ID);
				values.put(Topic.Topic_Content, Utils.pad(Topic_Content));
				values.put(Topic.Topic_Time, Topic_Time);
				values.put(Topic.Topic_Name, Utils.pad(Topic_Name));
				values.put(Topic.Topic_Photo, Utils.pad(Topic_photo));
				Topic.insert(stat, Topic.TABLE_NAME, values);
				stat.close();
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

	public String getRequestType() {
		return "AddTopic";
	}

	public String getResponseParam() {
		return responseParam==null?"":responseParam;
	}

}
