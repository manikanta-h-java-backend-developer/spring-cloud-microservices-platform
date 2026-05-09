package com.mani.microservices.product.service;
import com.mani.microservices.product.api.ProductRequest;
import com.mani.microservices.product.model.Product;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
@Service
public class ProductService {
    private final Map<String, Product> productStore = new ConcurrentHashMap<>();
    public Product create(ProductRequest request) {
        String id = UUID.randomUUID().toString();
        Product product = new Product(id, request.name(), request.quantity(), request.price());
        productStore.put(id, product);
        return product;
    }
    public Product getById(String id) {
        return productStore.get(id);
    }
    public java.util.List<Product> getAll() {
        return new ArrayList<>(productStore.values());
    }
}