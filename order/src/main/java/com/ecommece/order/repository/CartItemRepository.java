package com.ecommece.order.repository;

import com.ecommece.order.models.CartItem;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    CartItem findByUserIdAndProductId(String userId, String productId);

    @Transactional
    CartItem deleteCartItemByUserIdAndProductId(String userId, String productId);

    List<CartItem> findByUserId(String userId);

    @Transactional
    List<CartItem> deleteCartByUserId(String userId);
}
