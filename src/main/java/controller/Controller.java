package controller;

import http.HttpRequest;
import http.HttpResponse;

/**
 * @author Kj Nam
 * @since 2016-10-06
 */
public interface Controller {
    void service(HttpRequest request, HttpResponse response);
}
