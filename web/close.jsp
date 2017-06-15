<%@ page import="ua.kiev.client.ChatWebsocketClient" %>
<%@ page import="ua.kiev.client.Message" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    session.setAttribute("login", null);
    ChatWebsocketClient client = (ChatWebsocketClient) session.getAttribute("client");
    if (client == null) {
        // do nothing
    } else {
        client.disconnect();
    }
    response.sendRedirect("/login.jsp");
%>
