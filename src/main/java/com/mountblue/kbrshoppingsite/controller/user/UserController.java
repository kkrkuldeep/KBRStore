package com.mountblue.kbrshoppingsite.controller.user;

import com.mountblue.kbrshoppingsite.model.Category;
import com.mountblue.kbrshoppingsite.model.Product;
import com.mountblue.kbrshoppingsite.model.Stock;
import com.mountblue.kbrshoppingsite.service.user.CategoryService;
import com.mountblue.kbrshoppingsite.service.user.OrderService;
import com.mountblue.kbrshoppingsite.service.user.ProductService;
import com.mountblue.kbrshoppingsite.service.user.StockService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;


@Controller
@RequestMapping("/user/")
public class UserController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @Autowired
    private StockService stockService;

    @Autowired
    private OrderService orderService;

    @GetMapping("/kkr")
    public String checkout() {
        return "checkout-details";
    }

    @GetMapping("/")
    public String homePage() {
        return "user-page";
    }


    @GetMapping("addProduct")
    public String productAddPage(Product product, Model model) {
        model.addAttribute("categories", categoryService.findAll());
        return "product-form";
    }

    @PostMapping("addProduct")
    public String addProduct(@Valid Product product, BindingResult result,
                             @RequestParam("files") MultipartFile[] files, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.findAll());
            return "product-form";
        }
        try {
            productService.save(product, files);
        } catch (Exception e) {
            result.rejectValue("name", "error.productName", e.getMessage());
            model.addAttribute("categories", categoryService.findAll());
            return "product-form";
        }
        model.addAttribute("success", product.getName() + " product added " + product.getId());
        return "user-page";
    }

    @GetMapping("addCategory")
    public String categoryAddPage(Category category) {

        return "category";
    }

    @GetMapping("addStock/{id}")
    public String stockAddPage(@PathVariable("id") long id, Stock stock, Model model) {
        try {
            model.addAttribute("product", productService.findById(id));
        } catch (NotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "user-page";
        }
        return "stock-form";
    }

    @GetMapping("updateProduct/{id}")
    public String editProduct(@PathVariable("id") long id, Model model) {
        try {
            model.addAttribute("product", productService.findById(id));
            return "update-product";
        } catch (NotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "user-page";
        }
    }

    @PostMapping("updateProduct/{id}")
    public String updateProduct(@PathVariable("id") long id, @Valid Product product, BindingResult result,
                                Model model) {
        if (result.hasErrors()) {
            product.setId(id);
            return "update-product";
        }
        productService.update(id, product);
        model.addAttribute("success", "Product updated " + id);
        return "user-page";
    }

    @PostMapping("addStock")
    public String stockAdd(@Valid Stock stock, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "stock-form";
        }
        stock.setEmptyStock(true);
        stockService.save(stock);
        model.addAttribute("success", "Stock added " + stock.getId());
        return "user-page";
    }

    @PostMapping("addCategory")
    public String addCategory(@Valid Category category, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "category";
        }
        try {
            categoryService.save(category);
        } catch (Exception e) {
            result.rejectValue("categoryName", "category.exist", e.getMessage());
            return "category";
        }
        model.addAttribute("success", category.getCategoryName() + " category added " + category.getId());
        return "user-page";
    }

    @GetMapping("productList")
    public String showProductList(Model model) {
        model.addAttribute("categories", categoryService.findAll());
        return "product-list";
    }

    @GetMapping("orderDetails")
    public String successOrderDetails(Model model) {
        model.addAttribute("orders", orderService.successOrderList());
        return "order-details";
    }

    @GetMapping("stockDetails")
    public String stockDetails(Model model) {
        model.addAttribute("stocks", productService.stockDetailsByStock());
        return "stockDetails";
    }

    @GetMapping("productList/{category}")
    public String showProductList(@PathVariable("category") long id, Model model) {
        model.addAttribute("productList", productService.findProductByCategoryId(id));
        model.addAttribute("categories", categoryService.findAll());
        return "product-list";
    }
}
