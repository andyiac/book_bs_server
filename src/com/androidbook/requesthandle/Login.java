package com.androidbook.requesthandle;

public class Login implements HandleRequest {

	private String responseParam = null;
	
	public String getRequestType() {
		return "Login";
	}

	public int handleRequest(long uid, String password, String... param) {

		int result = -1;
		
		GetPersonInfo gpi = new GetPersonInfo();
		result = gpi.handleRequest(uid, password, String.valueOf(uid));
		responseParam = gpi.getResponseParam();
		
		return result;
	}

	public String getResponseParam() {
		return responseParam==null?"":responseParam;
	}

}
