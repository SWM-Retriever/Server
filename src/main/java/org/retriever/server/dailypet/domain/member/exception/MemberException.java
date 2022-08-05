package org.retriever.server.dailypet.domain.member.exception;

import org.retriever.server.dailypet.global.error.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class MemberException extends ApplicationException {
    public MemberException(String errorCode, String message, HttpStatus httpStatus) {
        super(errorCode, message, httpStatus);
    }
}
