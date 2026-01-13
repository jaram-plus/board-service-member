package jaram.jaramplus.mopp_service.repository;

import jaram.jaramplus.mopp_service.domain.Post;
import jaram.jaramplus.mopp_service.repository.projection.PostSummaryProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long> {
	Page<PostSummaryProjection> findAllBy(Pageable pageable);

	@Modifying
	@Query(value = "UPDATE post SET views = views + 1 WHERE post_id = :postID", nativeQuery = true)
	void incrementViewCount(@Param("postID") Long postID);
}
