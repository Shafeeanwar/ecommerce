package com.shafee.ecommerce.eventHandler;

import com.shafee.ecommerce.model.Order;

public interface OrderEventInterface {

    public void processOrder(Order order);

    public void rollbackOrder(Order order);
}
