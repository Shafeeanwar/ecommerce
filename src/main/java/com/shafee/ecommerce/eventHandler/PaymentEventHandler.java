package com.shafee.ecommerce.eventHandler;

import com.shafee.ecommerce.messaging.MessageQueueService;
import com.shafee.ecommerce.model.Order;
import com.shafee.ecommerce.model.OrderStatus;
import com.shafee.ecommerce.repository.OrderRepository;
import org.springframework.stereotype.Component;

@Component
public class PaymentEventHandler implements PaymentEventInterface {

    private final OrderRepository orderRepository;

    public PaymentEventHandler(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void processPayment(Order order) {
        if (order.getTotalPrice() > 1000L) {
            order.setOrderStatus(OrderStatus.CANCELLING);
        } else {
            order.setOrderStatus(OrderStatus.PAID);
        }
        orderRepository.save(order);
    }

    @Override
    public void rollbackPayment(Order order) {
        // TODO: Implement payment rollback logic
    }
}
