package jaram.jaramplus.mopp_service.dto;

import java.util.List;

public record PostListResponse(
		List<PostSummaryDto> postList
) {}
