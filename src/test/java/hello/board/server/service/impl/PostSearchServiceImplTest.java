package hello.board.server.service.impl;

import hello.board.server.dto.PostDto;
import hello.board.server.dto.request.PostSearchRequest;
import hello.board.server.mapper.PostSearchMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostSearchServiceImplTest {

    @Mock
    private PostSearchMapper postSearchMapper;

    @InjectMocks
    private PostSearchServiceImpl postSearchService;

    @Test
    void searchPosts() {
        PostDto post1 = create(1L, 1L);
        PostDto post2 = create(2L, 1L);

        when(postSearchMapper.searchPosts(any()))
                .thenReturn(List.of(post1, post2));

        List<PostDto> postDtos = postSearchService.searchPosts(new PostSearchRequest());

        assertThat(postDtos).hasSize(2);
        assertThat(postDtos).extracting("id")
                .containsExactly(1L, 2L);
        verify(postSearchMapper).searchPosts(any());
    }

    private static PostDto create(long postId, long userId) {
        return PostDto.builder()
                .id(postId)
                .name("제목" + postId)
                .contents("내용" + postId)
                .userId(userId)
                .createTime(LocalDateTime.now())
                .build();
    }

}
