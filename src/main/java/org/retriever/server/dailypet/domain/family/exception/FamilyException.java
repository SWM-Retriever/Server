package org.retriever.server.dailypet.domain.family.exception;

import org.retriever.server.dailypet.global.error.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class FamilyException extends ApplicationException {
    public FamilyException(String errorCode, String message, HttpStatus httpStatus) {
        super(errorCode, message, httpStatus);
    }
}
