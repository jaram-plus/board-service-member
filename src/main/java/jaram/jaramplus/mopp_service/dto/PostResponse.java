package jaram.jaramplus.mopp_service.dto;

import jaram.jaramplus.mopp_service.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.format.DateTimeFormatter;

@Getter
@AllArgsConstructor
public class PostResponse {
    private Long id;
    private String title;
    private String content;
    private String author;
    private String time;
	private int views;

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");

    public static PostResponse from(Post post) {
        return new PostResponse(
            post.getId(),
            post.getTitle(),
            post.getContent(),
	        getDisplayAuthor(post),
            post.getTime().format(TIME_FORMATTER),
	        post.getViews()
        );
    }

	private static String getDisplayAuthor(Post post) {
		if (post.isAnonymous()) {
			return "익명";
		}

		// 탈퇴한 회원의 경우 author가 null일 수 있음
		if (post.getAuthor() == null) {
			return "삭제된 사용자";
		}

		return post.getAuthor().getName();
	}
}
