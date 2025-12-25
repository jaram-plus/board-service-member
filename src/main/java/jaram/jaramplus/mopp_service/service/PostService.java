package jaram.jaramplus.mopp_service.service;

import jaram.jaramplus.mopp_service.domain.Post;
import jaram.jaramplus.mopp_service.dto.CreatePostRequest;
import jaram.jaramplus.mopp_service.dto.PostResponse;
import jaram.jaramplus.mopp_service.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
