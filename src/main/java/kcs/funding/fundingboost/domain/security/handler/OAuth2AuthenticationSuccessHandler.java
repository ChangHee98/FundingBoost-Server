package kcs.funding.fundingboost.domain.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import kcs.funding.fundingboost.domain.dto.global.ResponseDto;
import kcs.funding.fundingboost.domain.dto.response.login.JwtDto;
import kcs.funding.fundingboost.domain.security.service.JwtAuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Slf4j
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtAuthenticationService jwtAuthenticationService;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        String accessToken = jwtAuthenticationService.createAccessToken(authentication);
        String refreshToken = jwtAuthenticationService.createRefreshToken(authentication).getToken();

        JwtDto jwtTokenDto = JwtDto.fromEntity(accessToken, refreshToken);
        ResponseDto<JwtDto> responseDto = ResponseDto.ok(jwtTokenDto);

        //body에 넣어 전송
        response.setContentType("application/json; charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(objectMapper.writeValueAsString(responseDto));


    }

}
