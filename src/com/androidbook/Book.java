package com.androidbook;

import com.androidbook.RequestParam.RequestParam;
import com.androidbook.RequestParam.RequestParamAnalysis;
import com.androidbook.ResponseParam.BulidResponseParam;
import com.androidbook.authenticate.AuthenticateUser;
import com.androidbook.requesthandle.HandleRequest;
import com.androidbook.requesthandle.HandleRequestFactory;
import com.androidbook.utils.Utils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class Book extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final String TAG = "Book.java";
	private static final boolean D = true;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			String json = request.getParameter("requestParam");
			json = new String(json.getBytes("ISO8859_1"), "utf-8");
			if (json != null) {
				// 解析请求的数据内容
				RequestParam requestParam = RequestParamAnalysis.getInstance()
						.analysisRequestParam(json);

				// 获得数据
				String UID = requestParam.getUserName();
				String password = requestParam.getPassword();
				String requestType = requestParam.getRequestType();
				String[] params = requestParam.getParams();

				System.out.println("-----Book.java--- UID>>" + UID);
				System.out.println("-------password----->>" + password);
				System.out.println("---------requestType>>" + requestType);
				System.out.println("--------params--->>>>>" + params);

				// 认证用户
				int authenticateUser = -1;
				// if (requestType != "Signin") {
				if ("Signin".endsWith(requestType)) {
					authenticateUser = AuthenticateUser.getInstance()
							.IsIllegalUser(String.valueOf(UID), password);
				}
				String responseParam = "";
				// if (requestType != "Signin" && authenticateUser == -1) {
				if ("Signin".endsWith(requestType) && authenticateUser == -1) {
					// 数据库中没有此用户则插入新记录
					responseParam = BulidResponseParam.getInstance()
							.bulidParam(authenticateUser, requestType, "");
					HandleRequest handle = HandleRequestFactory
							.getHandleRequestInstance(requestType);

					// String param = handle.getResponseParam();
					int result = handle.handleRequest(0L, null, params);
					String param = handle.getResponseParam();
					responseParam = BulidResponseParam.getInstance()
							.bulidParam(result, requestType, param);

				}

				// 登录请求
				if ("Login".endsWith(requestType)) {
					// 获得处理请求的具体文件
					HandleRequest handle = HandleRequestFactory
							.getHandleRequestInstance(requestType);
					int result = handle.handleRequest(
							Long.parseLong(Utils.IsEmpty(UID) ? "0" : UID),
							password, params);
					String param = handle.getResponseParam();
					responseParam = BulidResponseParam.getInstance()
							.bulidParam(result, requestType, param);

				}

				// ==========================
				// GetAllPeople 这里本来不应该放业务逻辑
				// if ("GetAllPeople".endsWith(requestType)) {
				//
				// }
				else {
					// 这个哥们不是能处理所有的请求吧
					// 获得处理请求的具体文件
					HandleRequest handle = HandleRequestFactory
							.getHandleRequestInstance(requestType);
					int result = handle.handleRequest(
							Long.parseLong(Utils.IsEmpty(UID) ? "0" : UID),
							password, params);
					String param = handle.getResponseParam();
					responseParam = BulidResponseParam.getInstance()
							.bulidParam(result, requestType, param);
				}

				response.setContentType("text/html");
				response.setCharacterEncoding("utf-8");
				System.out.println("输出 ： " + responseParam.toString());

                //利用PrintWriter对象的方法将数据发送给客户端
				PrintWriter out = response.getWriter();
				out.println(responseParam);
				out.flush();
				out.close();

			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("book errer:" + e.toString());
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);//这条语句的作用是，当客户端发送POST请求时，调用doGet()方法进行处理
	}

}
