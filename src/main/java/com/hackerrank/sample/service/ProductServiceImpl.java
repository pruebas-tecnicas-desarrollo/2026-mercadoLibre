package com.hackerrank.sample.service;

import com.hackerrank.sample.dto.ProductCreatedResponseDto;
import com.hackerrank.sample.dto.ProductDetailsResponseDto;
import com.hackerrank.sample.dto.ProductResponseDto;
import com.hackerrank.sample.dto.ProductsRequestDto;
import com.hackerrank.sample.model.Product;
import com.hackerrank.sample.model.ProductInformation;
import com.hackerrank.sample.repository.ProductRepository;
import com.hackerrank.sample.exception.NoSuchResourceFoundException;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    private Product requireExistingProduct(Long id) {
        log.info("checking if product id '{}' exists...", id);
    
        return productRepository.getProductById(id)
                .orElseGet(() -> {
                    log.warn("product id '{}' not found", id);
                    throw new NoSuchResourceFoundException("product does not exist");
                });
    }

    private ProductResponseDto toProductResponseDto(Product product) {
        return ProductResponseDto.builder()
                .id(product.getId())
                .information(product.getInformation())
                .build();
    }

    @Transactional
    public ProductCreatedResponseDto createProduct(ProductsRequestDto productsRequestDto) {        
        log.debug("received product information: {}", productsRequestDto);
        
        log.info("trying to create the requested product...");
        ProductInformation productInformation = productsRequestDto.getInformation();
        Long id = productRepository.createProduct(productInformation);
        log.debug("the requested product was created successfully");

        return ProductCreatedResponseDto
                .builder()
                .productId(id)
                .build();
    }

    public ProductDetailsResponseDto getProductById(Long id){
        Product product = requireExistingProduct(id);
        
        return ProductDetailsResponseDto.builder()
                .information(product.getInformation())
                .build();
    }


    public List<ProductResponseDto> getAllProducts(){
        log.info("returning all products...");

        List<Product> products = productRepository.getAllProducts();

        return products.stream()
                .map(this::toProductResponseDto)
                .toList();
    }

    
    public List<ProductResponseDto> getProductByIds(List<Long> ids){
        log.info("returning product information of ids: " + ids);

        List<Product> products = productRepository.getProductsByIds(ids);

        return products.stream()
                .map(this::toProductResponseDto)
                .toList();
    }

    @Transactional
    public void deleteProductById(Long id) {
        requireExistingProduct(id);

        boolean removed = productRepository.deleteProductById(id);

        if (!removed) {
            log.warn("product id={} not found for deletion", id);
            throw new NoSuchResourceFoundException("product does not exist");
        }

        log.debug("product deleted successfully, id={}", id);
    }

    @Transactional
    public void deleteAllProducts() {
        log.info("trying to delete all existing products...");
        productRepository.deleteAllProducts();
        log.info("all the products were deleted successfully");
    }

    @Transactional
    public void updateProductById(Long id, ProductInformation productInformation) {
        Product product = requireExistingProduct(id);
        log.debug("old product information: {}", product.getInformation());
        log.debug("new product information: {}", productInformation);

        log.info("trying to update the requested product...");
        boolean updated = productRepository.updateProductById(id, productInformation);

        if (!updated) {
            log.warn("product id={} not found for update", id);
            throw new NoSuchResourceFoundException("product does not exist");
        }

        log.debug("product updated successfully, id={}", id);
    }


}