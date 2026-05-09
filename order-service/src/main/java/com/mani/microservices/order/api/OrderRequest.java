package com.mani.microservices.order.api;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
public record OrderRequest(
        @NotBlank String productId,
        @NotNull @Min(1) Integer quantity,
        @NotNull BigDecimal unitPrice
) {
}