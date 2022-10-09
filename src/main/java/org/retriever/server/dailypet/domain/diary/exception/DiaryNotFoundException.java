package org.retriever.server.dailypet.domain.diary.exception;

import org.springframework.http.HttpStatus;

public class DiaryNotFoundException extends DiaryException {

    private static final String ERROR_CODE = "DIARY-001";
    private static final String MESSAGE = "해당 반려 일지를 찾을 수 없습니다.";
    private static final HttpStatus STATUS = HttpStatus.BAD_REQUEST;

    public DiaryNotFoundException() {
        super(ERROR_CODE, MESSAGE, STATUS);
    }
}
