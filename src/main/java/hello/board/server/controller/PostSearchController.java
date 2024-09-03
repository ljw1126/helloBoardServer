package hello.board.server.controller;

import hello.board.server.dto.PostDto;
import hello.board.server.dto.request.PostSearchRequest;
import hello.board.server.dto.response.PostSearchResponse;
import hello.board.server.service.PostSearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Slf4j
@RestController
@RequestMapping("/search")
public class PostSearchController {
    private final PostSearchService postSearchService;
    private final CacheManager cacheManager;

    public PostSearchController(PostSearchService postSearchService, CacheManager cacheManager) {
        this.postSearchService = postSearchService;
        this.cacheManager = cacheManager;
    }

    @PostMapping
    public ResponseEntity<PostSearchResponse> searchPosts(@RequestBody PostSearchRequest postSearchRequest) {
        List<PostDto> postDtos = postSearchService.searchPosts(postSearchRequest);
        return ResponseEntity.ok(new PostSearchResponse(postDtos));
    }

    @PostMapping("/cache")
    public ResponseEntity<PostSearchResponse> searchByCache(@RequestBody PostSearchRequest postSearchRequest) {
        List<PostDto> postDtos = postSearchService.searchPostsByCache(postSearchRequest);
        return ResponseEntity.ok(new PostSearchResponse(postDtos));
    }

    @PostMapping("/asyncCache")
    public ResponseEntity<PostSearchResponse> searchByAsyncCache(@RequestBody PostSearchRequest postSearchRequest)
            throws ExecutionException, InterruptedException {
        List<PostDto> postDtos = postSearchService.searchPostsByAsyncCache(postSearchRequest).get();
        return ResponseEntity.ok(new PostSearchResponse(postDtos));
    }
}
