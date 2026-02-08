package jaram.jaramplus.mopp_service.dto;

import jaram.jaramplus.mopp_service.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class PostSummaryInternal {
	private final String title;
	private final String author;
	private final boolean anonymous;
	private LocalDateTime time;
	private int views;

	public PostSummaryResponse toResponse() {
		return new PostSummaryResponse(
				title,
				anonymous ? "익명" : author,
				time,
				views
		);
	}
}
