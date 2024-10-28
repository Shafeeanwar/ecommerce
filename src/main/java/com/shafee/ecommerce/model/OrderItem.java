
package com.shafee.ecommerce.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.FetchType;
import java.math.BigDecimal;

@Embeddable
public class OrderItem {
    @ManyToOne(fetch = FetchType.EAGER)
    private Product product;

    private Long quantity;

    protected OrderItem() {}

    public OrderItem(Product product, Long quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public BigDecimal getSubtotal() {
        return product.getPrice().multiply(BigDecimal.valueOf(quantity));
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }
}
