package kcs.funding.fundingboost.domain.dto.response.login;

public record JwtRefreshTokenDto(
        String refreshToken
) {
    public static JwtRefreshTokenDto fromEntity(String refreshToken) {
        return new JwtRefreshTokenDto(refreshToken);
    }
}
