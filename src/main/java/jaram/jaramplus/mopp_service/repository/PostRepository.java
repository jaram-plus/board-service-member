package jaram.jaramplus.mopp_service.repository;

import jaram.jaramplus.mopp_service.domain.Post;
import jaram.jaramplus.mopp_service.dto.PostSummaryInternal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long> {
	@Query("""
    SELECT new jaram.jaramplus.mopp_service.dto.PostSummaryInternal(
        p.title,
        m.name,
        p.anonymous,
        p.time,
        p.views
    )
    FROM Post p
    LEFT JOIN p.author m
    ORDER BY p.time DESC
    """)
	Page<PostSummaryInternal> findAllBy(Pageable pageable);

	@Modifying
	@Query(value = "UPDATE post SET views = views + 1 WHERE post_id = :postID", nativeQuery = true)
	void incrementViewCount(@Param("postID") Long postID);
}
