package com.prakash.gmcemp.auth.exception;

public class BusinessException
        extends RuntimeException {

    public BusinessException(
            String message) {

        super(message);
    }
}