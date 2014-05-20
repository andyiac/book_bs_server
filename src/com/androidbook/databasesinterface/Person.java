package com.androidbook.databasesinterface;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Person extends DBInterface{

	public static final String TABLE_NAME = "person";
	public static final String PERSON_NAME = "person_name";
	public static final String PERSON_PASSWORD = "person_password";
	
	public static ResultSet query2(Statement stat, String table, String... field) throws SQLException{
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
		
		System.out.println("--------->>>Person.java sb sql>>>>>"+sb);
		rs = stat.executeQuery(sb.toString());
		return rs;
	}
	
}
