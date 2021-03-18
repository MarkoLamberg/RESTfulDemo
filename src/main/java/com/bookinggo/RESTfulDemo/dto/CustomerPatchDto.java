package com.bookinggo.RestfulDemo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CustomerPatchDto {

    @Size(max = 5)
    @ApiModelProperty(example = "Mr")
    private String title;

    @Size(max = 50)
    @ApiModelProperty(example = "John Smith")
    private String name;

    @Override
    public String toString() {
        return String.format("CustomerPatchDto: %s, %s", title, name);
    }
}
