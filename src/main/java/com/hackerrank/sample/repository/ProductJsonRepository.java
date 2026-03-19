package com.hackerrank.sample.repository;
import com.hackerrank.sample.storage.AtomicJsonStore;

import org.springframework.stereotype.Repository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.hackerrank.sample.model.Product;
import com.hackerrank.sample.model.ProductInformation;

import java.util.Map;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
// Repository implementation backed by a JSON file-based storage.
public class ProductJsonRepository implements ProductRepository {
    private final AtomicJsonStore store;

    private Long generateNextId(Map<String, ProductInformation> data) {
        return data.keySet().stream()
                .map(Long::valueOf)
                .max(Long::compareTo)
                .orElse(0L) + 1;
    }

    @Override
    public Long createProduct(ProductInformation productInformation) {
        log.debug("creating product in json repository");
    
        Long id = store.writeAtomically(data -> {
            Long nextId = generateNextId(data);
        
            data.put(String.valueOf(nextId), productInformation);
            return nextId;
        });
    
        log.debug("product persisted successfully, id={}", id);
        return id;
    }

    @Override
    public boolean deleteProductById(Long id) {
        log.debug("deleting product by id={}", id);

        Boolean removed = store.writeAtomically(data -> data.remove(String.valueOf(id)) != null);
        
        return removed;
    }

    @Override
    public boolean deleteAllProducts() {
        log.debug("deleting all products");

        boolean deleted = store.writeAtomically(data -> {
            boolean hadProducts = !data.isEmpty();
            data.clear();
            return hadProducts;
        });

        return deleted;
    }

    @Override
    public Optional<Product> getProductById(Long id) {
        log.debug("obtaining product by id={}", id);

        return Optional.ofNullable(store.readAll().get(String.valueOf(id)))
                .map(productInformation -> Product.builder()
                        .id(id)
                        .information(productInformation)
                        .build());
    }

    @Override
    public List<Product> getAllProducts() {
        log.debug("obtaining all products");

        return store.readAll().entrySet().stream()
                .map(entry -> Product.builder()
                        .id(Long.valueOf(entry.getKey()))
                        .information(entry.getValue())
                        .build()
                )
                .toList();
    }

    @Override
    public List<Product> getProductsByIds(List<Long> ids) {
        log.debug("obtaining products by ids={}", ids);

        Map<String, ProductInformation> data = store.readAll();

        return ids.stream()
                .distinct()
                .map(String::valueOf)
                .filter(data::containsKey)
                .map(id -> Product.builder()
                        .id(Long.valueOf(id))
                        .information(data.get(id))
                        .build()
                )
                .toList();
}

    @Override
    public boolean updateProductById(Long id, ProductInformation productInformation) {
        log.debug("updating product by id={}", id);

        Boolean updated = store.writeAtomically(data -> {
            String key = String.valueOf(id);

            if (!data.containsKey(key)) {
                return false;
            }

            data.put(key, productInformation);
            return true;
        });

        return updated;
    }
}
