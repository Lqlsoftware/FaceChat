package com.lqlsoftware.fuckchat.utils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.lqlsoftware.fuckchat.dao.DBManager;

public class userUtil {

    public static String login(String username, String password) throws IOException {
        if (username == null || password == null)
            return null;

        String id = null;
        Connection conn = DBManager.getConnection();
		PreparedStatement ps = null;
        String sql = "SELECT * FROM user WHERE user_name=? AND password=?";
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, username);
			ps.setString(2, password);
			ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                return null;
            }
            id = rs.getString("id");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		TokenManager TMR = new TokenManager();
		TokenModel TM = TMR.createToken(id);
        return TM.getAuthentication();
    }


}