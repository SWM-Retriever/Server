package org.retriever.server.dailypet.global.error.exception;

import lombok.extern.slf4j.Slf4j;
import org.retriever.server.dailypet.global.error.exception.dto.ApiErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static java.util.Objects.requireNonNull;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionControllerAdvice {

    private static final String VALID_ERROR_CODE = "VALID-001";
    private static final String INTERNAL_SERVER_ERROR_CODE = "SERVER-001";

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ApiErrorResponse> applicationException(ApplicationException exception) {
        String errorCode = exception.getErrorCode();
        log.error("Exception : {} ErrorCode : {} Message : {}",
                exception.getClass().getSimpleName(), errorCode, exception.getMessage());
        return ResponseEntity
                .status(exception.getHttpStatus())
                .body(new ApiErrorResponse(errorCode, exception.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> methodArgumentNotValidException(MethodArgumentNotValidException exception) {
        String errorCode = VALID_ERROR_CODE;
        String message = requireNonNull(exception.getFieldError())
                .getDefaultMessage();
        log.error("Exception : {} ErrorCode : {} Message : {}",
                exception.getClass().getSimpleName(), errorCode, message);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST.value())
                .body(new ApiErrorResponse(errorCode, message));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiErrorResponse> runtimeException(RuntimeException exception) {
        String errorCode = INTERNAL_SERVER_ERROR_CODE;
        String message = "내부 서버 에러입니다.";
        log.error("Exception : {} ErrorCode : {} Message : {} Detail : {}",
                exception.getClass().getSimpleName(), errorCode, message, exception.getMessage());
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .body(new ApiErrorResponse(errorCode, message));
    }
}
