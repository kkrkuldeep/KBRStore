package com.mountblue.kbrshoppingsite.service.user;

import com.mountblue.kbrshoppingsite.model.Order;
import com.mountblue.kbrshoppingsite.model.ProductOrderDetails;
import com.mountblue.kbrshoppingsite.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    public List<Order> successOrderList(){
        return orderRepository.findAllByOrderByOrderAtDesc();
    }
}
