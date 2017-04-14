package controller;


import http.Method;
import http.Request;
import http.Response;

/**
 * @author Kj Nam
 * @since 2017-04-14
 */
public abstract class AbstractController implements Controller {
    @Override
    public void service(Request request, Response response) {
        Method method = request.getMethod();

        if (method.isPost()) {
            doPost(request, response);
        }

        if (method.isGet()) {
            doGet(request, response);
        }
    }

    void doPost(Request request, Response response) {
        // will be overriding
    }

    void doGet(Request request, Response response) {
        // will be overriding
    }
}
