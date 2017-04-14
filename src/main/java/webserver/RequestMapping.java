package webserver;

import controller.Controller;
import controller.CreateUserController;
import controller.ListUserController;
import controller.LoginController;
import http.Request;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Kj Nam
 * @since 2017-04-14
 */
class RequestMapping {
    private final Map<String, Controller> controllers = new HashMap() {{
        put("/user/create", new CreateUserController());
        put("/user/login", new LoginController());
        put("/user/list", new ListUserController());
    }};

    Controller getController(Request request) {
        return controllers.get(request.getPath());
    }
}
