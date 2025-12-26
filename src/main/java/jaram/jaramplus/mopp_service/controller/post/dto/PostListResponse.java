package jaram.jaramplus.mopp_service.controller.post.dto;

import java.util.List;

public record PostListResponse(
		List<PostSummaryDto> postList
) {}
