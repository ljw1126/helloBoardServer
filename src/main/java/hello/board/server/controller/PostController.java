package hello.board.server.controller;

import hello.board.server.aop.LoginCheck;
import hello.board.server.dto.PostDto;
import hello.board.server.dto.UserDto;
import hello.board.server.dto.request.PostRequest;
import hello.board.server.dto.response.CommonResponse;
import hello.board.server.dto.response.PostResponse;
import hello.board.server.service.PostService;
import hello.board.server.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/post")
public class PostController {

    private final PostService postService;
    private final UserService userService;

    public PostController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    @PostMapping
    @LoginCheck(type = LoginCheck.UserType.USER)
    public ResponseEntity<Void> register(String userId, @RequestBody PostRequest postRequest) {
        UserDto userDto = userService.getUserInfo(userId);
        postService.register(userDto.getId(), postRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("my-posts")
    @LoginCheck(type = LoginCheck.UserType.USER)
    public ResponseEntity<PostResponse> getMyPosts(String userId) {
        UserDto userDto = userService.getUserInfo(userId);
        List<PostDto> postDtos = postService.getMyPosts(userDto.getId());
        return ResponseEntity.ok().body(new PostResponse(postDtos));
    }

    @PatchMapping
    @LoginCheck(type = LoginCheck.UserType.USER)
    public ResponseEntity<CommonResponse<PostRequest>> update(String userId, @RequestBody PostRequest postRequest) {
        UserDto userDto = userService.getUserInfo(userId);
        postService.update(userDto.getId(), postRequest);

        CommonResponse<PostRequest> response = CommonResponse.success("SUCCESS", postRequest);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{postId}")
    @LoginCheck(type = LoginCheck.UserType.USER)
    public ResponseEntity<CommonResponse<Long>> deleteBy(String userId, @PathVariable(name = "postId") long postId) {
        UserDto userDto = userService.getUserInfo(userId);
        postService.deleteBy(userDto.getId(), postId);

        CommonResponse<Long> response = CommonResponse.success("SUCCESS", postId);
        return ResponseEntity.ok(response);
    }

}
