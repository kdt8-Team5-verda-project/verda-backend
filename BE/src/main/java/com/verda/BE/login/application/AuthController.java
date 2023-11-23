package com.verda.BE.login.application;

import com.verda.BE.login.domain.AuthTokens;
import com.verda.BE.login.infra.kakao.KakaoLoginParams;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final OAuthLoginService oAuthLoginService;

    @PostMapping("/kakao")
    public ResponseEntity<AuthTokens> loginKakao(@RequestBody KakaoLoginParams params) {
        return ResponseEntity.ok(oAuthLoginService.login(params));
    }

//    @PostMapping("/naver")
//    public ResponseEntity<AuthTokens> loginNaver(@RequestBody NaverLoginParams params) {
//        return ResponseEntity.ok(oAuthLoginService.login(params));
//    }
}