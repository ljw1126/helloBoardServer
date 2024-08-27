package hello.board.server.service.impl;

import hello.board.server.dto.PostDto;
import hello.board.server.dto.request.PostRequest;
import hello.board.server.exception.PostDeleteFailedException;
import hello.board.server.exception.PostUpdateFailedException;
import hello.board.server.mapper.PostMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostServiceImplTest {

    @Mock
    private PostMapper postMapper;

    @InjectMocks
    private PostServiceImpl postService;

    @Test
    void register() {
        long userId = 1L;
        PostRequest postRequest = new PostRequest(0L, "제목", "내용", 1);

        when(postMapper.register(any()))
                .thenReturn(1);

        assertThatNoException()
                .isThrownBy(() -> postService.register(userId, postRequest));
    }

    @Test
    void 게시글신규생성시_반환값이0이면_예외를던진다() {
        long userId = 1L;
        PostRequest postRequest = new PostRequest(0L, "제목", "내용", 1);

        when(postMapper.register(any()))
                .thenReturn(0);

        assertThatThrownBy(() -> postService.register(userId, postRequest))
                .isInstanceOf(IllegalStateException.class);
    }

    // https://www.arhohuttunen.com/test-data-builders/
    @Test
    void getMyPosts_작성자_키값으로_게시글을_조회한다() {
        long userId = 1L;

        when(postMapper.selectByUserId(userId))
                .thenReturn(List.of(new PostDto(), new PostDto()));

        List<PostDto> myPosts = postService.getMyPosts(userId);
        assertThat(myPosts).hasSize(2);
    }

    @Test
    void update() {
        PostDto postDto = PostDto.builder()
                .id(2L)
                .name("제목")
                .contents("내용")
                .createTime(LocalDateTime.now())
                .categoryId(1)
                .userId(1L)
                .build();

        when(postMapper.selectById(anyLong()))
                .thenReturn(postDto);
        when(postMapper.update(any()))
                .thenReturn(1);

        long userId = 1L;
        PostRequest postRequest = new PostRequest(2L, "수정된 제목", "수정된 내용", 2);
        postService.update(userId, postRequest);

        verify(postMapper, times(1))
                .selectById(anyLong());
        verify(postMapper, times(1))
                .update(any());
        assertThat(postDto).extracting("name", "contents", "categoryId")
                .containsExactly("수정된 제목", "수정된 내용", 2);
    }

    @Test
    void 수정시_게시글이_조회되지않으면_예외를_던진다() {
        when(postMapper.selectById(anyLong()))
                .thenReturn(null);

        long userId = 99L;
        PostRequest postRequest = new PostRequest(2L, "수정된 제목", "수정된 내용", 2);
        assertThatThrownBy(() -> postService.update(userId, postRequest))
                .isInstanceOf(PostUpdateFailedException.class)
                .hasMessage("Post not found");
    }

    @Test
    void 게시글_작성자가_아니면_예외를던진다() {
        PostDto postDto = PostDto.builder()
                .id(2L)
                .name("제목")
                .contents("내용")
                .createTime(LocalDateTime.now())
                .categoryId(1)
                .userId(1L)
                .build();

        when(postMapper.selectById(anyLong()))
                .thenReturn(postDto);

        long userId = 99L;
        PostRequest postRequest = new PostRequest(2L, "수정된 제목", "수정된 내용", 2);
        assertThatThrownBy(() -> postService.update(userId, postRequest))
                .isInstanceOf(PostUpdateFailedException.class);
    }

    @Test
    void 게시글_수정에_실패하면_예외를던진다() {
        PostDto postDto = PostDto.builder()
                .id(2L)
                .name("제목")
                .contents("내용")
                .createTime(LocalDateTime.now())
                .categoryId(1)
                .userId(1L)
                .build();

        when(postMapper.selectById(anyLong()))
                .thenReturn(postDto);
        when(postMapper.update(any()))
                .thenReturn(0);

        long userId = 1L;
        PostRequest postRequest = new PostRequest(2L, "수정된 제목", "수정된 내용", 2);
        assertThatThrownBy(() -> postService.update(userId, postRequest))
                .isInstanceOf(PostUpdateFailedException.class);
    }

    @Test
    void deleteBy() {
        PostDto postDto = PostDto.builder()
                .id(2L)
                .name("제목")
                .contents("내용")
                .createTime(LocalDateTime.now())
                .categoryId(1)
                .userId(1L)
                .build();

        when(postMapper.selectById(anyLong()))
                .thenReturn(postDto);
        doNothing().when(postMapper)
                .deleteById(anyLong());

        long userId = 1L;
        long postId = 2L;
        assertThatNoException()
                .isThrownBy(() -> postService.deleteBy(userId, postId));

        verify(postMapper, times(1))
                .deleteById(postId);
    }

    @ParameterizedTest
    @CsvSource(value = {"1, 0", "0, 1"})
    void 삭제시_유효하지않은_키값이_주어지면_예외를_던진다(String arg1, String arg2) {
        long userId = Long.parseLong(arg1);
        long postId = Long.parseLong(arg2);

        assertThatThrownBy(() -> postService.deleteBy(userId, postId))
                .isInstanceOf(PostDeleteFailedException.class);
    }

    @Test
    void 삭제시_게시글이_조회되지않으면_예외를던진다() {
        when(postMapper.selectById(anyLong()))
                .thenReturn(null);

        long userId = 1L;
        long postId = 99L;
        assertThatThrownBy(() -> postService.deleteBy(userId, postId))
                .isInstanceOf(PostDeleteFailedException.class);
    }

    @Test
    void 삭제시_게시글_작성자가_아니면_예외를_던진다() {
        PostDto postDto = PostDto.builder()
                .id(2L)
                .name("제목")
                .contents("내용")
                .createTime(LocalDateTime.now())
                .categoryId(1)
                .userId(1L)
                .build();

        when(postMapper.selectById(anyLong()))
                .thenReturn(postDto);

        long userId = 99L;
        long postId = 2L;
        assertThatThrownBy(() -> postService.deleteBy(userId, postId))
                .isInstanceOf(PostDeleteFailedException.class);
    }
}
