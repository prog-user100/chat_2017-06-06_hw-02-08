package ua.kiev.server;

import javax.websocket.Session;

public class User {
    private String login;
    private String password;
    private Session websocketSession;

    public User(String login, String password, Session session) {
        this.login = login;
        this.password = password;
        this.websocketSession = session;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public Session getWebsocketSession() {
        return websocketSession;
    }

    public void putWebsocketSession(Session websocketSession) {
        this.websocketSession = websocketSession;
    }

    public void removeWebsocketSession() {
        this.websocketSession = null;
    }
}
