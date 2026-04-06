package com.hackerrank.sample.dto;

import com.hackerrank.sample.model.ProductInformation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProductsRequestDto {

    @NotNull(message = "information is required")
    @Valid
    private ProductInformation information;
}
