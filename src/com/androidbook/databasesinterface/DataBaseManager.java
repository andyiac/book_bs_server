package com.androidbook.databasesinterface;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * 
 * 创建数据库连接池
 * 
 * @author Administrator
 *
 */
public class DataBaseManager {
	
	// 数据库连接驱动
	private  String driverClass = "com.mysql.jdbc.Driver";
	// mysql数据库地址
//	private  String jdbcUrl = "jdbc:mysql://localhost:3306/first";
	private  String jdbcUrl = "jdbc:mysql://localhost:3307/jwgl";
	// 用户名
	private  String user = "root";
	// 密码
	private  String password = "littleOctopus";

	public String getDriverClass() {
		return driverClass;
	}

	public void setDriverClass(String driverClass) {
		this.driverClass = driverClass;
	}

	public String getJdbcUrl() {
		return jdbcUrl;
	}

	public void setJdbcUrl(String jdbcUrl) {
		this.jdbcUrl = jdbcUrl;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	private DataBaseManager(){
		
		cpds = new ComboPooledDataSource();
		// 连接数据库
		cpds.setUser(user);
		cpds.setPassword(password);
		cpds.setJdbcUrl(jdbcUrl);
		try {
			cpds.setDriverClass(driverClass);
		} catch (PropertyVetoException e) {
			e.printStackTrace();
			System.out.println("数据库连接错误");
		}
		cpds.setInitialPoolSize(20);
		cpds.setMaxPoolSize(1000);
		cpds.setMinPoolSize(10);
	};
	
	private static DataBaseManager pool = null;
	
	private static ComboPooledDataSource cpds = null;
	
	/**
	 * 单例模式
	 * @return
	 */
	public synchronized static DataBaseManager getInstance(){
		if(pool == null){
			return pool = new DataBaseManager();
		}
		return pool;
	}
	
	/**
	 * 创建数据库连接
	 */
	public synchronized Connection getConnection(){
		try {
			return cpds.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
