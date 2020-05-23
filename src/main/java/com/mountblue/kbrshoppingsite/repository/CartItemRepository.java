package com.mountblue.kbrshoppingsite.repository;

import com.mountblue.kbrshoppingsite.model.Cart;
import com.mountblue.kbrshoppingsite.model.CartItem;
import com.mountblue.kbrshoppingsite.model.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface CartItemRepository extends CrudRepository<CartItem, Long> {
     List<CartItem> findCartItemByCart(Cart cart);
     CartItem findCartItemByCartAndProduct(Cart cart, Product product);
     void deleteCartItemsByCart(Cart cart);
}
