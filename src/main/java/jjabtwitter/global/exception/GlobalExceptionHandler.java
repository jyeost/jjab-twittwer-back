package jjabtwitter.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.EnumMap;

import static jjabtwitter.global.exception.ExceptionInformation.*;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final EnumMap<ExceptionInformation, HttpStatus> exceptionInfoToHttpStatus = new EnumMap<>(
            ExceptionInformation.class);

    public GlobalExceptionHandler() {
        exceptionInfoToHttpStatus.put(LOGIN_FAIL, UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleException(final Exception e) {
        final ExceptionResponse exceptionResponse = new ExceptionResponse(0, "알지 못하는 예외 발생");
        log.error("알지 못하는 예외 발생", e);

        return ResponseEntity.internalServerError()
                .body(exceptionResponse);
    }

    @ExceptionHandler(ClientException.class)
    public ResponseEntity<ExceptionResponse> handlerClientException(final ClientException e) {
        final ExceptionResponse exceptionResponse = new ExceptionResponse(e.getCode(), e.getMessage());

        final HttpStatus httpStatus = exceptionInfoToHttpStatus.getOrDefault(e.getExceptionInformation(), BAD_REQUEST);
        return ResponseEntity.status(httpStatus)
                .body(exceptionResponse);
    }

    @ExceptionHandler(BusinessLogicException.class)
    public ResponseEntity<ExceptionResponse> handlerBusinessLogicException(final BusinessLogicException e) {
        final ExceptionResponse exceptionResponse = new ExceptionResponse(e.getCode(), e.getMessage());

        return ResponseEntity.internalServerError()
                .body(exceptionResponse);
    }
}
