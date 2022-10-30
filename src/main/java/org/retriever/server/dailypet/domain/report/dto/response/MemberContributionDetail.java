package org.retriever.server.dailypet.domain.report.dto.response;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
@Getter
public class MemberContributionDetail {

    private int rank;
    private String familyRoleName;
    private float contributionPercent;
    private List<CareInfoDetail> careInfoDetailList = new ArrayList<>();

    public static MemberContributionDetail of(String familyRoleName, float percent, List<CareInfoDetail> careInfoDetailList) {
        return MemberContributionDetail.builder()
                .familyRoleName(familyRoleName)
                .contributionPercent(percent)
                .careInfoDetailList(careInfoDetailList)
                .build();
    }

    public void setRanking(int rank) {
        this.rank = rank;
    }
}
