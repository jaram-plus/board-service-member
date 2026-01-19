package jaram.jaramplus.mopp_service.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jaram.jaramplus.mopp_service.dto.AccessTokenResponse;
import jaram.jaramplus.mopp_service.dto.TokenResponse;
import jaram.jaramplus.mopp_service.service.MemberService;

import jaram.jaramplus.mopp_service.util.CookieUtil;
import jaram.jaramplus.mopp_service.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final MemberService memberService;


    @PostMapping("/refresh")  //accessToken이 만료되었을 때 refreshToken을 통해 새로운 accesstoken생성
    public ResponseEntity<TokenResponse> refreshToken(HttpServletRequest request, HttpServletResponse response){
        TokenResponse tokenDto = memberService.validateToken(request, response);
        return ResponseEntity.ok(tokenDto);
    }

    @PostMapping("/token")
    public ResponseEntity<?> getAccessTokenByResponse(
            @CookieValue(name = "accessToken")String accessToken){
        AccessTokenResponse response = new AccessTokenResponse(accessToken);

        if(!memberService.isValidAccessToken(accessToken)){
            return ResponseEntity.status(401)
                    .body(Map.of("error", "Access token not found in cookies")); //todo 예외처리 개선
        }
        return ResponseEntity.ok(response);

    }

}
