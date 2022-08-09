package org.retriever.server.dailypet.domain.pet.exception;

import org.springframework.http.HttpStatus;

public class DuplicatePetNameInFamilyException extends PetException {

    private static final String ERROR_CODE = "PET-001";
    private static final String MESSAGE = "가족 내 중복된 반려동물 이름이 있습니다.";
    private static final HttpStatus STATUS = HttpStatus.CONFLICT;

    public DuplicatePetNameInFamilyException() {
        super(ERROR_CODE, MESSAGE, STATUS);
    }
}
