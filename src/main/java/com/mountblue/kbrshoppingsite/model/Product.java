package com.mountblue.kbrshoppingsite.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Category category;

    @OneToMany(mappedBy = "order",
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            })
    private Set<ProductOrderDetails> productOrderDetails = new HashSet<ProductOrderDetails>();

    @NotEmpty(message = "name cannot be empty")
    private String name;

    @NotEmpty(message = "description cannot be Empty")
    private String description;

    private Float price;

    private double stock;

    private boolean status;


    @Transient
    private String encodedBase64String;

    public Product() {
    }

    public Product(Category category, @NotEmpty(message = "name cannot be empty") String name, @NotEmpty(message = "description cannot be Empty") String description, @NotEmpty(message = "price cannot be Empty") Float price) {
        this.category = category;
        this.name = name;
        this.description = description;
        this.price = price;
    }


    public Set<ProductOrderDetails> getProductOrderDetails() {
        return productOrderDetails;
    }

    public void setProductOrderDetails(Set<ProductOrderDetails> productOrderDetails) {
        this.productOrderDetails = productOrderDetails;
    }


    public double getStock() {
        return stock;
    }

    public void setStock(double stock) {
        this.stock = stock;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }


    public String getEncodedBase64String() {
        return encodedBase64String;
    }

    public void setEncodedBase64String(String encodedBase64String) {
        this.encodedBase64String = encodedBase64String;
    }
}
