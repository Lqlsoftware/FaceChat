package com.lqlsoftware.fuckchat.utils;

import javax.websocket.Session;  
import java.util.Map;  
import java.util.concurrent.ConcurrentHashMap;  
  
/** 
 * 功能说明：用来存储业务定义的sessionId和连接的对应关系 
 *          利用业务逻辑中组装的sessionId获取有效连接后进行后续操作 
 * 作者：liuxing
 */  
public class SessionUtil {  
  
    public static final Map<String, Session> clients = new ConcurrentHashMap<>();
  
    public static void put(String userId, Session session){  
        clients.put(userId, session);  
    }  
  
    public static Session get(String userId){  
        return clients.get(userId);  
    }  
  
    public static void remove(String userId){  
        clients.remove(userId);  
    }  
  
    /** 
     * 判断是否有连接
     * @param userId
     */  
    public static boolean hasConnection(String userId) {  
        return clients.containsKey(userId);  
    }
} 