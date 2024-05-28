package kcs.funding.fundingboost.domain.dto.response.login;

import static kcs.funding.fundingboost.domain.security.utils.SecurityConst.TOKEN_PREFIX;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record JwtDto(@JsonProperty("access_token") String accessToken,
                     @JsonProperty("refresh_token") String refreshToken) {

    public static JwtDto fromEntity(String accessToken, String refreshToken) {
        return new JwtDto(
                TOKEN_PREFIX + accessToken,
                refreshToken);
    }
}
