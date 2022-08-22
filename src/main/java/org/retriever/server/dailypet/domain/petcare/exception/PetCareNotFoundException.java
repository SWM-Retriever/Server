package org.retriever.server.dailypet.domain.petcare.exception;

import org.springframework.http.HttpStatus;

public class PetCareNotFoundException extends PetCareException{

    private static final String ERROR_CODE = "CARE-001";
    private static final String MESSAGE = "해당하는 챙겨주기 항목이 존재하지 않습니다.";
    private static final HttpStatus STATUS = HttpStatus.BAD_REQUEST;

    public PetCareNotFoundException() {
        super(ERROR_CODE, MESSAGE, STATUS);
    }
}
