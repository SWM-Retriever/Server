package org.retriever.server.dailypet.domain.pet.exception;

import org.springframework.http.HttpStatus;

public class PetTypeNotFoundException extends PetException {

    private static final String ERROR_CODE = "PET-002";
    private static final String MESSAGE = "해당 PetType의 품종 정보를 조회할 수 없습니다.";
    private static final HttpStatus STATUS = HttpStatus.BAD_REQUEST;

    public PetTypeNotFoundException() {
        super(ERROR_CODE, MESSAGE, STATUS);
    }
}
