package controller;

import db.DataBase;
import http.HttpRequest;
import http.HttpResponse;
import http.HttpSessions;
import model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * @author Kj Nam
 * @since 2016-10-06
 */
public class ControllerTest {
    private InputStream in;
    private HttpRequest request;

    private HttpResponse response;
    private ByteArrayOutputStream byteArrayOutputStream;
    private byte[] byteArray;
    private String responseMessage;
    private String[] responseMessages;
    private Controller controller;
    String httpPostRequestMessage;
    private String responseCode;
    private String responseStatus;

    @Before
    public void setUp() {
        byteArrayOutputStream = new ByteArrayOutputStream();
        response = new HttpResponse(byteArrayOutputStream);

    }

    @After
    public void tearDown() {
        DataBase.deleteAll();
        HttpSessions.removeAll();
    }

    @Test
    public void 유저_생성_요청() throws UnsupportedEncodingException {
        //given
        httpPostRequestMessage =
                "POST /user/create HTTP/1.1\n" +
                        "Host: localhost:8080\n" +
                        "Connection: keep-alive\n" +
                        "Content-Length: 53\n" +
                        "Content-Type: application/x-www-form-urlencoded\n" +
                        "Accept: */*\n" +
                        "\n" +
                        "userId=tester&password=pass&name=myname&email=a@b.com";
        in = new ByteArrayInputStream(httpPostRequestMessage.getBytes("UTF-8"));
        request = new HttpRequest(in);
        controller = new CreateUserController();

        //when
        controller.service(request, response);
        interceptOutputStream();

        //then
        assertThat(responseCode, is("302"));
        assertThat(responseStatus, is("Found"));
        assertTrue(responseMessage.contains("Location: /index.html"));
        assertThat(DataBase.findAll().size(), is(1));
    }

    @Test
    public void 로그인_요청() throws UnsupportedEncodingException {
        //given
        User anUser = new User("tester", "pass", null, null);
        DataBase.addUser(anUser);

        httpPostRequestMessage =
                "POST /user/login HTTP/1.1\n" +
                "localhost:8080\n" +
                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\n" +
                "Accept-Encoding: gzip, deflate\n" +
                "Accept-Language: ko-kr\n" +
                "Content-Type: application/x-www-form-urlencoded\n" +
                "Content-Length: 27\n" +
                "Origin: http://localhost:8080\n" +
                "User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12) AppleWebKit/602.1.50 (KHTML, like Gecko) Version/10.0\n" +
                "\n" +
                "userId=tester&password=pass";
        in = new ByteArrayInputStream(httpPostRequestMessage.getBytes("UTF-8"));
        request = new HttpRequest(in);
        controller = new LoginController();

        //when
        controller.service(request, response);
        interceptOutputStream();

        //then
        assertThat(responseCode, is("302"));
        assertThat(responseStatus, is("Found"));
        assertTrue(responseMessage.contains("Location: /user/list"));
        User logindUser  = (User) request.getSession().getAttribute("user");
        assertThat(logindUser.getUserId(), is(anUser.getUserId()));
    }

    @Test
    public void 로그인_요청_잘못된_비밀번호() throws UnsupportedEncodingException {
        //given
        User anUser = new User("tester", "pass", null, null);
        DataBase.addUser(anUser);

        httpPostRequestMessage =
                "POST /user/login HTTP/1.1\n" +
                        "localhost:8080\n" +
                        "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\n" +
                        "Accept-Encoding: gzip, deflate\n" +
                        "Accept-Language: ko-kr\n" +
                        "Content-Type: application/x-www-form-urlencoded\n" +
                        "Content-Length: 27\n" +
                        "Origin: http://localhost:8080\n" +
                        "User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12) AppleWebKit/602.1.50 (KHTML, like Gecko) Version/10.0\n" +
                        "\n" +
                        "userId=tester&password=wrongpass";
        in = new ByteArrayInputStream(httpPostRequestMessage.getBytes("UTF-8"));
        request = new HttpRequest(in);
        controller = new LoginController();

        //when
        controller.service(request, response);
        interceptOutputStream();

        //then
        assertThat(responseCode, is("302"));
        assertThat(responseStatus, is("Found"));
        assertTrue(responseMessage.contains("Location: /user/login_failed.html"));
    }

    @Test
    public void 로그인_요청_존재하지_않는_회원() throws UnsupportedEncodingException {
        //given
        httpPostRequestMessage =
                "POST /user/login HTTP/1.1\n" +
                        "localhost:8080\n" +
                        "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\n" +
                        "Accept-Encoding: gzip, deflate\n" +
                        "Accept-Language: ko-kr\n" +
                        "Content-Type: application/x-www-form-urlencoded\n" +
                        "Content-Length: 27\n" +
                        "Origin: http://localhost:8080\n" +
                        "User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12) AppleWebKit/602.1.50 (KHTML, like Gecko) Version/10.0\n" +
                        "\n" +
                        "userId=tester&password=pass";
        in = new ByteArrayInputStream(httpPostRequestMessage.getBytes("UTF-8"));
        request = new HttpRequest(in);
        controller = new LoginController();

        //when
        controller.service(request, response);
        interceptOutputStream();

        //then
        assertThat(responseCode, is("302"));
        assertThat(responseStatus, is("Found"));
        assertTrue(responseMessage.contains("Location: /user/login_failed.html"));
    }



    @Ignore @Test
    public void 유저_목록_조회_비회원이면_로그인_페이지_리다이렉트() throws UnsupportedEncodingException {
        //given
        httpPostRequestMessage =
                "GET /user/list HTTP/1.1\n" +
                "Host: localhost:8080\n" +
                "\n";
        in = new ByteArrayInputStream(httpPostRequestMessage.getBytes("UTF-8"));
        request = new HttpRequest(in);
        controller = new ListUserController();

        //when
        controller.service(request, response);
        interceptOutputStream();

        //then
        assertThat(responseCode, is("302"));
        assertThat(responseStatus, is("Found"));
        assertTrue(responseMessage.contains("Location: /user/login.html"));
    }

    @Test
    public void 유저_목록_조회() throws UnsupportedEncodingException {
        //given
        User anUser = new User("findme", "pass", null, null);
        DataBase.addUser(anUser);
        httpPostRequestMessage =
                "GET /user/list HTTP/1.1\n" +
                        "Host: localhost:8080\n" +
                        "Cookie: logind=true\n" +
                        "\n";
        in = new ByteArrayInputStream(httpPostRequestMessage.getBytes("UTF-8"));
        request = new HttpRequest(in);
        controller = new ListUserController();

        //when
        controller.service(request, response);
        interceptOutputStream();

        //then
        assertThat(responseCode, is("200"));
        assertThat(responseStatus, is("OK"));
        assertTrue(responseMessage.contains("<td>findme</td>"));
    }

    public void interceptOutputStream() {
        byteArray = byteArrayOutputStream.toByteArray();
        responseMessage = new String(byteArray);
        parseResponseMessage(responseMessage);
    }

    public void parseResponseMessage(String requestMessage) {
        responseMessages = responseMessage.split("\n");

        String[] requestLineTokens = responseMessages[0].split(" ");
        responseCode = requestLineTokens[1].trim();
        responseStatus = requestLineTokens[2].trim();
    }
}




