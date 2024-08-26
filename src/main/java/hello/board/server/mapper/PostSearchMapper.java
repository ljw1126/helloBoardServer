package hello.board.server.mapper;

import hello.board.server.dto.PostDto;
import hello.board.server.dto.request.PostSearchRequest;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PostSearchMapper {
    List<PostDto> searchPosts(PostSearchRequest postSearchRequest);
}
