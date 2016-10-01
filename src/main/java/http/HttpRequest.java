package http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Kj Nam
 * @since 2016-10-01
 */
public class HttpRequest {
    private static final Logger log = LoggerFactory.getLogger(HttpRequest.class);

    private RequestLine requestLine;
    private Map<String, String> headers;
    private Map<String, String> params;

    public HttpRequest(InputStream in) {
        headers = new HashMap<>();
        params = new HashMap<>();

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String line = br.readLine();
            if (line == null) {
                return;
            }

            requestLine = new RequestLine(line);
            line = br.readLine();

            while (!line.equals("")) {
                log.debug("header: {}", line);
                String[] tokens = line.split(":");
                headers.put(tokens[0].trim(), tokens[1].trim());
                line = br.readLine();
            }

            if (getMethod().isPost()) {
                String body = IOUtils.readData(br, Integer.parseInt(headers.get("Content-Length")));
                params = HttpRequestUtils.parseQueryString(body);
            } else {
                params = requestLine.getParams();
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public HttpMethod getMethod() {
        return requestLine.getMethod();
    }

    public String getHeader(String name) {
        return headers.get(name);
    }

    public String getParameter(String name) {
        return params.get(name);
    }

    public String getPath() {
        return requestLine.getPath();
    }
}
