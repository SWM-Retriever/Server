package org.retriever.server.dailypet.domain.family.exception;

import org.springframework.http.HttpStatus;

public class DuplicateFamilyNameException extends FamilyException {

    private static final String ERROR_CODE = "FAMILY-001";
    private static final String MESSAGE = "중복된 가족 이름이 존재합니다.";
    private static final HttpStatus STATUS = HttpStatus.CONFLICT;

    public DuplicateFamilyNameException() {
        super(ERROR_CODE, MESSAGE, STATUS);
    }
}
