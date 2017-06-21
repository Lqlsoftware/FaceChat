package com.lqlsoftware.fuckchat.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBManager {

    // 数据库驱动
    private static String driver = "com.mysql.jdbc.Driver";

    // 数据库url
    private static String url = "jdbc:mysql://localhost:3306/fuckchat?useUnicode=true&characterEncoding=UTF-8";

    // 数据库用户名 root
    private static String userName = "root";

    // 数据库的用户密码
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

    public synchronized static Connection getConnection() {
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
