package org.retriever.server.dailypet.domain.diary.dto.request;

import lombok.Getter;

@Getter
public class EditDiaryRequest {

    private String diaryText;

    private String diaryImageUrl;
}
