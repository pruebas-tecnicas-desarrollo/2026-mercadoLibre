package com.hackerrank.sample.model;

import jakarta.validation.Valid;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Product {
    private Long id;

    @Valid
    private ProductInformation information;
}
