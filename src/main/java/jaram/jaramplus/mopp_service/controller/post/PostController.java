package jaram.jaramplus.mopp_service.controller.post;

import jakarta.validation.Valid;
import jaram.jaramplus.mopp_service.dto.PostListResponse;
import jaram.jaramplus.mopp_service.dto.CreatePostRequest;
import jaram.jaramplus.mopp_service.dto.PostResponse;
import jaram.jaramplus.mopp_service.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/jaram/board")
@RequiredArgsConstructor
public class PostController {

	private final PostService postService;

	@GetMapping("/posts")
	public PostListResponse getPosts(
			@PageableDefault(size=20, sort = "time", direction= Sort.Direction.DESC)
			Pageable pageable
	) {
		return postService.getPosts(pageable);
	}

	@PostMapping("/posts")
	public ResponseEntity<PostResponse> createPost(
			@Valid @RequestBody CreatePostRequest request) {
		PostResponse response = postService.createPost(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@GetMapping(value = "/posts/{postId}")
	public ResponseEntity<PostResponse> getPostById(@PathVariable Long postId, @AuthenticationPrincipal Long memberId) {
		PostResponse response = postService.getPostById(postId, memberId);
		return ResponseEntity.ok(response);
	}

}
