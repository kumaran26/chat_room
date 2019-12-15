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

/**
 * WebSocket Server
 * @see ServerEndpoint WebSocket Client
 * @see Session WebSocket Session
 */
@Component
@ServerEndpoint("/chat/{username}")
public class WebSocketChatServer {

    // All chat online sessions
    private static Map<String, Session> onlineSessions = new ConcurrentHashMap<>();

    private static HashMap<String, String> onlineUsers = new HashMap<>();

    private static Constants constants = new Constants();

    /**
     * This method is used to open the websocket connection
     * @param msg Message to be sent to all the users
     */
    private static void sendMessageToAll(String msg) {
        onlineSessions.forEach((id, session) -> {
            try{
                session.getBasicRemote().sendText(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * This method is used to open the websocket connection
     * @param session WebSocket Session
     * @param user User that needed to be added to the session
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("username") String user) {
        onlineSessions.put(session.getId(), session);
        onlineUsers.put(session.getId(), user);
        sendMessageToAll(Message.strToJson(constants.ENTER_CHAT, user, onlineSessions.size(), constants.ENTER));
    }

    /**
     * This method is used to send message to all users
     * @param session WebSocket Session
     * @param jsonStr Message to be sent to users
     */
    @OnMessage
    public void onMessage(Session session, String jsonStr) {
        Message message = JSON.parseObject(jsonStr, Message.class);
        sendMessageToAll(Message.strToJson(message.getMsg(), message.getUsername(), onlineSessions.size(), constants.SPEAK));
    }

    /**
     * This method is used to close the session and remove the session
     * @param session WebSocket Session.
     */
    @OnClose
    public void onClose(Session session) {
        onlineSessions.remove(session.getId());
    }

    /**
     * This method is used to print the exception
     * @param session WebSocket Session
     * @param error WebSocket connection error.
     */
    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

}
