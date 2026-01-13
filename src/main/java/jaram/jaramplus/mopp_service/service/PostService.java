package jaram.jaramplus.mopp_service.service;

import jaram.jaramplus.mopp_service.dto.PostListResponse;
import jaram.jaramplus.mopp_service.dto.PostSummaryDto;
import jaram.jaramplus.mopp_service.domain.Post;
import jaram.jaramplus.mopp_service.dto.CreatePostRequest;
import jaram.jaramplus.mopp_service.dto.PostResponse;
import jaram.jaramplus.mopp_service.repository.PostRepository;
import jaram.jaramplus.mopp_service.repository.projection.PostSummaryProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

	@Value("${spring.data.redis.view-dedupe-ttl}")
	private Duration VIEW_TTL;

    private final PostRepository postRepository;
	private final StringRedisTemplate stringRedisTemplate;

	@Transactional
    public PostResponse createPost(CreatePostRequest request) {
        Post post = Post.createPost(
            request.getTitle(),
            request.getContent(),
            request.getAuthor()
        );

        Post savedPost = postRepository.save(post);

        // DTO 변환 후 리턴
        return PostResponse.from(savedPost);
    }

    public PostListResponse getPosts(Pageable pageable) {
        Page<PostSummaryProjection> page = postRepository.findAllBy(pageable);

        List<PostSummaryDto> list = page.getContent().stream()
                .map(p -> new PostSummaryDto(p.getTitle(), p.getAuthor(), p.getTime(), p.getViews()))
                .toList();
        return new PostListResponse(list);
    }

	@Transactional
	public PostResponse getPostById(Long postId, Long memberId) {

		String key = "post:view:dedupe:" + postId +  ":" + memberId;
		Boolean firstView = stringRedisTemplate.opsForValue()
				.setIfAbsent(key, "1", VIEW_TTL);

		if (Boolean.TRUE.equals(firstView)) {
			try{
				postRepository.incrementViewCount(postId);
			} catch (RuntimeException e){
				stringRedisTemplate.delete(key);
				throw e;
			}
		}

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다. ID: " + postId));
        return PostResponse.from(post);
    }
}
