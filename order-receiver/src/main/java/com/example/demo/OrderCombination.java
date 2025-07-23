package com.example.demo;

import jakarta.persistence.*;

@Entity
@Table(name = "order_combinations", uniqueConstraints = @UniqueConstraint(columnNames = {"item_id", "cart_id"}))
public class OrderCombination {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "item_id", nullable = false)
    private String itemId;

    @Column(name = "cart_id", nullable = false)
    private String cartId;

    public OrderCombination() {}
    public OrderCombination(String itemId, String cartId) {
        this.itemId = itemId;
        this.cartId = cartId;
    }
    public Long getId() { return id; }
    public String getItemId() { return itemId; }
    public String getCartId() { return cartId; }
} 