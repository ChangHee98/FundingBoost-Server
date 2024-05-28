package kcs.funding.fundingboost.domain.dto.request.login;

public record SignupDto(
        String nickName,
        String password,
        String email
) {
}
