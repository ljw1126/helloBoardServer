package hello.board.server.controller;

import hello.board.server.aop.LoginCheck;
import hello.board.server.dto.UserDto;
import hello.board.server.dto.request.DeleteUserRequest;
import hello.board.server.dto.request.LoginRequest;
import hello.board.server.dto.request.UpdateUserPasswordRequest;
import hello.board.server.dto.response.LoginResponse;
import hello.board.server.dto.response.UserInfoResponse;
import hello.board.server.service.UserService;
import hello.board.server.utils.SessionUtil;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("sign-up")
    public ResponseEntity<String> singUp(@RequestBody UserDto userDto) {
        if (userDto.hasNullDataBeforeSignup()) {
            return ResponseEntity.badRequest().body("회원가입시 필수 데이터를 모두 입력해야 합니다");
        }

        try {
            userService.register(userDto);
            return ResponseEntity.status(HttpStatus.CREATED).body("회원가입이 완료되었습니다");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("회원가입에 실패했습니다");
        }
    }

    @PostMapping("sign-in")
    public ResponseEntity<LoginResponse> signIn(@RequestBody LoginRequest loginRequest, HttpSession session) {
        String userId = loginRequest.getUserId();
        String password = loginRequest.getPassword();
        UserDto userDto = userService.login(userId, password);

        if (userDto == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        setAttributeByStatus(session, userId, userDto);

        return ResponseEntity.ok(LoginResponse.success(userDto));
    }

    private void setAttributeByStatus(HttpSession session, String userId, UserDto userDto) {
        if (userDto.isAdmin()) {
            SessionUtil.setLoginAdminId(session, userId);
            return;
        }

        SessionUtil.setLoginMemberId(session, userId);
    }

    @GetMapping("my-info")
    public ResponseEntity<UserInfoResponse> userInfo(HttpSession session) {
        String userId = SessionUtil.getLoginMemberId(session);
        if (userId.isEmpty()) {
            userId = SessionUtil.getLoginAdminId(session);
        }

        UserDto userDto = userService.getUserInfo(userId);
        return ResponseEntity.ok(new UserInfoResponse(userDto));
    }

    @LoginCheck(type = LoginCheck.UserType.USER)
    @PatchMapping("password")
    public ResponseEntity<String> updatePassword(String userId, @RequestBody UpdateUserPasswordRequest updateUserPasswordRequest,
                                                 HttpSession session) {
        try {
            String beforePassword = updateUserPasswordRequest.getBeforePassword();
            String afterPassword = updateUserPasswordRequest.getAfterPassword();

            userService.updatePassword(userId, beforePassword, afterPassword);

            return ResponseEntity.ok("Password updated successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid request data");
        }
    }

    @DeleteMapping
    public ResponseEntity<String> deleteUser(@RequestBody DeleteUserRequest deleteUserRequest, HttpSession session) {
        try {
            String userId = SessionUtil.getLoginMemberId(session);
            userService.deleteUser(userId, deleteUserRequest.getPassword());

            return ResponseEntity.ok("User deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Invalid password");
        }
    }

    @PostMapping("logout")
    public ResponseEntity<Void> logout(HttpSession session) {
        SessionUtil.clear(session);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
