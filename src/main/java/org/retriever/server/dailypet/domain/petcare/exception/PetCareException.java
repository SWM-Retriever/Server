package org.retriever.server.dailypet.domain.petcare.exception;

import org.retriever.server.dailypet.global.error.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class PetCareException extends ApplicationException {
    public PetCareException(String errorCode, String message, HttpStatus httpStatus) {
        super(errorCode, message, httpStatus);
    }
}
