package org.retriever.server.dailypet.domain.family.exception;

import org.springframework.http.HttpStatus;

public class DuplicateFamilyRoleNameException extends FamilyException {

    private static final String ERROR_CODE = "FAMILY-002";
    private static final String MESSAGE = "가족 내 중복된 이름이 존재합니다.";
    private static final HttpStatus STATUS = HttpStatus.CONFLICT;

    public DuplicateFamilyRoleNameException() {
        super(ERROR_CODE, MESSAGE, STATUS);
    }
}
