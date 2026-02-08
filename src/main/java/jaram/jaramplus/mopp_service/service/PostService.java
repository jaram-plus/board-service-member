package jaram.jaramplus.mopp_service.service;

import jaram.jaramplus.mopp_service.domain.Member;
import jaram.jaramplus.mopp_service.dto.*;
import jaram.jaramplus.mopp_service.domain.Post;
import jaram.jaramplus.mopp_service.repository.MemberRepository;
import jaram.jaramplus.mopp_service.repository.PostRepository;
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

	private final MemberRepository memberRepository;
	@Value("${spring.data.redis.view-dedupe-ttl}")
	private Duration VIEW_TTL;

    private final PostRepository postRepository;
	private final StringRedisTemplate stringRedisTemplate;

	@Transactional
    public PostResponse createPost(Long memberId, CreatePostRequest request) {

		Member member = memberRepository.findById(memberId)
				.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다. memberId=" + memberId));

        Post post = Post.createPost(
            request.getTitle(),
            request.getContent(),
			member,
	        request.isAnonymous()
        );

        Post savedPost = postRepository.save(post);

        // DTO 변환 후 리턴
        return PostResponse.from(savedPost);
    }

    public PostListResponse getPosts(Pageable pageable) {
        Page<PostSummaryInternal> page = postRepository.findAllBy(pageable);

        List<PostSummaryResponse> list = page.getContent().stream()
		        .map(PostSummaryInternal::toResponse)
		        .toList();

        return new PostListResponse(list);
    }

	@Transactional
	public PostResponse getPostById(Long postId, Long memberId) {
		Post post = postRepository.findById(postId)
				.orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다. ID: " + postId));

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

        return PostResponse.from(post);
    }
}
