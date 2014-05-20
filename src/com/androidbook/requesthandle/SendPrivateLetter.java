package com.androidbook.requesthandle;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import com.androidbook.authenticate.AuthenticatePerson;
import com.androidbook.databasesinterface.DataBaseManager;
import com.androidbook.databasesinterface.Letter;
import com.androidbook.databasesinterface.UserLetter;
import com.androidbook.utils.Utils;

public class SendPrivateLetter implements HandleRequest{

	private String responseParam = null;
	
	public int handleRequest(long uid, String password, String... param) {
		Connection conn = null;
		int result = -1;
		
		long privateLetterUID = Long.parseLong(param[0]);
		String privateLetterContent = param[1];
		int privateLetterTime = Integer.parseInt(param[2]);
		String privateLetterName = param[3];
		String privateLetterPhoto = param[4];
		long privateLetterID = System.currentTimeMillis();
		//私信读情况
		boolean status = false;
		boolean delete = false;
		
		//先认证一下私信接收者是否合法
		result =  AuthenticatePerson.getInstance().IsIllegalUser(param[0]);
		if(result == -1){
			return result;
		}
		
		try {
			conn = DataBaseManager.getInstance().getConnection();
			if(conn != null){
				Statement stat = conn.createStatement();
				
				insertPrivate(uid, privateLetterUID, privateLetterContent,
						privateLetterTime, privateLetterName,
						privateLetterPhoto, privateLetterID, status, delete, stat);
				
				responseParam = String.valueOf(privateLetterID);
				addPrivate(uid, stat, responseParam);
				addPrivate(privateLetterUID, stat, responseParam);
				
				result = 0;
				
				stat.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("发送私信出错： " + e.toString());
		} finally {
			try {
				conn.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		return result;
		
	}

	public void insertPrivate(long uid, long privateLetterUID,
			String privateLetterContent, int privateLetterTime,
			String privateLetterName, String privateLetterPhoto,
			long privateLetterID, boolean status, boolean delete, Statement stat)
			throws SQLException {
		
		HashMap<String, Object> values = new HashMap<String, Object>();
		values.put(Letter.ID, Letter.getMaxID(stat, Letter.TABLE_NAME));
		values.put(Letter.UID, uid);
		values.put(Letter.PrivateLetter_UID, privateLetterUID);
		values.put(Letter.PrivateLetter_ID, privateLetterID);
		values.put(Letter.PrivateLetter_Content, Utils.pad(privateLetterContent));
		values.put(Letter.PrivateLetter_Time, privateLetterTime);
		values.put(Letter.PrivateLetter_Name, Utils.pad(privateLetterName));
		values.put(Letter.PrivateLetter_Photo, Utils.pad(privateLetterPhoto));
		values.put(Letter.PrivateLetter_isSend, status);
		values.put(Letter.PrivateLetter_delete, delete);
		Letter.insert(stat, Letter.TABLE_NAME, values);
	}

	public ResultSet addPrivate(long uid, Statement stat, String... param)
			throws SQLException {
		ResultSet rs = UserLetter.query(stat, UserLetter.TABLE_NAME, uid, "");
		if(rs.next()){
			String plid = rs.getString(3);
			String nlid = rs.getString(4);

			update(uid, stat, plid, UserLetter.PRIVATE_LETTER_ID, param);
			update(uid, stat, nlid, UserLetter.NEW_LETTER_ID, param);
		}
		return rs;
	}

	public void update(long uid, Statement stat, String plid, String what, String... param)
			throws SQLException {
		StringBuffer sb = new StringBuffer();
		for(int i = 0; i < param.length; i++){
			if(i==0 && (Utils.IsEmpty(plid))){
				sb.append(param[i]);
			}else{
				if(i==0){
					sb.append(plid);
				}
				sb.append("#").append(param[i]);
			}
		}
		HashMap<String, Object> values = new HashMap<String, Object>();
		values.put(what,Utils.pad(sb.toString()));
		UserLetter.update(stat, UserLetter.TABLE_NAME, uid, values, what);
	}

	public String getRequestType() {
		return "SendPrivateLetter";
	}

	public String getResponseParam() {
		return responseParam==null?"":responseParam;
	}

}
