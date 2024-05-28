package kcs.funding.fundingboost.domain.service;

import kcs.funding.fundingboost.domain.entity.member.Member;
import kcs.funding.fundingboost.domain.repository.MemberRepository;
import kcs.funding.fundingboost.domain.security.CustomUserDetails;
import kcs.funding.fundingboost.domain.security.KakaoOAuth2User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public CustomUserDetails loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        KakaoOAuth2User kakaoOAuth2User = new KakaoOAuth2User(oAuth2User.getAttributes());

        //사용자 정보 잘 넘어왔는지 확인
        if (!kakaoOAuth2User.validateNecessaryFields()) {
            throw new RuntimeException();
        }
        log.info(kakaoOAuth2User.toString());

        String provider = userRequest.getClientRegistration().getClientId(); //kakao
        String providerId = kakaoOAuth2User.getId();
        String username = kakaoOAuth2User.getName();
        String password = passwordEncoder.encode(provider + "_" + providerId);
        String email = kakaoOAuth2User.getEmail();
        String profileImgUrl = kakaoOAuth2User.getImageUrl();

        //회원일 경우 db에서 멤버탐색
        Member findMember = memberRepository.findByEmail(email).orElse(null);

        CustomUserDetails customUserDetails = null;
        //비회원일 경우 db에 맴버 저장
        if (findMember == null) {
            Member createMember = Member.createMember(username, email, password, profileImgUrl, "aaaa");
            customUserDetails = new CustomUserDetails(kakaoOAuth2User.getAttributes(), createMember);
            memberRepository.save(createMember);

        } else {
            customUserDetails = new CustomUserDetails(kakaoOAuth2User.getAttributes(), findMember);
        }

        return customUserDetails;
    }
}
