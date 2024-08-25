package hello.board.server.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@Builder
public class UserDto {

    public enum Status {
        DEFAULT, ADMIN, DELETED;

        boolean isAdmin() {
            return this == ADMIN;
        }
    }

    private long id;
    private String userId;
    private String password;
    private String nickName;
    private boolean isAdmin;
    private Status status = Status.DEFAULT;
    private boolean isWithDraw; // 탈퇴 여부
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public UserDto() {
    }

    public UserDto(String userId, String password, String nickName, boolean isAdmin, Status status, LocalDateTime createTime, LocalDateTime updateTime) {
        this(0L, userId, password, nickName, isAdmin, status, false, createTime, updateTime);
    }

    public boolean hasNullDataBeforeSignup() {
        return this.userId == null || this.password == null || this.nickName == null;
    }

    public boolean isAdmin() {
        return status.isAdmin();
    }
}
