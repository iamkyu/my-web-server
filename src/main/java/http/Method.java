package http;

/**
 * @author Kj Nam
 * @since 2017-04-11
 */
public enum Method {
    GET("GET"),
    POST("POST");

    private String value;

    Method(String value) {
        this.value = value;
    }
}
