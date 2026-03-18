package com.hackerrank.sample.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductInformation {
    @NotBlank(message = "name is required")
    private String name;

    @NotBlank(message = "imageUrl is required")
    private String imageUrl;

    @NotBlank(message = "description is required")
    private String description;

    @NotNull(message = "price is required")
    @Positive(message = "price must be a positive number")
    private BigDecimal price;

    @PositiveOrZero(message = "rating must be a positive number or zero")
    private BigDecimal rating;
    private List<ProductSpecification> specifications;

}
