package com.hackerrank.sample.model;

import jakarta.validation.Valid;
import lombok.Data;

@Data
public class Product {
    private Long id;

    @Valid
    private ProductInformation information;
}
