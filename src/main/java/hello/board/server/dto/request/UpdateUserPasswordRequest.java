package hello.board.server.dto.request;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserPasswordRequest {

    @NonNull
    private String beforePassword;

    @NonNull
    private String afterPassword;

    public UpdateUserPasswordRequest() {
    }

    public UpdateUserPasswordRequest(String beforePassword, String afterPassword) {
        this.beforePassword = beforePassword;
        this.afterPassword = afterPassword;
    }
}
