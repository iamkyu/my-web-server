package model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * @author Kj Nam
 * @since 2016-10-01
 */
public class UserTest {

    private User user1;
    private String id;
    private String password;
    private String name;
    private String email;

    @Before
    public void setUp() {
        id = "user1";
        password = "password";
        name = "name1";
        email = "a@b.com";
    }

    @After
    public void tearDown() {
    }

    @Test
    public void 유저_생성시_모든_정보를_생성자로_전달해야_한다() {
        //given
        user1 = new User(id, password, name, email);

        //when then
        assertThat(user1.getUserId(), is(id));
        assertThat(user1.getPassword(), is(password));
        assertThat(user1.getName(), is(name));
        assertThat(user1.getEmail(), is(email));
    }

    @Test
    public void 비밀번호_일치_여부를_확인후_boolean_값을_반환한다_isLogin() {
        //given
        user1 = new User(id, password, name, email);

        //when
        boolean isEqualsPassword = user1.login(password);

        //then
        assertTrue(isEqualsPassword);
    }

    @Test
    public void 회원정보를_String형으로_반환한다_toString() {
        //given
        user1 = new User(id, password, name, email);
        String userInfo = "User [userId=" + id + ", password=" + password + ", name=" + name + ", email=" + email + "]";

        //when
        String getInfo = user1.toString();

        //then
        assertTrue(getInfo.equals(userInfo));
    }
}