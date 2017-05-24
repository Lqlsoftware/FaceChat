package com.lqlsoftware.fuckchat.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

	/**
	 * java��mySQL������
	 * 
	 * @author Administrator
	 * 
	 */
	public class DBManager {
	    // ���ݿ�����
	    private static String driver = "com.mysql.jdbc.Driver";  
	    
	    // ���ݿ�url
	    private static String url = "jdbc:mysql://localhost:3306/fuckchat";
	    
		// ���ݿ��û��� root
		private static String userName = "root";
		
		// ���ݿ���û�����
		private static String userPassword = "123456";

		public static String getDriver() {
			return driver;
		}

		public static void setDriver(String driver) {
			DBManager.driver = driver;
		}

		public static String getUrl() {
			return url;
		}

		public static void setUrl(String url) {
			DBManager.url = url;
		}

		public static String getUserName() {
			return userName;
		}

		public static void setUserName(String userName) {
			DBManager.userName = userName;
		}

		public static String getUserPassword() {
			return userPassword;
		}

		public static void setUserPassword(String userPassword) {
			DBManager.userPassword = userPassword;
		}

		public synchronized static Connection getConnection() throws IOException {
			try {
				Class.forName(driver);
			} catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			Connection conn = null;
			try {
				conn = DriverManager.getConnection(url, userName, userPassword);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return conn;
		}
	}
