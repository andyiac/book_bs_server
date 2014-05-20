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
import com.androidbook.databasesinterface.Letter;
import com.androidbook.databasesinterface.UserLetter;
import com.androidbook.utils.Utils;

public class DeletePrivateLetter implements HandleRequest{

	private String responseParam = null;
	
	public int handleRequest(long uid, String password, String... param) {
		Connection conn = null;
		int result = -1;
		
		try {
			conn = DataBaseManager.getInstance().getConnection();
			if(conn != null){
				Statement stat = conn.createStatement();
				deleteLetter(uid, stat, param);
				result = 0;
				stat.close();
			}
		} catch (Exception e) {
			System.out.println("删除私信出错： " + e.toString());
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

	private void deleteLetter(long uid, Statement stat, String... param)
			throws SQLException {
		ResultSet rs2 = UserLetter.query(stat, UserLetter.TABLE_NAME, uid, "");
		if(rs2.next()){
			String letters = rs2.getString(3);
			String news = rs2.getString(4);
			String deletes = rs2.getString(5);
			
			update(uid, stat, letters, UserLetter.PRIVATE_LETTER_ID, param);
			update(uid, stat, news, UserLetter.NEW_LETTER_ID, param);
			updateDelete(uid, stat, deletes, UserLetter.DEL_LETTER_ID, param);
			
		}
		rs2.close();
		//到Letter中查看私信标志位privateLetter_delete是否是真，是真的话删除该私信
		String sql = "select * from letter where PrivateLetter_ID=" + param[0];
		ResultSet rs = stat.executeQuery(sql);
		if(rs.next()){
			boolean deleteable = rs.getBoolean(Letter.PrivateLetter_delete);
			if(deleteable){
				//删除
				String sql2 = "delete from letter where PrivateLetter_ID=" + param[0];
				stat.execute(sql2);
			} else {
				//更新该字段
				String sql3 = "update letter set PrivateLetter_delete=-1 where PrivateLetter_ID=" + param[0];
				stat.execute(sql3);
			}
		}
		rs.close();
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
		HashMap<String, Object> values = new HashMap<String, Object>();
		values.put(what, Utils.pad(sb.toString()));
		UserLetter.update(stat, UserLetter.TABLE_NAME, uid, values, what);
	}

	public String getRequestType() {
		return "DeletePrivateLetter";
	}

	public String getResponseParam() {
		return responseParam==null?"":responseParam;
	}

}
