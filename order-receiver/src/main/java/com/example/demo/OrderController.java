package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
public class OrderController {
    private final OrderCombinationRepository repository;

    @Autowired
    public OrderController(OrderCombinationRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/order/item/{itemId}/cart/{cartId}")
    public ResponseEntity<String> receiveOrder(@PathVariable String itemId, @PathVariable String cartId) {
        var existing = repository.findByItemIdAndCartId(itemId, cartId);
        if (existing.isPresent()) {
            System.out.println("Read from DB: itemId=" + itemId + ", cartId=" + cartId);
            return ResponseEntity.ok("Already exists: itemId=" + itemId + ", cartId=" + cartId);
        } else {
            repository.save(new OrderCombination(itemId, cartId));
            System.out.println("Saved to DB: itemId=" + itemId + ", cartId=" + cartId);
            return ResponseEntity.ok("Saved: itemId=" + itemId + ", cartId=" + cartId);
        }
    }
} 