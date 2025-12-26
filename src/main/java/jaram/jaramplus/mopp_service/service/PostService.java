package jaram.jaramplus.mopp_service.service;

import jaram.jaramplus.mopp_service.controller.post.dto.PostListResponse;
import jaram.jaramplus.mopp_service.controller.post.dto.PostSummaryDto;
import jaram.jaramplus.mopp_service.domain.Post;
import jaram.jaramplus.mopp_service.dto.CreatePostRequest;
import jaram.jaramplus.mopp_service.dto.PostResponse;
import jaram.jaramplus.mopp_service.repository.PostRepository;
import jaram.jaramplus.mopp_service.repository.projection.PostSummaryProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;

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
                .map(p -> new PostSummaryDto(p.getTitle(), p.getAuthor(), p.getTime()))
                .toList();
        return new PostListResponse(list);
    }
}
