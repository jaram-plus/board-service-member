package jaram.jaramplus.mopp_service.dto;

import jaram.jaramplus.mopp_service.domain.Post;

import java.time.LocalDateTime;

public record PostSummaryDto(
		String title,
		String author,
		@com.fasterxml.jackson.annotation.JsonFormat(pattern = "yyyy-MM-dd")
		LocalDateTime time,
		int views
) {
	public static PostSummaryDto from(Post post) {
		return new PostSummaryDto(post.getTitle(), post.getAuthor(), post.getTime(), post.getViews());
	}
}
