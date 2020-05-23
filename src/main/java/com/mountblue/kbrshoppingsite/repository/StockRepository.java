package com.mountblue.kbrshoppingsite.repository;

import com.mountblue.kbrshoppingsite.model.Product;
import com.mountblue.kbrshoppingsite.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
    Stock findByProduct_Id(Long id);

    Stock findByProduct(Product product);
}
