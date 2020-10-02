package com.bookinggo.RESTfulDemo.exception;

public class TourServiceException extends RuntimeException {

    public TourServiceException(String message, Throwable cause) {
        super(message, cause);
    }

}
