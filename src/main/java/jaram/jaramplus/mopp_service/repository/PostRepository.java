package jaram.jaramplus.mopp_service.repository;

import jaram.jaramplus.mopp_service.controller.post.dto.PostSummaryDto;
import jaram.jaramplus.mopp_service.domain.Post;
import jaram.jaramplus.mopp_service.repository.projection.PostSummaryProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
	Page<PostSummaryProjection> findAllBy(Pageable pageable);
}
