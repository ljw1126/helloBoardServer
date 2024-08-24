package hello.board.server.dto.request;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class DeleteUserRequest {

    @NonNull
    private String password;

    public DeleteUserRequest() {
    }

    public DeleteUserRequest(String password) {
        this.password = password;
    }
}
