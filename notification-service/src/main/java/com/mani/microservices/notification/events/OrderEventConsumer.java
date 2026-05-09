package com.mani.microservices.notification.events;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
@Component
public class OrderEventConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderEventConsumer.class);
    @KafkaListener(topics = "${app.kafka.topic.orderPlaced:order-placed}", groupId = "${spring.kafka.consumer.group-id:notificationId}")
    public void handleOrderPlaced(OrderPlacedEvent event) {
        LOGGER.info("Notification received for order: {}, product: {}, quantity: {}",
                event.orderId(), event.productId(), event.quantity());
    }
}