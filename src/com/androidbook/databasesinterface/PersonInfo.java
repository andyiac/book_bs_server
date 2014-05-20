package com.androidbook.databasesinterface;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PersonInfo extends DBInterface{
	
	public static final String TABLE_NAME = "personinfo";
	
	public static final String PERSON_NAME = "person_name";
	public static final String PERSON_PHOTO = "person_photo";
	public static final String PERSON_MOBILE = "person_mobile";
	public static final String PERSON_SEX = "person_sex";
	public static final String PERSON_PASSWORD = "person_password";
	public static final String PERSON_ADDRESS = "person_address";
	public static final String STATUS = "status";
	
	
	public static ResultSet query(Statement stat, String table, String... field) throws SQLException{
		ResultSet rs = null;
		StringBuffer sb = new StringBuffer();
		sb.append("select ");
		boolean bool = false;
		if(field == null || field.length == 0 || field[0].equals("")){
			sb.append(" * ");
			bool =true;
		}
		if(!bool){
			for(int i = 0; i < field.length; i++){
				if(i == field.length -1){
					sb.append(field[i]);
					break;
				}
				sb.append(field[i]).append(",");
			}
		}
		sb.append(" from ")
		.append(table);
		rs = stat.executeQuery(sb.toString());
		return rs;
	}
	public static ResultSet query(Statement stat, String table, long uid, String... field) throws SQLException{
		ResultSet rs = null;
		StringBuffer sb = new StringBuffer();
		sb.append("select ");
		boolean bool = false;
		if(field == null || field.length == 0 || field[0].equals("")){
			sb.append(" * ");
			bool =true;
		}
		if(!bool){
			for(int i = 0; i < field.length; i++){
				if(i == field.length -1){
					sb.append(field[i]);
					break;
				}
				sb.append(field[i]).append(",");
			}
		}
		sb.append(" from ")
		.append(table)
		.append(" where UID=")
		.append(uid);
		rs = stat.executeQuery(sb.toString());
		return rs;
	}


}
