package com.androidbook.requesthandle;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import net.sf.json.JSONArray;

import com.androidbook.databasesinterface.DataBaseManager;
import com.androidbook.databasesinterface.UserLetter;

/**
 * 通过两个字段来判断，一个是UID，另一个是privateLetterUID，一个作为发送者，另一个作为接受者
 * @author Administrator
 *
 */
public class GetAllPrivateLetter implements HandleRequest{

	private String responseParam = null;

	public int handleRequest(long uid, String password, String... param) {
		Connection conn = null;
		int result = -1;
		
		JSONArray jsonArray = new JSONArray();
		try {
			conn = DataBaseManager.getInstance().getConnection();
			if(conn != null){
				Statement stat = conn.createStatement();
				ResultSet rs = UserLetter.query(stat, UserLetter.TABLE_NAME, uid, UserLetter.PRIVATE_LETTER_ID);
				while(rs.next()){
					String[] letters = rs.getString(UserLetter.PRIVATE_LETTER_ID).split("#");
					GetPrivateLetter gpl = new GetPrivateLetter();
					int res = gpl.handleRequest(uid, password, letters);
					if(res == 0){
						jsonArray.addAll(JSONArray.fromObject(gpl.getResponseParam()));
					}
				}
				this.setResponseParam(jsonArray.toString());
				result = 0;
				rs.close();
				stat.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("获取私信出错： " + e.toString());
		} finally {
			try {
				conn.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		return result;
	}
	
	public void setResponseParam(String responseParam) {
		this.responseParam = responseParam;
	}
	
	public String getRequestType() {
		return "GetAllPrivateLetter";
	}

	public String getResponseParam() {
		return responseParam==null ? "" : responseParam;
	}

}
