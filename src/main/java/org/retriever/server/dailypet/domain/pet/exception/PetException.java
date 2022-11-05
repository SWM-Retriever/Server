package org.retriever.server.dailypet.domain.pet.exception;

import org.retriever.server.dailypet.global.error.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class PetException extends ApplicationException {

    public PetException(String errorCode, String message, HttpStatus httpStatus) {
        super(errorCode, message, httpStatus);
    }
}
