package jaram.jaramplus.mopp_service.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jaram.jaramplus.mopp_service.dto.TokenResponse;
import jaram.jaramplus.mopp_service.service.MemberService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
