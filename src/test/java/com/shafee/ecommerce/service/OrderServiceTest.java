package com.shafee.ecommerce.service;

import com.shafee.ecommerce.dto.CreateOrderRequest;
import com.shafee.ecommerce.messaging.MessageQueueService;
import com.shafee.ecommerce.model.Order;
import com.shafee.ecommerce.model.OrderStatus;
import com.shafee.ecommerce.model.Product;
import com.shafee.ecommerce.repository.OrderRepository;
import com.shafee.ecommerce.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import jakarta.persistence.EntityNotFoundException;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private MessageQueueService messageQueueService;

    @InjectMocks
    private OrderService orderService;

    private Product testProduct;
    private CreateOrderRequest validOrderRequest;

    @BeforeEach
    void setUp() {
        testProduct = new Product();
        testProduct.setId(1L);
        testProduct.setName("Test Product");
        testProduct.setPrice(new BigDecimal("100.00"));

        CreateOrderRequest.OrderItemRequest itemRequest = new CreateOrderRequest.OrderItemRequest();
        itemRequest.setProductId(1L);
        itemRequest.setQuantity(2L);

        validOrderRequest = new CreateOrderRequest();
        validOrderRequest.setItems(Arrays.asList(itemRequest));
    }

    @Test
    void createOrder_Success() {
        // Arrange
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Order result = orderService.createOrder(validOrderRequest);

        // Assert
        assertNotNull(result);
        assertEquals(OrderStatus.DRAFT, result.getOrderStatus());
        assertEquals(1, result.getOrderItems().size());
        verify(messageQueueService).sendNewOrderEvent(result);
    }

    @Test
    void createOrder_ProductNotFound() {
        // Arrange
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            orderService.createOrder(validOrderRequest);
        });
        verify(orderRepository, never()).save(any());
        verify(messageQueueService, never()).sendNewOrderEvent(any());
    }
}



