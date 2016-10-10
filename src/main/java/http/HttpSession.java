package http;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Kj Nam
 * @since 2016-10-10
 */
public class HttpSession {
    private static Map<String, Object> values= new HashMap<>();

    private String id;

    public HttpSession(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setAttribute(String name, Object value) {
        values.put(name, value);
    }

    public Object getAttribute(String name) {
        return values.get(name);
    }

    public void removeAttribute(String name) {
        values.remove(name);
    }

    public void invallidate() {
        HttpSessions.remove(id);
    }
}
