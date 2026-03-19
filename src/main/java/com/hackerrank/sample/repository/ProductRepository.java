package com.hackerrank.sample.repository;

import org.springframework.stereotype.Repository;
import com.hackerrank.sample.model.Product;
import com.hackerrank.sample.model.ProductInformation;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository {
    Long createProduct(ProductInformation productInformation );
    boolean deleteProductById(Long id);
    boolean deleteAllProducts();
    Optional<Product> getProductById(Long id);
    List<Product> getAllProducts();
    List<Product> getProductsByIds(List<Long> ids);
    boolean updateProductById(Long id, ProductInformation productInformation);
}
