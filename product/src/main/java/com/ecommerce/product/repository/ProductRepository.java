package com.ecommerce.product.repository;

import com.ecommerce.product.dto.ProductResponse;
import com.ecommerce.product.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {

    @Query("select p from products as p where p.active = true and p.stockQuantity > 0 and lower(p.name) like lower(concat('%', :keyword, '%'))")
    List<Product> searchProduct(String keyword);

    Product getProductByProductId(Long productId);

    Product findByProductIdAndActiveTrue(Long productId);
}
