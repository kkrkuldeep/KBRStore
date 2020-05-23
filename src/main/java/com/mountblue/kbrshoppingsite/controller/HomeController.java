package com.mountblue.kbrshoppingsite.controller;

import com.mountblue.kbrshoppingsite.model.Address;
import com.mountblue.kbrshoppingsite.model.Category;
import com.mountblue.kbrshoppingsite.service.CustomerService.CustomerAddressService;
import com.mountblue.kbrshoppingsite.service.user.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {
    private CategoryService categoryService;

    @Autowired
    public HomeController(CategoryService categoryService) {
        this.categoryService = categoryService;

    }

    @GetMapping("/")
    public String index(Model model) {
        List<Category> categoryList = categoryService.findAll();
        model.addAttribute("categoryList", categoryList);
        return "index";
    }


}
