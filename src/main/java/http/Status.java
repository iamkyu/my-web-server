package http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Kj Nam
 * @since 2017-04-12
 */
class Status {
    private static final Logger log = LoggerFactory.getLogger(Status.class);

    private Method method;
    private String path;
    private String version;

    Status(String statusLine) {
        String [] tokens = statusLine.split(" ");
        if (tokens.length != 3) {
            throw new IllegalArgumentException("올바른 HTTP 메세지가 아닙니다.");
        }

        this.method = Method.valueOf(tokens[0]);
        this.path = tokens[1];
        this.version = tokens[2];
        log.debug("request line: {}", statusLine);
    }

    Method getMethod() {
        return method;
    }

    String getPath() {
        return path;
    }

    String getVersion() {
        return version;
    }
}
