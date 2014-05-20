package com.androidbook.authenticate;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.androidbook.databasesinterface.DataBaseManager;

/**
 * 认证用户
 * 
 * @author Administrator
 * 
 */
public class AuthenticateUser {

	public static final int CORRECT = 0;
	public static final int ERROR = -1;
	
	private static boolean D= true;

	private static AuthenticateUser authenticateUser = new AuthenticateUser();

	private AuthenticateUser() {

	}

	public synchronized static AuthenticateUser getInstance() {
		return authenticateUser;
	}

	/**
	 * 认证合法用户<br>
	 * 认证用户查询的都是person表
	 * 
	 * @return 0 合法<br>
	 *         -1 不合法
	 */
	public synchronized int IsIllegalUser(String name, String password) {

		System.out.println("Authenticateuser ------name+password----- " + name
				+ password);
		Connection conn = DataBaseManager.getInstance().getConnection();
		int result = ERROR;
		try {
			if (conn != null) {
				Statement stat = conn.createStatement();
				String sql = "select * from person where " + "UID=" + name
						+ " and " + "person_password=" + password;
				
				if(D)System.out.println("Authenticate user .java sql>>>"+sql);
				
				
				ResultSet rs = stat.executeQuery(sql);
				if (rs.next()) {
					sql = "update personinfo set status=0 where UID=" + name;
					stat.execute(sql);
					result = CORRECT;
				}
				stat.close();
				rs.close();
			}
		} catch (Exception e) {
			System.out.println("认证出错： " + e.toString());
		} finally {
			try {
				conn.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}

		System.out
				.println("----------AuthenticateUser--" +
						"-if == 0 connecte datebase success " +
						"and the user is correct ----->"
						+ result);
		return result;
	}
}
