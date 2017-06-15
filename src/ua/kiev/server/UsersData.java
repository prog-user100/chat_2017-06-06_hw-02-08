package ua.kiev.server;

import javax.websocket.Session;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class UsersData {
    private static Map<String,User> users = Collections.synchronizedMap(new HashMap<>());

    static {
        users.put("admin1", new User("admin1", "1", null));
        users.put("admin2", new User("admin2", "12", null));
        users.put("admin3", new User("admin3", "123", null));
        users.put("admin4", new User("admin4", "1234", null));
        users.put("admin5", new User("admin5", "12345", null));
    }

    public static boolean containsUser(String login, String password) {
        return users.containsKey(login) && users.get(login).getPassword().equals(password);
    }

    public static void putSessionForUser(String login, Session session) {
        users.get(login).putWebsocketSession(session);
    }

    public static void removeSessionForUser(String login) {
        users.get(login).putWebsocketSession(null);
    }

    public static Session getSession(String login) {
        return users.get(login).getWebsocketSession();
    }

    public static String removeSession(Session session) {
        for(String login: users.keySet()) {
            if(users.get(login).getWebsocketSession() == session) {
                users.get(login).removeWebsocketSession();
                return login;
            }
        }
        return null;
    }

    public static Set<String> getLogins() {
        return users.keySet();
    }

    public static Map<String, Status> getLoginsAndStatus() {
        Map<String, Status> usersStatus = new HashMap<>();
        for(String login: users.keySet()) {
            Status status = users.get(login).getWebsocketSession() == null ? Status.OFF_LINE : Status.ON_LINE;
            usersStatus.put(login, status);
        }
        return usersStatus;
    }

}
