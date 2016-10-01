package http;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static http.HttpMethod.GET;
import static http.HttpMethod.POST;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Kj Nam
 * @since 2016-10-01
 */
public class RequestLineTest {
    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void GET_요청_라인을_전송하면_메서드와_경로와_파라미터를_분리한다() {
        //given
        String getRequestLine =
                "GET /user/create?userId=tester&password=pass&name=myname&email=a@b.com HTTP/1.1";

        //when
        RequestLine requestLine = new RequestLine(getRequestLine);

        //then
        assertThat(requestLine.getMethod(), is(GET));
        assertThat(requestLine.getPath(), is("/user/create"));
        assertTrue(requestLine.getParams().size() == 4);
    }

    @Test
    public void GET_요청_라인에_파라미터가_없으면_메서드와_경로만_분리한다() {
        //given
        String getRequestLine =
                "GET /user/create HTTP/1.1";

        //when
        RequestLine requestLine = new RequestLine(getRequestLine);

        //then
        assertThat(requestLine.getMethod(), is(GET));
        assertThat(requestLine.getPath(), is("/user/create"));
        assertNull(requestLine.getParams());
    }

    @Test
    public void POST_요청_라인을_전송하면_메서드와_경로를_분리한다() {
        //given
        String postRequestLine =
                "POST /user/create HTTP/1.1";

        //when
        RequestLine requestLine = new RequestLine(postRequestLine);

        //then
        assertThat(requestLine.getMethod(), is(POST));
        assertThat(requestLine.getPath(), is("/user/create"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void 잘못된_형식의_요청_라인이_전송되면_예외가_발생한다() {
        //given
        String wrongRequestLine = "WRONG";

        //when
        RequestLine requestLine = new RequestLine(wrongRequestLine);

        //then
        //exception
    }
}