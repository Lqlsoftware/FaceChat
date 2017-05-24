package com.lqlsoftware.fuckchat.servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import com.lqlsoftware.fuckchat.utils.SessionUtil;
import com.lqlsoftware.fuckchat.utils.SocketUtil;
import com.lqlsoftware.fuckchat.utils.msgUtil;

@ServerEndpoint(value = "/chat/{userId}")
public class WebSocket {
	
	// �յ���Ϣ
	@OnMessage
	public synchronized void onMessage(@PathParam("userId") String userId, String message,
			Session session) throws IOException, InterruptedException {
		try {
			msgUtil.addMsg(userId, userId + " : " + message, "msg");
			SocketUtil.broadcastWithout(userId + " : " + message, session);
		} catch (Exception e) {
			session.getBasicRemote().sendText("sys:Invaild Input");
		}
	}

	// ������
	@OnOpen
	public void onOpen(@PathParam("userId") String userId, Session session)
			throws IOException, InterruptedException, SQLException {
		if (SessionUtil.hasConnection(userId)) {
			SocketUtil.sendTo("sys:You are current offline.", userId);
			SessionUtil.remove(userId);
		}
		SessionUtil.put(userId, session);
		msgUtil.sendHistoryMsg(userId);
		SocketUtil.broadcast("sys:��ӭС��� " + userId + " ����FuckChat");
		System.out.println(userId + " online");
	}

	// �ر�����
	@OnClose
	public void onClose(@PathParam("userId") String userId)  throws IOException{
		SocketUtil.sendTo("sys:You are current offline.", userId);
		SessionUtil.remove(userId);
		System.out.println(userId + " offine");
	}
	
    @OnError
    public void onError(@PathParam("userId") String userId,Throwable throwable) {
		SessionUtil.remove(userId);
        System.out.println(throwable.getMessage());
    }
}