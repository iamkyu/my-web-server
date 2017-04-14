package http;

import util.HttpRequestUtils;
import util.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import static http.ConstPool.CONTENT_LENGTH;
import static http.Method.POST;

/**
 * @author Kj Nam
 * @since 2017-04-10
 */
public class Request {
    private Status status;
    private Headers headers;
    private Map<String, String> params;
    private final BufferedReader br;

    public Request(InputStream in) throws IOException {
        headers = new Headers();
        params = new HashMap<>();
        br = new BufferedReader(new InputStreamReader(in));
        parseMessage();
    }

    private void parseMessage() throws IOException {
        String line = br.readLine();

        if (!validMessageLine(line))
            return;

        status = new Status(line);
        line = br.readLine();

        while (validMessageLine(line)) {
            headers.add(line);
            line = br.readLine();
        }
        parseMessageBody();
    }

    private void parseMessageBody() throws IOException {
        if (!POST.equals(status.getMethod()))
            return;

        String body = IOUtils.readData(br, Integer.parseInt(getHeader(CONTENT_LENGTH)));
        params = HttpRequestUtils.parseQueryString(body);
    }

    private boolean validMessageLine(String line) {
        return (line != null) && (!"".equals(line));
    }

    public boolean isLoggedIn() {
        return headers.getLoginStatus();
    }

    public String getHeader(String key) {
        return headers.get(key);
    }

    public Method getMethod() {
        return status.getMethod();
    }

    public String getPath() {
        return status.getPath();
    }

    public String getParameter(String key) {
        return params.get(key);
    }
}
