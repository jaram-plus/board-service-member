package jaram.jaramplus.mopp_service.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jaram.jaramplus.mopp_service.domain.Member;
import jaram.jaramplus.mopp_service.dto.TokenResponse;
import jaram.jaramplus.mopp_service.repository.MemberRepository;
import jaram.jaramplus.mopp_service.util.CookieUtil;
import jaram.jaramplus.mopp_service.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final JwtUtil jwtUtil;
    private final RedisService redisService;
    private final CookieUtil cookieUtil;
    private final MemberRepository memberRepository;

    @Value("${jwt.access-token-expirations}")
    private int accessExp;

    public TokenResponse validateToken(HttpServletRequest request, HttpServletResponse response) {
         String refreshToken =  cookieUtil.getCookie(request, "refreshToken")
                                .map(Cookie::getValue)
                                .orElseThrow(() -> new IllegalArgumentException("Refresh token not found"));   //todo 예외처리

        if (!jwtUtil.validateToken(refreshToken)) {
            throw new RuntimeException();   //todo 리프레시 토큰이 만료되어서 다시 로그인 필요 401
        }

        Long memberId = jwtUtil.getMemberId(refreshToken);
        String storedRefreshToken = redisService.getRefreshToken(memberId);
        if (storedRefreshToken == null || !storedRefreshToken.equals(refreshToken)) {
            throw new RuntimeException();   //todo 레디스에 저장된 토큰과 불일치 401
        }

        Member member = getUserById(memberId);
        String newAccessToken = jwtUtil.createAccessToken(memberId, member.getEmail());
        cookieUtil.addCookie(response, "accessToken", newAccessToken, accessExp);

        return TokenResponse.of(newAccessToken, accessExp / 1000);
    }


    public Member getUserById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + memberId));  //todo 예외처리
    }

    public boolean isExpiredAccessToken(String accessToken){
        return jwtUtil.validateToken(accessToken);
    }

}
