package com.mountblue.kbrshoppingsite.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class ProductOrderPk implements Serializable {
    @Column(name="product_id")
    private long productId;

    @Column(name="order_id")
    private long orderId;

    public ProductOrderPk() {
    }

    public ProductOrderPk(long productId, long orderId) {
        this.productId = productId;
        this.orderId = orderId;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        System.out.println("setter");
        this.productId = productId;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        System.out.println("setter");
        this.orderId = orderId;
    }
}
