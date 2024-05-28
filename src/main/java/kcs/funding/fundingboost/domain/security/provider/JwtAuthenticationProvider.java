package kcs.funding.fundingboost.domain.security.provider;

import static kcs.funding.fundingboost.domain.exception.ErrorCode.EXPIRED_TOKEN_ERROR;
import static kcs.funding.fundingboost.domain.exception.ErrorCode.INVALID_TOKEN_ERROR;
import static kcs.funding.fundingboost.domain.exception.ErrorCode.TOKEN_MALFORMED_ERROR;
import static kcs.funding.fundingboost.domain.exception.ErrorCode.TOKEN_UNSUPPORTED_ERROR;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import kcs.funding.fundingboost.domain.exception.CommonException;
import kcs.funding.fundingboost.domain.security.CustomUserDetails;
import kcs.funding.fundingboost.domain.security.CustomUserDetailsService;
import kcs.funding.fundingboost.domain.security.entity.BlackList;
import kcs.funding.fundingboost.domain.security.repository.BlackListRepository;
import kcs.funding.fundingboost.domain.security.service.JwtAuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final CustomUserDetailsService customUserDetailsService;
    private final BlackListRepository blackListRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        try {

            // Authentication에서 access token을 얻어옴
            String token = (String) authentication.getPrincipal();

            // access token이 blackList에 등록되어 있는지 확인
            Optional<BlackList> findAccessToken = blackListRepository.findById(token);
            if (findAccessToken.isPresent()) {
                throw new CommonException(EXPIRED_TOKEN_ERROR);
            }

            ObjectMapper mapper = new ObjectMapper();

            Jws<Claims> tokenParser = Jwts.parserBuilder()
                    .setSigningKey(JwtAuthenticationService.getKey())
                    .build()
                    .parseClaimsJws(token);

            // header 정보
            JwsHeader header = tokenParser.getHeader();

            // body 정보
            Claims body = tokenParser.getBody();
            String payLoad = mapper.writeValueAsString(body);

            // signature 정보
            String signature = tokenParser.getSignature();

            // header와 payLoad를 이용해 signature 계산
            String calculatedSignature = Jwts.builder()
                    .signWith(JwtAuthenticationService.getKey(), SignatureAlgorithm.HS512)
                    .setHeader((Map<String, Object>) header)
                    .setPayload(payLoad)
                    .compact()
                    .split("\\.")[2];

            if (signature.equals(calculatedSignature)) {
                // request에서 얻은 signature와 서버가 계산한 signature가 동일한 경우
                Long userId = Long.parseLong(body.getSubject());
                CustomUserDetails principal = customUserDetailsService.loadUserByUserId(userId);

                return new UsernamePasswordAuthenticationToken(principal, null,
                        List.of(new SimpleGrantedAuthority("ROLE_USER")));
            } else {
                throw new CommonException(TOKEN_MALFORMED_ERROR);
            }
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException exception) {
            throw new CommonException(TOKEN_MALFORMED_ERROR);
        } catch (ExpiredJwtException e) {
            // 토큰이 만료되면 EXPIRED_TOKEN_ERROR를 던짐
            throw new CommonException(EXPIRED_TOKEN_ERROR);
        } catch (UnsupportedJwtException e) {
            throw new CommonException(INVALID_TOKEN_ERROR);
        } catch (IllegalArgumentException e) {
            throw new CommonException(TOKEN_UNSUPPORTED_ERROR);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(UsernamePasswordAuthenticationToken.class);
    }
}
