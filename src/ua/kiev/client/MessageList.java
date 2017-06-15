package ua.kiev.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.LinkedList;
import java.util.List;

public class MessageList {
    private int LIMIT = 100;
    private Gson gson = new GsonBuilder().create();
    private List<Message> list = new LinkedList<Message>();

    public void add(Message m) {
        if (list.size() + 1 == LIMIT) {
            list.remove(0);
        }
        list.add(m);
    }

    public String toJSON(int n) {
        if (n == list.size()) return null;
        return gson.toJson(new JsonMessages(list, n));
    }

    public String getChatRoomMessages(String to) {
        StringBuilder builder = new StringBuilder();
        for(Message msg : list) {
            if(msg.getTo().equals(to)) {
                builder.append(msg.toStringBuilder());
            }
        }
        return builder.toString();
    }

    public String getPrivateMessages(String to) {
        StringBuilder builder = new StringBuilder();
        for(Message msg : list) {
            if(msg.getTo().equals(to) || msg.getType().equals("private")) {
                builder.append(msg.toStringBuilder());
            }
        }
        return builder.toString();
    }
}
