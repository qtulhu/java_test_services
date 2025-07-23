package com.example.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class OrderRequestSender implements CommandLineRunner {
    private static final String RECEIVER_URL = "http://order-receiver:8025/order/item/%d/cart/%d";
    private static final int COMBO_COUNT = 5000;
    private final List<long[]> combos = new ArrayList<>(COMBO_COUNT);
    private final AtomicInteger index = new AtomicInteger(0);
    private final RestTemplate restTemplate = new RestTemplate();

    public OrderRequestSender() {
        // Генерируем 5000 уникальных пар itemId/cartId
        for (int i = 0; i < COMBO_COUNT; i++) {
            combos.add(new long[]{1000000L + i, 2000000L + i});
        }
        Collections.shuffle(combos);
    }

    @Override
    public void run(String... args) {
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            int i = index.getAndUpdate(val -> (val + 1) % COMBO_COUNT);
            long[] combo = combos.get(i);
            String url = String.format(RECEIVER_URL, combo[0], combo[1]);
            try {
                restTemplate.getForObject(url, String.class);
                System.out.println("Sent request to: " + url);
            } catch (Exception e) {
                System.err.println("Failed to send request: " + e.getMessage());
            }
        }, 0, 500, TimeUnit.MILLISECONDS);
    }
} 