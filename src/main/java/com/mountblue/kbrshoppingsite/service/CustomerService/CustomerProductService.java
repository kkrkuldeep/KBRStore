package com.mountblue.kbrshoppingsite.service.CustomerService;

import com.mountblue.kbrshoppingsite.model.Category;
import com.mountblue.kbrshoppingsite.model.Product;
import com.mountblue.kbrshoppingsite.model.ProductImage;
import com.mountblue.kbrshoppingsite.repository.CategoryRepository;
import com.mountblue.kbrshoppingsite.repository.ProductImageRepository;
import com.mountblue.kbrshoppingsite.repository.ProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.List;

@Service
public class CustomerProductService {
    ProductRepository productRepository;
    CategoryRepository categoryRepository;
    ProductImageRepository productImageRepository;

    @Autowired
    public CustomerProductService(ProductRepository productRepository,
                                  CategoryRepository categoryRepository,
                                  ProductImageRepository productImageRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.productImageRepository = productImageRepository;
    }

    public List<Product> listProductByCategory(Long categoryId) throws UnsupportedEncodingException {
        Category category = categoryRepository.findCategoryById(categoryId);
        List<Product> products = productRepository.findAllByCategory(category);
        for(Product product : products) {
            ProductImage productImage = productImageRepository.findProductImageByProductEquals(product);
            byte[] encode =  Base64.getEncoder().encode(productImage.getImage());
            String utfEncode = new String(encode, "UTF-8");
            product.setEncodedBase64String(utfEncode);
        }
        return products;
    }

    public Product getProduct(Long productId) throws UnsupportedEncodingException {
        Product product = productRepository.findProductById(productId);
        ProductImage productImage = productImageRepository.findProductImageByProductEquals(product);
        byte[] encode =  Base64.getEncoder().encode(productImage.getImage());
        String utfEncode = new String(encode, "UTF-8");
        product.setEncodedBase64String(utfEncode);
        return product;
    }
}
