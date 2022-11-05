package org.retriever.server.dailypet.domain.pet.exception;

import org.springframework.http.HttpStatus;

public class PetNotFoundException extends PetException{

    private static final String ERROR_CODE = "PET-003";
    private static final String MESSAGE = "해당하는 반려동물이 없습니다.";
    private static final HttpStatus STATUS = HttpStatus.BAD_REQUEST;

    public PetNotFoundException() {
        super(ERROR_CODE, MESSAGE, STATUS);
    }
}
