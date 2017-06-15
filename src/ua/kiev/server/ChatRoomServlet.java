package ua.kiev.server;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

public class ChatRoomServlet extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession httpSession = request.getSession(false);
        String chatName = request.getParameter("chat_name");
        if( chatName == null || chatName.isEmpty() ) {
            // do nothing
        } else {
            ChatsData.createNewChat(chatName);
        }
        response.sendRedirect("chat.jsp");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession httpSession = request.getSession(false);
        String chatName = request.getParameter("chat_name");
        if( chatName == null || chatName.isEmpty() ) {
            // do nothing
        } else {
            ChatsData.putUserInChat(chatName, request.getSession(false).getAttribute("login").toString());
            httpSession.setAttribute("room", chatName);
        }
        response.sendRedirect("chat.jsp");
    }
}
