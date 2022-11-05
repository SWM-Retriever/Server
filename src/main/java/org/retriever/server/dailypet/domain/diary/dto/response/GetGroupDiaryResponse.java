package org.retriever.server.dailypet.domain.diary.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class GetGroupDiaryResponse {

    private List<DiaryView> diaryList;

    public static GetGroupDiaryResponse from(List<DiaryView> list) {
        return GetGroupDiaryResponse.builder()
                .diaryList(list)
                .build();
    }
}
