package com.bookinggo.RESTfulDemo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CustomerDto {

    @Size(max = 5)
    @NotNull
    private String title;

    @Size(max = 50)
    @NotNull
    private String name;

    @Override
    public String toString() {
        return String.format("CustomerDto: %s, %s", title, name);
    }
}
