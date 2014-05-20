package com.androidbook.ResponseParam;

public class ResponseParam {

	/**
	 * 请求的结果码
	 */
	private int result;
	
	/**
	 * 请求的类型
	 */
	private String requestType;
	
	/**
	 * 请求的结果内容
	 */
	private String[] content;

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public String[] getContent() {
		return content;
	}

	public void setContent(String[] content) {
		this.content = content;
	}
	
}
