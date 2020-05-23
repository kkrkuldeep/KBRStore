package com.mountblue.kbrshoppingsite.service.CustomerService;

import com.mountblue.kbrshoppingsite.model.*;
import com.mountblue.kbrshoppingsite.repository.OrderRepository;
import com.mountblue.kbrshoppingsite.repository.ProductImageRepository;
import com.mountblue.kbrshoppingsite.repository.ProductOrderDetailsRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.List;

@Service
public class CustomerOrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private MyUserDetailsService myUserDetailsService;
    @Autowired
    private ProductOrderDetailsRepository orderDetailsRepository;
    @Autowired
    private ProductImageRepository productImageRepository;


    public List<Order> findOrderByCustomer(String email) throws NotFoundException {
        Customer customer = myUserDetailsService.findCustomerByEmail(email);
        return orderRepository.findByAddress_CustomerOrderByOrderAtDesc(customer);
    }

    public Order findById(long id) throws NotFoundException {
        if(orderRepository.findById(id).isPresent()){
            return orderRepository.findById(id).get();
        }
        throw new NotFoundException("Order not Found");
    }
    public List<ProductOrderDetails> findOrderDetailsByOrderId(long id) throws NotFoundException, UnsupportedEncodingException {
        Order order = findById(id);
        List<ProductOrderDetails> products = orderDetailsRepository.findByOrder(order);
        for(ProductOrderDetails product : products) {
            ProductImage productImage = productImageRepository.findProductImageByProductEquals(product.getProduct());
            byte[] encode =  Base64.getEncoder().encode(productImage.getImage());
            String utfEncode = new String(encode, "UTF-8");
            product.getProduct().setEncodedBase64String(utfEncode);
        }
        return products;
    }
}
