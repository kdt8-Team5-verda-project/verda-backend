package com.verda.BE.login.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// 사용자에게 내려주는 서비스의 인증 토큰 값입니다.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthTokens {
    private String accessToken;
    private String refreshToken;
    private String grantType;
    private Long expiresIn;
    private String email;

    public static AuthTokens of(String accessToken, String refreshToken, String grantType, Long expiresIn, String email) {
        return new AuthTokens(accessToken, refreshToken, grantType, expiresIn, email);
    }
}