package jaram.jaramplus.mopp_service.controller.post;

import jaram.jaramplus.mopp_service.controller.post.dto.PostListResponse;
import jaram.jaramplus.mopp_service.service.PostService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/board")
public class PostController {

	private final PostService postService;

	public PostController(PostService postService) {
		this.postService = postService;
	}

	@GetMapping("/posts")
	public PostListResponse getPosts(
			@PageableDefault(size=20, sort = "time", direction= Sort.Direction.DESC)
			Pageable pageable
	) {
		return postService.getPosts(pageable);
	}
}
