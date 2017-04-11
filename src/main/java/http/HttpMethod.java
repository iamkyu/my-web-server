package http;

/**
 * @author Kj Nam
 * @since 2017-04-11
 */
public enum HttpMethod {
    GET("GET"),
    POST("POST");

    private String value;

    HttpMethod(String value) {
        this.value = value;
    }
}
