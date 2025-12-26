package jaram.jaramplus.mopp_service.controller.post.dto;

import jaram.jaramplus.mopp_service.domain.Post;

import java.time.LocalDateTime;

public record PostSummaryDto(
		String postTitle,
		String author,
		@com.fasterxml.jackson.annotation.JsonFormat(pattern = "yyyy-MM-dd")
		LocalDateTime creationDate
) {
	public static PostSummaryDto from(Post post) {
		return new PostSummaryDto(post.getTitle(), post.getAuthor(), post.getTime());
	}
}
