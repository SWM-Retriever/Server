package org.retriever.server.dailypet.domain.member.exception;

import org.springframework.http.HttpStatus;

public class DifferentProviderTypeException extends MemberException{

    private static final String ERROR_CODE = "MEMBER-004";
    private static final String MESSAGE = "다른 SNS 계정으로 이미 가입된 회원입니다. 해당 SNS으로 로그인하세요.";
    private static final HttpStatus STATUS = HttpStatus.BAD_REQUEST;

    public DifferentProviderTypeException() {
        super(ERROR_CODE, MESSAGE, STATUS);
    }
}
