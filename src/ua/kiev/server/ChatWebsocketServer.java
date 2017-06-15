package ua.kiev.server;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;

@ServerEndpoint("/main-chat/{login}")
public class ChatWebsocketServer {
    private MessageList msgList = MessageList.getInstance();

    @OnOpen
    public void onOpen(Session session, @PathParam("login") String login) {
        ChatsData.getAllUsersOfChat(ChatsData.ALL).add(login);
        UsersData.putSessionForUser(login, session);
    }

    @OnClose
    public void onClose(Session session) {
        String login = UsersData.removeSession(session);
        ChatsData.removeUserAndNotifyOthers(session, login, msgList);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        // TODO
    }

    @OnMessage
    public void handleMessage(String json) {
        Message msg = Message.fromJSON(json);
        msgList.add(msg);
        String from = msg.getFrom();
        String to = msg.getTo();
        if( !from.equals(to) ) {
            forwardMessages(msg.getFrom(), msg.getTo(), json);
        }
    }

    public void forwardMessages(String from, String to, String json) {
        Set<String> users = ChatsData.getAllUsersOfChat(to);
        if (users == null) { // it is private message
            try {
                UsersData.getSession(to).getBasicRemote().sendText(json);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else { // it is chat's message
            for(String user : users) {
                if(user.equals(from)) {
                    continue;
                }
                try {
                    UsersData.getSession(user).getBasicRemote().sendText(json);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
