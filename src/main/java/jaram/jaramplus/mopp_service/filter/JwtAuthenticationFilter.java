package jaram.jaramplus.mopp_service.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jaram.jaramplus.mopp_service.domain.Member;
import jaram.jaramplus.mopp_service.domain.Role;
import jaram.jaramplus.mopp_service.repository.MemberRepository;
import jaram.jaramplus.mopp_service.util.CookieUtil;
import jaram.jaramplus.mopp_service.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CookieUtil cookieUtil;
    private final MemberRepository memberRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = resolveToken(request);

            if (token != null && jwtUtil.validateToken(token)) {
                Long memberId = jwtUtil.getMemberId(token);
                Role role = jwtUtil.getRole(token);

                List<GrantedAuthority> authorities = new ArrayList<>();

                // JWT에서 role 추출하여 권한 추가
                if (role != null) {
                    authorities.add(new SimpleGrantedAuthority("ROLE_" + role.name()));
                }

                // DB에서 member 조회하여 status 권한 추가
                memberRepository.findById(memberId).ifPresent(member -> {
                    authorities.add(new SimpleGrantedAuthority("ROLE_" + member.getStatus().name()));
                });

                UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(memberId, null, authorities);

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request){
        // 1. Authorization 헤더에서 토큰 확인
        String bearerToken = request.getHeader("Authorization");
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7);
        }

        // 2. 쿠키에서 토큰 확인 (타임리프 페이지용)
        String cookieToken = cookieUtil.getCookie(request, "accessToken")
                .map(cookie -> cookie.getValue())
                .orElse(null);
        if (StringUtils.hasText(cookieToken)) {
            return cookieToken;
        }

        return null;
    }
}
