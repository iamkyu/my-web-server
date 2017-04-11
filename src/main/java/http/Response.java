package http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static http.ConstPool.CONTENT_LENGTH;
import static http.ConstPool.CONTENT_TYPE;
import static http.ConstPool.ROOT_PATH;

/**
 * @author Kj Nam
 * @since 2017-04-11
 */
public class Response {
    private static final Logger log = LoggerFactory.getLogger(Response.class);

    private ContentType contentType;
    private DataOutputStream dos;
    private Map<String, String> headers;

    public Response(OutputStream out) {
        contentType = new ContentType();
        headers = new HashMap<>();
        dos = new DataOutputStream(out);
    }

    public void addHeader(String key, String value) {
        headers.put(key, value);
    }

    public void forward(String url) {
        try {
            byte[] body = Files.readAllBytes(new File(ROOT_PATH + url).toPath());
            headers.put(CONTENT_TYPE, contentType.getTypes(url));
            headers.put(CONTENT_LENGTH, String.valueOf(body.length));
            response200Header();
            responseBody(body);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public void forwardBody(String body) {
        byte[] contents = body.getBytes();
        headers.put(CONTENT_TYPE, contentType.getTypes("html"));
        headers.put(CONTENT_LENGTH, contents.length + "\r\n");
        response200Header();
        responseBody(contents);
    }

    public void response200Header() {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            processHeaders();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public void responseBody(byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public void sendRedirect(String url) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            headers.put("Location", url);
            processHeaders();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public void processHeaders() {
        Set<String> keys = headers.keySet();
        try {
            for (String key : keys) {
                dos.writeBytes(key + ": " + headers.get(key) + "\r\n");
            }

            dos.writeBytes("\r\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
