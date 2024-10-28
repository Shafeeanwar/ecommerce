package com.shafee.ecommerce.eventHandler;

import com.shafee.ecommerce.model.Order;
import com.shafee.ecommerce.model.OrderStatus;
import com.shafee.ecommerce.repository.OrderRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class OrderEventHandler implements OrderEventInterface {

    private final OrderRepository orderRepository;


    public OrderEventHandler(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void processOrder(Order order){
        order.setOrderStatus(OrderStatus.CONFIRMED);
        orderRepository.save(order);
    }

    @Override
    public void rollbackOrder(Order order){
        //mark as cancelleed
        order.setOrderStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
    }
}
