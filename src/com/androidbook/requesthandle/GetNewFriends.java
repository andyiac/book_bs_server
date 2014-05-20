package com.androidbook.requesthandle;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.androidbook.databasesinterface.DataBaseManager;
import com.androidbook.databasesinterface.Friend;

import net.sf.json.JSONArray;

public class GetNewFriends implements HandleRequest{

	
	private String responseParam = null;
	public int handleRequest(long uid, String password, String... param) {
		
		Connection conn = null;
		int result = -1;
		JSONArray jsonArray = new JSONArray();
		try {
			conn = DataBaseManager.getInstance().getConnection();
			if(conn != null){
				Statement stat = conn.createStatement();
				ResultSet rs = Friend.query(stat, Friend.TABLE_NAME, uid, Friend.NEW_UID);
				if(rs.next()){
					String[] friends =rs.getString(Friend.NEW_UID).split("#");
					if(friends.length > 0){
						GetPersonInfo gpi = new GetPersonInfo();
//						for(String str : friends){
//							System.out.println(str);
//						}
						//	long fid = Long.parseLong(str);
						int res = gpi.handleRequest(uid, "", friends);
						
						if(res == 0){
							String personinfojsonarray = gpi.getResponseParam();
							JSONArray j = JSONArray.fromObject(personinfojsonarray);
							jsonArray.addAll(j);
							//jsonArray.addAll(JSONArray.fromObject(gpi.getResponseParam()));
						}
						//}
					}
				
				}
				Friend.blankField(uid, stat,Friend.TABLE_NAME, Friend.NEW_UID);
				this.responseParam = jsonArray.toString();
				rs.close();
				stat.close();
				result = 0;
			}
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println("获得新添加好友出错： " + e.toString());
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
		
		return "GetNewFriends";
	}

	public String getResponseParam() {
		// TODO Auto-generated method stub
		return responseParam==null ? "" : responseParam;
	}

}
