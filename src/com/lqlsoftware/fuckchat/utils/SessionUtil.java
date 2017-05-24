package com.lqlsoftware.demo.dao;

import javax.websocket.Session;  
import java.util.Map;  
import java.util.concurrent.ConcurrentHashMap;  
  
/** 
 * ����˵���������洢ҵ�����sessionId�����ӵĶ�Ӧ��ϵ 
 *          ����ҵ���߼�����װ��sessionId��ȡ��Ч���Ӻ���к������� 
 * ���ߣ�liuxing
 */  
public class SessionUtil {  
  
    public static Map<String, Session> clients = new ConcurrentHashMap<>();  
  
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
     * �ж��Ƿ������� 
     * @param relationId 
     * @param userCode 
     * @return 
     */  
    public static boolean hasConnection(String userId) {  
        return clients.containsKey(userId);  
    }
} 