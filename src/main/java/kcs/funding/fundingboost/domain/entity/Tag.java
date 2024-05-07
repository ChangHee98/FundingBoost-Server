package kcs.funding.fundingboost.domain.entity;

import kcs.funding.fundingboost.domain.exception.CommonException;
import kcs.funding.fundingboost.domain.exception.ErrorCode;
import lombok.Getter;

@Getter
public enum Tag {
    BIRTHDAY("생일"),
    GRADUATE("졸업"),
    ETC("기타");

    private final String tag;

    Tag(String tag) {
        this.tag = tag;
    }

    public String getDisplayName() {
        return "#" + this.tag;
    }

    public static Tag getTag(String targetTag) {
        for (Tag tag : Tag.values()) {
            if (tag.getTag().equals(targetTag)) {
                return tag;
            }
        }
        throw new CommonException(ErrorCode.INTERNAL_SERVER_ERROR);
    }
}