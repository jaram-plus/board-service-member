package jaram.jaramplus.mopp_service.repository.projection;

import java.time.LocalDateTime;

public interface PostSummaryProjection {
	String getTitle();
	String getAuthor();
	LocalDateTime getTime();
}
