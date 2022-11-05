package org.retriever.server.dailypet.domain.family.exception;

import org.springframework.http.HttpStatus;

public class FamilyNotFoundException extends FamilyException{

    private static final String ERROR_CODE = "FAMILY-003";
    private static final String MESSAGE = "해당하는 가족이 없습니다.";
    private static final HttpStatus STATUS = HttpStatus.BAD_REQUEST;

    public FamilyNotFoundException() {
        super(ERROR_CODE, MESSAGE, STATUS);
    }
}
