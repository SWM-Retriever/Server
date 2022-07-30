package org.retriever.server.dailypet.exception.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ApiErrorResponse {

    private String errorCode;

    public ApiErrorResponse(String errorCode) {
        this.errorCode = errorCode;
    }
}
