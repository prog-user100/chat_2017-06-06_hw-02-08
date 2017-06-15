package ua.kiev.server;

import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginServlet extends javax.servlet.http.HttpServlet {

    public void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        if(UsersData.containsUser(login, password)) {
            HttpSession httpSession = request.getSession(true);
            httpSession.setAttribute("login", login);
            response.sendRedirect("chat.jsp");
        } else {
            response.sendRedirect("login.jsp");
        }
    }
}
