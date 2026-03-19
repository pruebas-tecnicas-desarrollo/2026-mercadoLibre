package com.hackerrank.sample.storage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackerrank.sample.model.ProductInformation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class AtomicJsonStoreTest {

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

    @Test
    void writeAtomically_persistsAndReadAllReturnsData() {
        Path file = tempDir.resolve("products.json");
        AtomicJsonStore store = new AtomicJsonStore(file, new ObjectMapper());

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
        Path file = tempDir.resolve("products.json");
        AtomicJsonStore store = new AtomicJsonStore(file, new ObjectMapper());

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

