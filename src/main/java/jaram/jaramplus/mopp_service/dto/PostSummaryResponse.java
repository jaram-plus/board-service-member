package jaram.jaramplus.mopp_service.dto;

import jaram.jaramplus.mopp_service.domain.Post;

import java.time.LocalDateTime;

public record PostSummaryResponse(
		String title,
		String author,
		@com.fasterxml.jackson.annotation.JsonFormat(pattern = "yyyy-MM-dd")
		LocalDateTime time,
		int views
) {
	public static PostSummaryResponse from(Post post) {
		String authorName = post.isAnonymous() ? "익명" : post.getAuthor().getName();
		return new PostSummaryResponse(post.getTitle(), authorName, post.getTime(), post.getViews());
	}
}
