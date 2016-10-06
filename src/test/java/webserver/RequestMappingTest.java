package webserver;

import controller.Controller;
import controller.CreateUserController;
import controller.ListUserController;
import controller.LoginController;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Kj Nam
 * @since 2016-10-06
 */
public class RequestMappingTest {
    private Controller controller;

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void 회원가입_요청시_컨트롤러_생성_테스트() {
        //given

        //when
        controller = RequestMapping.getController("/user/create");

        //then
        assertTrue(controller instanceof Controller);
        assertTrue(controller instanceof CreateUserController);
    }

    @Test
    public void 로그인_요청시_컨트롤러_생성_테스트() {
        //given

        //when
        controller = RequestMapping.getController("/user/login");

        //then
        assertTrue(controller instanceof Controller);
        assertTrue(controller instanceof LoginController);
    }

    @Test
    public void 유저_목록_조회_요청시_컨트롤러_생성_테스트() {
        //given

        //when
        controller = RequestMapping.getController("/user/list");

        //then
        assertTrue(controller instanceof Controller);
        assertTrue(controller instanceof ListUserController);
    }
}