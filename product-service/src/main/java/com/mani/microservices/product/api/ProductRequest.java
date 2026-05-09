package com.mani.microservices.product.api;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
public record ProductRequest(
        @NotBlank String name,
        @NotNull @Min(0) Integer quantity,
        @NotNull BigDecimal price
) {
}