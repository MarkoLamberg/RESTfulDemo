package com.bookinggo.RestfulDemo.exception;

public class CustomerServiceException extends RuntimeException {

    public CustomerServiceException(String message, Throwable cause) {
        super(message, cause);
    }

}
