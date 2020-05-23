package com.mountblue.kbrshoppingsite.service.CustomerService;

import com.mountblue.kbrshoppingsite.model.*;
import com.mountblue.kbrshoppingsite.repository.*;
import com.mountblue.kbrshoppingsite.service.user.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CustomerCartService {
    CartRepository cartRepository;
    CartItemRepository cartItemRepository;
    ProductRepository productRepository;
    OrderRepository orderRepository;
    CustomerRepository customerRepository;
    AddressRepository addressRepository;
    ProductOrderDetailsRepository productOrderDetailsRepository;
    ProductImageRepository productImageRepository;
    CustomerProductService customerProductService;
    StockService stockService;

    @Autowired
    public CustomerCartService(CartRepository cartRepository,
                               CartItemRepository cartItemRepository,
                               ProductRepository productRepository,
                               OrderRepository orderRepository,
                               CustomerRepository customerRepository,
                               AddressRepository addressRepository,
                               ProductOrderDetailsRepository productOrderDetailsRepository,
                               ProductImageRepository productImageRepository,
                               CustomerProductService customerProductService,
                               StockService stockService) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.addressRepository = addressRepository;
        this.productOrderDetailsRepository = productOrderDetailsRepository;
        this.productImageRepository = productImageRepository;
        this.customerProductService = customerProductService;
        this.stockService = stockService;
    }

    public Cart getCart(Principal principal) {
        Customer customer = customerRepository.findCustomerByEmail(principal.getName());
        Cart cart = cartRepository.findCartByCustomer(customer);
        return cart;
    }


      //also check for different user if cart is working or not.
    public List<CartItem> getItemsFromCart(Principal principal) throws UnsupportedEncodingException {
        Customer customer = customerRepository.findCustomerByEmail(principal.getName());
        Cart cart = cartRepository.findCartByCustomer(customer);
        List<CartItem> cartItemList = cartItemRepository.findCartItemByCart(cart);
        for(CartItem cartItem : cartItemList) {
            ProductImage productImage = productImageRepository.findProductImageByProductEquals(cartItem.getProduct());
            byte[] encode =  Base64.getEncoder().encode(productImage.getImage());
            String utfEncode = new String(encode, "UTF-8");
            cartItem.getProduct().setEncodedBase64String(utfEncode);
        }

        return cartItemList;
    }


    public Product addToCart(Long productId, Principal principal, int quantity) throws Exception {
        Customer customer = customerRepository.findCustomerByEmail(principal.getName());
        Product product = customerProductService.getProduct(productId);
        Cart cart =  cartRepository.findCartByCustomer(customer);
        CartItem cartItem = getCartItem(product, cart);
        Stock stock = stockService.findByProduct_Id(productId);
        if(cartItem != null) {
            cart.setTotalPrice(cart.getTotalPrice() -  cartItem.getPrice()/*product.getPrice() * quantity*/); //setting amount in cart.
            cart.setTotalItem(cart.getTotalItem() - cartItem.getQuantity()/*quantity*/);


            if(stock.getProductQuantityInStock() - quantity < 10 || quantity > 10) {
                throw new Exception("stock invalid");
            }
            if(quantity == 0) {
                removeFromCart(productId, principal);
                return product;
            }
            cartItem.setQuantity(/*cartItem.getQuantity() + */quantity);
            System.out.println(cartItem.getQuantity());
            cartItem.setPrice(/*cartItem.getPrice() +*/ product.getPrice() * quantity);

            cart.setTotalPrice(cart.getTotalPrice() +  cartItem.getPrice()); //setting amount in cart.
            cart.setTotalItem(cart.getTotalItem() + quantity);

            cartItemRepository.save(cartItem);
        }
        else {
            CartItem newCartItem = new CartItem();
            newCartItem.setCart(cart);
            if(stock.getProductQuantityInStock() - quantity < 10 || quantity > 10) {
                throw new Exception("stock invalid");
            }
            if(quantity == 0) {
                throw new Exception("add at least one item to cart");
            }
            newCartItem.setQuantity(quantity);
            newCartItem.setPrice(product.getPrice() * quantity);
            newCartItem.setProduct(product);

            cart.setTotalPrice(cart.getTotalPrice() + newCartItem.getPrice());
            cart.setTotalItem(cart.getTotalItem() + quantity);
            cartItemRepository.save(newCartItem);
        }
        return product;
    }
          //leave it for now
    public void updateCart(Long productId, Principal principal) {
        Customer customer = customerRepository.findCustomerByEmail(principal.getName());
        Product product = productRepository.findProductById(productId);
        Cart cart =  cartRepository.findCartByCustomer(customer);
        CartItem cartItem = getCartItem(product, cart);
        if(cartItem != null) {
            cartItem.setQuantity(cartItem.getQuantity() - 1);
            cartItem.setPrice(cartItem.getPrice() - cartItem.getProduct().getPrice());
            if(cartItem.getQuantity() == 0) {
                cartItemRepository.delete(cartItem);
            }
            else {
                cartItemRepository.save(cartItem);
            }

        }
        //else exception.
    }

    public void removeFromCart(Long productId, Principal principal) {
        Customer customer = customerRepository.findCustomerByEmail(principal.getName());
        Product product = productRepository.findProductById(productId);
        Cart cart =  cartRepository.findCartByCustomer(customer);
        CartItem cartItem = getCartItem(product, cart);
        if(cartItem != null) {
            cart.setTotalItem(cart.getTotalItem() - cartItem.getQuantity());
            cart.setTotalPrice(cart.getTotalPrice() - cartItem.getPrice());
            cartItemRepository.delete(cartItem);
        }
    }

    public void clearCart(Principal principal) {
        Customer customer = customerRepository.findCustomerByEmail(principal.getName());
        Cart cart =  cartRepository.findCartByCustomer(customer);
        cart.setTotalPrice(0f);
        cart.setTotalItem(0);
        cartItemRepository.deleteCartItemsByCart(cart);
    }


    public void buyAll(Long addressId, Principal principal) {
        Customer customer = customerRepository.findCustomerByEmail(principal.getName());
        Address address = addressRepository.findAddressById(addressId);
        Cart cart =  cartRepository.findCartByCustomer(customer);
        Order order = new Order();
        order.setAddress(address);
        order.setPaymentMethod("Razorpay");
        order.setOrderStatus("pending");
        order.setOrderTotal(cart.getTotalPrice());
        orderRepository.save(order);

        List<CartItem> cartItemList = cartItemRepository.findCartItemByCart(cart);


        for(CartItem cartItem : cartItemList) {
            Product currentProduct = cartItem.getProduct();
            stockService.updateStock(cartItem);
            ProductOrderDetails productOrderDetails = new ProductOrderDetails();

            productOrderDetails.setPrice(cartItem.getProduct().getPrice());
            productOrderDetails.setOrder(order);
            productOrderDetails.setProduct(currentProduct);
            productOrderDetails.setQuantity(cartItem.getQuantity());
            productOrderDetails.setTotal_price(cartItem.getPrice());

            currentProduct.getProductOrderDetails().add(productOrderDetails);
            order.getProductOrderDetails().add(productOrderDetails);

            System.out.println(order.getProductOrderDetails());
          // (no need )productOrderDetailsRepository.save(productOrderDetails);
            // because i'm using cascade merge and persist.
        };

        clearCart(principal); //clear cart where principle.

    }


    public CartItem getCartItem(Product product, Cart cart) {
         CartItem cartItem = cartItemRepository.findCartItemByCartAndProduct(cart, product);
         return cartItem;
    }




}
