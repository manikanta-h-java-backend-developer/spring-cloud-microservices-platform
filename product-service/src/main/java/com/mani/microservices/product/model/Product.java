package com.mani.microservices.product.model;
import java.math.BigDecimal;
public record Product(String id, String name, Integer quantity, BigDecimal price) {
}