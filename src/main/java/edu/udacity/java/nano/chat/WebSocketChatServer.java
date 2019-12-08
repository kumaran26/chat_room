package edu.udacity.java.nano.chat;

import com.alibaba.fastjson.JSON;
import edu.udacity.java.nano.util.Constants;
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

   private static Constants constants = new Constants();

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
        onlineSessions.put(session.getId(), session);
        onlineUsers.put(session.getId(), user);
        sendMessageToAll(Message.strToJson(constants.ENTER_CHAT, user, onlineSessions.size(), constants.ENTER));
    }

    @OnMessage
    public void onMessage(Session session, String jsonStr) {
        Message message = JSON.parseObject(jsonStr, Message.class);
        sendMessageToAll(Message.strToJson(message.getMsg(), message.getUsername(), onlineSessions.size(), constants.SPEAK));
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
