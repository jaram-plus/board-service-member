package jaram.jaramplus.mopp_service.config.oauth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jaram.jaramplus.mopp_service.filter.JwtAuthenticationFilter;
import jaram.jaramplus.mopp_service.service.RedisService;
import jaram.jaramplus.mopp_service.util.CookieUtil;
import jaram.jaramplus.mopp_service.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtLogoutHandler implements LogoutHandler {

	private final JwtUtil jwtUtil;
	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	private final RedisService redisService;
	private final CookieUtil cookieUtil;

	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		Long userId = extractUserId(authentication);

		if(userId == null){
			String refreshToken = jwtAuthenticationFilter.resolveRefreshToken(request);
			if(refreshToken != null && jwtUtil.validateToken(refreshToken)) {
				userId = jwtUtil.getMemberId(refreshToken);
			}
		}

		if(userId != null){
			redisService.deleteRefreshToken(userId);
		}
		cookieUtil.deleteCookie(response, "refreshToken");
		cookieUtil.deleteCookie(response, "accessToken");
	}

	private Long extractUserId(Authentication authentication) {
		if (authentication == null) {
			return null;
		}
		Object principal = authentication.getPrincipal();
		if (principal instanceof Long userId) {
			return userId;
		}
		else if(principal instanceof String s){
			try {
				return Long.parseLong(s);
			} catch (NumberFormatException e) {
				return null;
			}
		}
		return null;
	}
}
