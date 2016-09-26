package webserver;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 * @author Kj Nam
 * @since 2016-09-24
 */
public class RequestHandlerTest {
    private String mockHttpMessage;
    private Socket mockSocket;
    private ByteArrayOutputStream byteArrayOutputStream;
    private ByteArrayInputStream byteArrayInputStream;
    private BufferedReader bufferedReader;
    private InputStream inputStream;
    private OutputStream outputStream;
    private InputStreamReader inputStreamReader;

    @Before
    public void setUp() {
        mockSocket = mock(Socket.class);
        byteArrayOutputStream = mock(ByteArrayOutputStream.class);
        byteArrayInputStream = mock(ByteArrayInputStream.class);
        inputStreamReader = Mockito.mock(InputStreamReader.class);

        bufferedReader = mock(BufferedReader.class);
        inputStream = mock(InputStream.class);
        outputStream = mock(OutputStream.class);
        mockHttpMessage = "GET /index.html HTTP/1.1Host: localhost:8080Connection: keep-aliveCache-Control: max-age=0Upgrade-Insecure-Requests: 1User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.116 Safari/537.36Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8Accept-Encoding: gzip, deflate, sdchAccept-Language: ko-KR,ko;q=0.8,en-US;q=0.6,en;q=0.4Cookie: Idea-5b5ff5ea=af3e68fa-3356-4dc3-8a2b-bb6b9d738798";
        String tmp = "/user/create?userId=id&password=pass&name=nam&email=kyujnam%40gmail.com";
    }

    @After
    public void tearDown() {

    }

    @Ignore @Test
    public void test() throws Exception {
        //given
        when(mockSocket.getPort()).thenReturn(8080);
        when(mockSocket.getOutputStream()).thenReturn(outputStream);
        when(mockSocket.getInputStream()).thenReturn(inputStream);
        when(bufferedReader.readLine())
                .thenReturn("POST /user/create HTTP/1.1")
                .thenReturn("Host: localhost:8080")
                .thenReturn("Content-Length: 53")
                .thenReturn("Content-Type: application/x-www-form-urlencoded");
        RequestHandler handler = new RequestHandler(mockSocket);


        //when
        handler.run();

        //then
        //No Exception
    }
}