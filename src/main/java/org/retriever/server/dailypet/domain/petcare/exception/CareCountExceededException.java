package org.retriever.server.dailypet.domain.petcare.exception;

import org.springframework.http.HttpStatus;

public class CareCountExceededException extends PetCareException{

    private static final String ERROR_CODE = "CARE-002";
    private static final String MESSAGE = "해당하는 챙겨주기의 할당량을 초과했습니다.";
    private static final HttpStatus STATUS = HttpStatus.BAD_REQUEST;

    public CareCountExceededException() {
        super(ERROR_CODE, MESSAGE, STATUS);
    }
}
