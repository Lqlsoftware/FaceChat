package com.lqlsoftware.demo.servlet;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.websocket.Session;

import com.lqlsoftware.demo.dao.SessionUtil;
import com.lqlsoftware.demo.dao.ImgCompress;

@WebServlet(name = "ImgUploadServlet" ,urlPatterns = "/imgUpload")
@MultipartConfig
public class ImgUploadServlet extends HttpServlet {

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
		String userId = request.getParameter("userId");
		
		String root = request.getServletContext().getRealPath("/");
		String file = "";
		
		// 获取图片
		Part part = request.getPart("img");
		if ( part.getSize() > 0 ) {
			String type = part.getContentType();
			// 存入服务器
			file = "/imgs/" + new Date().getTime() + "." + type.substring(type.indexOf('/')+1);
			String path = root  + file;
			part.write(path);
			
			File img = new File(path);
			BufferedImage imgsrc = ImageIO.read(img);
			// 压缩图片并保存
			ImgCompress imgCompress = new ImgCompress();
			imgCompress.setType(type.substring(type.lastIndexOf("image")+6));
			imgCompress.setImg(imgsrc);
			imgCompress.setNewFilePath( "/imgs_s/" + file.substring(6));
			imgCompress.resizeFix(800, 800);
		}
		for (Session value : SessionUtil.clients.values()) {
				value.getBasicRemote().sendText(userId + ":imghttp://lqlsoftware.top/fuckchat" + file);
		}
		return;
	}
}
