package hello.board.server.dao;

import hello.board.server.dto.PostDto;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Component
public class RedisDao {

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisDao(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // 캐시에서 게시글 목록 가져오기
    public CompletableFuture<Optional<List<PostDto>>> getPostsFromCache(String cacheKey) {
        return CompletableFuture.supplyAsync(() -> {
            List<PostDto> cachedPosts = (List<PostDto>) redisTemplate.opsForHash().get(cacheKey, "posts");
            return Optional.ofNullable(cachedPosts);
        });
    }

    // 게시글 목록을 캐시에 저장
    public void savePostsToCache(String cacheKey, List<PostDto> posts) {
        redisTemplate.opsForHash().put(cacheKey, "posts", posts);
        redisTemplate.expire(cacheKey, 60, TimeUnit.MINUTES);
    }

    // 캐시에서 특정 게시글 목록 삭제
    public void deletePostsFromCache(String cacheKey) {
        redisTemplate.opsForHash().delete(cacheKey, "posts");
    }

    // 캐시 전체 삭제 (필요할 경우)
    public void clearCache() {
        redisTemplate.getConnectionFactory().getConnection().flushAll();
    }

}
