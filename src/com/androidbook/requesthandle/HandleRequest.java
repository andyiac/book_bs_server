package com.androidbook.requesthandle;

public interface HandleRequest {
	
	/**
	 * 处理请求的接口
	 * @param name
	 * @param password
	 * @param param
	 * @return
	 */
	public int handleRequest(long uid, String password, String... param);
	
	/**
	 * 获得请求类型
	 * @return
	 */
	public String getRequestType();
	
	/**
	 * 返回响应的数据
	 * @return
	 */
	public String getResponseParam();
}
