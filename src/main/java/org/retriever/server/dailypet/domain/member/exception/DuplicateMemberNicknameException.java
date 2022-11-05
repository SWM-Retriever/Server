package org.retriever.server.dailypet.domain.member.exception;

import org.springframework.http.HttpStatus;

public class DuplicateMemberNicknameException extends MemberException{

    private static final String ERROR_CODE = "MEMBER-002";
    private static final String MESSAGE = "중복된 닉네임입니다.";
    private static final HttpStatus STATUS = HttpStatus.CONFLICT;

    public DuplicateMemberNicknameException() {
        super(ERROR_CODE, MESSAGE, STATUS);
    }
}
