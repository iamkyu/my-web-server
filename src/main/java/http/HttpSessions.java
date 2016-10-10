package http;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Kj Nam
 * @since 2016-10-10
 */
public class HttpSessions {
    private static Map<String, HttpSession> sessions = new HashMap<>();

    public static HttpSession getSession(String id) {
        HttpSession session = sessions.get(id);

        if (session == null) {
            session = new HttpSession(id);
            sessions.put(id, session);
        }
        return session;
    }

    public static void remove(String id) {
        sessions.remove(id);
    }

    public static void removeAll() {
        sessions = new HashMap<>();
    }
}
