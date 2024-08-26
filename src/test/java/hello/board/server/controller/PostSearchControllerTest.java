package hello.board.server.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hello.board.server.dto.PostDto;
import hello.board.server.dto.request.PostSearchRequest;
import hello.board.server.service.PostSearchService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PostSearchController.class)
class PostSearchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PostSearchService postSearchService;

    @Test
    void searchPosts() throws Exception {
        PostDto post1 = create(1L, 1L);
        PostDto post2 = create(2L, 1L);
        when(postSearchService.searchPosts(any()))
                .thenReturn(List.of(post1, post2));

        mockMvc.perform(post("/search")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new PostSearchRequest()))
        ).andExpectAll(status().isOk(),
                jsonPath("$.postDtos.length()").value(2),
                jsonPath("$.postDtos[0].id").value(1L),
                jsonPath("$.postDtos[1].id").value(2L)
        );
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
