package com.androidbook.requesthandle;

public class SoftWareUpdate implements HandleRequest {

	private final static String UPDATE_URL = "http://172.18.106.200:8080/book/book.apk";	
	
	private String responseParam = null;
	
	public String getRequestType() {
		return "SoftWareUpdate";
	}

	public int handleRequest(long uid, String password, String... param) {
		responseParam = UPDATE_URL;
		return 0;
	}

	public String getResponseParam() {
		return responseParam==null?"":responseParam;
	}

}
