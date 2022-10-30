package org.retriever.server.dailypet.domain.report.dto.response;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
@Getter
public class GetMyContributionResponse {

    private float contributionPercent;

    public static GetMyContributionResponse of(int careCount, int totalCount) {
        float contributionPercent = (float) careCount / totalCount;
        return GetMyContributionResponse.builder()
                .contributionPercent(contributionPercent * 100)
                .build();
    }
}
