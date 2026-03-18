package com.hackerrank.sample.repository;

import org.springframework.stereotype.Repository;
import com.hackerrank.sample.model.ProductInformation;
import java.util.List;
import com.hackerrank.sample.model.Product;

@Repository
public interface ProductJsonRepository {
    void createProduct(ProductInformation productInformation );
    void deleteProductById(Long id);
    void deleteAllProducts();
    Product getProductById(Long id);
    List<Product> getAllProducts();
    List<Product> getProductsByIds(List<Long> ids);
}
