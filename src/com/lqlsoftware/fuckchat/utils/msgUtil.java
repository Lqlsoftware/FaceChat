package com.lqlsoftware.fuckchat.utils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lqlsoftware.fuckchat.dao.DBManager;

public class msgUtil {

	public static String getMsg(String userId, String context, String type) {
        JSONObject msg = new JSONObject();
        JSONObject data = new JSONObject();
        data.put("from", userId);
        data.put("type", type);
        data.put("context", context);
        data.put("timestamp", new Date().getTime());
        msg.put("data", data);
        msg.put("code", 1);
        msg.put("errMsg", "");
        return msg.toString();
    }

    public static String getSysMsg(int code, String context, String errMsg) {
        JSONObject msg = new JSONObject();
        JSONObject data = new JSONObject();
        data.put("from", "system");
        data.put("type", "text");
        if (code == 2)
        	data.put("context", JSONObject.parse(context));
        else
        	data.put("context", context);
        data.put("timestamp", new Date().getTime());
        msg.put("data", data);
        msg.put("code", code);
        msg.put("errMsg", errMsg);
        return msg.toString();
    }

	
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
				SocketUtil.sendTo(msgUtil.getMsg(rs.getString("from_user"), rs.getString("context"), rs.getString("type")), userId);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ps.close();
			conn.close();
		}
	}

	public static JSONObject getErrorMsg(String errMsg) {
        JSONObject msg = new JSONObject();
        msg.put("code", -1);
        msg.put("errMsg", errMsg);
        return msg;
    }
}
