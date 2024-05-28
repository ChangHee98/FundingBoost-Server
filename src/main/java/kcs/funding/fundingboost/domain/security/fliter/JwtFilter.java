package kcs.funding.fundingboost.domain.security.fliter;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.util.StringUtils.hasText;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import kcs.funding.fundingboost.domain.security.provider.JwtAuthenticationProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final JwtAuthenticationProvider jwtAuthenticationProvider;

    public JwtFilter(JwtAuthenticationProvider jwtAuthenticationProvider) {
        this.jwtAuthenticationProvider = jwtAuthenticationProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String jwt = resolveToken(request); // Bearer 뒤, 토큰 부분만 파싱

        if (hasText(jwt)) {
            // Bearer 텍스트로 갖는 accessToken을 갖는다면 authentication을 만들어 검증을 시도
            UsernamePasswordAuthenticationToken requestAuthentication = new UsernamePasswordAuthenticationToken(
                    jwt, "");
            // AuthenticationProvider를 이용해 검증
            Authentication authentication = jwtAuthenticationProvider.authenticate(requestAuthentication);
            // 생성된 Authentication을 SecurityContextHolder에 있는 SecurityContext에 저장
            SecurityContextHolder.getContextHolderStrategy().getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

    /**
     * accessToken을 반환
     */
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION);
        if (hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
