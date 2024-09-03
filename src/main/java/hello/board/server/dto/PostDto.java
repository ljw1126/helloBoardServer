package hello.board.server.dto;

import hello.board.server.dto.request.PostRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostDto implements Serializable {
    private static final long serialVersionUID = 987654321L;

    private long id;
    private String name;
    private String contents;
    private int categoryId;
    private long userId;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private int views;

    public static PostDto of(long userId, PostRequest postRequest) {
        return PostDto.builder()
                .name(postRequest.getName())
                .contents(postRequest.getContents())
                .categoryId(postRequest.getCategoryId())
                .userId(userId)
                .createTime(LocalDateTime.now())
                .build();
    }

    public boolean validateOwnerShip(long userId) {
        return this.userId == userId;
    }

    public void updated(PostRequest postRequest) {
        this.name = postRequest.getName();
        this.contents = postRequest.getContents();
        this.categoryId = postRequest.getCategoryId();
        this.updateTime = LocalDateTime.now();
    }
}
