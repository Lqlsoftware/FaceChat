package com.lqlsoftware.fuckchat.utils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import com.lqlsoftware.fuckchat.dao.DBManager;

public class msgUtil {
	
	public static void addMsg(String userId, String msg, String type) throws IOException, SQLException {
		Connection conn = DBManager.getConnection();
		PreparedStatement ps = null;
		String sql = "INSERT INTO msg (from_user,type,context,send_date)values(?,?,?,?)";
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, userId);
			ps.setString(2, type);
			ps.setString(3, msg);
			ps.setLong(4, new Date().getTime());
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ps.close();
			conn.close();
		}
	}
	
	public static void sendHistoryMsg(String userId) throws IOException, SQLException {
		Connection conn = DBManager.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "SELECT * FROM msg WHERE send_date>?";
		try {
			ps = conn.prepareStatement(sql);
			ps.setLong(1, new Date().getTime() - 180000);
			rs = ps.executeQuery();
			while (rs.next()) {
				SocketUtil.sendTo(rs.getString("context"), userId);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ps.close();
			conn.close();
		}
	}
	
}
