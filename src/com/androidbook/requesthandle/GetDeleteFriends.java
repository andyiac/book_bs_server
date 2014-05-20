package com.androidbook.requesthandle;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import net.sf.json.JSONArray;

import com.androidbook.databasesinterface.DataBaseManager;
import com.androidbook.databasesinterface.Friend;
import com.androidbook.utils.Utils;

public class GetDeleteFriends implements HandleRequest{

	private String responseParam = null;
	
	public void setResponseParam(String responseParam) {
		this.responseParam = responseParam;
	}

	public int handleRequest(long uid, String password, String... param) {
		Connection conn = null;
		int result = -1;
		JSONArray jsonArray = new JSONArray();
		try {
			conn = DataBaseManager.getInstance().getConnection();
			if(conn != null){
				Statement stat = conn.createStatement();
				ResultSet rs = Friend.query(stat, Friend.TABLE_NAME, uid, Friend.DETELE_UID);
				while(rs.next()){
					String[] friends = rs.getString(Friend.DETELE_UID).split("#");
					int fCount = friends.length;
					if(fCount > 0){
						for(int i=0; i<fCount; i++){
							GetPersonInfo gpi = new GetPersonInfo();
							int ret = gpi.handleRequest(Long.parseLong(Utils.IsEmpty(friends[0])?"0":friends[0]), password, param);
							if(ret == 0){
								String personinfojsonarray = gpi.getResponseParam();
								JSONArray j = JSONArray.fromObject(personinfojsonarray);
								jsonArray.addAll(j);
							}
						}
					}
					result = 0;
				}
				
				Friend.blankField(uid, stat,Friend.TABLE_NAME, Friend.DETELE_UID);
				this.setResponseParam(jsonArray.toString());
				rs.close();
				stat.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("获取好友出错： " + e.toString());
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
		return "GetDeleteFriends";
	}

	public String getResponseParam() {
		return responseParam==null ? "" : responseParam;
	}

}
