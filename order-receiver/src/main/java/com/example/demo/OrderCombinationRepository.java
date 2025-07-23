package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface OrderCombinationRepository extends JpaRepository<OrderCombination, Long> {
    Optional<OrderCombination> findByItemIdAndCartId(String itemId, String cartId);
} 