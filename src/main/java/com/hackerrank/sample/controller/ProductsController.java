package com.hackerrank.sample.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import java.util.List;
import com.hackerrank.sample.model.ProductInformation;
import com.hackerrank.sample.dto.ProductCreatedResponseDto;
import com.hackerrank.sample.dto.ProductDetailsResponseDto;
import com.hackerrank.sample.dto.ProductResponseDto;
import com.hackerrank.sample.service.ProductService;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/products")
@Slf4j
@RequiredArgsConstructor
public class ProductsController {
    private final ProductService productService;

    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public ProductCreatedResponseDto createProduct(@RequestBody ProductInformation productInformation) {
        log.info("createProduct - request received");
        ProductCreatedResponseDto productCreatedResponseDto = productService.createProduct(productInformation);
        log.info("createProduct - request processed successfully");
        return productCreatedResponseDto;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProductDetailsResponseDto getProductById(@PathVariable Long id) {
        log.info("getProductById - request received");
        ProductDetailsResponseDto productDetailsResponseDto = productService.getProductById(id);
        log.info("getProductById - request processed successfully");
        return productDetailsResponseDto;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponseDto> getAllProducts() {
        log.info("getAllProducts - request received");
        List<ProductResponseDto> productResponseDtos = productService.getAllProducts();
        log.info("getAllProducts - request processed successfully");
        return productResponseDtos;
    }

    @GetMapping("/ids")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponseDto> getProductByIds(@RequestParam List<Long> ids) {
        log.info("getProductByIds - request received");
        List<ProductResponseDto> productResponseDto = productService.getProductByIds(ids);
        log.info("getProductByIds - request processed successfully");
        return productResponseDto;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProductById(@PathVariable Long id) {
        log.info("deleteProductById - request received");
        productService.deleteProductById(id);
        log.info("deleteProductById - request processed successfully");
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAllProducts() {
        log.info("deleteAllProducts - request received");
        productService.deleteAllProducts();
        log.info("deleteAllProducts - request processed successfully");
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void updateProduct(@PathVariable Long id, @RequestBody ProductInformation productInformation) {
        log.info("updateProduct - request received");
        productService.updateProduct(id, productInformation);
        log.info("updateProduct - request processed successfully");
    }
}
