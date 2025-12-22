package jaram.jaramplus.mopp_service.repository;

import jaram.jaramplus.mopp_service.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
