package edu.udacity.java.nano.chat;

import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import javax.websocket.server.PathParam;
import java.util.Map;
import java.util.HashMap;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint("/chat/{username}")
public class WebSocketChatServer {

    private static Map<String, Session> onlineSessions = new ConcurrentHashMap<>();

    private static HashMap<String, String> onlineUsers = new HashMap<>();

    private static void sendMessageToAll(String msg) {
        onlineSessions.forEach((id, session) -> {
            try{
                session.getBasicRemote().sendText(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String user) {
        System.out.println(session.getId());
        System.out.println(user);
        onlineSessions.put(session.getId(), session);
        onlineUsers.put(session.getId(), user);
        sendMessageToAll(Message.strToJson("ENTERED THE CHAT", user, onlineSessions.size(), "ENTER"));
    }

    @OnMessage
    public void onMessage(Session session, String jsonStr) {
        System.out.println("message from client : " + session);
        System.out.println("message : " + jsonStr);
        Message message = JSON.parseObject(jsonStr, Message.class);
        sendMessageToAll(Message.strToJson(message.getMsg(), message.getUsername(), onlineSessions.size(), "SPEAK"));
    }

    @OnClose
    public void onClose(Session session) {
        onlineSessions.remove(session.getId());
    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

}
