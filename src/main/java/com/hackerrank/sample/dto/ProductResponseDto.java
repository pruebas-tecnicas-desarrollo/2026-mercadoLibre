package com.hackerrank.sample.dto;

import com.hackerrank.sample.model.ProductInformation;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductResponseDto {
    private Long id;
    private ProductInformation information;
}
