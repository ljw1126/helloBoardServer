package hello.board.server.aop;

import hello.board.server.utils.SessionUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginCheckAspectTest {

    @Mock
    private ProceedingJoinPoint proceedingJoinPoint;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpSession session;

    @Mock
    private LoginCheck loginCheck;

    @InjectMocks
    private LoginCheckAspect loginCheckAspect;

    @BeforeEach
    void setUp() {
        // AOP 실행시 RequestContextHolder 에서 reuqest를 가져온다
        ServletRequestAttributes servletRequestAttributes = new ServletRequestAttributes(request);
        RequestContextHolder.setRequestAttributes(servletRequestAttributes);
    }

    @Test
    void loginCheckAspectTest() throws Throwable {
        when(request.getSession(false)).thenReturn(session);
        when(loginCheck.type()).thenReturn(LoginCheck.UserType.USER);
        when(SessionUtil.getLoginMemberId(session)).thenReturn("tester");
        when(proceedingJoinPoint.getArgs()).thenReturn(new Object[]{""});
        when(proceedingJoinPoint.proceed(any(Object[].class))).thenReturn("success");

        Object result = loginCheckAspect.doAtAnnotation(proceedingJoinPoint, loginCheck);
        assertThat(result).isEqualTo("success");

        verify(proceedingJoinPoint).proceed(new Object[]{"tester"});
    }

    @Test
    void 세션이_null이면_예외를_반환한다() {
        when(request.getSession(false)).thenReturn(null);

        assertThatThrownBy(() -> loginCheckAspect.doAtAnnotation(proceedingJoinPoint, loginCheck))
                .isInstanceOf(HttpClientErrorException.class)
                .hasMessage("401 로그인이 필요한 서비스입니다");
    }

    @Test
    void 세션은_존재하지만_userId가_null이면_예외를반환다() {
        when(request.getSession(false)).thenReturn(session);
        when(loginCheck.type()).thenReturn(LoginCheck.UserType.USER);
        when(SessionUtil.getLoginMemberId(session)).thenReturn(null);

        assertThatThrownBy(() -> loginCheckAspect.doAtAnnotation(proceedingJoinPoint, loginCheck))
                .isInstanceOf(HttpClientErrorException.class)
                .hasMessage("401 로그인이 필요한 서비스입니다");
    }
}
