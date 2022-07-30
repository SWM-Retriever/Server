package org.retriever.server.dailypet.exception;

import org.retriever.server.dailypet.exception.dto.ApiErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionControllerAdvice {

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ApiErrorResponse> applicationException(ApplicationException exception) {
        String errorCode = exception.getErrorCode();

        return ResponseEntity
                .status(exception.getHttpStatus())
                .body(new ApiErrorResponse(errorCode));
    }
}
