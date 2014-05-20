package com.androidbook.RequestParam;

public class RequestParam {

	/**
	 * 用户名
	 */
	private String userName;
	
	/**
	 * 密码
	 */
	private String password;
	
	/**
	 * 客户端随机字符串
	 */
	private String randomkey;
	
	/**
	 * 请求类型
	 */
	private String requestType;
	
	/**
	 * 请求参数
	 */
	private String[] params;

	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRandomkey() {
		return randomkey;
	}

	public void setRandomkey(String randomkey) {
		this.randomkey = randomkey;
	}

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public String[] getParams() {
		return params;
	}

	public void setParams(String[] params) {
		this.params = params;
	}
	
	@Override
	public RequestParam clone() {
		
		try {
			return (RequestParam) super.clone();
		} catch (CloneNotSupportedException e) {
			return new RequestParam();
		}
	}
}
