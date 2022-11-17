package org.retriever.server.dailypet.domain.diary.dto.response;

import lombok.*;
import org.retriever.server.dailypet.domain.diary.entity.Diary;
import org.retriever.server.dailypet.domain.diary.enums.ViewType;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class DiaryView {

    private ViewType viewType;

    private LocalDate date;

    private Long diaryId;

    private String authorImageUrl;

    private String authorNickName;

    private String diaryImageUrl;

    private String diaryText;

    public static DiaryView createDataView(LocalDate date) {
        return DiaryView.builder()
                .viewType(ViewType.DATE)
                .date(date)
                .build();
    }

    public static DiaryView createContentView(Diary diary) {
        return DiaryView.builder()
                .viewType(ViewType.DIARY)
                .date(diary.getPublishDate())
                .diaryId(diary.getDiaryId())
                .diaryText(diary.getDiaryText())
                .authorNickName(diary.getAuthor().getFamilyRoleName())
                .authorImageUrl(diary.getAuthor().getProfileImageUrl())
                .diaryImageUrl(diary.getDiaryImageUrl())
                .build();
    }
}
