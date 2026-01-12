package jaram.jaramplus.mopp_service.config.oauth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jaram.jaramplus.mopp_service.service.RedisService;
import jaram.jaramplus.mopp_service.util.CookieUtil;
import jaram.jaramplus.mopp_service.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final RedisService redisService;
    private final CookieUtil cookieUtil;

    @Value("${jwt.access-token-expirations}")
    private int accessExp;

    @Value("${jwt.refresh-token-expirations}")
    private int refreshExp;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

       CustomOAuth2Member oAuth2Member = (CustomOAuth2Member)authentication.getPrincipal();

        String accessToken = jwtUtil.createAccessToken(oAuth2Member.getMemberId(), oAuth2Member.getEmail());
        String refreshToken = jwtUtil.createRefreshToken(oAuth2Member.getMemberId(), oAuth2Member.getEmail());

        redisService.saveRefreshToken(oAuth2Member.getMemberId(), refreshToken, jwtUtil.getRefreshTokenExpTime());

        cookieUtil.addCookie(response, "accessToken", accessToken, accessExp);
        cookieUtil.addCookie(response, "refreshToken", refreshToken, refreshExp);

        String redirectUrl = UriComponentsBuilder.fromUriString(frontendUrl + "/auth/success")
                .build()
                .toUriString();

        getRedirectStrategy().sendRedirect(request, response, redirectUrl);

    }
}
