package kcs.funding.fundingboost.domain.dto.response.login;

import com.fasterxml.jackson.annotation.JsonProperty;

public record JwtAccessTokenDto(
        @JsonProperty("access_token") String accessToken
) {
    public static JwtAccessTokenDto fromEntity(String accessToken) {
        return new JwtAccessTokenDto(accessToken);
    }
}
