package org.retriever.server.dailypet.global.error.exception.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ApiErrorResponse {

    private String errorCode;

    private String message;

    public ApiErrorResponse(String errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }
}
