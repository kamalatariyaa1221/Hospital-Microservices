package com.hospitalDepartment.Exception;

import org.springframework.http.HttpStatus;

public class AuthenticationNotFoundException extends Throwable {

    public AuthenticationNotFoundException(HttpStatus httpStatus, String invalidJwTsignature) {
    }
    public AuthenticationNotFoundException(String invalidJwTsignature) {
    }
}
