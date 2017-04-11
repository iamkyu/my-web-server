package http;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static http.HttpMethod.GET;
import static http.HttpMethod.POST; import static org.hamcrest.CoreMatchers.is; import static org.junit.Assert.*;

/**
 * @author Kj Nam
 * @since 2017-04-11
 */
public class HttpRequestTest {
    private InputStream in;
    private HttpRequest request;

    @Test
    public void GET_요청을_보낸다() throws Exception {
        //given
        String message =
                "GET /index.html HTTP/1.1\n" +
                "Host: localhost:8080\n" +
                "Connection: keep-alive\n" +
                "Accept: */*\n" +
                "\n";
        in = new ByteArrayInputStream(message.getBytes("UTF-8"));

        //when
        request = new HttpRequest(in);

        //then
        assertThat(request.getMethod(), is(GET));
        assertThat(request.getPath(), is("/index.html"));
        assertThat(request.getHeader("Connection"), is("keep-alive"));
    }

    @Test
    public void POST_요청을_보낸다() throws Exception {
        //given
        String body = "userId=tester&password=pass&name=myname&email=a@b.com";
        String message =
                "POST /user/create HTTP/1.1\n" +
                "Host: localhost:8080\n" +
                "Connection: keep-alive\n" +
                "Content-Length: 53\n" +
                "Content-Type: application/x-www-form-urlencoded\n" +
                "Accept: */*\n" +
                "\n" + body;
        in = new ByteArrayInputStream(message.getBytes("UTF-8"));

        //when
        request = new HttpRequest(in);

        //then
        assertThat(request.getMethod(), is(POST));
        assertThat(request.getPath(), is("/user/create"));
        assertThat(request.getHeader("Connection"), is("keep-alive"));
        assertThat(request.getHeader("Content-Length"), is(String.valueOf(body.length())));
        assertThat(request.getParameter("userId"), is("tester"));
    }
}