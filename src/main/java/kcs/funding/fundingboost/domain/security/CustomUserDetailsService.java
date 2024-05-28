package kcs.funding.fundingboost.domain.security;

import static kcs.funding.fundingboost.domain.exception.ErrorCode.NOT_FOUND_MEMBER;

import kcs.funding.fundingboost.domain.entity.member.Member;
import kcs.funding.fundingboost.domain.exception.CommonException;
import kcs.funding.fundingboost.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("userDetailsService")
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 회원 이름이 있는지 먼저 검증
        Member member = memberRepository.findByNickName(username).orElseThrow(
                () -> new CommonException(NOT_FOUND_MEMBER)
        );

        return new CustomUserDetails(member);
    }

    public CustomUserDetails loadUserByUserId(Long userId) {
        Member member = memberRepository.findById(userId).orElseThrow(
                () -> new CommonException(NOT_FOUND_MEMBER)
        );

        return new CustomUserDetails(member);
    }

    public CustomUserDetails loadUserByEmail(String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow(
                () -> new CommonException(NOT_FOUND_MEMBER)
        );

        return new CustomUserDetails(member);
    }
}
