package org.retriever.server.dailypet.domain.report.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class GetContributionGraphResponse {

    private List<GraphView> graphList;

    public static GetContributionGraphResponse from(List<GraphView> list) {
        return GetContributionGraphResponse.builder()
                .graphList(list)
                .build();
    }
}
