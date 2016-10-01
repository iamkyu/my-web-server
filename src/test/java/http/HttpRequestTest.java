package http;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static http.HttpMethod.GET;
import static http.HttpMethod.POST;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Kj Nam
 * @since 2016-10-01
 */
public class HttpRequestTest {
    private InputStream in;
    private HttpRequest request;

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {

    }

    @Test
    public void GET_방식으로_회원가입을_요청한다() throws Exception {
        //given
        String httpGetRequestMessage =
                "GET /user/create?userId=tester&password=pass&name=myname&email=a@b.com HTTP/1.1\n" +
                "Host: localhost:8080\n" +
                "Connection: keep-alive\n" +
                "Accept: */*\n" +
                "\n";
        in = new ByteArrayInputStream(httpGetRequestMessage.getBytes("UTF-8"));

        //when
        request = new HttpRequest(in);

        //then
        assertThat(request.getMethod(), is(GET));
        assertThat(request.getPath(), is("/user/create"));
        assertThat(request.getHeader("Connection"), is("keep-alive"));
        assertThat(request.getParameter("userId"), is("tester"));
    }

    @Test
    public void POST_방식으로_회원가입을_요청한다() throws Exception {
        //given
        String httpPostRequestMessage =
                "POST /user/create HTTP/1.1\n" +
                "Host: localhost:8080\n" +
                "Connection: keep-alive\n" +
                "Content-Length: 53\n" +
                "Content-Type: application/x-www-form-urlencoded\n" +
                "Accept: */*\n" +
                "\n" +
                "userId=tester&password=pass&name=myname&email=a@b.com";
        in = new ByteArrayInputStream(httpPostRequestMessage.getBytes("UTF-8"));
        String parameters = "userId=tester&password=pass&name=myname&email=a@b.com";

        //when
        request = new HttpRequest(in);

        //then
        assertThat(request.getMethod(), is(POST));
        assertThat(request.getPath(), is("/user/create"));
        assertThat(request.getHeader("Connection"), is("keep-alive"));
        assertThat(request.getHeader("Content-Length"), is(String.valueOf(parameters.length())));
        assertThat(request.getParameter("userId"), is("tester"));
    }

    @Test
    public void 빈_메시지가_전송_되면_예외_발생() throws IOException {
        //given
//        httpGetRequestMessage = "";
//        in = new ByteArrayInputStream(httpGetRequestMessage.getBytes("UTF-8"));

        BufferedReader mockBr = mock(BufferedReader.class);
        when(mockBr.readLine()).thenThrow(new IOException());

        //when
//        in = null;
//        request = new HttpRequest(System.setIn(in));

        //then
    }
}