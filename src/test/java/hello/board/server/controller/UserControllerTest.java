package hello.board.server.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hello.board.server.aop.LoginCheckAspect;
import hello.board.server.dto.UserDto;
import hello.board.server.dto.request.DeleteUserRequest;
import hello.board.server.dto.request.LoginRequest;
import hello.board.server.dto.request.UpdateUserPasswordRequest;
import hello.board.server.service.UserService;
import hello.board.server.utils.SessionUtil;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import static hello.board.server.dto.UserDto.Status;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.doNothing;
import static org.mockito.BDDMockito.doThrow;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.mock;
import static org.mockito.BDDMockito.times;
import static org.mockito.BDDMockito.verify;
import static org.mockito.BDDMockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LoginCheckAspect loginCheckAspect;

    @MockBean
    private UserService userService;

    @Test
    void signUp() throws Exception {
        doNothing()
                .when(userService).register(any());

        UserDto request = new UserDto("tester", "test1234", "테스터", false, Status.DEFAULT, null, null);

        mockMvc.perform(post("/users/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(status().isCreated(),
                content().contentType("text/plain;charset=UTF-8"),
                jsonPath("$").exists(),
                jsonPath("$").value("회원가입이 완료되었습니다"));
    }

    @Test
    void 회원가입_필수_파라미터_누락한_경우() throws Exception {
        mockMvc.perform(post("/users/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new UserDto()))
        ).andExpectAll(status().isBadRequest(),
                content().contentType("text/plain;charset=UTF-8"),
                jsonPath("$").exists(),
                jsonPath("$").value("회원가입시 필수 데이터를 모두 입력해야 합니다"));
    }

    @Test
    void 회원가입_실패한경우() throws Exception {
        doThrow(new RuntimeException())
                .when(userService).register(any());

        UserDto request = new UserDto("tester", "test1234", "테스터", false, Status.DEFAULT, null, null);
        mockMvc.perform(post("/users/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(status().isInternalServerError(),
                content().contentType("text/plain;charset=UTF-8"),
                jsonPath("$").exists(),
                jsonPath("$").value("회원가입에 실패했습니다"));
    }

    @Test
    void signIn() throws Exception {
        given(userService.login(any(), any()))
                .willReturn(new UserDto("tester", "test1234", "테스터", false, Status.DEFAULT, null, null));

        mockMvc.perform(post("/users/sign-in")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new LoginRequest("tester", "tester123")))
        ).andExpectAll(status().isOk(),
                content().contentType(MediaType.APPLICATION_JSON),
                jsonPath("$").exists(),
                jsonPath("$.status").value("SUCCESS"),
                jsonPath("$.id").value(0),
                jsonPath("$.nickName").value("테스터"));
    }

    @Test
    void 로그인할때_회원정보가_조회되지_않는경우() throws Exception {
        given(userService.login(any(), any()))
                .willReturn(null);

        mockMvc.perform(post("/users/sign-in")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new LoginRequest("unknown", "invalid")))
        ).andExpectAll(status().isUnauthorized());
    }

    @Test
    void userInfo() throws Exception {
        String userId = "tester";
        HttpSession session = mock(HttpSession.class);
        when(SessionUtil.getLoginMemberId(session)).thenReturn(userId);

        given(userService.getUserInfo(any()))
                .willReturn(new UserDto("tester", "test1234", "테스터", false, Status.DEFAULT, null, null));

        mockMvc.perform(get("/users/my-info")
                .contentType(MediaType.APPLICATION_JSON)
                .sessionAttr("LOGIN_MEMBER_ID", userId) // Mock session attribute
        ).andExpectAll(status().isOk(),
                content().contentType(MediaType.APPLICATION_JSON),
                jsonPath("$").exists(),
                jsonPath("$.id").value(0),
                jsonPath("$.nickName").value("테스터"));
    }

    /**
     * spring-aop에서 userId 전달하기 때문에 통합테스트로 다루는게 적합하다 판
     *
     * @WebMvcTest 적합하지 않아 null 처리
     */
    @Test
    void updatePassword() throws Exception {
        doNothing()
                .when(userService).updatePassword(any(), any(), any());

        mockMvc.perform(patch("/users/password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new UpdateUserPasswordRequest("test1234", "qwer1234!@#$")))
        ).andExpectAll(status().isOk(),
                jsonPath("$").value("Password updated successfully"));

        verify(userService, times(1)).updatePassword(null, "test1234", "qwer1234!@#$");
    }

    @Test
    void 비밀번호_변경시_예외발생할_경우() throws Exception {
        doThrow(new IllegalArgumentException())
                .when(userService).updatePassword(any(), any(), any());

        mockMvc.perform(patch("/users/password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new UpdateUserPasswordRequest("test1234", "qwer1234!@#$")))
        ).andExpectAll(status().isBadRequest(),
                jsonPath("$").value("Invalid request data"));
    }

    @Test
    void deleteUser() throws Exception {
        // Mock session behavior
        String userId = "tester";
        HttpSession session = mock(HttpSession.class);
        when(SessionUtil.getLoginMemberId(session)).thenReturn(userId);

        doNothing()
                .when(userService).deleteUser(any(), any());

        mockMvc.perform(delete("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new DeleteUserRequest("password")))
                        .sessionAttr("LOGIN_MEMBER_ID", userId) // Mock session attribute
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$").value("User deleted successfully"));
    }

    @Test
    void 삭제요청시_예외발생한경우() throws Exception {
        doThrow(new RuntimeException())
                .when(userService).deleteUser(any(), any());

        mockMvc.perform(delete("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new DeleteUserRequest("invalidPassword")))
                ).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Invalid password"));
    }

    @Test
    void logout() throws Exception {
        MockHttpSession session = new MockHttpSession();

        mockMvc.perform(post("/users/logout")
                .contentType(MediaType.APPLICATION_JSON)
                .session(session)
        ).andExpect(status().isOk());
    }
}
