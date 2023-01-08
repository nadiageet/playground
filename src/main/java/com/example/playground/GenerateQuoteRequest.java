package com.example.playground;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class GenerateQuoteRequest {

    @Max(value = 3)
    @NotNull
    private Integer generationNumber;

    @NotBlank
    private String why;
}
