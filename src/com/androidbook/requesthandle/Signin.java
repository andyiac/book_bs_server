package com.androidbook.requesthandle;

import com.androidbook.databasesinterface.*;
import com.androidbook.utils.Utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class Signin implements HandleRequest{

	public int handleRequest(long uid, String pawd, String... param) {
		
		String UID = param[0];
		String name = param[1];
		String password = param[2];
		String mobile = param[3];
		String photo = param[4];
		String sex = param[5];
		String address = param[6];
		int status = -1;
		
		Connection conn = null;
		conn = DataBaseManager.getInstance().getConnection();
		int result = -1;
		boolean exit = false;
		if(conn != null){
			try {
				Statement stat;
				stat = conn.createStatement();
				ResultSet rs = DBInterface.query(stat, Person.TABLE_NAME, Long.parseLong(UID), DBInterface.UID); 
				if(rs.next()){//注册过了
					result = 1;
					exit = true;
					rs.close();
				}
				if(!exit){
					HashMap<String, Object> values = new HashMap<String, Object>();
					values.put(PersonInfo.ID, DBInterface.getMaxID(stat, PersonInfo.TABLE_NAME));
					values.put(PersonInfo.UID, UID);
					values.put(PersonInfo.PERSON_NAME, Utils.pad(name));
					values.put(PersonInfo.PERSON_PHOTO, Utils.pad(photo));
					values.put(PersonInfo.PERSON_MOBILE, Utils.pad(mobile));
					values.put(PersonInfo.PERSON_SEX, Utils.pad(sex));
					values.put(PersonInfo.PERSON_PASSWORD, Utils.pad(password));
					values.put(PersonInfo.PERSON_ADDRESS, Utils.pad(address));
					values.put(PersonInfo.STATUS, status);
					DBInterface.insert(stat, PersonInfo.TABLE_NAME, values);
					
					values = new HashMap<String, Object>();
					System.out.println("------Signin-->>"+values.get(UID));
					
					
					values.put(Person.ID, DBInterface.getMaxID(stat, Person.TABLE_NAME));
					values.put(Person.UID, UID);
					values.put(Person.PERSON_NAME, Utils.pad(name));
					values.put(Person.PERSON_PASSWORD, Utils.pad(password));
					DBInterface.insert(stat, Person.TABLE_NAME, values);
					
					stat.close();
					result = 0 ;
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("注册出错"+e.toString());
				result = -1 ;
			}
		}
		return result;
	}

	public String getRequestType() {
		return "Signin";
	}

	/*
	 * Login--
	 * Logout--
	 * AddTopic--
	 * GetAllTopic--
	 * GetNewTopic--
	 * GetPersonInfo--
	 * SendPrivateLetter
	 * GetNewPrivateLetter--
	 * AddFriends--
	 * GetAllFriends--
	 * DeleteFriends--
	 * GetDeleteFriends--
	 * GetNewFriends--
	 * Signin--
	 * (non-Javadoc)
	 * @see com.androidbook.requesthandle.HandleRequest#getResponseParam()
	 */
	public String getResponseParam() {
		return "";
	}

}
