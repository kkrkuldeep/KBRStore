package com.mountblue.kbrshoppingsite.repository;

import com.mountblue.kbrshoppingsite.model.Product;
import com.mountblue.kbrshoppingsite.model.ProductImage;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface ProductImageRepository extends CrudRepository<ProductImage, Integer> {
    ProductImage findProductImageByProductEquals(Product product);
}
