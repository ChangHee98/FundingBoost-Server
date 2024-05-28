package kcs.funding.fundingboost.domain.security.controller;

import static kcs.funding.fundingboost.domain.security.utils.SecurityConst.REFRESH;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import jakarta.servlet.http.HttpServletRequest;
import kcs.funding.fundingboost.domain.dto.common.CommonSuccessDto;
import kcs.funding.fundingboost.domain.dto.global.ResponseDto;
import kcs.funding.fundingboost.domain.dto.response.login.JwtAccessTokenDto;
import kcs.funding.fundingboost.domain.security.service.JwtAuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class JwtAuthenticationController {

    private final JwtAuthenticationService jwtAuthenticationService;

    /**
     * refreshToken을 이용해 accessToken 발급
     */
    @PostMapping("/access-reissue")
    public ResponseDto<JwtAccessTokenDto> reIssueAccessToken(HttpServletRequest request) {
        String refreshToken = request.getHeader(AUTHORIZATION);
        JwtAccessTokenDto accessToken = jwtAuthenticationService.createAccessToken(refreshToken);
        return ResponseDto.ok(accessToken);
    }

    @PostMapping("/logout")
    public ResponseDto<CommonSuccessDto> logOut(@RequestHeader(AUTHORIZATION) String accessToken,
                                                @RequestHeader(REFRESH) String refreshToken) {
        return ResponseDto.ok(jwtAuthenticationService.deleteRefreshAndAccessToken(accessToken, refreshToken));
    }
}
