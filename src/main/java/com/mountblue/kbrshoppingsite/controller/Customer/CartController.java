package com.mountblue.kbrshoppingsite.controller.Customer;

import com.mountblue.kbrshoppingsite.model.*;
import com.mountblue.kbrshoppingsite.repository.*;
import com.mountblue.kbrshoppingsite.service.CustomerService.CustomerAddressService;
import com.mountblue.kbrshoppingsite.service.CustomerService.CustomerCartService;
import com.mountblue.kbrshoppingsite.service.CustomerService.CustomerProductService;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.mountblue.kbrshoppingsite.service.RazorpayPayment;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.List;

import static com.mountblue.kbrshoppingsite.properties.constant.ImageConstant.*;
import static com.mountblue.kbrshoppingsite.properties.constant.RazorPayConstant.*;

@Controller
public class CartController {
    private CustomerCartService customerCartService;
    private OrderRepository orderRepository;
    private ProductImageRepository productImageRepository;
    private CustomerProductService customerProductService;
    private CustomerAddressService customerAddressService;
    private CustomerRepository customerRepository;
    private Logger logger = LoggerFactory.getLogger(CartController.class);

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    public CartController(CustomerCartService customerCartService,
                          OrderRepository orderRepository,
                          ProductImageRepository productImageRepository,
                          CustomerProductService customerProductService,
                          CustomerAddressService customerAddressService,
                          CustomerRepository customerRepository) throws RazorpayException {
        this.customerCartService = customerCartService;
        this.orderRepository = orderRepository;
        this.productImageRepository = productImageRepository;
        this.customerProductService = customerProductService;
        this.customerAddressService = customerAddressService;
        this.customerRepository = customerRepository;
    }

    @Autowired
    ProductRepository productRepository;

    @Autowired
    RazorpayPayment razorpayPayment;

    public static String uploadDirectory = System.getProperty("user.dir") + IMG_PATH;

    RazorpayClient razorpay = new RazorpayClient(RAZORPAY_KEY, RAZORPAY_SECRET);

    @PostMapping("/upload")
    @ResponseBody
    public String newTest(@RequestParam("files") MultipartFile[] files) {
        StringBuilder fileNames = new StringBuilder();
        for (MultipartFile file : files) {

            fileNames.append(file.getOriginalFilename() + " ");
            try {
                ProductImage productImage = new ProductImage(fileNames.toString(), file.getBytes());
                productImage.setImage(file.getBytes());
                productImage.setImageName(fileNames.toString());
                productImage.setProduct(productRepository.findProductById(2l));

                productImageRepository.save(productImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "image uploaded";
    }


    @GetMapping("cart")
    public String viewCart(Principal principal, Model model) throws UnsupportedEncodingException {
        List<CartItem> cartItems = customerCartService.getItemsFromCart(principal);
        model.addAttribute("cartItems", cartItems);
        logger.info("" + cartItems);
        return "basket";
    }

    @GetMapping("cart/add/{productId}")
    public String add(@RequestParam("categoryId") Long categoryId, @RequestParam(value = "quantity", defaultValue =
            "1") int quantity,
                      @PathVariable("productId") Long productId, Principal principal, Model model) throws Exception {

        Product product = customerCartService.addToCart(productId, principal, quantity);

        model.addAttribute("categoryId", categoryId);
        model.addAttribute("msg", quantity + " " + product.getName() + " successfully added to cart");
        model.addAttribute("singleProduct", product);
        model.addAttribute("quantity", quantity);
        return "single-product";
    }

    @GetMapping("cart/deleteCheckout/{productId}")
    public String update(@PathVariable("productId") Long productId, Principal principal) {
        Cart cart = customerCartService.getCart(principal);
        customerCartService.removeFromCart(productId, principal);
        if (cart.getTotalItem() == 0) return "redirect:/cart";
        return "redirect:/checkoutDetails";
    }

    @GetMapping("cart/delete/{productId}")
    public String remove(@PathVariable("productId") Long productId, Principal principal) {
        customerCartService.removeFromCart(productId, principal);
        return "redirect:/cart";
    }

    @GetMapping("cart/clear-cart")
    public String clearAll(Principal principal) {
        customerCartService.clearCart(principal);
        return "redirect:/cart";
    }


    @GetMapping("/checkoutDetails")
    @PreAuthorize("hasRole('ROLE_CUSTOMER') or hasRole('ROLE_ADMIN')")
    public String checkoutDetails(Principal principal, Model model) throws Exception {
        List<CartItem> cartItemList = customerCartService.getItemsFromCart(principal);
        if (cartItemList.isEmpty()) {
            throw new Exception("cart is empty handle exception here");   //handle exception here;
        }
        List<Address> addressList = customerAddressService.getAddressList(principal);
        Customer customer = customerRepository.findCustomerByEmail(principal.getName());
        model.addAttribute("customer", customer);
        model.addAttribute("addressList", addressList);
        model.addAttribute("cartItemList", cartItemList);
        model.addAttribute("newAddress", new Address());
        return "checkout-details";
    }

    @PostMapping("/newAddress")
    @PreAuthorize("hasRole('ROLE_CUSTOMER') or hasRole('ROLE_ADMIN')")
    public String addAddress(Address newAddress, Principal principal) {
        customerAddressService.addAddress(newAddress, principal);
        return "redirect:/checkoutDetails";
    }


    @GetMapping("cart/checkout")
    @PreAuthorize("hasRole('ROLE_CUSTOMER') or hasRole('ROLE_ADMIN')")
    public String buyItems(@RequestParam("address") Long addressId, Principal principal) {

        Cart cart = customerCartService.getCart(principal);

        try {
            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", cart.getTotalPrice());
            orderRequest.put("currency", "INR");
            orderRequest.put("receipt", "order_rcptid_11");
            orderRequest.put("payment_capture", false);

            com.razorpay.Order order = razorpay.Orders.create(orderRequest);
            logger.info("Inside try block");
        } catch (RazorpayException e) {
            logger.info("Inside Catch");
            logger.info(e.getMessage());
        }
        logger.info("Done Try Catch blocks");
        customerCartService.buyAll(addressId, principal);
        return "redirect:/";
    }
}
