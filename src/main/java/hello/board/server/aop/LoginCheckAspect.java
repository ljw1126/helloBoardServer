package hello.board.server.aop;


import hello.board.server.utils.SessionUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static hello.board.server.aop.LoginCheck.UserType;

@Component
@Aspect
@Slf4j
public class LoginCheckAspect {

    @Around("@annotation(hello.board.server.aop.LoginCheck) && @annotation(loginCheck)")
    public Object doAtAnnotation(ProceedingJoinPoint joinPoint, LoginCheck loginCheck) throws Throwable {
        ServletRequestAttributes servletRequestAttributes = ((ServletRequestAttributes) (RequestContextHolder.getRequestAttributes()));
        assert servletRequestAttributes != null;
        HttpServletRequest request = servletRequestAttributes.getRequest();
        HttpSession session = request.getSession(false);

        if (session == null) {
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "로그인이 필요한 서비스입니다");
        }

        String userId = getUserIdByUserType(session, loginCheck);
        if (userId == null) {
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "로그인이 필요한 서비스입니다");
        }

        Object[] args = joinPoint.getArgs();
        if (args != null && args.length > 0 && args[0] instanceof String) {
            args[0] = userId;
        }

        return joinPoint.proceed(args);
    }

    private String getUserIdByUserType(HttpSession session, LoginCheck loginCheck) {
        UserType type = loginCheck.type();
        if (type == UserType.USER) {
            return SessionUtil.getLoginMemberId(session);
        }

        return SessionUtil.getLoginAdminId(session);
    }
}
