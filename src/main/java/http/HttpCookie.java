package http;

import util.HttpRequestUtils;

import java.util.Map;

/**
 * @author Kj Nam
 * @since 2016-10-10
 */
public class HttpCookie {
    private Map<String, String> cookies;

    public HttpCookie(String cookieValue) {
        cookies = HttpRequestUtils.parseCookies(cookieValue);
    }

    public String getCookie(String name) {
        return cookies.get(name);
    }
}
