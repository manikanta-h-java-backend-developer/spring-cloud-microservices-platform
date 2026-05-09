package com.mani.microservices.order.service;
import com.mani.microservices.order.api.OrderPlacedEvent;
import com.mani.microservices.order.api.OrderRequest;
import com.mani.microservices.order.model.Order;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
@Service
public class OrderService {
    private final Map<String, Order> orders = new ConcurrentHashMap<>();
    private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;
    private final String topic;
    public OrderService(KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate,
                        @Value("${app.kafka.topic.orderPlaced:order-placed}") String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }
    @CircuitBreaker(name = "product-service", fallbackMethod = "placeOrderFallback")
    public Order placeOrder(OrderRequest request) {
        String id = UUID.randomUUID().toString();
        Order order = new Order(
                id,
                request.productId(),
                request.quantity(),
                request.unitPrice().multiply(java.math.BigDecimal.valueOf(request.quantity())),
                Instant.now()
        );
        orders.put(id, order);
        kafkaTemplate.send(topic, new OrderPlacedEvent(order.id(), order.productId(), order.quantity()));
        return order;
    }
    public Order placeOrderFallback(OrderRequest request, Throwable ex) {
        String id = "fallback-" + UUID.randomUUID();
        Order fallback = new Order(id, request.productId(), request.quantity(), request.unitPrice(), Instant.now());
        orders.put(id, fallback);
        return fallback;
    }
    public Order getById(String id) {
        return orders.get(id);
    }
}