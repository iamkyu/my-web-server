package http;

/**
 * @author Kj Nam
 * @since 2016-10-01
 */
public enum  HttpMethod {
    GET,
    POST;

    public boolean isPost() {
        return this == POST;
    }
}
