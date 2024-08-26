package hello.board.server.mapper;

import hello.board.server.dto.PostDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PostMapper {
    int register(PostDto postDto);

    List<PostDto> selectByUserId(@Param(value = "userId") long userId);

    PostDto selectById(@Param(value = "id") long id);

    int update(PostDto postDto);

    void deleteById(@Param("id") long postId);
}
