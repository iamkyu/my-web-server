package controller;

import db.DataBase;
import http.Request;
import http.Response;
import model.User;

import java.util.Collection;

/**
 * @author Kj Nam
 * @since 2017-04-14
 */
public class ListUserController extends AbstractController {
    @Override
    void doGet(Request request, Response response) {
        if (!request.isLoggedIn()) {
            response.sendRedirect("/user/login.html");
        }
        Collection<User> users = DataBase.findAll();
        response.forwardBody(buildUserListHTML(users));
    }

    private String buildUserListHTML(Collection<User> users) {
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

        return sb.toString();
    }
}
