package com.hackerrank.sample.service;

import com.hackerrank.sample.dto.ProductCreatedResponseDto;
import com.hackerrank.sample.dto.ProductsRequestDto;
import com.hackerrank.sample.exception.NoSuchResourceFoundException;
import com.hackerrank.sample.model.Product;
import com.hackerrank.sample.model.ProductInformation;
import com.hackerrank.sample.repository.ProductRepository;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProductServiceImplTest {

    private static ProductInformation info(String name) {
        ProductInformation i = new ProductInformation();
        i.setName(name);
        i.setImageUrl("https://example.com/img.png");
        i.setDescription("desc");
        i.setPrice(new BigDecimal("10.00"));
        i.setRating(new BigDecimal("4.5"));
        return i;
    }

    @Test
    void createProduct_delegatesToRepository_andReturnsId() {
        ProductRepository repo = mock(ProductRepository.class);
        ProductServiceImpl service = new ProductServiceImpl(repo);

        when(repo.createProduct(any())).thenReturn(7L);

        ProductsRequestDto req = new ProductsRequestDto();
        req.setInformation(info("p1"));

        ProductCreatedResponseDto res = service.createProduct(req);
        assertEquals(7L, res.getProductId());
        verify(repo).createProduct(req.getInformation());
    }

    @Test
    void getProductById_throwsNotFound_whenMissing() {
        ProductRepository repo = mock(ProductRepository.class);
        ProductServiceImpl service = new ProductServiceImpl(repo);

        when(repo.getProductById(1L)).thenReturn(Optional.empty());
        assertThrows(NoSuchResourceFoundException.class, () -> service.getProductById(1L));
    }

    @Test
    void getAllProducts_mapsToResponseDto() {
        ProductRepository repo = mock(ProductRepository.class);
        ProductServiceImpl service = new ProductServiceImpl(repo);

        when(repo.getAllProducts()).thenReturn(List.of(
                Product.builder().id(1L).information(info("p1")).build(),
                Product.builder().id(2L).information(info("p2")).build()
        ));

        var out = service.getAllProducts();
        assertEquals(2, out.size());
        assertEquals(1L, out.get(0).getId());
        assertEquals("p1", out.get(0).getInformation().getName());
    }

    @Test
    void deleteProductById_throwsNotFound_whenRepoReturnsFalse() {
        ProductRepository repo = mock(ProductRepository.class);
        ProductServiceImpl service = new ProductServiceImpl(repo);

        when(repo.getProductById(10L)).thenReturn(Optional.of(Product.builder().id(10L).information(info("x")).build()));
        when(repo.deleteProductById(10L)).thenReturn(false);

        assertThrows(NoSuchResourceFoundException.class, () -> service.deleteProductById(10L));
    }

    @Test
    void updateProductById_throwsNotFound_whenRepoReturnsFalse() {
        ProductRepository repo = mock(ProductRepository.class);
        ProductServiceImpl service = new ProductServiceImpl(repo);

        when(repo.getProductById(10L)).thenReturn(Optional.of(Product.builder().id(10L).information(info("old")).build()));
        when(repo.updateProductById(eq(10L), any())).thenReturn(false);

        assertThrows(NoSuchResourceFoundException.class, () -> service.updateProductById(10L, info("new")));
    }
}

