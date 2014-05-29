package com.androidbook.requesthandle;

import com.androidbook.databasesinterface.DataBaseManager;
import com.androidbook.databasesinterface.PersonInfo;
import com.androidbook.utils.Utils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class GetPersonInfo implements HandleRequest{

	private String responseParam = null;

	public int handleRequest(long uid, String password, String... param) {
		Connection conn = null;
		int result = -1;
		JSONArray jsonArray = new JSONArray();
		try {
			conn = DataBaseManager.getInstance().getConnection();
			if(conn != null && param != null && param.length > 0){
				Statement stat = conn.createStatement();
				for(int i = 0; i< param.length; i++){
					ResultSet rs = PersonInfo.query(stat, PersonInfo.TABLE_NAME, Long.parseLong(Utils.IsEmpty(param[i])?"0":param[i]), "");
					if(rs.next()){
						jsonArray.add(buildParam(uid, rs));
					}
					rs.close();
				}
				this.setResponseParam(jsonArray.toString());
				result = 0;
				stat.close();
			}
		} catch (Exception e) {
            System.out.println("获取用户资料出错： " + e.toString());
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
	
	public void setResponseParam(String responseParam) {
		this.responseParam = responseParam;
	}
	
	private JSONObject buildParam(long uid, ResultSet rs) throws SQLException {
		String personName = rs.getString("person_name");
		String personPhoto = rs.getString("person_Photo");
		String personMobile = rs.getString("person_mobile");
		String personSex = rs.getString("person_sex");
		String personAddress = rs.getString("person_address");
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("personName", personName);
		jsonObject.put("personPhoto", personPhoto);
		jsonObject.put("personMobile", personMobile);
		jsonObject.put("personSex", personSex);
		jsonObject.put("personAddress", personAddress);
		jsonObject.put("UID", uid);
		
		return jsonObject;
		
		
	}
	
	public String getRequestType() {
		return "GetPersonInfo";
	}

	public String getResponseParam() {
		return responseParam==null ? "" : responseParam;
	}

}
