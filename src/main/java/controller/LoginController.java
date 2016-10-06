package controller;

import db.DataBase;
import http.HttpRequest;
import http.HttpResponse;
import model.User;

/**
 * @author Kj Nam
 * @since 2016-10-06
 */
public class LoginController implements Controller {
    @Override
    public void service(HttpRequest request, HttpResponse response) {
        User user = DataBase.findUserById(request.getParameter("userId"));
        if (user != null) {
            if (user.login(request.getParameter("password"))) {
                response.addHeader("Set-Cookie", "logind=true");
                response.sendRedirect("/user/list");
            } else {
                response.sendRedirect("/user/login_failed.html");
            }
        } else {
            response.sendRedirect("/user/login_failed.html");
        }
    }
}
