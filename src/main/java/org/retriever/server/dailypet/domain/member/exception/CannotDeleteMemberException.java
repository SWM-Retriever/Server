package org.retriever.server.dailypet.domain.member.exception;

import org.springframework.http.HttpStatus;

public class CannotDeleteMemberException extends MemberException {

    private static final String ERROR_CODE = "MEMBER-005";
    private static final String MESSAGE = "그룹원이 존재하는 그룹의 그룹장은 회원탈퇴를 할 수 없습니다.";
    private static final HttpStatus STATUS = HttpStatus.FORBIDDEN;

    public CannotDeleteMemberException() {
        super(ERROR_CODE, MESSAGE, STATUS);
    }
}
