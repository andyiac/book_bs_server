package com.androidbook.ResponseParam;

import net.sf.json.JSONObject;

public class BulidResponseParam {

	private static BulidResponseParam instance = new BulidResponseParam();
	
	private BulidResponseParam(){
		
	}
	
	public static BulidResponseParam getInstance(){
		return instance;
	}
	
	public String bulidParam(int result, String requsetType, String params){
		
		JSONObject param = new JSONObject();
		param.put("result", result);
		param.put("requsetType", requsetType);
		param.put("content", params);
		
		return param.toString();
		
	}
	
}
