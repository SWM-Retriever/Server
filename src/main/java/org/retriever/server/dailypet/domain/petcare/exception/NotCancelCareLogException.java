package org.retriever.server.dailypet.domain.petcare.exception;

import org.springframework.http.HttpStatus;

public class NotCancelCareLogException extends PetCareException {

    private static final String ERROR_CODE = "CARE-004";
    private static final String MESSAGE = "취소 가능한 항목이 존재하지 않습니다.";
    private static final HttpStatus STATUS = HttpStatus.BAD_REQUEST;

    public NotCancelCareLogException() {
        super(ERROR_CODE, MESSAGE, STATUS);
    }
}
