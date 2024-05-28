package kcs.funding.fundingboost.domain.security.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import kcs.funding.fundingboost.domain.dto.global.ResponseDto;
import kcs.funding.fundingboost.domain.exception.ErrorCode;
import net.minidev.json.JSONValue;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

public class OAuth2AuthenticationFailureHandler implements AuthenticationFailureHandler {


    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        String redirectUri = "http://localhost:8080/api/v1/loginForm";

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(ErrorCode.INTERNAL_SERVER_ERROR.getHttpStatus().value());

        ResponseDto<Object> responseDto = ResponseDto.fail(exception);

        response.getWriter().write(JSONValue.toJSONString(responseDto));
    }
}
