package ua.kiev.client;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.websocket.*;
import java.io.IOException;
import java.net.URI;

@ClientEndpoint
public class ChatWebsocketClient {
    private MessageList msgList;
    private Session session;
    private String login;

    public ChatWebsocketClient(HttpSession httpSession, HttpServletRequest request) {
        connect(httpSession, request);
    }

    public void connect(HttpSession httpSession, HttpServletRequest request) {
        login = httpSession.getAttribute("login").toString();
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        String wsUrl = String.format("ws://%s:%s/main-chat/%s", request.getServerName(), request.getServerPort(), login);
        try {
            container.connectToServer(this, URI.create(wsUrl));
            msgList = new MessageList();
        } catch (DeploymentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
    }

    @OnClose
    public void onClose(Session session) {
        this.session = null;
        login = null;
        msgList = null;
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        // TODO
    }

    @OnMessage
    public void onMessage(String json) {
        Message msg = Message.fromJSON(json);
        msgList.add(msg);
    }

    public void disconnect() {
        try {
            session.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(Message message) {
        try {
            session.getBasicRemote().sendText(message.toJSON());
            msgList.add(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String showChatRoomMessages(String chatName) {
        return msgList.getChatRoomMessages(chatName);
    }

    public String showPrivateMessages() {
        return msgList.getPrivateMessages(login);
    }

    public boolean isClosed() {
        return session == null;
    }
}
