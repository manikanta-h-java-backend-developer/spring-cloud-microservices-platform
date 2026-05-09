package com.mani.microservices.notification.events;
public record OrderPlacedEvent(String orderId, String productId, Integer quantity) {
}