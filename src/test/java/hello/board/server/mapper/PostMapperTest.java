package hello.board.server.mapper;

import hello.board.server.dto.PostDto;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

@MybatisTest
class PostMapperTest {

    @Autowired
    private PostMapper postMapper;

    @Test
    void register() {
        PostDto postDto = createPostDto(1L, 1);

        int count = postMapper.register(postDto);

        assertThat(count).isPositive();
    }

    @Test
    void selectByUserId() {
        long userId = 1L;
        postMapper.register(createPostDto(userId, 1));
        postMapper.register(createPostDto(userId, 2));

        List<PostDto> postDtos = postMapper.selectByUserId(userId);

        assertThat(postDtos).hasSize(2);
    }

    @Test
    void selectByUserId_게시글이없으면_빈ArrayList를_반환한다() {
        List<PostDto> postDtos = postMapper.selectByUserId(99L);

        assertThat(postDtos).isInstanceOf(ArrayList.class)
                .isEmpty();
    }

    @Test
    void selectById() {
        PostDto postDto = createPostDto(1L, 1);
        postMapper.register(postDto);

        PostDto result = postMapper.selectById(postDto.getId());

        assertThat(result).isNotNull();
        assertThat(result).extracting("userId", "categoryId")
                .containsExactly(1L, 1);
    }

    @Test
    void selectById_조회데이터가없으면_null반환한다() {
        PostDto postDto = postMapper.selectById(99L);

        assertThat(postDto).isNull();
    }

    @Test
    void deleteById() {
        PostDto postDto = createPostDto(1L, 1);
        postMapper.register(postDto);

        assertThatNoException()
                .isThrownBy(() -> postMapper.deleteById(postDto.getId()));
    }

    private static PostDto createPostDto(long userId, int categoryId) {
        return PostDto.builder()
                .name("제목")
                .contents("내용")
                .userId(userId)
                .categoryId(categoryId)
                .createTime(LocalDateTime.now())
                .build();
    }

}
