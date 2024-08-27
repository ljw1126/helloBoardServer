package hello.board.server.service.impl;

import hello.board.server.dto.PostDto;
import hello.board.server.dto.request.PostRequest;
import hello.board.server.exception.PostDeleteFailedException;
import hello.board.server.exception.PostUpdateFailedException;
import hello.board.server.mapper.PostMapper;
import hello.board.server.service.PostService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class PostServiceImpl implements PostService {

    private final PostMapper postMapper;

    public PostServiceImpl(PostMapper postMapper) {
        this.postMapper = postMapper;
    }

    @Override
    public void register(long userId, PostRequest postRequest) {
        PostDto postDto = PostDto.of(userId, postRequest);

        int count = postMapper.register(postDto);

        if (count != 1) {
            throw new IllegalStateException("post register error");
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<PostDto> getMyPosts(long userId) {
        return postMapper.selectByUserId(userId);
    }

    @Override
    public void update(long userId, PostRequest postRequest) {
        PostDto postDto = postMapper.selectById(postRequest.getId());
        if (postDto == null) {
            throw new PostUpdateFailedException("Post not found");
        }

        if (!postDto.validateOwnerShip(userId)) {
            throw new PostUpdateFailedException("User is not authorized to modify this post : " + userId);
        }

        postDto.updated(postRequest);
        int count = postMapper.update(postDto);

        if (count != 1) {
            throw new PostUpdateFailedException("post update error");
        }
    }

    @Override
    public void deleteBy(long userId, long postId) {
        if (userId < 1L || postId < 1L) {
            throw new PostDeleteFailedException("post deleteBy error");
        }

        PostDto postDto = postMapper.selectById(postId);
        if (postDto == null) {
            throw new PostDeleteFailedException("Post not found");
        }

        if (!postDto.validateOwnerShip(userId)) {
            throw new PostDeleteFailedException("User is not authorized to delete this post : " + userId);
        }

        postMapper.deleteById(postId);
    }
}
