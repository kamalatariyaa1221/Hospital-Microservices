package com.patient.exception;

import org.springframework.http.HttpStatus;

public class AuthenticationNotFoundException extends Throwable {
    public AuthenticationNotFoundException(HttpStatus httpStatus, String invalidJwTsignature) {
    }
}
