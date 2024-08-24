package hello.board.server.dto.request;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Setter
@Getter
public class LoginRequest {

    @NonNull
    private String userId;

    @NonNull
    private String password;

    public LoginRequest() {
    }

    public LoginRequest(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }
}
