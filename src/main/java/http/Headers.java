package http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Kj Nam
 * @since 2017-04-14
 */
class Headers {
    private static final Logger log = LoggerFactory.getLogger(Headers.class);

    private Map<String, String> headers = new HashMap<>();
    private Map<String, String> cookies = new HashMap<>();

    void add(String headerLine) {
        String[] header = headerLine.split(": ");
        headers.put(header[0].trim(), header[1].trim());

        if (headerLine.contains("Cookie")) {
            cookies = HttpRequestUtils.parseCookies(header[1].trim());
        }
        log.debug("header: {}", headerLine);
    }

    String get(String key) {
        return headers.get(key);
    }

    boolean getLoginStatus() {
        String value = cookies.get("login");
        if (value == null) {
            return false;
        }
        return Boolean.parseBoolean(value);
    }
}
