package com.mani.microservices.order.api;
public record OrderPlacedEvent(String orderId, String productId, Integer quantity) {
}