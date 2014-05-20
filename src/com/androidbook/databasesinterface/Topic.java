package com.androidbook.databasesinterface;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class Topic extends DBInterface{
	public static final String TABLE_NAME = "topic";
	
	public static final String Topic_ID = "Topic_ID";
	public static final String Topic_Content = "Topic_Content";
	public static final String Topic_Time = "Topic_Time";
	public static final String Topic_Name = "Topic_Name";
	public static final String Topic_Photo = "Topic_Photo";
	public static final String Topic_Com_ID = "Topic_Com_ID";
	
	
	public static int update2(Statement stat, String table, long topic_id, HashMap<String, Object> values, String field) throws SQLException{
		StringBuffer sb = new StringBuffer();
		sb.append("update ").append(table).append(" set ");
		sb.append(field).append("=").append(values.get(field));
		sb.append(" where Topic_ID=").append(topic_id).append(";");
		System.out.println("sql----"+sb.toString());
		return stat.execute(sb.toString()) ? 0 : -1 ;
		
	}
	
	
	
}
