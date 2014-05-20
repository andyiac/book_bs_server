package com.androidbook.RequestParam;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


/**
 * 请求数据分析
 * @author Administrator
 *
 */
public class RequestParamAnalysis {
	
	private static final RequestParamAnalysis analysis = new RequestParamAnalysis();
	private RequestParam requestParam;

	private RequestParamAnalysis() {
		this.requestParam = new RequestParam();
	}
	
	public static RequestParamAnalysis getInstance() {
		return RequestParamAnalysis.analysis;
	}
	
	/**
	 * 解析请求的数据
	 * @param json - 请求数据中的json字符串
	 * @return
	 */
	public RequestParam analysisRequestParam(String json) {
		
		RequestParam request = this.requestParam.clone();
		
		JSONObject jsonObject = JSONObject.fromObject(json);
		request.setUserName(jsonObject.getString("userName"));
		request.setPassword(jsonObject.getString("password"));
		request.setRandomkey(jsonObject.getString("randomKey") );
		request.setRequestType(jsonObject.getString("requestType"));
		JSONArray jsonArray = jsonObject.getJSONArray("params");
		String[] params = new String[jsonArray.size()];
		params = (String[]) jsonArray.toArray(params);
		request.setParams(params);
		
		return request;
	}
	
	
}
