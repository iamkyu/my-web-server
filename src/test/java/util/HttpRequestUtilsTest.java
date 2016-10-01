package util;

import org.junit.Test;
import util.HttpRequestUtils.Pair;

import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.*;

public class HttpRequestUtilsTest {
    @Test
    public void parseQueryString() {
        String queryString = "userId=javajigi";
        Map<String, String> parameters = HttpRequestUtils.parseQueryString(queryString);
        assertThat(parameters.get("userId"), is("javajigi"));
        assertThat(parameters.get("password"), is(nullValue()));

        queryString = "userId=javajigi&password=password2";
        parameters = HttpRequestUtils.parseQueryString(queryString);
        assertThat(parameters.get("userId"), is("javajigi"));
        assertThat(parameters.get("password"), is("password2"));
    }

    @Test
    public void parseQueryString_null() {
        Map<String, String> parameters = HttpRequestUtils.parseQueryString(null);
        assertThat(parameters.isEmpty(), is(true));

        parameters = HttpRequestUtils.parseQueryString("");
        assertThat(parameters.isEmpty(), is(true));

        parameters = HttpRequestUtils.parseQueryString(" ");
        assertThat(parameters.isEmpty(), is(true));
    }

    @Test
    public void parseQueryString_invalid() {
        String queryString = "userId=javajigi&password";
        Map<String, String> parameters = HttpRequestUtils.parseQueryString(queryString);
        assertThat(parameters.get("userId"), is("javajigi"));
        assertThat(parameters.get("password"), is(nullValue()));
    }

    @Test
    public void parseCookies() {
        String cookies = "logined=true; JSessionId=1234";
        Map<String, String> parameters = HttpRequestUtils.parseCookies(cookies);
        assertThat(parameters.get("logined"), is("true"));
        assertThat(parameters.get("JSessionId"), is("1234"));
        assertThat(parameters.get("session"), is(nullValue()));
    }

    @Test
    public void getKeyValue() throws Exception {
        Pair pair = HttpRequestUtils.getKeyValue("userId=javajigi", "=");
        assertThat(pair, is(new Pair("userId", "javajigi")));
    }

    @Test
    public void getKeyValue_invalid() throws Exception {
        Pair pair = HttpRequestUtils.getKeyValue("userId", "=");
        assertThat(pair, is(nullValue()));
    }

    @Test
    public void parseHeader() throws Exception {
        String header = "Content-Length: 59";
        Pair pair = HttpRequestUtils.parseHeader(header);
        assertThat(pair, is(new Pair("Content-Length", "59")));
    }

    @Test
    public void 값은_키와_값을_가지면_같은_해쉬코드를_반환한다_hashCode() {
        //given
        Pair pair1 = HttpRequestUtils.getKeyValue("userId=javajigi", "=");
        Pair pair2 = HttpRequestUtils.getKeyValue("userId=javajigi", "=");

        //when
        int pair1Hash = pair1.hashCode();
        int pair2Hash = pair2.hashCode();

        //then
        assertThat(pair1Hash, is(pair2Hash));
    }

    @Test
    public void 값은_키와_값을_가지면_동등한_객체로_판단한다_equals() {
        //given
        Pair pair1 = HttpRequestUtils.getKeyValue("userId=javajigi", "=");
        Pair pair2 = new Pair("userId", "javajigi");
        Object obj = new Pair("userId", "javajigi");
        Object noKeylObj = new Pair("", "javajigi");
        Object noValObj = new Pair("userId", "");

        //when then
        assertTrue(pair1.equals(obj));
        assertTrue(pair1.equals(pair2));
        assertFalse(pair1.equals(null));
        assertFalse(pair1.equals(noKeylObj));
        assertFalse(pair1.equals(noValObj));
    }

    @Test
    public void 키와_값_정보를_String형으로_반환한다_toString() {
        //given
        Pair pair1 = HttpRequestUtils.getKeyValue("userId=javajigi", "=");

        //when
        String pair1Str = pair1.toString();

        //then
        assertThat(pair1Str.toString(), is("Pair [key=userId, value=javajigi]"));
    }
}
