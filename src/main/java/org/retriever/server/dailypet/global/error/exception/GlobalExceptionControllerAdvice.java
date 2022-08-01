package org.retriever.server.dailypet.global.error.exception;

import lombok.extern.slf4j.Slf4j;
import org.retriever.server.dailypet.global.error.exception.dto.ApiErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionControllerAdvice {

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ApiErrorResponse> applicationException(ApplicationException exception) {
        String errorCode = exception.getErrorCode();
        log.error("Exception : {} ErrorCode : {} Message : {}",
                exception.getClass().getSimpleName(), errorCode,  exception.getMessage());
        return ResponseEntity
                .status(exception.getHttpStatus())
                .body(new ApiErrorResponse(errorCode));
    }
}
