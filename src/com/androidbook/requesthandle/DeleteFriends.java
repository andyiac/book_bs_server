package com.androidbook.requesthandle;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.androidbook.databasesinterface.DataBaseManager;
import com.androidbook.databasesinterface.Friend;
import com.androidbook.utils.Utils;

public class DeleteFriends implements HandleRequest{

	private String responseParam = null;
	
	public int handleRequest(long uid, String password, String... param) {
		Connection conn = null;
		int result = -1;
		
		try {
			conn = DataBaseManager.getInstance().getConnection();
			if(conn != null){
				Statement stat = conn.createStatement();
				deleteFriends(uid, stat, param);
				for(int i = 0; i<param.length; i++){
					deleteFriends(Long.parseLong(param[i]), stat, String.valueOf(uid));
				}
				result = 0;
				stat.close();
			}
		} catch (Exception e) {
			System.out.println(" 删除好友出错： " + e.toString());
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		return result;
		
	}

	private void deleteFriends(long uid, Statement stat, String... param)
			throws SQLException {
//		String friendsUIDSql = "select * from friend where UID=" + uid;
//		ResultSet rs2 = stat.executeQuery(friendsUIDSql);
		ResultSet rs2 = Friend.query(stat, Friend.TABLE_NAME, uid, "");
		if(rs2.next()){
			String friends = rs2.getString(3);
			String news = rs2.getString(4);
			String deletes = rs2.getString(5);
			
			update(uid, stat, friends,Friend.FRIEND_UID, param);
			update(uid, stat, news, Friend.NEW_UID, param);
			updateDelete(uid, stat, deletes,Friend.DETELE_UID, param);
			
		}
		rs2.close();
	}

	public void updateDelete(long uid, Statement stat, String deletes, String what, String... param) throws SQLException {
		LinkedList<String> friendsUID = getListFromDB(deletes);
		List<String> friendList2 = Arrays.asList(param);
		for(String str : friendList2){
			if(!friendsUID.contains(str)){
				friendsUID.add(str);
			}
		}
		updateString(uid, stat, what, friendsUID);
	}
	
	private void update(long uid, Statement stat, String friends, String what, String... param) throws SQLException {
		LinkedList<String> friendsUID = getListFromDB(friends);
		List<String> friendList2 = Arrays.asList(param);
		for(String str : friendList2){
			if(friendsUID.contains(str)){
				friendsUID.remove(str);
			}
		}
		updateString(uid, stat, what, friendsUID);
	}

	public LinkedList<String> getListFromDB(String friends) {
		LinkedList<String> friendsUID = new LinkedList<String>();
		if(!Utils.IsEmpty(friends)){
			List<String> friendList = Arrays.asList(friends.split("#"));
			for(String str : friendList){
				friendsUID.add(str);
			}
		}
		return friendsUID;
	}

	public void updateString(long uid, Statement stat,
			String what, LinkedList<String> friendsUID) throws SQLException {
		StringBuffer sb = new StringBuffer();
		for(int i = 0; i < friendsUID.size(); i++){
			if(i==0){
				sb.append(friendsUID.get(i));
			}else{
				sb.append("#").append(friendsUID.get(i));
			}
		}
//		String insertSql = "update friend set "+ what +"='"+ sb.toString()+"'" + " where UID=" + uid;
//		stat.execute(insertSql);
		HashMap<String, Object> values = new HashMap<String, Object>();
		values.put(what, Utils.pad(sb.toString()));
		Friend.update(stat, Friend.TABLE_NAME, uid, values, what);
	}

	public String getRequestType() {
		return "DeleteFriends";
	}

	public String getResponseParam() {
		return responseParam==null?"":responseParam;
	}

}
