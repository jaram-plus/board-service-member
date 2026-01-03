package jaram.jaramplus.mopp_service.domain;

import jakarta.persistence.*;
import jaram.jaramplus.mopp_service.config.OAuth2MemberInfo;
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

    @Builder
    private Member(String googleId, String email, String name) {
        this.googleId = googleId;
        this.email = email;
        this.name = name;
        this.createdAt = LocalDateTime.now();
        this.status = MemberStatus.PENDING;
    }

    public static Member from(OAuth2MemberInfo oAuth2UserInfo) {
        return Member.builder()
                .email(oAuth2UserInfo.getEmail())
                .googleId(oAuth2UserInfo.getId())
                .name(oAuth2UserInfo.getName())
                .build();
    }
}
