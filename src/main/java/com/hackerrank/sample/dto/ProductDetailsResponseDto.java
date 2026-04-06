package com.hackerrank.sample.dto;

import com.hackerrank.sample.model.ProductInformation;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductDetailsResponseDto {
    private ProductInformation information;

}
