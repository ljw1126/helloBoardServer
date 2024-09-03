package hello.board.server.service.impl;

import hello.board.server.dao.RedisDao;
import hello.board.server.dto.PostDto;
import hello.board.server.dto.request.PostSearchRequest;
import hello.board.server.mapper.PostSearchMapper;
import hello.board.server.service.PostSearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class PostSearchServiceImpl implements PostSearchService {

    private final PostSearchMapper postSearchMapper;
    private final RedisDao redisDao;

    public PostSearchServiceImpl(PostSearchMapper postSearchMapper, RedisDao redisDao) {
        this.postSearchMapper = postSearchMapper;
        this.redisDao = redisDao;
    }

    @Transactional(readOnly = true)
    @Override
    public List<PostDto> searchPosts(PostSearchRequest postSearchRequest) {
        return postSearchMapper.searchPosts(postSearchRequest);
    }

    @Cacheable(value = "posts",
            key = "'posts::' + #postSearchRequest.getName() + '::' + #postSearchRequest.getCategoryId()"
    )
    @Transactional(readOnly = true)
    @Override
    public List<PostDto> searchPostsByCache(PostSearchRequest postSearchRequest) {
        return postSearchMapper.searchPosts(postSearchRequest);
    }

    @Async(value = "myExecutor")
    @Transactional(readOnly = true)
    @Override
    public CompletableFuture<List<PostDto>> searchPostsByAsyncCache(PostSearchRequest postSearchRequest) {
        String cacheKey = generateCacheKey(postSearchRequest);

        // 1. Redis에서 캐시 확인
        return redisDao.getPostsFromCache(cacheKey)
                .thenCompose(cachedPosts -> {
                    if (cachedPosts.isPresent()) {
                        return CompletableFuture.completedFuture(cachedPosts.get());
                    }
                    
                    // 3. 캐시가 없으면 DB에서 조회 후 캐시에 저장
                    return CompletableFuture.supplyAsync(() -> postSearchMapper.searchPosts(postSearchRequest))
                            .thenApply(posts -> {
                                redisDao.savePostsToCache(cacheKey, posts);
                                return posts;
                            });
                });
    }

    // Cache Key 생성 로직
    private String generateCacheKey(PostSearchRequest postSearchRequest) {
        return "posts::" + postSearchRequest.getName() + "::" + postSearchRequest.getCategoryId();
    }
}
