package com.hackerrank.sample.service;

import com.hackerrank.sample.dto.ProductCreatedResponseDto;
import com.hackerrank.sample.dto.ProductDetailsResponseDto;
import com.hackerrank.sample.dto.ProductResponseDto;
import com.hackerrank.sample.model.ProductInformation;
import java.util.List;

public interface ProductService {
    ProductCreatedResponseDto createProduct(ProductInformation productInformation);

    ProductDetailsResponseDto getProductById(Long id);
    List<ProductResponseDto> getProductByIds(List<Long> ids);
    List<ProductResponseDto> getAllProducts();

    void deleteProductById(Long id);
    void deleteAllProducts();

    void updateProductById(Long id, ProductInformation productInformation);
}