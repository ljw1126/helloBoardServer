package hello.board.server.controller;

import hello.board.server.dto.PostDto;
import hello.board.server.dto.request.PostSearchRequest;
import hello.board.server.dto.response.PostSearchResponse;
import hello.board.server.service.PostSearchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/search")
public class PostSearchController {
    private final PostSearchService postSearchService;

    public PostSearchController(PostSearchService postSearchService) {
        this.postSearchService = postSearchService;
    }

    @PostMapping
    public ResponseEntity<PostSearchResponse> searchPosts(@RequestBody PostSearchRequest postSearchRequest) {
        List<PostDto> postDtos = postSearchService.searchPosts(postSearchRequest);
        return ResponseEntity.ok(new PostSearchResponse(postDtos));
    }
}
