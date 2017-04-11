package http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import static http.HttpMethod.POST;

/**
 * @author Kj Nam
 * @since 2017-04-10
 */
public class HttpRequest {
    private static final Logger log = LoggerFactory.getLogger(HttpRequest.class);
    private final String CONTENT_LENGTH = "Content-Length";

    private HttpMethod method;
    private String path;
    private boolean loggedIn = false;

    private Map<String, String> params;
    private Map<String, String> headers;
    private final BufferedReader br;

    public HttpRequest(InputStream in) throws IOException {
        params = new HashMap<>();
        headers = new HashMap<>();
        br = new BufferedReader(new InputStreamReader(in));
        parseMessage();
    }

    private void parseMessage() throws IOException {
        String line = br.readLine();

        if (!validMessageLine(line))
            return;

        extractStatusLine(line);
        line = br.readLine();

        while (validMessageLine(line)) {
            extractHeaders(line);
            line = br.readLine();
        }
        parseMessageBody();
    }

    private void parseMessageBody() throws IOException {
        if (!POST.equals(method))
            return;

        String body = IOUtils.readData(br, Integer.parseInt(getHeader(CONTENT_LENGTH)));
        params = HttpRequestUtils.parseQueryString(body);
    }

    private void extractStatusLine(String statusLine) {
        String [] tokens = statusLine.split(" ");
        this.method = HttpMethod.valueOf(tokens[0]);
        this.path = tokens[1];
        log.debug("request line: {}", statusLine);
    }

    private void extractHeaders(String headerLine) {
        String[] header = headerLine.split(": ");
        headers.put(header[0].trim(), header[1].trim());

        if (headerLine.contains("Cookie")) {
            loggedIn = setLoginStatus(headerLine);
        }
        log.debug("header: {}", headerLine);
    }

    private boolean validMessageLine(String line) {
        return (line != null) && (!"".equals(line));
    }

    private boolean setLoginStatus(String line) {
        String[] headerTokens = line.split(":");
        Map<String, String> cookies = HttpRequestUtils.parseCookies(headerTokens[1].trim());
        String value = cookies.get("login");
        if (value == null) {
            return false;
        }
        return Boolean.parseBoolean(value);
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getHeader(String key) {
        return headers.get(key);
    }

    public String getParameter(String key) {
        return params.get(key);
    }
}
