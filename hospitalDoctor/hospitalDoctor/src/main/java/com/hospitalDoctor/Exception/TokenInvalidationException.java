package com.hospitalDoctor.Exception;

public class TokenInvalidationException extends RuntimeException {

    public TokenInvalidationException(String message) {
        super(message);
    }

    public TokenInvalidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
