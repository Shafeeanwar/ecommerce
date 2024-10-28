package com.shafee.ecommerce.messaging;

import com.shafee.ecommerce.eventHandler.OrderEventHandler;
import com.shafee.ecommerce.model.Order;
import com.shafee.ecommerce.model.OrderStatus;
import org.springframework.stereotype.Component;

@Component
public class OrderQueueConsumer {
    private final OrderEventHandler orderEventHandler;

    public OrderQueueConsumer(OrderEventHandler orderEventHandler) {
        this.orderEventHandler = orderEventHandler;
    }

    public void processOrder(Order order) {
        if (order.getOrderStatus().equals(OrderStatus.PAID)) {
            orderEventHandler.processOrder(order);
        } else if (order.getOrderStatus().equals(OrderStatus.CANCELLING)) {
            orderEventHandler.rollbackOrder(order);
        } else {
            System.out.println("Invalid order state");
        }
    }
}
