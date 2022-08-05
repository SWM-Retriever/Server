package org.retriever.server.dailypet.domain.member.exception;

import org.retriever.server.dailypet.domain.member.exception.MemberException;
import org.springframework.http.HttpStatus;

public class MemberNotFoundException extends MemberException {

    private static final String MESSAGE = "등록되지 않은 회원입니다.";
    private static final String ERROR_CODE = "MEMBER-001";

    public MemberNotFoundException() {
        super(ERROR_CODE, MESSAGE, HttpStatus.UNAUTHORIZED);
    }
}
