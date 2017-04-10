package webserver;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Map;

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
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line = br.readLine();

            if (line == null)
                return;

            log.debug("request line: {}", line);

            String [] tokens = line.split(" ");
            final String HTTP_METHOD = tokens[0];
            final String REQUEST_URI = tokens[1];
            int contentLength = 0;
            DataOutputStream dos = new DataOutputStream(out);

            while (!("").equals(line)) {
                line  = br.readLine();
                log.debug("header: {}", line);
                if (line.contains("Content-Length")) {
                    contentLength = getContentLength(line);
                }
            }

            if (HTTP_METHOD.equals("POST")) {
                String body = IOUtils.readData(br, contentLength);
                Map<String, String> params = HttpRequestUtils.parseQueryString(body);

                if (REQUEST_URI.endsWith("user/create")) {
                    DataBase.addUser(new User(
                            params.get("userId"),
                            params.get("password"),
                            params.get("name"),
                            params.get("email")));
                    log.debug("New User Register! ID: {}", params.get("userId"));
                    response302Header(dos, "/index.html");
                }

                if (REQUEST_URI.endsWith("user/login")) {
                    User user = DataBase.findUserById(params.get("userId"));
                    if (user == null || (!user.getPassword().equals(params.get("password")))) {
                        response302Header(dos, "/user/login_failed.html");
                        return;
                    }
                    log.debug("User Login Success! ID: {}", params.get("userId"));
                    response302LoginSuccessHeader(dos);
                }
            } else {
                responseResource(dos, REQUEST_URI);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private int getContentLength(String headerLine) {
        return Integer.parseInt(HttpRequestUtils.parseHeader(headerLine).getValue());
    }

    private void responseResource(OutputStream out, String url) {
        try {
            DataOutputStream dos = new DataOutputStream(out);
            byte [] body = Files.readAllBytes(new File("./webapp" + url).toPath());
            response200Header(dos, body.length);
            responseBody(dos, body);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response302Header(DataOutputStream dos, String location) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: " + location);
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response302LoginSuccessHeader(DataOutputStream dos) {
        try {
            dos.writeBytes("HTTP/1.1 302 Redirect \r\n");
            dos.writeBytes("Set-Cookie: login=true \r\n");
            dos.writeBytes("Location: /index.html \r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
