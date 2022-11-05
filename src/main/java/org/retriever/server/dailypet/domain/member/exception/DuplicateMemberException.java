package org.retriever.server.dailypet.domain.member.exception;

import org.springframework.http.HttpStatus;

public class DuplicateMemberException extends MemberException{

    private static final String ERROR_CODE = "MEMBER-003";
    private static final String MESSAGE = "이미 등록된 회원입니다.";
    private static final HttpStatus STATUS = HttpStatus.CONFLICT;

    public DuplicateMemberException() {
        super(ERROR_CODE, MESSAGE, STATUS);
    }
}
