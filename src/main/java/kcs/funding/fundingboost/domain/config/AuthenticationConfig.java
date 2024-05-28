package kcs.funding.fundingboost.domain.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import kcs.funding.fundingboost.domain.security.handler.OAuth2AuthenticationFailureHandler;
import kcs.funding.fundingboost.domain.security.handler.OAuth2AuthenticationSuccessHandler;
import kcs.funding.fundingboost.domain.security.service.JwtAuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class AuthenticationConfig {

    private final JwtAuthenticationService jwtAuthenticationService;
    private final ObjectMapper objectMapper;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler() {
        return new OAuth2AuthenticationSuccessHandler(jwtAuthenticationService, objectMapper);
    }

    @Bean
    public OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler() {
        return new OAuth2AuthenticationFailureHandler();
    }
}
