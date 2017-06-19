package com.lqlsoftware.fuckchat.servlet;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import com.lqlsoftware.fuckchat.utils.ImgCompress;
import com.lqlsoftware.fuckchat.utils.SocketUtil;
import com.lqlsoftware.fuckchat.utils.msgUtil;

/**
 *  @author Robin Lu
 */

@WebServlet(name = "ImgUploadServlet" ,urlPatterns = "/imgUpload")
@MultipartConfig
public class ImgUploadServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		// 设置传输数据格式
		request.setCharacterEncoding("UTF-8");
		response.setHeader("content-type","text/html;charset=UTF-8");
		response.setHeader("Access-Control-Allow-Origin", "*");

		// 获取页面数据
		String userId = request.getParameter("userId");
		
		String root = request.getServletContext().getRealPath("/");
		String file = "";
		
		// 获取图片
		Part part = request.getPart("img");
		if ( part.getSize() > 0 ) {
			String type = part.getContentType();
			if (type.matches("^(video|VIDEO).*$")) {
				// 存入服务器
				// .quicktime好像safari播放不了
				file = "vids/" + new Date().getTime() + ".mp4";
				String path = root  + file;
				File dir = new File(root + "vids/");
				if (!dir.exists())
				    dir.mkdirs();
				part.write(path);
				try {
					msgUtil.addMsg(userId, file, "vid");
					SocketUtil.broadcast(msgUtil.getMsg(userId, file, "vid"));
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			else if (type.matches("^(image|IMAGE).*$")) {
				// 存入服务器
				file = "imgs/" + new Date().getTime() + "." + type.substring(type.indexOf('/')+1);
				String path = root  + file;
                File dir = new File(root + "imgs/");
                if (!dir.exists())
                    dir.mkdirs();
                dir = new File(root + "imgs_s/");
                if (!dir.exists())
                    dir.mkdirs();
				part.write(path);
				if (!type.substring(type.indexOf('/')+1).equals("gif") && !type.substring(type.indexOf('/')+1).equals("GIF")) {
					File img = new File(path);
					BufferedImage imgsrc = ImageIO.read(img);
					// 压缩图片并保存
					ImgCompress imgCompress = new ImgCompress();
					imgCompress.setType(type.substring(type.lastIndexOf("image")+6));
					imgCompress.setImg(imgsrc);
					imgCompress.setNewFilePath(root + "/imgs_s/" + file.substring(6));
					imgCompress.resizeFix(800, 800);
					file = "imgs_s/" + file.substring(6);
				}
				try {
					msgUtil.addMsg(userId, file, "img");
					SocketUtil.broadcast(msgUtil.getMsg(userId, file, "img"));
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return;
	}
}
