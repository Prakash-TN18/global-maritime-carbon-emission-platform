package com.prakash.gmcemp.route.exception;

public class BusinessException
        extends RuntimeException {

    public BusinessException(
            String message) {

        super(message);
    }
}