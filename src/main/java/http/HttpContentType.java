package http;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Kj Nam
 * @since 2017-04-12
 */
public class HttpContentType {
    private Map<String, String> types;

    public HttpContentType() {
        types= new HashMap() {{
            put("html", "text/html;charset=utf-8");
            put("css", "text/css");
            put("js", "application.javascript");
        }};
    }

    public String getTypes(String requestURL) {
        String [] split = requestURL.split("\\.");
        String extension = split[split.length-1];

        return types.get(extension);
    }
}
