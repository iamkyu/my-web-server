package webserver;

import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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

    @Before
    public void setUp() {
        mockSocket = mock(Socket.class);
        mockHttpMessage = "GET /index.html HTTP/1.1Host: localhost:8080Connection: keep-aliveCache-Control: max-age=0Upgrade-Insecure-Requests: 1User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.116 Safari/537.36Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8Accept-Encoding: gzip, deflate, sdchAccept-Language: ko-KR,ko;q=0.8,en-US;q=0.6,en;q=0.4Cookie: Idea-5b5ff5ea=af3e68fa-3356-4dc3-8a2b-bb6b9d738798";
        String tmp = "/user/create?userId=id&password=pass&name=nam&email=kyujnam%40gmail.com";
    }

    @After
    public void tearDown() {

    }

    @Test
    public void 소켓이_생성되면_리퀘스트_핸들러가_수행된다() throws Exception {
        //given
        byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayInputStream = new ByteInputStream();

        when(mockSocket.getOutputStream())
                .thenReturn(byteArrayOutputStream);
        when(mockSocket.getInputStream())
                .thenReturn(byteArrayInputStream);

        //when
        RequestHandler requestHandler = new RequestHandler(mockSocket);
        requestHandler.run();

        //then
        //No Exception
    }
}