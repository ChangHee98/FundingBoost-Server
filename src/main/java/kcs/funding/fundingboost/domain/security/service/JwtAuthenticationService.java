package kcs.funding.fundingboost.domain.security.service;

import static kcs.funding.fundingboost.domain.exception.ErrorCode.EXPIRED_REFRESH_TOKEN_ERROR;
import static kcs.funding.fundingboost.domain.security.utils.SecurityConst.ACCESS_TOKEN_VALIDITY_IN_MILLISECONDS;
import static kcs.funding.fundingboost.domain.security.utils.SecurityConst.SECRET;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import kcs.funding.fundingboost.domain.dto.common.CommonSuccessDto;
import kcs.funding.fundingboost.domain.dto.response.login.JwtAccessTokenDto;
import kcs.funding.fundingboost.domain.exception.CommonException;
import kcs.funding.fundingboost.domain.security.CustomUserDetails;
import kcs.funding.fundingboost.domain.security.entity.BlackList;
import kcs.funding.fundingboost.domain.security.entity.RefreshToken;
import kcs.funding.fundingboost.domain.security.repository.BlackListRepository;
import kcs.funding.fundingboost.domain.security.repository.RefreshTokenRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtAuthenticationService implements InitializingBean {

    private final RefreshTokenRepository refreshTokenRepository;
    private final BlackListRepository blackListRepository;

    @Getter
    private static Key key;

    @Override
    public void afterPropertiesSet() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        key = Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * SimpleAuthenticatoinService에서 accessToken 생성 nickname과 password 이용
     */
    public String createAccessToken(Authentication authentication) {
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        Long memberId = principal.getMemberId();
        return getAccessToken(memberId);
    }

    /**
     * refreshToken을 이용해 accessToken 생성
     */
    public JwtAccessTokenDto createAccessToken(String refreshToken) {
        RefreshToken findRefreshToken = refreshTokenRepository.findById(refreshToken)
                .orElseThrow(() -> new CommonException(EXPIRED_REFRESH_TOKEN_ERROR));

        String accessToken = getAccessToken(findRefreshToken.getUserId());
        return JwtAccessTokenDto.fromEntity(accessToken);
    }

    private String getAccessToken(Long memberId) {
        long now = (new Date()).getTime();
        Date accessTokenValidity = new Date(now + ACCESS_TOKEN_VALIDITY_IN_MILLISECONDS);

        return Jwts.builder()
                .signWith(key, SignatureAlgorithm.HS512)
                .setHeaderParam("typ", "JWT")
                .setSubject(String.valueOf(memberId))
                .setExpiration(accessTokenValidity)
                .setIssuedAt(new Date())
                .compact();
    }

    public RefreshToken createRefreshToken(Authentication authentication) {
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        Long memberId = principal.getMemberId();

        String token = Jwts.builder()
                .signWith(key, SignatureAlgorithm.HS512)
                .setSubject(String.valueOf(memberId))
                .compact();
        RefreshToken refreshToken = RefreshToken.createRefreshToken(token, memberId);
        // redis에 refresh token 저장
        refreshTokenRepository.save(refreshToken);

        return refreshToken;
    }

    public CommonSuccessDto deleteRefreshAndAccessToken(String accessToken, String refreshToken) {
        // refreshToken 삭제
        refreshTokenRepository.deleteById(refreshToken);

        // accessToken 블랙 처리
        BlackList blackListToken = BlackList.createBlackList(accessToken);
        blackListRepository.save(blackListToken);

        return CommonSuccessDto.fromEntity(true);
    }
}
