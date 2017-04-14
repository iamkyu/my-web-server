package controller;

import http.Request;
import http.Response;

/**
 * @author Kj Nam
 * @since 2017-04-14
 */
public interface Controller {
    void service(Request request, Response response);
}
