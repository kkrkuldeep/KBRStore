package com.mountblue.kbrshoppingsite.model;

import org.hibernate.annotations.Formula;

import javax.persistence.*;

@Entity
@Table(name = "stocks")
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stock_id")
    private long id;

    @OneToOne(targetEntity = Product.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "product_id")
    private Product product;

    private Float productQuantityInStock;

    private boolean isEmptyStock;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Float getProductQuantityInStock() {
        return productQuantityInStock;
    }

    public void setProductQuantityInStock(Float productQuantityInStock) {
        this.productQuantityInStock = productQuantityInStock;
    }

    public boolean isEmptyStock() {
        return isEmptyStock;
    }

    public void setEmptyStock(boolean emptyStock) {
        isEmptyStock = emptyStock;
    }
}
