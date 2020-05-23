package com.mountblue.kbrshoppingsite.model;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private long id;

    @OneToOne(targetEntity = Address.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "address_id")
    private Address address;

    @OneToMany(mappedBy = "product",
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            })
    private Set<ProductOrderDetails> productOrderDetails = new HashSet<ProductOrderDetails>();


    private String paymentMethod;
    private Float orderTotal;

    @NotEmpty(message = "orderStatus cannot be Empty")
    private String orderStatus;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Date orderAt;

    public Set<ProductOrderDetails> getProductOrderDetails() {
        return productOrderDetails;
    }

    public void setProductOrderDetails(Set<ProductOrderDetails> productOrderDetails) {
        this.productOrderDetails = productOrderDetails;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }


    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Float getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(Float orderTotal) {
        this.orderTotal = orderTotal;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Date getOrderAt() {
        return orderAt;
    }

}
