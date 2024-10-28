
// Updated Service with proper error handling
package com.shafee.ecommerce.service;

import com.shafee.ecommerce.dto.CreateOrderRequest;
import com.shafee.ecommerce.messaging.MessageQueueService;
import com.shafee.ecommerce.model.Order;
import com.shafee.ecommerce.model.OrderItem;
import com.shafee.ecommerce.model.OrderStatus;
import com.shafee.ecommerce.model.Product;
import com.shafee.ecommerce.repository.OrderRepository;
import com.shafee.ecommerce.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final MessageQueueService messageQueueService;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderService(
            MessageQueueService messageQueueService,
            OrderRepository orderRepository,
            ProductRepository productRepository
    ) {
        this.messageQueueService = messageQueueService;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public Order createOrder(CreateOrderRequest request) {
        Order order = new Order();
        order.setOrderStatus(OrderStatus.DRAFT);

        // Process each order item
        request.getItems().forEach(itemRequest -> {
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Product not found with id: " + itemRequest.getProductId()));

            OrderItem orderItem = new OrderItem(product, itemRequest.getQuantity());
            order.addOrderItem(orderItem);
        });

        Order savedOrder = orderRepository.save(order);
        messageQueueService.sendNewOrderEvent(savedOrder);
        return savedOrder;
    }

    @Transactional(readOnly = true)
    public Optional<Order> getOrder(Long id) {
        return orderRepository.findById(id);
    }
}
