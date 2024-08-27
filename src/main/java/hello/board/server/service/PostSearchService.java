package hello.board.server.service;

import hello.board.server.dto.PostDto;
import hello.board.server.dto.request.PostSearchRequest;

import java.util.List;

public interface PostSearchService {
    List<PostDto> searchPosts(PostSearchRequest postSearchRequest);

    List<PostDto> searchPostsByCache(PostSearchRequest postSearchRequest);
}
