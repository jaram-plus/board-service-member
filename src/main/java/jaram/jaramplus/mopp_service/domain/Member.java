package jaram.jaramplus.mopp_service.domain;

import jakarta.persistence.*;
import jaram.jaramplus.mopp_service.config.oauth.OAuth2MemberInfo;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String googleId;

    private String email;

    private String name;

    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private MemberStatus status;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder
    private Member(String googleId, String email, String name) {
        this.googleId = googleId;
        this.email = email;
        this.name = name;
        this.createdAt = LocalDateTime.now();
        this.status = MemberStatus.PENDING;
        this.role = Role.USER;
    }

    public static Member from(OAuth2MemberInfo oAuth2UserInfo) {
        return Member.builder()
                .email(oAuth2UserInfo.getEmail())
                .googleId(oAuth2UserInfo.getId())
                .name(oAuth2UserInfo.getName())
                .build();
    }

    public static Member createAdmin(String email, String name) {
        Member admin = Member.builder()
                .googleId("ADMIN_" + email)
                .email(email)
                .name(name)
                .build();
        admin.status = MemberStatus.APPROVED;
        admin.role = Role.ADMIN;
        return admin;
    }

    public void approve() {
        this.status = MemberStatus.APPROVED;
    }

    public void reject() {
        this.status = MemberStatus.REJECTED;
    }

    public void reapply() {
        this.status = MemberStatus.PENDING;
    }
}
