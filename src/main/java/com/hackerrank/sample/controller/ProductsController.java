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
import com.hackerrank.sample.dto.ProductsRequestDto;
import com.hackerrank.sample.service.ProductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/products")
@Slf4j
@RequiredArgsConstructor
public class ProductsController {
    private final ProductService productService;

    @Operation(summary = "Creates a new product", description = "Create a new product, persists it and returns the generated productId")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Product created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProductCreatedResponseDto.class)
                    )),
            @ApiResponse(responseCode = "400", description = "Invalid request payload", content = @Content)
    })
    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public ProductCreatedResponseDto createProduct(@RequestBody @Valid ProductsRequestDto productsRequestDto) {
        log.info("createProduct - request received");
        ProductCreatedResponseDto productCreatedResponseDto = productService.createProduct(productsRequestDto);
        log.info("createProduct - request processed successfully");
        return productCreatedResponseDto;
    }

    @Operation(summary = "Get product by id", description = "Retrieves a single product by its identifier")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Product returned successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProductDetailsResponseDto.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Invalid product id", content = @Content),
            @ApiResponse(responseCode = "404", description = "Product not found", content = @Content)
    })
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProductDetailsResponseDto getProductById(@PathVariable @Positive(message = "product id must be a positive number") Long id) {
        log.info("getProductById - request received");
        ProductDetailsResponseDto productDetailsResponseDto = productService.getProductById(id);
        log.info("getProductById - request processed successfully");
        return productDetailsResponseDto;
    }

    /**
     * For comparison use cases, prefer the multi-item endpoint (?ids=).
     */
    @Operation(summary = "Get all products", description = "Retrieves all stored products")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Products returned successfully",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ProductResponseDto.class))
                    )
            )
    })
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponseDto> getAllProducts() {
        log.info("getAllProducts - request received");
        List<ProductResponseDto> productResponseDtos = productService.getAllProducts();
        log.info("getAllProducts - request processed successfully");
        return productResponseDtos;
    }

    /**
     * Retrieves multiple products by their identifiers.
     * <p>
     * This endpoint exists to support the product comparison use case
     * described in the challenge requirements.
     * <p>
     * Validation rules:
     * - At least 2 and at most 10 ids
     * - All ids must be positive
     * - Duplicate ids are rejected at service level
     * <p>
     * If any product does not exist, the service raises a NotFound exception.
     */
    @Operation(summary = "Get products by ids", description = "Retrieves multiple products by their identifiers (example ?ids=1,2,3)")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Products returned successfully",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ProductResponseDto.class))
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Invalid ids list", content = @Content),
            @ApiResponse(responseCode = "404", description = "Any product not found", content = @Content)
    })
    @GetMapping(params = "ids")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponseDto> getProductByIds(@RequestParam(name = "ids")
                                                    @NotEmpty(message = "ids list cannot be empty")
                                                    @Size(min = 2, max = 10, message = "ids list size must be between 2 and 10")
                                                    List<@Positive(message = "product id must be positive") Long> ids) {
        log.info("getProductByIds - request received");
        List<ProductResponseDto> productResponseDto = productService.getProductByIds(ids);
        log.info("getProductByIds - request processed successfully");
        return productResponseDto;
    }

    @Operation(summary = "Delete product by id", description = "Deletes a single product by its identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Product deleted successfully", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid product id", content = @Content),
            @ApiResponse(responseCode = "404", description = "Product not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProductById(@PathVariable @Positive(message = "product id must be a positive number") Long id) {
        log.info("deleteProductById - request received");
        productService.deleteProductById(id);
        log.info("deleteProductById - request processed successfully");
    }

    /**
     * Utility endpoint kept to maintain parity with the base project template.
     */
    @Operation(summary = "Delete all products", description = "Deletes all stored products (utility endpoint; use carefully)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "All products deleted successfully", content = @Content)
    })
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAllProducts() {
        log.info("deleteAllProducts - request received");
        productService.deleteAllProducts();
        log.info("deleteAllProducts - request processed successfully");
    }

    @Operation(summary = "Update product by id", description = "Updates an existing product by its identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Product updated successfully", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid request payload or product id", content = @Content),
            @ApiResponse(responseCode = "404", description = "Product not found", content = @Content)
    })
    @PutMapping(path = "/{id}", consumes = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public void updateProductById(@PathVariable @Positive(message = "product id must be a positive number") Long id, 
                                @RequestBody @Valid ProductInformation productInformation) {
        log.info("updateProduct - request received");
        productService.updateProductById(id, productInformation);
        log.info("updateProduct - request processed successfully");
    }
}
