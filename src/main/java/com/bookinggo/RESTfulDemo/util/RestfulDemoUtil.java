package com.bookinggo.RESTfulDemo.util;

import com.bookinggo.RESTfulDemo.dto.ErrorDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.sql.Timestamp;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class RestfulDemoUtil {

    public static ResponseEntity<ErrorDto> badRequestResponse(String message) {
        String path = ServletUriComponentsBuilder.fromCurrentRequest().toUriString();
        return ResponseEntity
                .badRequest()
                .body(ErrorDto.builder()
                        .timestamp(new Timestamp(System.currentTimeMillis()))
                        .status(BAD_REQUEST.value())
                        .error(BAD_REQUEST.name().toLowerCase().replace("bad_request", "Bad Request"))
                        .message(message)
                        .path(path)
                        .build());
    }
}
