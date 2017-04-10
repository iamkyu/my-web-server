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
                    contentLength = Integer.parseInt(
                            HttpRequestUtils.parseHeader(line).getValue());
                }
            }

            if (REQUEST_URI.startsWith("/user/create")) {
                String body = IOUtils.readData(br, contentLength);
                log.debug("body: {}", body);

                Map<String, String> params = HttpRequestUtils.parseQueryString(body);
                User user = new User(params.get("userId"), params.get("password"),
                        params.get("name"), params.get("email"));
                DataBase.addUser(user);

                log.debug("New User Register: {}", user.getUserId());
                response302Header(dos, "/index.html");
                return;
            }

            byte[] body = Files.readAllBytes(new File("./webapp" + REQUEST_URI).toPath());

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

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
