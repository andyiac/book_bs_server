package com.androidbook.requesthandle;

import com.androidbook.RequestParam.RequestParam;
import com.androidbook.RequestParam.RequestParamAnalysis;
import com.androidbook.ResponseParam.BulidResponseParam;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class SigninServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=utf-8");
		
		String json = request.getParameter("requestParam");
		json = new String(json.getBytes("ISO8859_1"), "utf-8");
		
		if(json != null){
			RequestParam requestParam = RequestParamAnalysis.getInstance().analysisRequestParam(json);
			HandleRequest handle = HandleRequestFactory.getHandleRequestInstance(requestParam.getRequestType());
			int result = handle.handleRequest(0L, "",requestParam.getParams());
			
			String responseParam = "";
			responseParam= BulidResponseParam.getInstance().bulidParam(result, requestParam.getRequestType(), "");
			
			response.setContentType("text/html");
			response.setCharacterEncoding( "utf-8" );
			PrintWriter out = response.getWriter();
			out.println(responseParam);
			out.flush();
			out.close();
			
		}

	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}
	
}
