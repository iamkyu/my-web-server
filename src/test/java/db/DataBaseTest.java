package db;

import model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Kj Nam
 * @since 2016-10-01
 */
public class DataBaseTest {
    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
        DataBase.deleteAll();
    }

    @Test
    public void 여러_DataBase를_생성하여_사용해도_하나의_static_회원_목록으로_관리한다() {
        //given
        DataBase dataBase1 = new DataBase();
        DataBase dataBase2 = new DataBase();
        User tester1 = new User("tester1", "password", "name1", "a@b.com");

        //when
        dataBase1.addUser(tester1);
        User foundFromDb1 = dataBase1.findUserById(tester1.getUserId());
        User foundFromDb2 = dataBase2.findUserById(tester1.getUserId());

        //then
        assertThat(foundFromDb1.getUserId(), is(tester1.getUserId()));
        assertThat(foundFromDb2.getUserId(), is(tester1.getUserId()));
    }

    @Test
    public void 아이디로_회원을_검색한다() {
        //given
        User tester = new User("tester", "password", "name", "a@b.com");

        //when
        DataBase.addUser(tester);
        User found = DataBase.findUserById(tester.getUserId());

        //then
        assertThat(found.getUserId(), is(tester.getUserId()));
    }

    @Test
    public void 모든_회원_목록을_가져온다() {
        //given
        User tester1 = new User("tester1", "password", "name1", "a@b.com");
        User tester2 = new User("tester2", "password", "name2", "b@c.com");

        //when
        DataBase.addUser(tester1);
        DataBase.addUser(tester2);
        Collection<User> users = DataBase.findAll();

        //then
        assertThat(users.size(), is(2));

    }
}