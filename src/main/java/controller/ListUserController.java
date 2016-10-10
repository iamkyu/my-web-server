package controller;

import db.DataBase;
import http.HttpRequest;
import http.HttpResponse;
import http.HttpSession;
import model.User;

import java.util.Collection;

/**
 * @author Kj Nam
 * @since 2016-10-06
 */
public class ListUserController implements Controller {
    @Override
    public void service(HttpRequest request, HttpResponse response) {
        if (!isLogin(request.getSession())) {
            response.sendRedirect("/user/login.html");
            return;
        }

        Collection<User> users = DataBase.findAll();
        StringBuilder sb = new StringBuilder();
        sb.append("<div><ul><li><a href='/'>Home</a></li></ul></div>");
        sb.append("<table border='1'>");
        sb.append("<tr>");
        sb.append("<th>아이디</th>");
        sb.append("<th>이름</th>");
        sb.append("<th>이메일</th>");
        sb.append("<tr>");
        for (User user : users) {
            sb.append("<tr>");
            sb.append("<td>" + user.getUserId() + "</td>");
            sb.append("<td>" + user.getName() + "</td>");
            sb.append("<td>" + user.getEmail() + "</td>");
            sb.append("</tr>");
        }
        sb.append("</table>");
        response.forwardBody(sb.toString());
    }

    private boolean isLogin(HttpSession session) {
        Object user = session.getAttribute("user");
        if (user == null) {
            return false;
        }
        return true;
    }
}
