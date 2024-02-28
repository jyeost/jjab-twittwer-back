package jjabtwitter.global.exception;

import lombok.Getter;

@Getter
public class ClientException extends RuntimeException {

    private final ExceptionInformation exceptionInformation;

    public ClientException(ExceptionInformation exceptionInformation) {
        super();
        this.exceptionInformation = exceptionInformation;
    }

    public int getCode() {
        return exceptionInformation.getCode();
    }

    @Override
    public String getMessage() {
        return exceptionInformation.getMessage();
    }
}
