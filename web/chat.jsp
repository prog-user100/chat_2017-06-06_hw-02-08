<%@ page import="java.util.Map" %>
<%@ page import="ua.kiev.server.Status" %>
<%@ page import="ua.kiev.server.ChatsData" %>
<%@ page import="java.util.Set" %>
<%@ page import="ua.kiev.server.UsersData" %>
<%@ page import="ua.kiev.client.ChatWebsocketClient" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% String login; %>
<% ChatWebsocketClient client = null; %>
<html>
<head>
    <title>Chat</title>
</head>
<body>
    <%
        login = (String) session.getAttribute("login");
        if(login == null || login.isEmpty()) {
            response.sendRedirect("login.jsp");
        } else {
            out.print("You are logged in as " + login);
            client = (ChatWebsocketClient) session.getAttribute("client");
            if(client == null) {
                client = new ChatWebsocketClient(session, request);
                session.setAttribute("client", client);
            } else if ( client.isClosed()) {
                client.connect(session, request);
                session.setAttribute("client", client);
            }
        }
    %>
    <br/>
    <a href="/close.jsp" >Quit the chat</a>


    <hr/><!-- Chat rooms ------->
    <h3>Chat rooms</h3>

    <form action="/new-chat-room" name="create_chat" method="POST">
        <input type="hidden" name="form_type" value="create_chat" />
        <input type="text" name="chat_name" required/>
        <input type="submit" value="new chat room">
    </form>

    Chat-Rooms:
    <%
        Set<String> chats = ChatsData.getChatsNames();
        if(chats.size() > 1) {
            out.print("<ul>");
            for(String chat: chats) {
                if(!chat.equals("all")) {
                    out.print("<li><a href=\"/enter-room?chat_name=" + chat + "\">" + chat + "</a></li>");
                }
            }
            out.print("</ul>");
        } else {
            out.print("None");
        }
    %>
    <br/>


    <%
        String room = (String) session.getAttribute("room");
        if(room == null || room.isEmpty() ) {
            // do nothing
        } else {
    %>

        <form action="/send.jsp" name="send_to_<%= room %>" method="POST">
            <input type="hidden" name="form_type" value="<%= room %>" />
            Send to <%= room %>'s users:<br/>
            <textarea name="message_body" cols="60" rows="2"></textarea>
            <input type="submit" name="to" value="Send to <%= room %>" />
        </form>

        <form action="" name="recipient_<%= room %>">
            Messages from <%= room %>:<br/>
            <textarea name="" cols="60" rows="5" readonly> <% if(client!=null) out.print(client.showChatRoomMessages(room)); %></textarea>
        </form>

    <%
        }

    %>
    <br/>

    <hr/> <!-- Chat for all ------->
    <h3>Main chat</h3>

    <form action="/send.jsp" name="send_to_all" method="POST">
        <input type="hidden" name="form_type" value="all" />
        Send to all's users:<br/>
        <textarea name="message_body" cols="60" rows="2"></textarea>
        <input type="submit" name="to" value="Send to all" />
    </form>

    <form action="" name="recipient_all">
        Messages from all:<br/>
        <textarea name="" cols="60" rows="5" readonly> <% if(client!=null) out.print(client.showChatRoomMessages("all")); %></textarea>
    </form>


    <hr/><!-- Private chat------->
    <h3>Private chat</h3>

    <form action="/send.jsp" name="send_to_private" method="POST">
        <input type="hidden" name="form_type" value="private" />
        Send to one user:<br/>
        <textarea name="message_body" cols="60" rows="2" ></textarea>
    <ul>
        <%
            Map<String, Status> users = UsersData.getLoginsAndStatus();
            for(String name: users.keySet()) {
                out.print(name + " | " + users.get(name).print() + " | ");
        %>
        <input type="submit" name="to" value="Send to <%= name %>" <% if(users.get(name) == Status.OFF_LINE) out.print("disabled"); %> />
        <br/>
        <%
            }
        %>
    </ul>
    </form>

    <form action="" name="recipient_private">
        Private messages:<br/>
        <textarea name="" cols="60" rows="5" readonly> <% if(client!=null) out.print(client.showPrivateMessages()); %></textarea>
    </form>

</body>
</html>
