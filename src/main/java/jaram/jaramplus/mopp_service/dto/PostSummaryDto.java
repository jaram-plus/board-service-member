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
		String authorName = post.isAnonymous() ? null : post.getAuthor().getName();
		return new PostSummaryDto(post.getTitle(), authorName, post.getTime(), post.getViews());
	}
}
