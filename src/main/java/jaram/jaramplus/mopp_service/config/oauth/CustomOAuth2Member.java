package jaram.jaramplus.mopp_service.config.oauth;

import jaram.jaramplus.mopp_service.domain.Member;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Getter
public class CustomOAuth2Member implements OAuth2User {

    private Long memberId;
    private String email;
    private String name;
    private Map<String, Object> attributes;

    public CustomOAuth2Member(Member member, Map<String, Object> attributes) {
        this.memberId = member.getId();
        this.email = member.getEmail();
        this.name = member.getName();
        this.attributes = attributes;
    }


    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getName() {
        return name;
    }
}
