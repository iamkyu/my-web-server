package webserver;

import db.DataBase;
import http.HttpRequest;
import http.HttpResponse;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Collection;

import static http.HttpMethod.GET;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            HttpRequest request = new HttpRequest(in);
            HttpResponse response = new HttpResponse(out);

            if (GET.equals(request.getMethod())) {
                if ("/user/list".equals(request.getPath())) {
                    if (!request.isLoggedIn()) {
                        response.sendRedirect("/user/login.html");
                    }
                    Collection<User> users = DataBase.findAll();
                    response.forwardBody(buildUserListHTML(users));
                }
                response.forward(request.getPath());
            } else {
                if ("/user/create".equals(request.getPath())) {
                    DataBase.addUser(new User(
                            request.getParameter("userId"),
                            request.getParameter("password"),
                            request.getParameter("name"),
                            request.getParameter("email")));
                    log.debug("New User Register! ID: {}", request.getParameter("userId"));

                    response.sendRedirect("/index.html");
                }

                if ("/user/login".equals(request.getPath())) {
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
        } catch (IOException e) {
            log.error(e.getMessage());
        }
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
