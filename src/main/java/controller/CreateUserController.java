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
public class CreateUserController extends AbstractController {
    private static final Logger log = LoggerFactory.getLogger(CreateUserController.class);

    @Override
    void doPost(Request request, Response response) {
        DataBase.addUser(new User(
                request.getParameter("userId"),
                request.getParameter("password"),
                request.getParameter("name"),
                request.getParameter("email")));
        log.debug("New User Register! ID: {}", request.getParameter("userId"));

        response.sendRedirect("/index.html");
    }
}
