package kcs.funding.fundingboost.domain.service;

import static kcs.funding.fundingboost.domain.security.utils.SecurityConst.TOKEN_PREFIX;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import kcs.funding.fundingboost.domain.dto.common.CommonSuccessDto;
import kcs.funding.fundingboost.domain.dto.request.login.LoginDto;
import kcs.funding.fundingboost.domain.dto.request.login.SignupDto;
import kcs.funding.fundingboost.domain.dto.response.login.TokenDto;
import kcs.funding.fundingboost.domain.entity.member.Member;
import kcs.funding.fundingboost.domain.repository.MemberRepository;
import kcs.funding.fundingboost.domain.security.SimpleAuthenticationProvider;
import kcs.funding.fundingboost.domain.security.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final SimpleAuthenticationProvider authenticationProvider;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 토큰이 없는 사용자는 SimpleAuthenticationProvider가 검증을 하고 검증에 성공하면 Jwt Token을 생성해서 반환
     */
    public ResponseEntity<TokenDto> createJwtToken(LoginDto loginDto) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                loginDto.nickName(),
                loginDto.password());
        Authentication authenticate = authenticationProvider.authenticate(authentication);
        // username과 password를 이용해 token 생성
        String jwt = JwtUtils.createToken(authenticate);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(AUTHORIZATION, TOKEN_PREFIX + jwt); // header에 토큰을 추가

        return new ResponseEntity<>(TokenDto.fromEntity(jwt), httpHeaders, HttpStatus.OK);
    }

    public CommonSuccessDto signup(SignupDto signupDto) {
        String encodedPassword = passwordEncoder.encode(signupDto.password());

        Member member = Member.createSignUpMember(signupDto.nickname(), encodedPassword, signupDto.email());
        memberRepository.save(member);

        return CommonSuccessDto.fromEntity(true);
    }
}
