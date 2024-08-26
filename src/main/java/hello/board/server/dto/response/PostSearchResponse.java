package hello.board.server.dto.response;

import hello.board.server.dto.PostDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostSearchResponse {
    private List<PostDto> postDtos;
}
