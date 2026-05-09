package com.mani.microservices.order.model;
import java.math.BigDecimal;
import java.time.Instant;
public record Order(String id, String productId, Integer quantity, BigDecimal totalPrice, Instant createdAt) {
}