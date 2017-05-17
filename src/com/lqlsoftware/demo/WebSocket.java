package com.lqlsoftware.demo;

import java.io.IOException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/chat/{userId}")
public class WebSocket {

	// 收到信息
	@OnMessage
	public void onMessage(@PathParam("userId") String userId, String message,
			Session session) throws IOException, InterruptedException {
		System.out.println(message);
		String[] text = message.split("->");
		try {
			String touserId = text[1];
			String msg = text[0];
			if (touserId.equals("sys:public")) {
				for (Session value : SessionUtil.clients.values()) {
					if (!value.equals(session))
						value.getAsyncRemote().sendText(userId + " : " + msg);
				}  
			}
			else if (SessionUtil.hasConnection(touserId)) {
				SessionUtil.get(touserId).getAsyncRemote()	.sendText(userId + " : " + msg);
			} else {
				session.getBasicRemote().sendText("sys:" + touserId + " is offline");
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			session.getBasicRemote().sendText("sys:Invaild Input");
			return;
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
		session.getBasicRemote()
				.sendText(
						"sys:Welcome to fuckchat v0.1, type message->user to send a message");
		System.out.println(userId + " online");
	}

	// 关闭连接
	@OnClose
	public void onClose(@PathParam("userId") String userId) {
		SessionUtil.remove(userId);
		System.out.println(userId + " offine");
	}
}