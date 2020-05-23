package com.mountblue.kbrshoppingsite.repository;

import com.mountblue.kbrshoppingsite.model.Order;
import com.mountblue.kbrshoppingsite.model.ProductOrderDetails;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface ProductOrderDetailsRepository extends CrudRepository<ProductOrderDetails, Long> {

    List<ProductOrderDetails> findByOrder(Order order);
}
