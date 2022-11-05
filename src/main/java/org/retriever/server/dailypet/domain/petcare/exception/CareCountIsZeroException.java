package org.retriever.server.dailypet.domain.petcare.exception;

import org.springframework.http.HttpStatus;

public class CareCountIsZeroException extends PetCareException {

    private static final String ERROR_CODE = "CARE-003";
    private static final String MESSAGE = "해당하는 챙겨주기의 횟수가 0입니다.";
    private static final HttpStatus STATUS = HttpStatus.BAD_REQUEST;

    public CareCountIsZeroException() {
        super(ERROR_CODE, MESSAGE, STATUS);
    }
}
