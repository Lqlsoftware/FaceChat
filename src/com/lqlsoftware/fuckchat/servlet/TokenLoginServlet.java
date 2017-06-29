package com.lqlsoftware.fuckchat.servlet;

import com.alibaba.fastjson.JSONObject;
import com.lqlsoftware.fuckchat.utils.TokenManager;
import com.lqlsoftware.fuckchat.utils.TokenModel;
import com.lqlsoftware.fuckchat.utils.userUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "TokenLoginServlet" ,urlPatterns = "/tokenLogin")
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
		String token = request.getParameter("token");

        // 登陆失败
        if (token == null || token.equals("")) {
            JSONObject msg = new JSONObject();
            msg.put("code", -1);
            msg.put("errMsg", "Username/Password not match.");
            response.getWriter().write(msg.toString());
            return;
        }

        TokenManager TMR = new TokenManager();
        TokenModel TM = TMR.getToken(token);
        if (!TMR.checkToken(TM)) {
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