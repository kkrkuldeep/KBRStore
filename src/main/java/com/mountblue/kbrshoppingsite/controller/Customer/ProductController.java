package com.mountblue.kbrshoppingsite.controller.Customer;

import com.mountblue.kbrshoppingsite.model.*;
import com.mountblue.kbrshoppingsite.repository.ProductImageRepository;
import com.mountblue.kbrshoppingsite.repository.ProductRepository;
import com.mountblue.kbrshoppingsite.service.CustomerService.CustomerCartService;
import com.mountblue.kbrshoppingsite.service.CustomerService.CustomerProductService;
import com.mountblue.kbrshoppingsite.service.ProductImageService;
import com.mountblue.kbrshoppingsite.service.user.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Base64;
import java.util.List;

@Controller
public class ProductController {
    private Logger log = LoggerFactory.getLogger(this.getClass());
    private CustomerProductService customerProductService;
    private CustomerCartService customerCartService;
    private CategoryService categoryService;
    private ProductImageService productImageService;


    @Autowired
    public ProductController(CustomerProductService customerProductService,
                             CustomerCartService customerCartService,
                             ProductImageService productImageService,
                             CategoryService categoryService) {
        this.customerProductService = customerProductService;
        this.customerCartService = customerCartService;
        this.productImageService = productImageService;
        this.categoryService = categoryService;
    }


    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductImageRepository productImageRepository;

    @GetMapping("hi")
    public void intro() {
    }

    @GetMapping("category/{categoryId}/products")
    public String listProductByCategoryId(@PathVariable("categoryId") Long categoryId, Model model) throws Exception {
        List<Product> products = customerProductService.listProductByCategory(categoryId);
        if(products.isEmpty()) throw new Exception("empty category no products available");
        List<Category> categoryList = categoryService.findAll();

        model.addAttribute("categoryList", categoryList);
        model.addAttribute("products", products);
        return "show-items";
    }

    @GetMapping("category/{categoryId}/products/{productId}")
    public String viewProductDetails(@PathVariable("categoryId") Long categoryId,
                                     @PathVariable("productId") Long productId,
                                     Principal principal, Model model) throws UnsupportedEncodingException {
        int quantity = 0;
        Product product = customerProductService.getProduct(productId);
        if(principal != null) {
            Cart cart = customerCartService.getCart(principal);
            CartItem cartItem = customerCartService.getCartItem(product, cart);
            if (cartItem != null) quantity = cartItem.getQuantity();
        }

        model.addAttribute("singleProduct", product);
        model.addAttribute("quantity", quantity);
        return "single-product";
    }

}
