package http;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * @author Kj Nam
 * @since 2016-10-03
 */
public class HttpResponseTest {
    private HttpResponse response;
    private ByteArrayOutputStream byteArrayOutputStream;
    private byte[] byteArray;
    private String responseMessage;
    private String[] responseMessages;

    private String responseCode;
    private String responseStatus;

    @Before
    public void init() throws IOException {
        byteArrayOutputStream = new ByteArrayOutputStream();
        response = new HttpResponse(byteArrayOutputStream);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void 헤더_파라미터를_추가한다() {
        //given
        Map<String, String> result;

        //when
        response.addHeader("key", "value");

        //then
    }

    @Test
    public void 요청한_페이지로_이동시킨다_200_forward() throws IOException {
        //given

        //when
        response.forward("/index.html");
        interceptOutputStream();

        //then
        assertThat(responseCode, is("200"));
        assertThat(responseStatus, is("OK"));
        assertTrue(responseMessage.contains("Content-type: text/html;charset=utf-8"));
    }

    @Test
    public void 특정페이지로_이동시킨다_302_sendRedirect() {
        //given

        //when
        response.sendRedirect("/index.html");
        interceptOutputStream();

        //then
        assertThat(responseCode, is("302"));
        assertThat(responseStatus, is("Found"));
        assertTrue(responseMessage.contains("Location: /index.html"));
    }

    @Test
    public void 특정_리소스를_담아_응답한다_forwardBody() {
        //given
        String body = "<td><tr></tr></td>";

        //when
        response.forwardBody(body);
        interceptOutputStream();

        //then
        assertThat(responseCode, is("200"));
        assertThat(responseStatus, is("OK"));
        assertTrue(responseMessage.contains("Content-Length: " + body.length()));
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