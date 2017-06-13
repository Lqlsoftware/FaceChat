package com.lqlsoftware.fuckchat.servlet;

import com.alibaba.fastjson.JSONObject;
import com.lqlsoftware.fuckchat.utils.userUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "LoginServlet" ,urlPatterns = "/login")
public class TokenLoginServlet extends HttpServlet {

	/**
	 *  @author Robin Lu
	 */
	private static final long serialVersionUID = 1L;

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		// 设置传输数据格式
		request.setCharacterEncoding("UTF-8");
		response.setHeader("content-type","text/html;charset=UTF-8");
		
		// 获取页面数据
		String username = request.getParameter("username");
		String password = request.getParameter("password");

        String token = userUtil.login();

        // 登陆失败
        if (token == null || token.equals("")) {
            JSONObject msg = new JSONObject();
            msg.put("code", -1);
            msg.put("errMsg", "Username/Password not match.");
            response.getWriter().write(msg.toString());
            return;
        }

        JSONObject msg = new JSONObject();
        JSONObject data = new JSONObject();
        data.put("token", token);
        msg.put("code", 1);
        msg.put("data", data);
        msg.put("errMsg", "");
        response.getWriter().write(msg.toString());
	}
}
