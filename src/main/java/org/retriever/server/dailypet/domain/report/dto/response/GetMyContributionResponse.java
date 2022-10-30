package org.retriever.server.dailypet.domain.report.dto.response;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
@Getter
public class GetMyContributionResponse {

    private float contributionPercent;

    public static GetMyContributionResponse from(float contributionPercent) {
        return GetMyContributionResponse.builder()
                .contributionPercent(contributionPercent)
                .build();
    }
}
