package com.mountblue.kbrshoppingsite.repository;

import com.mountblue.kbrshoppingsite.model.Category;
import com.mountblue.kbrshoppingsite.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    public List<Product> findAllByCategory(Category category);

    Product findProductByName(String name);

    public Product findProductById(Long productId);

    Product findByName(String name);

    List<Product> findByCategory_Id(Long id);

    List<Product> findAllByStatusIsFalseOrderByStock();
}
