package jaram.jaramplus.mopp_service.repository;

import jaram.jaramplus.mopp_service.domain.Member;
import jaram.jaramplus.mopp_service.domain.MemberStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

    List<Member> findByStatus(MemberStatus status);

}
