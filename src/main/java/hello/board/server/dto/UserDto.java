package hello.board.server.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
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

    public UserDto(long id, String userId, String password, String nickName, boolean isAdmin, Status status, boolean isWithDraw, LocalDateTime createTime, LocalDateTime updateTime) {
        this.id = id;
        this.userId = userId;
        this.password = password;
        this.nickName = nickName;
        this.isAdmin = isAdmin;
        this.status = status;
        this.isWithDraw = isWithDraw;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public boolean hasNullDataBeforeSignup() {
        return this.userId == null || this.password == null || this.nickName == null;
    }

    public boolean isAdmin() {
        return status.isAdmin();
    }
}
