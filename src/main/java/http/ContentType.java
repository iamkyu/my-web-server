package http;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Kj Nam
 * @since 2017-04-12
 */
class ContentType {
    private Map<String, String> types;

    ContentType() {
        types= new HashMap() {{
            put("html", "text/html;charset=utf-8");
            put("css", "text/css");
            put("js", "application.javascript");
        }};
    }

    String getTypes(String requestURL) {
        String [] split = requestURL.split("\\.");
        String extension = split[split.length-1];

        return types.get(extension);
    }
}
