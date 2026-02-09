package jaram.jaramplus.mopp_service.dto;

import jaram.jaramplus.mopp_service.domain.Post;

import java.time.LocalDateTime;

public record PostSummaryResponse(
		String title,
		String author,
		@com.fasterxml.jackson.annotation.JsonFormat(pattern = "yyyy-MM-dd")
		LocalDateTime time,
		int views
) { }
