package webserver;

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
            // HTTP Header를 읽는다.
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String line = br.readLine();

            log.debug("request line: {}", line);
            if (line == null) {
                return;
            }

            String [] tokens = line.split(" ");
            int contentLength = 0;
            while (!line.equals("")) {
                log.debug("header: {}", line);
                line = br.readLine();
                if (line.contains("Content-Length")) {
                    contentLength = getContentLength(line);
                }
            }

            String url = tokens[1];
            byte[] resoponseBody;
            DataOutputStream dos = new DataOutputStream(out);

            if (("/user/create").equals(url)) {
                String body = IOUtils.readData(br, contentLength);
                Map<String, String> params =
                        HttpRequestUtils.parseQueryString(body);
                 User user = new User(
                        params.get("userId"),
                        params.get("password"),
                        params.get("name"),
                        params.get("email")
                );
                log.debug("User: {}", user);
                resoponseBody = Files.readAllBytes(new File("./webapp/user/success.html").toPath());
                response201Header(dos, resoponseBody.length);
            } else {
                resoponseBody = Files.readAllBytes(new File("./webapp" + tokens[1]).toPath());
                response200Header(dos, resoponseBody.length);
            }

            responseBody(dos, resoponseBody);

        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private int getContentLength(String line) {
        String[] headerTokens = line.split(":");
        // Content-Length: 53 형식의 데이터 중 값만 리턴
        return Integer.parseInt(headerTokens[1].trim());
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

    private void response201Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 201 Created \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
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
