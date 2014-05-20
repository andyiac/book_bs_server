package com.androidbook.requesthandle;

/**
 * 用来分配任务
 * @author Administrator
 *
 */
public class HandleRequestFactory {
	
	// AddTopic
	public static HandleRequest getHandleRequestInstance(String requestType){
		HandleRequest handleRequest = null;
		/* 
		 * try catch 捕获异常，防止程序出错
		 */
		try {
			//通过java的反射 new 对象
			handleRequest = (HandleRequest) Class.forName(
							"com.androidbook.requesthandle." 
							+ requestType)
							.newInstance();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return handleRequest;
	}
}
