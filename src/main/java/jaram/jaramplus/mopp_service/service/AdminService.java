package jaram.jaramplus.mopp_service.service;

import jaram.jaramplus.mopp_service.domain.Member;
import jaram.jaramplus.mopp_service.domain.MemberStatus;
import jaram.jaramplus.mopp_service.dto.PendingMemberResponse;
import jaram.jaramplus.mopp_service.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminService {

    private final MemberRepository memberRepository;

    public List<PendingMemberResponse> getPendingMembers() {
        return memberRepository.findByStatus(MemberStatus.PENDING)
                .stream()
                .map(PendingMemberResponse::from)
                .toList();
    }

    @Transactional
    public Member approveMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다: " + memberId));

        if (member.getStatus() != MemberStatus.PENDING) {
            throw new IllegalStateException("대기 중인 회원만 승인할 수 있습니다.");
        }

        member.approve();
        return member;
    }

    @Transactional
    public Member rejectMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다: " + memberId));

        if (member.getStatus() != MemberStatus.PENDING) {
            throw new IllegalStateException("대기 중인 회원만 거부할 수 있습니다.");
        }

        member.reject();
        return member;
    }
}
