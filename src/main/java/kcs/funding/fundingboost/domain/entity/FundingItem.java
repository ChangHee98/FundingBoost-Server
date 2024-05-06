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
import kcs.funding.fundingboost.domain.service.utils.FundingUtils;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "funding_item")
public class FundingItem extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "funding_item_id")
    private Long fundingItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "funding_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Funding funding;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Item item;

    @NotNull
    @Column(name = "item_sequence")
    private int itemSequence;

    @NotNull
    @Column(name = "item_status")
    private boolean itemStatus;

    @NotNull
    @Column(name = "finished_status")
    private boolean finishedStatus;

    public static FundingItem createFundingItem(Funding funding, Item item, int itemSequence) {
        FundingItem fundingItem = new FundingItem();
        fundingItem.funding = funding;
        fundingItem.item = item;
        fundingItem.itemSequence = itemSequence;
        fundingItem.itemStatus = true;
        fundingItem.finishedStatus = true;
        funding.getFundingItems().add(fundingItem);
        funding.addFundingItemPrice(item.getItemPrice());
        return fundingItem;
    }

    /**
     * 금액이 다 찬 펀딩 아이템
     */
    public void completeFunding() {
        this.itemStatus = false;
    }

    /**
     * 배송지 입력/포인트 전환이 완료된 펀딩 아이템
     */
    public void finishFundingItem() {
        this.finishedStatus = false;
        FundingUtils.checkFundingFinished(funding); // funding이 종료되었는지 확인
    }
}
