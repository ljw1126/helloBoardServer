package hello.board.server.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hello.board.server.dto.PostDto;
import hello.board.server.dto.UserDto;
import hello.board.server.dto.request.PostRequest;
import hello.board.server.service.PostService;
import hello.board.server.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static hello.board.server.utils.SHA256Util.encrptySHA256;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PostController.class)
class PostControllerTest {
    @MockBean
    private PostService postService;

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void register() throws Exception {
        UserDto userDto = new UserDto("tester", encrptySHA256("123"), "테스터", false, UserDto.Status.DEFAULT, LocalDateTime.now(), null);
        when(userService.getUserInfo(any()))
                .thenReturn(userDto);

        doNothing()
                .when(postService).register(anyLong(), any());

        PostRequest postRequest = new PostRequest(0L, "제목", "내용", 1);
        mockMvc.perform(post("/post")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(postRequest))
        ).andExpect(status().isCreated());
    }

    @Test
    void getMyPosts() throws Exception {
        UserDto userDto = new UserDto("tester", encrptySHA256("123"), "테스터", false, UserDto.Status.DEFAULT, LocalDateTime.now(), null);
        when(userService.getUserInfo(any()))
                .thenReturn(userDto);

        PostDto post1 = create(1L, 1L);
        PostDto post2 = create(2L, 1L);
        when(postService.getMyPosts(anyLong()))
                .thenReturn(List.of(post1, post2));

        mockMvc.perform(get("/post/my-posts"))
                .andExpectAll(status().isOk(),
                        jsonPath("$.postDtos.length()").value(2),
                        jsonPath("$.postDtos[0].id").value(1L),
                        jsonPath("$.postDtos[1].id").value(2L)
                );
    }

    @Test
    void update() throws Exception {
        UserDto userDto = new UserDto("tester", encrptySHA256("123"), "테스터", false, UserDto.Status.DEFAULT, LocalDateTime.now(), null);
        when(userService.getUserInfo(any()))
                .thenReturn(userDto);

        doNothing()
                .when(postService).update(anyLong(), any());

        PostRequest postRequest = new PostRequest(1L, "수정된 제목", "수정된 내용", 2);
        mockMvc.perform(patch("/post")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(postRequest))
        ).andExpectAll(status().isOk(),
                jsonPath("$.status").value(HttpStatus.OK.name()),
                jsonPath("$.code").value(HttpStatus.OK.value()),
                jsonPath("$.message").value("SUCCESS"),
                jsonPath("$.body.id").value(1L),
                jsonPath("$.body.name").value("수정된 제목"),
                jsonPath("$.body.contents").value("수정된 내용")
        );
    }

    @Test
    void delete() throws Exception {
        UserDto userDto = new UserDto("tester", encrptySHA256("123"), "테스터", false, UserDto.Status.DEFAULT, LocalDateTime.now(), null);
        when(userService.getUserInfo(any()))
                .thenReturn(userDto);

        doNothing()
                .when(postService).deleteBy(anyLong(), anyLong());

        mockMvc.perform(MockMvcRequestBuilders.delete("/post/1"))
                .andExpectAll(status().isOk(),
                        jsonPath("$.status").value(HttpStatus.OK.name()),
                        jsonPath("$.code").value(HttpStatus.OK.value()),
                        jsonPath("$.message").value("SUCCESS"),
                        jsonPath("$.body").value(1L)
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
