package com.lqlsoftware.fuckchat.utils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class userUtil {
    private userName;

    private password;

    public static user login() {
        if (userName == null || password == null)
            return null;
        Connection conn = DBManager.getConnection();
		PreparedStatement ps = null;
        String sql = "SELECT * FROM user WHERE userName=? AND password=?";
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, userName);
			ps.setString(2, password);
			ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                return null;
            }
		} catch (SQLException e) {
			e.printStackTrace();
		}
        return null;
    }
}