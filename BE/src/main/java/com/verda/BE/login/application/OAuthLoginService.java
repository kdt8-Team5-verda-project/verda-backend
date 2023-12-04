package com.verda.BE.login.application;

import com.verda.BE.common.ErrorCode;
import com.verda.BE.exception.ApiException;
import com.verda.BE.login.domain.AuthTokens;
import com.verda.BE.login.domain.AuthTokensGenerator;
import com.verda.BE.login.domain.oauth.OAuthInfoResponse;
import com.verda.BE.login.domain.oauth.OAuthLoginParams;
import com.verda.BE.login.domain.oauth.OAuthProvider;
import com.verda.BE.login.domain.oauth.RequestOAuthInfoService;
import com.verda.BE.login.member.domain.FundEntity;
import com.verda.BE.login.member.domain.FundRepository;
import com.verda.BE.login.member.domain.UserEntity;
import com.verda.BE.login.member.domain.KakaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuthLoginService {
    private final KakaoRepository kakaoRepository;
    private final FundRepository fundRepository;
    private final AuthTokensGenerator authTokensGenerator;
    private final RequestOAuthInfoService requestOAuthInfoService;

    public AuthTokens UserLogin(OAuthLoginParams params) {
        try {
            OAuthInfoResponse oAuthInfoResponse = requestOAuthInfoService.request(params);
            if (isUserExists(oAuthInfoResponse)) {
                return getAuthTokens(oAuthInfoResponse.getEmail());
            } else {
                Long memberId = newUser(oAuthInfoResponse);
                AuthTokens authTokens = authTokensGenerator.generate(memberId, oAuthInfoResponse.getEmail());
                authTokens.setEmail(oAuthInfoResponse.getEmail());
                return authTokens;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public AuthTokens FundLogin(OAuthLoginParams params) {
        try {
            OAuthInfoResponse oAuthInfoResponse = requestOAuthInfoService.request(params);
            if (isFundExists(oAuthInfoResponse)) {
                return getAuthTokens(oAuthInfoResponse.getEmail());
            } else {
                Long memberId = newFund(oAuthInfoResponse);
                AuthTokens authTokens = authTokensGenerator.generate(memberId, oAuthInfoResponse.getEmail());
                authTokens.setEmail(oAuthInfoResponse.getEmail());
                return authTokens;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    public AuthTokens getAuthTokens(String email) {
        UserEntity userEntity = kakaoRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND_EMAIL));
        return authTokensGenerator.generate(userEntity.getUserId(), userEntity.getEmail());
    }

    private Long findOrCreateUser(OAuthInfoResponse oAuthInfoResponse) {
        return kakaoRepository.findByEmail(oAuthInfoResponse.getEmail())
                .map(UserEntity::getUserId)
                .orElseGet(() -> newUser(oAuthInfoResponse));
    }

    // User의 정보가 db에 저장되어 있는지 아닌지 boolean값 리턴
    private boolean isUserExists(OAuthInfoResponse oAuthInfoResponse) {

        return kakaoRepository.findByEmail(oAuthInfoResponse.getEmail()).isPresent();
    }
    private boolean isFundExists(OAuthInfoResponse oAuthInfoResponse) {

        return fundRepository.findByEmail(oAuthInfoResponse.getEmail()).isPresent();
    }

    private Long newUser(OAuthInfoResponse oAuthInfoResponse) {

        UserEntity userEntity = UserEntity.builder()
                .email(oAuthInfoResponse.getEmail())
                .name(oAuthInfoResponse.getName())
                .birthday(oAuthInfoResponse.getBirthday())
                .gender(oAuthInfoResponse.getGender())
                .age_range(oAuthInfoResponse.getAgeRange())
                .build();

        return kakaoRepository.save(userEntity).getUserId();
    }

    private Long newFund(OAuthInfoResponse oAuthInfoResponse) {
        FundEntity fundEntity = FundEntity.builder()
                .email(oAuthInfoResponse.getEmail())
                .name(oAuthInfoResponse.getName())
                .age_range(oAuthInfoResponse.getAgeRange())
                .gender(oAuthInfoResponse.getGender())
                .build();

        return fundRepository.save(fundEntity).getFmId();
    }
}