package com.bookinggo.RestfulDemo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CustomerDto {

    @Size(max = 5)
    @NotNull
    @ApiModelProperty(example = "Mr")
    private String title;

    @Size(max = 50)
    @NotNull
    @ApiModelProperty(example = "John Smith")
    private String name;

    @Override
    public String toString() {
        return String.format("CustomerDto: %s, %s", title, name);
    }
}
