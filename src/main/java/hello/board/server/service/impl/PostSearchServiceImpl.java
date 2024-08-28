package hello.board.server.service.impl;

import hello.board.server.dto.PostDto;
import hello.board.server.dto.request.PostSearchRequest;
import hello.board.server.mapper.PostSearchMapper;
import hello.board.server.service.PostSearchService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PostSearchServiceImpl implements PostSearchService {

    private final PostSearchMapper postSearchMapper;

    public PostSearchServiceImpl(PostSearchMapper postSearchMapper) {
        this.postSearchMapper = postSearchMapper;
    }

    @Transactional(readOnly = true)
    @Override
    public List<PostDto> searchPosts(PostSearchRequest postSearchRequest) {
        return postSearchMapper.searchPosts(postSearchRequest);
    }

    @Async
    @Cacheable(value = "posts",
            key = "'searchPostsByCache' + #postSearchRequest.getName() + #postSearchRequest.getCategoryId()",
            unless = "#result == null"
    )
    @Transactional(readOnly = true)
    @Override
    public List<PostDto> searchPostsByCache(PostSearchRequest postSearchRequest) {
        return postSearchMapper.searchPosts(postSearchRequest);
    }
}
