package db;

import model.User;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * @author Kj Nam
 * @since 2017-04-06
 */
public class DataBaseTest {
    @Before
    public void setUp() {
        // admin 1명이 추가 되어 있음
    }

    @Test
    public void 유저를_추가한다() {
        //given
        Collection<User> users = DataBase.findAll();
        assertThat(users.size(), is(1));

        //when
        DataBase.addUser(new User("user2", "my-password", "my-name", "my-email"));

        //then
        assertThat(users.size(), is(2));
    }

    @Test
    public void 유저아이디로_유저를_찾는다() {
        //given
        String userId = "admin";
        User user;

        //when
        user = DataBase.findUserById(userId);

        //then
        assertThat(user.getUserId(), is(userId));
    }
}