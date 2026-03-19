package com.hackerrank.sample.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackerrank.sample.model.ProductInformation;
import com.hackerrank.sample.storage.AtomicJsonStore;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductJsonRepositoryTest {

    @TempDir
    Path tempDir;

    private static ProductInformation info(String name) {
        ProductInformation i = new ProductInformation();
        i.setName(name);
        i.setImageUrl("https://example.com/img.png");
        i.setDescription("desc");
        i.setPrice(new BigDecimal("10.00"));
        i.setRating(new BigDecimal("4.5"));
        return i;
    }

    private ProductJsonRepository repo() {
        Path file = tempDir.resolve("products.json");
        AtomicJsonStore store = new AtomicJsonStore(file, new ObjectMapper());
        return new ProductJsonRepository(store);
    }

    @Test
    void createAndGetById() {
        ProductJsonRepository r = repo();
        Long id = r.createProduct(info("p1"));

        assertEquals(1L, id);
        assertTrue(r.getProductById(id).isPresent());
        assertEquals("p1", r.getProductById(id).orElseThrow().getInformation().getName());
    }

    @Test
    void createGeneratesIncrementalIds() {
        ProductJsonRepository r = repo();
        assertEquals(1L, r.createProduct(info("p1")));
        assertEquals(2L, r.createProduct(info("p2")));
    }

    @Test
    void updateProductById_returnsFalseWhenMissing() {
        ProductJsonRepository r = repo();
        assertFalse(r.updateProductById(999L, info("x")));
    }

    @Test
    void updateProductById_updatesWhenExists() {
        ProductJsonRepository r = repo();
        Long id = r.createProduct(info("old"));

        assertTrue(r.updateProductById(id, info("new")));
        assertEquals("new", r.getProductById(id).orElseThrow().getInformation().getName());
    }

    @Test
    void deleteProductById_removesAndReturnsTrue() {
        ProductJsonRepository r = repo();
        Long id = r.createProduct(info("p1"));

        assertTrue(r.deleteProductById(id));
        assertTrue(r.getProductById(id).isEmpty());
        assertFalse(r.deleteProductById(id));
    }

    @Test
    void deleteAllProducts_clearsAndIndicatesIfHadData() {
        ProductJsonRepository r = repo();
        assertFalse(r.deleteAllProducts());

        r.createProduct(info("p1"));
        r.createProduct(info("p2"));

        assertTrue(r.deleteAllProducts());
        assertTrue(r.getAllProducts().isEmpty());
    }

    @Test
    void getProductsByIds_filtersMissingAndDedupes() {
        ProductJsonRepository r = repo();
        Long id1 = r.createProduct(info("p1"));
        Long id2 = r.createProduct(info("p2"));

        var out = r.getProductsByIds(List.of(id2, id2, 999L, id1));
        assertEquals(2, out.size());
        assertEquals(List.of(id2, id1), out.stream().map(p -> p.getId()).toList());
    }
}

