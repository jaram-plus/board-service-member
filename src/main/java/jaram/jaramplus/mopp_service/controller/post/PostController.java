package jaram.jaramplus.mopp_service.controller.post;

import jakarta.validation.Valid;
import jaram.jaramplus.mopp_service.controller.post.dto.PostListResponse;
import jaram.jaramplus.mopp_service.dto.CreatePostRequest;
import jaram.jaramplus.mopp_service.dto.PostResponse;
import jaram.jaramplus.mopp_service.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/board")
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

	@GetMapping(value = "/posts", params = "id")
	public ResponseEntity<PostResponse> getPostById(@RequestParam Long id) {
		PostResponse response = postService.getPostById(id);
		return ResponseEntity.ok(response);
	}

}
