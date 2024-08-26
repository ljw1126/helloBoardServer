package hello.board.server.dto.response;

import hello.board.server.dto.PostDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse {
    private List<PostDto> postDtos;
}
