package kcs.funding.fundingboost.domain.config;

import kcs.funding.fundingboost.domain.security.fliter.JwtFilter;
import kcs.funding.fundingboost.domain.security.provider.JwtAuthenticationProvider;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class JwtSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final JwtAuthenticationProvider jwtAuthenticationProvider;

    public JwtSecurityConfig(JwtAuthenticationProvider jwtAuthenticationProvider) {
        this.jwtAuthenticationProvider = jwtAuthenticationProvider;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        JwtFilter jwtFilter = new JwtFilter(jwtAuthenticationProvider);
        http
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
