package ua.kiev.server;

import javax.websocket.Session;
import java.io.IOException;
import java.util.*;

public class ChatsData {
    public static final String ALL = "all";
    public static final int SIZE = 4;
    private static Map<String, Set<String>> chats = Collections.synchronizedMap(new HashMap<>());

    static {
        chats.put(ALL, Collections.synchronizedSet(new HashSet<>()));
    }

    public static void createNewChat(String name) {
        if(chats.size() < SIZE ) {
            chats.put(name, Collections.synchronizedSet(new HashSet<>()));
        }
    }

    public static void removeChat(String name) {
        chats.remove(name);
    }

    public static Set<String> getAllUsersOfChat(String chatName) {
        return chats.get(chatName);
    }

    public static Set<String> getChatsNames() {
        return chats.keySet();
    }

    public static boolean containsChat(String chatName) {
        return chats.containsKey(chatName);
    }

    public static void putUserInChat(String chatName, String login) {
        chats.get(chatName).add(login);
    }

    public static void removeUserAndNotifyOthers(Session session, String login, MessageList msgList) {
        for(String chatName : chats.keySet()) {
            if(chats.get(chatName).contains(login)) {
                chats.get(chatName).remove(login);
                Set<String> users = getAllUsersOfChat(chatName);
                Message msg = new Message("all", "server", chatName, login + " has left the chat");
                msgList.add(msg);
                for(String user : users) {
                    try {
                        UsersData.getSession(user).getBasicRemote().sendText(msg.toJSON());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}
