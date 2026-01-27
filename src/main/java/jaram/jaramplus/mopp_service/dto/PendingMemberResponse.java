package jaram.jaramplus.mopp_service.dto;

import jaram.jaramplus.mopp_service.domain.Member;

import java.time.LocalDateTime;

public record   PendingMemberResponse(
        Long id,
        String email,
        String name,
        LocalDateTime createdAt
) {
    public static PendingMemberResponse from(Member member) {
        return new PendingMemberResponse(
                member.getId(),
                member.getEmail(),
                member.getName(),
                member.getCreatedAt()
        );
    }
}
