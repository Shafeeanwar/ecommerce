package com.shafee.ecommerce.integration;

import com.shafee.ecommerce.dto.CreateOrderRequest;
import com.shafee.ecommerce.model.Order;
import com.shafee.ecommerce.model.Product;
import com.shafee.ecommerce.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrderIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ProductRepository productRepository;

    private Product testProduct;

    @BeforeEach
    void setUp() {
//        productRepository.deleteAll();
    }

    @Test
    void createOrder_EndToEnd() {

        testProduct = new Product();
        testProduct.setName("Integration Test Product");
        testProduct.setPrice(new BigDecimal("99.99"));
        testProduct = productRepository.save(testProduct);
        // Arrange
        CreateOrderRequest request = new CreateOrderRequest();
        CreateOrderRequest.OrderItemRequest itemRequest = new CreateOrderRequest.OrderItemRequest();
        itemRequest.setProductId(testProduct.getId());
        itemRequest.setQuantity(1L);
        request.setItems(Collections.singletonList(itemRequest));

        // Act
        ResponseEntity<Order> response = restTemplate.postForEntity(
                "/api/orders",
                request,
                Order.class
        );

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Order createdOrder = response.getBody();
        assertNotNull(createdOrder);
        assertNotNull(createdOrder.getId());
        assertEquals(1, createdOrder.getOrderItems().size());
    }
}
