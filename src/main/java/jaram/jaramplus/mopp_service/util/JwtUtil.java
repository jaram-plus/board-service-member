package jaram.jaramplus.mopp_service.util;

import io.jsonwebtoken.*;
import jaram.jaramplus.mopp_service.domain.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;

@Component
@Slf4j
public class JwtUtil {

    private final SecretKey secretKey;
    private final Long accessTokenExpTime;
    private final Long refreshTokenExpTime;

    public JwtUtil(@Value("${jwt.secret}") String secretKey,
                   @Value("${jwt.access-token-expirations}") Long accessTokenExpTime,
                   @Value("${jwt.refresh-token-expirations}")Long refreshTokenExpTime) {
        this.secretKey = new SecretKeySpec(secretKey.getBytes(), "HmacSHA256");
        this.accessTokenExpTime = accessTokenExpTime;
        this.refreshTokenExpTime = refreshTokenExpTime;
    }

    public String createAccessToken(Long id, String email, Role role) {
        return createJwt("accessToken", id, email, role, accessTokenExpTime);
    }

    public String createRefreshToken(Long id, String email) {
        return createJwt("refreshToken", id, email, null, refreshTokenExpTime);
    }

    private String createJwt(String category, Long memberId, String email, Role role, Long expTime) {
        Date date = new Date();
        var builder = Jwts.builder()
                .issuer("mopp")
                .subject(String.valueOf(memberId))
                .claim("category", category)
                .claim("email", email);

        if (role != null) {
            builder.claim("role", role.name());
        }

        return builder
                .issuedAt(date)
                .expiration(new Date(new Date().getTime() + expTime))
                .signWith(secretKey)
                .compact();
    }

    public boolean validateToken(String token){
        try{
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return true;

        }catch(ExpiredJwtException e){
            return false;
        } catch (UnsupportedJwtException e) {
            log.warn("지원되지 않는 JWT 토큰입니다: {}", e.getMessage());
            return false;
        } catch (MalformedJwtException e) {
            log.warn("잘못된 형식의 JWT 토큰입니다: {}", e.getMessage());
            return false;
        } catch (SignatureException e) {
            log.warn("JWT 서명 검증에 실패했습니다: {}", e.getMessage());
            return false;
        } catch (IllegalArgumentException e) {
            log.warn("잘못된 JWT 토큰 인자입니다: {}", e.getMessage());
            return false;
        } catch (JwtException e) {
            log.warn("JWT 처리 중 오류가 발생했습니다: {}", e.getMessage());
            return false;
        } catch (Exception e) {
            log.error("토큰 검증 중 예상치 못한 오류가 발생했습니다", e);
            return false;
        }

    }

    public long getRefreshTokenExpTime() {
        return refreshTokenExpTime;
    }

    public Long getMemberId(String token) {
        return Long.parseLong(Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject());
    }

    public String getEmail(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("email", String.class);
    }

    public Role getRole(String token) {
        String roleName = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("role", String.class);
        return roleName != null ? Role.valueOf(roleName) : null;
    }

}
