package hello.board.server.dto.response;

import hello.board.server.dto.UserDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {
    enum LoginStatus {
        SUCCESS, FAIL, DELETED
    }

    private LoginStatus status;
    private long id;
    private String nickName;

    public LoginResponse() {
    }

    public LoginResponse(LoginStatus status, long id, String nickName) {
        this.status = status;
        this.id = id;
        this.nickName = nickName;
    }

    public static LoginResponse success(UserDto userDto) {
        return new LoginResponse(LoginStatus.SUCCESS, userDto.getId(), userDto.getNickName());
    }
}
