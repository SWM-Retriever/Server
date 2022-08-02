package org.retriever.server.dailypet.domain.oauth;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends UserException{

    private static final String MESSAGE = "존재하지 않는 회원입니다.";
    private static final String ERROR_CODE = "USER-401";

    public UserNotFoundException() {
        super(ERROR_CODE, MESSAGE, HttpStatus.UNAUTHORIZED);
    }
}
