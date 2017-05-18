package com.lqlsoftware.demo.servlet;

import java.io.IOException;
import java.util.Date;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.websocket.Session;

import com.lqlsoftware.demo.dao.SessionUtil;

@WebServlet(name = "ImgUploadServlet" ,urlPatterns = "/imgUpload")
@MultipartConfig
public class ImgUploadServlet extends HttpServlet {

	/**
	 *  @author Robin Lu
	 */
	private static final long serialVersionUID = 1L;

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		// ���ô������ݸ�ʽ
		request.setCharacterEncoding("UTF-8");
		response.setHeader("content-type","text/html;charset=UTF-8");
		
		// ��ȡҳ������
		String userId = request.getParameter("userId");
		
		String root = request.getServletContext().getRealPath("/");
		String path = "";
		
		// ��ȡͼƬ
		Part part = request.getPart("img");
		if ( part.getSize() > 0 ) {
			// ���������
			path = root  + "/imgs/" + new Date().getTime() + "." + part.getContentType();
			part.write(path);
		}
		for (Entry<String, Session> i : SessionUtil.clients.entrySet()) {
			if (!i.getKey().equals(userId)) {
				i.getValue().getBasicRemote().sendText(userId + ":img" + path);
			}
		}
		response.getWriter().write(path);
		return;
	}
}
