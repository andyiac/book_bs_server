package com.androidbook.requesthandle;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

import com.androidbook.databasesinterface.DataBaseManager;
import com.androidbook.databasesinterface.Friend;
import com.androidbook.utils.Utils;

/**
 * 重复添加的好友不再添加
 * 
 * @author Administrator
 *
 */
public class AddFriends implements HandleRequest{

	private String responseParam = null;
	
	public int handleRequest(long uid, String password, String... param) {
		Connection conn = null;
		int result = -1;
		try {
			conn = DataBaseManager.getInstance().getConnection();
			if(conn != null){
				Statement stat = conn.createStatement();
				addFriends(uid, stat, param);
				for(int i = 0; i < param.length; i++){
					addFriends(Long.parseLong(param[i]), stat, String.valueOf(uid));
				}
				result = 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("插入好友出错： " + e.toString());
		} finally {
			try {
				conn.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		return result;
		
	}

	/**
	 * 
	 * @param uid
	 * @param stat
	 * @param param 要添加的好友
	 * @throws SQLException
	 */
	private void addFriends(long uid, Statement stat, String... param)
			throws SQLException {
		ResultSet rs = Friend.query(stat, Friend.TABLE_NAME, uid, "");
		if(rs.next()){
			//friendUID字段
			String friendUIDs = rs.getString(3);
			String newUIDs = rs.getString(4);
			String deleteUIDs = rs.getString(5);
			update(stat, friendUIDs, deleteUIDs, Friend.FRIEND_UID, uid, param);
			update(stat, newUIDs, null, Friend.NEW_UID, uid, param);
		}
		rs.close();
	}

	/**
	 * 要删除删除字段中的对应的好友id
	 * 情况是添加删除添加
	 * @param stat
	 * @param friendUIDs 原来的好友
	 * @param what
	 * @param uid
	 * @param param 要添加的好友
	 * @throws SQLException
	 */
	private void update(Statement stat, String friendUIDs, 
			String deleteUIDs, String what, 
			long uid, String... param) throws SQLException {
		StringBuffer sb = new StringBuffer();
		this.delete(uid, stat, Friend.DETELE_UID, param.clone(), deleteUIDs);
		for(int i = 0; i < param.length; i++){
			if(i==0 && Utils.IsEmpty(friendUIDs)){
				sb.append(param[i]);
			}else{
				if(i==0){
					sb.append(friendUIDs);
				}
				if(!Utils.IsEmpty(friendUIDs) && friendUIDs.contains(param[i])){
					continue;
				}
				sb.append("#").append(param[i]);
			}
		}
		HashMap<String, Object> values = new HashMap<String, Object>();
		values.put(what, Utils.pad(sb.toString()));
		Friend.update(stat, Friend.TABLE_NAME, uid, values, what);
	}
	/**
	 * 
	 * @param uid
	 * @param stat
	 * @param what
	 * @param friendUID 要添加的好友
	 * @param deleteUID 删除字段中的好友
	 * @throws SQLException
	 */
	public void delete(long uid, Statement stat, String what,
			String[] friendUID, String deleteUID) throws SQLException{
		
		if(Utils.IsEmpty(deleteUID)){
			return;
		}
		
		String[] deletes = deleteUID.split("#");
		LinkedList<String> temp = new LinkedList<String>();
		for(String str : deletes){
			if(!Arrays.toString(friendUID).contains(str)){
				temp.add(str);
			}			
		}
		DeleteFriends gdf = new DeleteFriends();
		gdf.updateString(uid, stat, what, temp);

	}
	
	public String getRequestType() {
		return "AddFriends";
	}

	public String getResponseParam() {
		return responseParam==null?"":responseParam;
	}

}
