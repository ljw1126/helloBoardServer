package hello.board.server.service;

import hello.board.server.dto.PostDto;
import hello.board.server.dto.request.PostRequest;

import java.util.List;

public interface PostService {
    void register(long userId, PostRequest postRequest);

    List<PostDto> getMyPosts(long userId);

    void update(long userId, PostRequest postRequest);

    void deleteBy(long userId, long postId);
}
