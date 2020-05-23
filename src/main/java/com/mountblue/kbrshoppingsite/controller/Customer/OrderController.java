package com.mountblue.kbrshoppingsite.controller.Customer;

import com.mountblue.kbrshoppingsite.service.CustomerService.CustomerOrderService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.UnsupportedEncodingException;

@Controller
public class OrderController {

    @Autowired
    private CustomerOrderService customerOrderService;

    @GetMapping("myOrder")
    @PreAuthorize("hasRole('ROLE_CUSTOMER') or hasRole('ROLE_ADMIN')")
    public String showCustomerOrder(Model model, Authentication authentication) {
        try {
            model.addAttribute("orders", customerOrderService.findOrderByCustomer(authentication.getName()));
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "myOrders";
        }
        return "myOrders";
    }

    @GetMapping("order/{id}")
    @PreAuthorize("hasRole('ROLE_CUSTOMER') or hasRole('ROLE_ADMIN')")
    public String orderDetails(@PathVariable("id") long id, Model model) {
        try {
            model.addAttribute("id", id);
            model.addAttribute("orderList", customerOrderService.findOrderDetailsByOrderId(id));
        } catch (NotFoundException | UnsupportedEncodingException e) {
            model.addAttribute("error", e.getMessage());
            return "order";
        }
        return "order";
    }
}
