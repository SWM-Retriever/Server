package org.retriever.server.dailypet.domain.report.dto.response;

import lombok.*;
import org.retriever.server.dailypet.domain.diary.enums.ViewType;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class GraphView {

    private ViewType viewType;

    private String careName;

    private List<CareCountInfo> careCountList;

    public static GraphView createTitleView(String careName) {
        return GraphView.builder()
                .viewType(ViewType.TITLE)
                .careName(careName)
                .build();
    }

    public static GraphView createContentView(String careName, List<CareCountInfo> careCountList) {
        return GraphView.builder()
                .viewType(ViewType.CONTENT)
                .careName(careName)
                .careCountList(careCountList)
                .build();
    }
}

