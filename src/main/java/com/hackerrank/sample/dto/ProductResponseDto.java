package com.hackerrank.sample.dto;

import com.hackerrank.sample.model.ProductInformation;
import lombok.Data;

@Data
public class ProductResponseDto {
    private Long id;
    private ProductInformation information;
}
