package com.androidbook.requesthandle;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;

import com.androidbook.databasesinterface.DataBaseManager;
import com.androidbook.databasesinterface.Person;

public class GetAllPeople implements HandleRequest{

	private String responseParam = null;
	
	public void setResponseParam(String responseParam) {
		this.responseParam = responseParam;
	}

	public int handleRequest(long uid, String password, String... param) {
		Connection conn = null;
		int result = -1;
		JSONArray jsonArray = new JSONArray();
		List<String> personUID = new ArrayList<String>();
		String[] tempUID = null;
		try {
			conn = DataBaseManager.getInstance().getConnection();
			if(conn != null){
				Statement stat = conn.createStatement();
				ResultSet rs = Person.query2(stat, Person.TABLE_NAME, Person.UID);
				while(rs.next()){
					String person = rs.getString(Person.UID);
					personUID.add(person);
				}
				if(personUID != null && personUID.size() > 0){
					tempUID = new String[personUID.size()];
					for(int i=0; i<personUID.size(); i++){
						tempUID[i] = personUID.get(i);
					}
					GetPersonInfo gpi = new GetPersonInfo();
					int ret = gpi.handleRequest(uid, password, tempUID);
					if(ret == 0){
						String personinfojsonarray = gpi.getResponseParam();
						JSONArray j = JSONArray.fromObject(personinfojsonarray);
						jsonArray.addAll(j);
					}
					result = 0;
				}
				this.setResponseParam(jsonArray.toString());
				rs.close();
				stat.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("获取全人出错： " + e.toString());
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
		return "GetAllPeople";
	}

	public String getResponseParam() {
		return responseParam==null ? "" : responseParam;
	}

}
