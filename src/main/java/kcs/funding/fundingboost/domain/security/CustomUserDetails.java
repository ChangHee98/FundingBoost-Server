package kcs.funding.fundingboost.domain.security;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import kcs.funding.fundingboost.domain.entity.member.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.util.StringUtils;


public class CustomUserDetails implements UserDetails, OAuth2User {

    private Member member;
    private Map<String, Object> attributes;

    //OAuth 관련 변수
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "nickname";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_IMAGE_URL = "profile_image_url";
    private static final String KEY_EXTRA_DATA = "kakao_account";

    //일반 로그인 시 생성되는 생성자
    public CustomUserDetails(Member member) {
        this.member = member;
    }

    //OAuth 로그인 시 생성되는 생성자
    public CustomUserDetails(Map<String, Object> attributes, Member member) {
        this.attributes = attributes;
        this.member = member;
    }

    //OAuth 유저 설정
    @Override
    public Map<String, Object> getAttributes() {
        return Map.of();
    }

    @Override
    public <A> A getAttribute(String name) {
        return OAuth2User.super.getAttribute(name);
    }

    @Override
    public String getName() {
        return (String) this.getProfile().get(KEY_NAME);
    }

    public String getId() {
        return Objects.toString(attributes.get(KEY_ID));
    }

    public String getEmail() {
        return (String) this.getExtraData().get(KEY_EMAIL);
    }

    public String getImageUrl() {
        return (String) this.getProfile().get(KEY_IMAGE_URL);
    }

    @SuppressWarnings({"unchecked"})
    private Map<String, Object> getProfile() {
        return (Map<String, Object>) this.getExtraData().get("profile");
    }

    @SuppressWarnings({"unchecked"})
    public Map<String, Object> getExtraData() {
        return (Map<String, Object>) attributes.get(KEY_EXTRA_DATA);
    }

    public boolean validateNecessaryFields() {
        return StringUtils.hasText(this.getEmail())
                && StringUtils.hasText(this.getId())
                && StringUtils.hasText(this.getName())
                && StringUtils.hasText(this.getImageUrl());
    }

    //auth 유저 설정
    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getNickName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Long getMemberId() {
        return member.getMemberId();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

}
