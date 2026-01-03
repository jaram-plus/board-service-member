package jaram.jaramplus.mopp_service.util;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;

@Component
@Slf4j
public class JwtTokenProvider {

    private final SecretKey secretKey;
    private final Long accessTokenExpTime;
    private final Long refreshTokenExpTime;

    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey,
                            @Value("${jwt.access-token-expirations}") Long accessTokenExpTime,
                            @Value("${jwt.refresh-token-expirations}")Long refreshTokenExpTime) {
        this.secretKey = new SecretKeySpec(secretKey.getBytes(), "HmacSHA256");
        this.accessTokenExpTime = accessTokenExpTime;
        this.refreshTokenExpTime = refreshTokenExpTime;
    }

    public String createAccessToken(String email,  Long id ){
        return createJwt("accessToken", id, email, accessTokenExpTime);
    }

    public String createRefreshToken(String email, Long id){
        return createJwt("refreshToken", id, email ,refreshTokenExpTime );
    }

    private String createJwt(String category, Long memberId, String email, Long expTime ){
        Date date = new Date();
        return Jwts.builder()
                .issuer("mopp")
                .subject(String.valueOf(memberId))
                .claim("category",category  )
                .claim("email", email)
                .issuedAt(date)
                .expiration(new Date(new Date().getTime()+ expTime ))
                .signWith(secretKey)
                .compact();
    }

    public boolean validateAccessToken(String token){
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






}
