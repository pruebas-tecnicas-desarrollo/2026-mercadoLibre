package com.hackerrank.sample.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackerrank.sample.model.ProductInformation;
import com.hackerrank.sample.storage.AtomicJsonStore;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductJsonRepositoryTest {

    private static final Path STORE_PATH = Paths.get("data/products.json");

    private ProductJsonRepository repo;

    @BeforeEach
    void setup() throws Exception {
        Path parent = STORE_PATH.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
        Files.writeString(STORE_PATH, "{}", StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

        AtomicJsonStore store = new AtomicJsonStore(new ObjectMapper());
        repo = new ProductJsonRepository(store);
    }

    @AfterEach
    void cleanup() throws Exception {
        Files.writeString(STORE_PATH, "{}", StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

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
    void createProduct_returnsIdStartingAt1_whenStoreIsEmpty() {
        assertEquals(1L, repo.createProduct(info("p1")));
    }

    @Test
    void createThenGetById_returnsProduct() {
        Long id = repo.createProduct(info("p1"));
        var found = repo.getProductById(id);
        assertTrue(found.isPresent());
        assertEquals("p1", found.orElseThrow().getInformation().getName());
    }

    @Test
    void createGeneratesIncrementalIds() {
        assertEquals(1L, repo.createProduct(info("p1")));
        assertEquals(2L, repo.createProduct(info("p2")));
        assertEquals(3L, repo.createProduct(info("p3")));
    }

    @Test
    void deleteProductById_isIdempotent() {
        Long id = repo.createProduct(info("p1"));
        assertTrue(repo.deleteProductById(id));
        assertFalse(repo.deleteProductById(id));
    }

    @Test
    void deleteAllProducts_returnsFalseWhenAlreadyEmpty() {
        assertFalse(repo.deleteAllProducts());
    }

    @Test
    void deleteAllProducts_returnsTrueWhenHadData() {
        repo.createProduct(info("p1"));
        assertTrue(repo.deleteAllProducts());
        assertTrue(repo.getAllProducts().isEmpty());
    }

    @Test
    void updateProductById_returnsFalseWhenMissing() {
        assertFalse(repo.updateProductById(999L, info("x")));
    }

    @Test
    void updateProductById_updatesWhenExists() {
        Long id = repo.createProduct(info("old"));
        assertTrue(repo.updateProductById(id, info("new")));
        assertEquals("new", repo.getProductById(id).orElseThrow().getInformation().getName());
    }

    @Test
    void getProductsByIds_filtersMissingAndDedupes() {
        Long id1 = repo.createProduct(info("p1"));
        Long id2 = repo.createProduct(info("p2"));

        var out = repo.getProductsByIds(List.of(id2, id2, 999L, id1));
        assertEquals(2, out.size());
        assertEquals(List.of(id2, id1), out.stream().map(p -> p.getId()).toList());
    }
}
