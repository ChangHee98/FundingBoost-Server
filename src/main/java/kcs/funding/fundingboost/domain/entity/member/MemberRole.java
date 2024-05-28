package kcs.funding.fundingboost.domain.entity.member;

public enum MemberRole {
    ROLE_USER("사용자");

    private String role;

    MemberRole(String role) {
        this.role = role;
    }
}
