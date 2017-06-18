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

import com.lqlsoftware.fuckchat.utils.*;

@ServerEndpoint(value = "/chat/{token}")
public class WebSocket {
	
	// 收到信息
	@OnMessage
	public synchronized void onMessage(@PathParam("token") String token, String message,
			Session session) throws IOException, InterruptedException {
		TokenModel TM = new TokenManager().getToken(token);
		try {
			msgUtil.addMsg(TM.getUserId(), message, "text");
			SocketUtil.broadcastWithout(msgUtil.getMsg(TM.getUserId(), message, "text"), session);
		} catch (Exception e) {
			session.getBasicRemote().sendText(msgUtil.getSysMsg(-1, "Invaild Input", ""));
		}
	}

	// 打开连接
	@OnOpen
	public void onOpen(@PathParam("token") String token, Session session)
			throws IOException, InterruptedException, SQLException {
		if (token == null || token.equals("")) {
			session.getBasicRemote().sendText(msgUtil.getSysMsg(-1, "Please relogin", ""));
			return;
		}
		TokenManager TMR = new TokenManager();
		TokenModel TM = TMR.getToken(token);
		if (!TMR.checkToken(TM)) {
			session.getBasicRemote().sendText(msgUtil.getSysMsg(-1, "Please relogin", ""));
			return;
		}
		SessionUtil.put(TM.getUserId(), session);
		msgUtil.sendHistoryMsg(TM.getUserId());
		SocketUtil.broadcast(msgUtil.getSysMsg(-1, "欢迎小伙伴 " + TM.getUserId() + " 来到FuckChat", ""));
		System.out.println(TM.getUserId() + " online");
	}

	// 关闭连接
	@OnClose
	public void onClose(@PathParam("token") String token)  throws IOException{
		TokenModel TM = new TokenManager().getToken(token);
		SocketUtil.sendTo(msgUtil.getSysMsg(-1, "You are current offline.", ""), TM.getUserId());
		SessionUtil.remove(TM.getUserId());
		System.out.println(TM.getUserId() + " offine");
	}
	
    @OnError
    public void onError(@PathParam("token") String token,Throwable throwable) {
		TokenModel TM = new TokenManager().getToken(token);
		SessionUtil.remove(TM.getUserId());
        System.out.println(throwable.getMessage());
    }
}