package hello.board.server.dto.request;

import hello.board.server.dto.SortStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PostSearchRequest {
    private String name;
    private String contents;
    private int categoryId;
    private SortStatus sortStatus;
}
