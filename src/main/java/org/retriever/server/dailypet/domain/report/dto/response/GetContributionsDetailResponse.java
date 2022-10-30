package org.retriever.server.dailypet.domain.report.dto.response;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
@Getter
public class GetContributionsDetailResponse {

    private List<MemberContributionDetail> contributionDetailList = new ArrayList<>();

    public static GetContributionsDetailResponse from(List<MemberContributionDetail> contributionDetailList) {
        return GetContributionsDetailResponse.builder()
                .contributionDetailList(contributionDetailList)
                .build();
    }
}
