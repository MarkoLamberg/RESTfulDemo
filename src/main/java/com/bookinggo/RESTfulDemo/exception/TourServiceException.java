package com.bookinggo.RestfulDemo.exception;

public class TourServiceException extends RuntimeException {

    public TourServiceException(String message, Throwable cause) {
        super(message, cause);
    }

}
