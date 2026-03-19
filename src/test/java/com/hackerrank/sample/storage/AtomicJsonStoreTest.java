package com.hackerrank.sample.storage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackerrank.sample.model.ProductInformation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class AtomicJsonStoreTest {

    private static final Path STORE_PATH = Paths.get("data/products.json");

    private AtomicJsonStore store;

    @BeforeEach
    void setup() throws Exception {
        Path parent = STORE_PATH.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
        Files.writeString(STORE_PATH, "{}", StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        store = new AtomicJsonStore(new ObjectMapper());
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
    void readAll_returnsEmpty_whenStoreIsEmpty() {
        Map<String, ProductInformation> out = store.readAll();
        assertNotNull(out);
        assertTrue(out.isEmpty());
    }

    @Test
    void writeAtomically_persistsData() {
        store.writeAtomically(data -> {
            data.put("1", info("p1"));
            return null;
        });

        Map<String, ProductInformation> out = store.readAll();
        assertEquals(1, out.size());
        assertEquals("p1", out.get("1").getName());
    }

    @Test
    void writeAtomically_updatesExistingKey() {
        store.writeAtomically(data -> {
            data.put("1", info("old"));
            return null;
        });

        store.writeAtomically(data -> {
            data.put("1", info("new"));
            return null;
        });

        assertEquals("new", store.readAll().get("1").getName());
    }
}