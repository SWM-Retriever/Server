package org.retriever.server.dailypet.global.utils.security.exception;

import org.retriever.server.dailypet.global.error.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class AuthenticationNotFoundException extends ApplicationException {

    private static final String ERROR_CODE = "SECURITY-001";
    private static final String MESSAGE = "저장된 인증 정보가 없습니다.";
    private static final HttpStatus STATUS = HttpStatus.UNAUTHORIZED;

    public AuthenticationNotFoundException() {
        super(ERROR_CODE, MESSAGE, STATUS);
    }
}
