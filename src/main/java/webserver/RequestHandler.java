package webserver;

import controller.Controller;
import controller.CreateUserController;
import controller.ListUserController;
import controller.LoginController;
import http.Request;
import http.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;
    private final Map<String, Controller> controllers;
    private final RequestMapping mapping;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
        mapping = new RequestMapping();
        controllers = new HashMap() {{
            put("/user/create", new CreateUserController());
            put("/user/login", new LoginController());
            put("/user/list", new ListUserController());
        }};
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}",
                connection.getInetAddress(), connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            Request request = new Request(in);
            Response response = new Response(out);

            Controller controller = mapping.getController(request);
            if (controller == null) {
                String path = getDefaultPath(request.getPath());
                response.forward(path);
            } else {
                controller.service(request, response);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private String getDefaultPath(String path) {
        if (path.endsWith("/")) {
            return "/index.html";
        }
        return path;
    }
}
