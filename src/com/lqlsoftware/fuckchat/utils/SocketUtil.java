package com.lqlsoftware.fuckchat.utils;

import java.io.IOException;

import javax.websocket.Session;

/**
 * @author Robin Lu
 * 
 */

public class SocketUtil {

	public static void broadcast(String msg) throws IOException {
		for (Session value : SessionUtil.clients.values()) {
				value.getBasicRemote().sendText(msg);
		}
	}
	
	public static void broadcastWithout(String msg, String key) throws IOException {
		Session session = SessionUtil.get(key);
		for (Session value : SessionUtil.clients.values()) {
			if (!value.equals(session)) {
				value.getBasicRemote().sendText(msg);
			}
		}
	}
	
	public static void broadcastWithout(String msg, Session session) throws IOException {
		for (Session value : SessionUtil.clients.values()) {
			if (!value.equals(session)) {
				value.getBasicRemote().sendText(msg);
			}
		}
	}
	
	public static void sendTo(String msg, String key) throws IOException {
		if (SessionUtil.hasConnection(key)) {
			Session target = SessionUtil.get(key);
			target.getBasicRemote().sendText(msg);
		}
	}
	
}
