package kcs.funding.fundingboost.domain.security;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.util.StringUtils;


@Getter
public class KakaoOAuth2User implements OAuth2User {

    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "nickname";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_IMAGE_URL = "profile_image_url";
    private static final String KEY_EXTRA_DATA = "kakao_account";

    private Map<String, Object> attributes;

    public KakaoOAuth2User(Map<String, Object> attributes) {

        this.attributes = attributes;
    }

    public String getId() {

        return Objects.toString(attributes.get(KEY_ID));
    }

    @Override
    public String getName() {

        return (String) this.getProfile().get(KEY_NAME);
    }

    public Map<String, Object> getExtraData() {

        return (Map<String, Object>) attributes.get(KEY_EXTRA_DATA);
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

    @Override
    public <A> A getAttribute(String name) {
        return OAuth2User.super.getAttribute(name);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    public boolean validateNecessaryFields() {
        return StringUtils.hasText(this.getEmail())
                && StringUtils.hasText(this.getId())
                && StringUtils.hasText(this.getName())
                && StringUtils.hasText(this.getImageUrl());
    }

}
