package com.mountblue.kbrshoppingsite.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name="address_table")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Customer customer;


   // private String name;

  //  @NotEmpty(message = "address cannot be empty")
    @Column(name="address")
    private String location;

   // @NotEmpty(message = "city cannot be empty")
   @Column(name="city")
    private String city;

   // @NotEmpty(message = "country cannot be empty")
   @Column(name="country")
    private String country;

   // @Size(min=0,max=10)
   // @NotEmpty(message = "phone number cannot be empty")
   @Column(name="phone_number")
   private String phoneNumber;

   // @NotEmpty(message = "zip code cannot be empty")
    @Column(name="zipCode")
    private String zipCode;

    public Address() {
    }

    public Address(Customer customer, /*@NotEmpty(message = "address cannot be empty")*/ String location,
                   /*@NotEmpty(message = "city cannot be empty")*/ String city,
                  /* @NotEmpty(message = "country cannot be empty")*/ String country,
                   /*@Size(min = 0, max = 10) @NotEmpty(message = "phone number cannot be empty")*/ String phoneNumber,
                   /*@NotEmpty(message = "zip code cannot be empty")*/ String zipCode) {
        this.customer = customer;
        this.location = location;
        this.city = city;
        this.country = country;
        this.phoneNumber = phoneNumber;
        this.zipCode = zipCode;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
}
