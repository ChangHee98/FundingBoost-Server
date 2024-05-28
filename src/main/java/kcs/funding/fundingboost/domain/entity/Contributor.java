package kcs.funding.fundingboost.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import kcs.funding.fundingboost.domain.entity.common.BaseTimeEntity;
import kcs.funding.fundingboost.domain.entity.member.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "contributor")
public class Contributor extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contributor_id")
    private Long contributorId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "funding_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Funding funding;

    @NotNull
    @Column(name = "contributor_price")
    private int contributorPrice;

    public static Contributor createContributor(int contributorPrice, Member member, Funding funding) {
        Contributor contributor = new Contributor();
        contributor.contributorPrice = contributorPrice;
        contributor.member = member;
        contributor.funding = funding;
        funding.fund(contributorPrice);
        return contributor;
    }
}
