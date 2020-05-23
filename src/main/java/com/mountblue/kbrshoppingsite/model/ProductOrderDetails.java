package com.mountblue.kbrshoppingsite.model;

import org.hibernate.annotations.Formula;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="product_order_details")
public class ProductOrderDetails {
    @EmbeddedId
    private ProductOrderPk id = new ProductOrderPk(); //use object here then only it will work.

    @ManyToOne
    @MapsId("productId") //This is the name of attr in EmployerDeliveryAgentPK class
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @MapsId("orderId")
    @JoinColumn(name = "order_id")
    private Order order;


    private int quantity; //removed validation


    private Float price; //removed validation


    private Float total_price;

    public ProductOrderPk getId() {
        return id;
    }

    public void setId(ProductOrderPk id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Float getTotal_price() {
        return total_price;
    }

    public void setTotal_price(Float total_price) {
        this.total_price = total_price;
    }
}
