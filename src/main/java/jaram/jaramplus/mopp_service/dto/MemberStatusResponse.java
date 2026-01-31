package jaram.jaramplus.mopp_service.dto;

import jaram.jaramplus.mopp_service.domain.Member;
import jaram.jaramplus.mopp_service.domain.MemberStatus;

public record MemberStatusResponse(
        String name,
        MemberStatus status
) {
    public static MemberStatusResponse from(Member member) {
        return new MemberStatusResponse(member.getName(), member.getStatus());
    }
}
