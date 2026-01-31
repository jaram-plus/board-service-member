package jaram.jaramplus.mopp_service.config.oauth;

import jakarta.transaction.Transactional;
import jaram.jaramplus.mopp_service.domain.Member;
import jaram.jaramplus.mopp_service.domain.MemberStatus;
import jaram.jaramplus.mopp_service.repository.MemberRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuth2MemberService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    private final String allowedEmailDomain = "@hanyang.ac.kr";

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2MemberInfo oAuth2UserInfo = getOAuth2MemberInfo(registrationId, oAuth2User.getAttributes());


        String email = validateMemberInfo(oAuth2UserInfo);
        Member member = memberRepository.findByEmail(email)
                .map(existingMember -> {
                    if (existingMember.getStatus() == MemberStatus.REJECTED) {
                        existingMember.reapply();
                    }
                    return existingMember;
                })
                .orElseGet(() -> memberRepository.save(Member.from(oAuth2UserInfo)));

        return new CustomOAuth2Member(member, oAuth2User.getAttributes());
    }

    private @NonNull String validateMemberInfo(OAuth2MemberInfo oAuth2UserInfo) {
        String email = oAuth2UserInfo.getEmail();
        String id = oAuth2UserInfo.getId();

        if(id == null){
            throw new OAuth2AuthenticationException(new OAuth2Error("missing_user_id"),
                    "사용자 ID를 가져올 수 없습니다.");
        }

        if (email == null || !email.endsWith(allowedEmailDomain)) {
            throw new OAuth2AuthenticationException(new OAuth2Error("invalid_email_domain"),
                    "한양대학교 이메일(@hanyang.ac.kr)만 사용 가능합니다.");
        }
        return email;
    }

    private OAuth2MemberInfo getOAuth2MemberInfo (String registrationId, Map <String, Object> attributes){

        if ("google".equals(registrationId)) {
            return new GoogleOAuthMemberInfo(attributes);
        }
        throw new OAuth2AuthenticationException(
                new OAuth2Error("unsupported_provider"),
                "지원하지 않는 로그인 제공자입니다: " + registrationId
        );
    }

}