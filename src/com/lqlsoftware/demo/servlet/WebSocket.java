package com.lqlsoftware.demo.servlet;

import java.io.EOFException;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import com.lqlsoftware.demo.dao.SessionUtil;

@ServerEndpoint(value = "/chat/{userId}")
public class WebSocket {
	
	// 收到信息
	@OnMessage
	public synchronized void onMessage(@PathParam("userId") String userId, String message,
			Session session) throws IOException, InterruptedException {
		System.out.println(userId + " : " + message);
		try {
			for (Session value : SessionUtil.clients.values()) {
				if (!value.equals(session))
					value.getBasicRemote().sendText(userId + " : " + message);
			}
		} catch (Exception e) {
			session.getBasicRemote().sendText("sys:Invaild Input");
		}
	}

	@OnMessage
	public synchronized void broadcast(@PathParam("userId") String userId, ByteBuffer data,
			Session session) throws IOException, EOFException {
		System.out.println(userId + ":audio-" + data);
		for (Session value : SessionUtil.clients.values()) {
			if (!value.equals(session)) {
				value.getBasicRemote().sendText(userId + ":audio");
				value.getBasicRemote().sendBinary(data);
			}
		}
	}

	// 打开连接
	@OnOpen
	public void onOpen(@PathParam("userId") String userId, Session session)
			throws IOException, InterruptedException {
		if (SessionUtil.hasConnection(userId)) {
			SessionUtil.remove(userId);
		}
		SessionUtil.put(userId, session);
		for (Session value : SessionUtil.clients.values()) {
			value.getAsyncRemote().sendText(
					"sys:欢迎小伙伴 " + userId + " 来到FuckChat");
		}
		System.out.println(userId + " online");
	}

	// 关闭连接
	@OnClose
	public void onClose(@PathParam("userId") String userId)  throws EOFException{
		SessionUtil.remove(userId);
		System.out.println(userId + " offine");
	}
}