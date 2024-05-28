package kcs.funding.fundingboost.domain.entity;

import static jakarta.persistence.CascadeType.ALL;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import kcs.funding.fundingboost.domain.entity.common.BaseTimeEntity;
import kcs.funding.fundingboost.domain.entity.member.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "funding")
public class Funding extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "funding_id")
    private Long fundingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @OneToMany(mappedBy = "funding", cascade = ALL)
    private List<FundingItem> fundingItems = new ArrayList<>();

    @Column(length = 50)
    private String message;

    @Column(length = 10)
    @Enumerated(EnumType.STRING)
    private Tag tag;

    @NotNull
    @Column(name = "total_price")
    private int totalPrice;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "collect_price")
    private int collectPrice;

    @NotNull
    private LocalDateTime deadline;

    @NotNull
    @Column(name = "funding_status")
    private boolean fundingStatus;


    /**
     * 펀딩 종료하기 버튼 눌렀을 때 펀딩이 종료되고 배송지입력/포인트 전환이 완료되지 않은 상태
     */
    public void terminate() {
        this.deadline = LocalDateTime.now();
    }

    /**
     * 펀딩이 완전히 종료된 상태
     */
    public void finish() {
        this.fundingStatus = false;
    }

    public void extendDeadline(int day) {
        this.deadline = this.deadline.plusDays(day);
    }

    public void addTotalPrice(int itemPrice) {
        this.totalPrice += itemPrice;
    }

    public void fund(int fundedPoint) {
        this.collectPrice += fundedPoint;
    }

    public static Funding createFunding(Member member, String message, Tag tag,
                                        LocalDateTime deadline) {
        Funding funding = new Funding();
        funding.member = member;
        funding.message = message;
        funding.tag = tag;
        funding.totalPrice = 0;
        funding.collectPrice = 0;
        funding.deadline = deadline;
        funding.fundingStatus = true;
        return funding;
    }

    public static Funding createFundingForTest(Member member, String message, Tag tag, int collectPrice,
                                               LocalDateTime deadline,
                                               boolean fundingStatus) {
        Funding funding = new Funding();
        funding.member = member;
        funding.message = message;
        funding.tag = tag;
        funding.totalPrice = 0;
        funding.collectPrice = collectPrice;
        funding.deadline = deadline;
        funding.fundingStatus = fundingStatus;
        return funding;
    }
}
