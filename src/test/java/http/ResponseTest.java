package http;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * @author Kj Nam
 * @since 2017-04-11
 */
public class ResponseTest {
    private Response response;
    private ByteArrayOutputStream byteArrayOutputStream;
    private byte[] byteArray;
    private String responseMessage;
    private String[] responseMessages;

    private String responseCode;
    private String responseStatus;

    @Before
    public void init() throws IOException {
        byteArrayOutputStream = new ByteArrayOutputStream();
        response = new Response(byteArrayOutputStream);
    }

    @Test
    public void 요청한_페이지로_이동시킨다_200() throws IOException {
        //given when
        response.forward("/index.html");
        interceptOutputStream();

        //then
        assertThat(responseCode, is("200"));
        assertThat(responseStatus, is("OK"));
        assertTrue(responseMessage.contains("Content-type: text/html;charset=utf-8"));
    }

    @Test
    public void 특정페이지로_리다이렉트한다_302() {
        //given when
        response.sendRedirect("/index.html");
        interceptOutputStream();

        //then
        assertThat(responseCode, is("302"));
        assertThat(responseStatus, is("Found"));
        assertTrue(responseMessage.contains("Location: /index.html"));
    }

    @Test
    public void html을_출력한다() {
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

    private void interceptOutputStream() {
        byteArray = byteArrayOutputStream.toByteArray();
        responseMessage = new String(byteArray);
        parseResponseMessage();
    }

    private void parseResponseMessage() {
        responseMessages = responseMessage.split("\n");

        String[] requestLineTokens = responseMessages[0].split(" ");
        responseCode = requestLineTokens[1].trim();
        responseStatus = requestLineTokens[2].trim();
    }
}