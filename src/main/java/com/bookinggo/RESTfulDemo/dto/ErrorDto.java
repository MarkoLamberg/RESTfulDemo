package com.bookinggo.RESTfulDemo.dto;

import lombok.*;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ErrorDto {

    private Timestamp timestamp;

    private int status;

    private String error;

    private String message;

    private String path;
}
