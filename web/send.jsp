<%@ page import="ua.kiev.client.ChatWebsocketClient" %>
<%@ page import="ua.kiev.client.Message" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    ChatWebsocketClient client = (ChatWebsocketClient) session.getAttribute("client");
    String from = (String) session.getAttribute("login");
    String to = request.getParameter("to").replace("Send to ", "");
    String text = request.getParameter("message_body");
    String type = request.getParameter("form_type");
    if (text == null || text.isEmpty()) {
        // do nothing
    } else {
        Message message = new Message(type, from, to, text);
        client.sendMessage(message);
    }
    response.sendRedirect("/chat.jsp");
%>