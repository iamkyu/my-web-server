package controller;

import db.DataBase;
import http.Request;
import http.Response;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Kj Nam
 * @since 2017-04-14
 */
public class LoginController extends AbstractController {
    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @Override
    void doPost(Request request, Response response) {
        User user = DataBase.findUserById(request.getParameter("userId"));
        if (user == null || (!user.getPassword().equals(request.getParameter("password")))) {
            response.sendRedirect("/user/login_failed.html");
            return;
        }
        log.debug("User Login Success! ID: {}", request.getParameter("userId"));

        response.addHeader("Set-Cookie", "login=true");
        response.sendRedirect("/index.html");
    }
}
