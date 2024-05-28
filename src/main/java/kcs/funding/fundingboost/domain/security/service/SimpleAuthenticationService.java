package kcs.funding.fundingboost.domain.security.service;

import kcs.funding.fundingboost.domain.dto.common.CommonSuccessDto;
import kcs.funding.fundingboost.domain.dto.request.login.LoginDto;
import kcs.funding.fundingboost.domain.dto.request.login.SignupDto;
import kcs.funding.fundingboost.domain.dto.response.login.JwtDto;
import kcs.funding.fundingboost.domain.entity.member.Member;
import kcs.funding.fundingboost.domain.repository.MemberRepository;
import kcs.funding.fundingboost.domain.security.entity.RefreshToken;
import kcs.funding.fundingboost.domain.security.provider.SimpleAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SimpleAuthenticationService {

    private final JwtAuthenticationService jwtAuthenticationService;
    private final SimpleAuthenticationProvider simpleAuthenticationProvider;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 토큰이 없는 사용자는 SimpleAuthenticationProvider가 검증을 하고 검증에 성공하면 Jwt Token을 생성해서 반환
     */
    public JwtDto initialLogin(LoginDto loginDto) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                loginDto.email(),
                loginDto.password());

        // username과 password를 검증
        Authentication authenticate = simpleAuthenticationProvider.authenticate(authentication);

        // username과 password를 이용해 token 생성
        String accessToken = jwtAuthenticationService.createAccessToken(authenticate);
        RefreshToken refreshToken = jwtAuthenticationService.createRefreshToken(authenticate);

        return JwtDto.fromEntity(accessToken, refreshToken.getToken());
    }

    /**
     * 회원 가입
     */
    @Transactional
    public CommonSuccessDto signup(SignupDto signupDto) {
        String encodedPassword = passwordEncoder.encode(signupDto.password());

        Member member = Member.createSignUpMember(signupDto.nickName(), encodedPassword, signupDto.email());
        memberRepository.save(member);

        return CommonSuccessDto.fromEntity(true);
    }
}
