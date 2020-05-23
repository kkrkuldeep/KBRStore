package com.mountblue.kbrshoppingsite.repository;

import com.mountblue.kbrshoppingsite.model.Cart;
import com.mountblue.kbrshoppingsite.model.Customer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface CartRepository extends CrudRepository<Cart, Long> {
    Optional<Cart>  findByCustomer(Customer customer);
    Cart findCartByCustomer(Customer customer);
}
