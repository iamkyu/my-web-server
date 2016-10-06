package controller;

import db.DataBase;
import http.HttpRequest;
import http.HttpResponse;
import model.User;
import util.HttpRequestUtils;

import java.util.Collection;
import java.util.Map;

/**
 * @author Kj Nam
 * @since 2016-10-06
 */
public class ListUserController implements Controller {
    @Override
    public void service(HttpRequest request, HttpResponse response) {
        if (!isLogin(request.getHeader("Cookie"))) {
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

    private boolean isLogin(String cookie) {
        Map<String, String> cookies = HttpRequestUtils.parseCookies(cookie);
        String value = cookies.get("logind");
        if (value == null) {
            return false;
        }
        return Boolean.parseBoolean(value);
    }
}
