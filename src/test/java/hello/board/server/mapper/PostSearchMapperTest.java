package hello.board.server.mapper;

import hello.board.server.dto.PostDto;
import hello.board.server.dto.request.PostSearchRequest;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@MybatisTest
class PostSearchMapperTest {

    @Autowired
    private PostSearchMapper postSearchMapper;

    @Autowired
    private PostMapper postMapper;


    @Test
    void searchPost() {
        PostDto post1 = create(1L, 1L);
        PostDto post2 = create(2L, 1L);
        postMapper.register(post1);
        postMapper.register(post2);

        List<PostDto> postDtos = postSearchMapper.searchPosts(new PostSearchRequest("", "", 1, null));

        assertThat(postDtos).hasSize(2);
    }

    private static PostDto create(long postId, long userId) {
        return PostDto.builder()
                .id(postId)
                .name("제목" + postId)
                .contents("내용" + postId)
                .userId(userId)
                .categoryId(1)
                .createTime(LocalDateTime.now())
                .build();
    }
}
