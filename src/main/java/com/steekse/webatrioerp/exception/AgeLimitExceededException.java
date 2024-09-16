package com.steekse.webatrioerp.exception;

public class AgeLimitExceededException extends RuntimeException {
    public AgeLimitExceededException(String message) {
        super(message);
    }
}
