package http;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * @author Kj Nam
 * @since 2017-04-14
 */
public class HeadersTest {
    @Test
    public void 헤더를_추가한다() {
        //given
        Headers headers = new Headers();

        //when
        headers.add("Connection: keep-alive\n");

        //then
        assertThat(headers.get("Connection"), is("keep-alive"));
    }

    @Test
    public void 쿠키에서_로그인_상태를_얻는다() {
        //given
        Headers headers = new Headers();

        //when
        headers.add("Cookie: login=true;\n");

        //then
        assertTrue(headers.getLoginStatus());
    }
}