package com.lqlsoftware.fuckchat.servlet;

import com.alibaba.fastjson.JSONObject;
import com.lqlsoftware.fuckchat.utils.msgUtil;
import com.lqlsoftware.fuckchat.utils.userUtil;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *  @author Robin Lu
 */

@WebServlet(name = "LoginServlet" ,urlPatterns = "/login")
public class LoginServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		// 设置传输数据格式
		request.setCharacterEncoding("UTF-8");
		response.setHeader("content-type","text/html;charset=UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
		
		// 获取页面数据
		String username = request.getParameter("username");
		String password = request.getParameter("password");

        String token = userUtil.login(username, password);

        // 登陆失败
        if (token == null || token.equals("")) {
            response.getWriter().write(msgUtil.getErrorMsg("Username/Password not match.").toString());
            return;
        }

        JSONObject msg = new JSONObject();
        msg.put("code", 1);
        msg.put("token", token);
        msg.put("data", "");
        msg.put("errMsg", "");
        response.getWriter().write(msg.toString());
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
	    doPost(request, response);
    }
}
