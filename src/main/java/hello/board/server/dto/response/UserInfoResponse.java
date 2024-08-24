package hello.board.server.dto.response;

import hello.board.server.dto.UserDto;
import lombok.Getter;

@Getter
public class UserInfoResponse {
    private long id;
    private String nickName;

    public UserInfoResponse() {
    }

    public UserInfoResponse(UserDto userDto) {
        this(userDto.getId(), userDto.getNickName());
    }

    public UserInfoResponse(long id, String nickName) {
        this.id = id;
        this.nickName = nickName;
    }
}
