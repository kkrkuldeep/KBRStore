package com.mountblue.kbrshoppingsite.Config;

import com.mountblue.kbrshoppingsite.model.*;
import com.mountblue.kbrshoppingsite.repository.*;

import com.mountblue.kbrshoppingsite.service.user.StockService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Component
public class SetUpDataLoader implements ApplicationListener<ContextRefreshedEvent> {
    public static String contextPath = System.getProperty("user.dir");
    private boolean alreadySetup = false;
    private CustomerRepository customerRepository;
    private CartRepository cartRepository;
    private PasswordEncoder bCryptPasswordEncoder;
    private CategoryRepository categoryRepository;
    private ProductRepository productRepository;
    private AddressRepository addressRepository;
    private ProductImageRepository productImageRepository;
    private StockService stockService;
    private StockRepository stockRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PrivilegeRepository privilegeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public SetUpDataLoader(CustomerRepository customerRepository,
                           PasswordEncoder bCryptPasswordEncoder,
                           CartRepository cartRepository,
                           CategoryRepository categoryRepository,
                           ProductRepository productRepository,
                           AddressRepository addressRepository,
                           ProductImageRepository productImageRepository,
                           StockService stockService,
                           StockRepository stockRepository) {

        this.customerRepository = customerRepository;
        this.cartRepository = cartRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.addressRepository = addressRepository;
        this.productImageRepository = productImageRepository;
        this.stockService = stockService;
        this.stockRepository = stockRepository;
    }

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if (alreadySetup) {
            return;
        }

        // == create initial privileges
        final Privilege readPrivilege = createPrivilegeIfNotFound("READ_PRIVILEGE");
        final Privilege writePrivilege = createPrivilegeIfNotFound("WRITE_PRIVILEGE");
        final Privilege passwordPrivilege = createPrivilegeIfNotFound("CHANGE_PASSWORD_PRIVILEGE");

        // == create initial roles
        final List<Privilege> adminPrivileges = new ArrayList<Privilege>(Arrays.asList(readPrivilege, writePrivilege,
                passwordPrivilege));
        final List<Privilege> userPrivileges = new ArrayList<Privilege>(Arrays.asList(readPrivilege,
                passwordPrivilege));
        final Role adminRole = createRoleIfNotFound("ROLE_ADMIN", adminPrivileges);
        final Role customerRole = createRoleIfNotFound("ROLE_CUSTOMER", userPrivileges);


        try {
            createUserIfNotFound("test@test.com", "Test", "test", "1234567890",
                    new ArrayList<Role>(Arrays.asList(adminRole)));
            createCustomerIfNotFound("ravijoshi5398@gmail.com", "ravi",
                    "12345", "1234567890", new ArrayList<Role>(Arrays.asList(customerRole)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        alreadySetup = true;
    }

    @Transactional
    Privilege createPrivilegeIfNotFound(final String name) {
        Privilege privilege = privilegeRepository.findByName(name);
        if (privilege == null) {
            privilege = new Privilege(name);
            privilege = privilegeRepository.save(privilege);
        }
        return privilege;
    }

    @Transactional
    Role createRoleIfNotFound(final String name, final Collection<Privilege> privileges) {
        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role(name);
        }
        role.setPrivileges(privileges);
        role = roleRepository.save(role);
        return role;
    }

    @Transactional
    void createCustomerIfNotFound(String email, String name,
                                  String password, String phone, final Collection<Role> roles) throws IOException {

        if (!customerRepository.findByEmail(email).isPresent()) {
            Customer customer = new Customer();
            Customer customer1 = new Customer();
            customer.setName(name);
            customer1.setName("ravi");

            customer.setEmail(email);
            customer1.setEmail("ravi@gmail.com");

            customer.setPassword(bCryptPasswordEncoder.encode(password));
            customer1.setPassword(bCryptPasswordEncoder.encode("12345"));

            customer.setPhoneNumber(phone);
            customer1.setPhoneNumber("1234567890");

            customer.setRoles(roles);
            customer1.setRoles(roles);
            customer.setEnabled(true);
            customer1.setEnabled(true);

            customerRepository.save(customer);
            customerRepository.save(customer1);

        }

        Customer customer = customerRepository.findCustomerByEmail(email);
        Customer customer1 = customerRepository.findCustomerByEmail("ravi@gmail.com");

        if (!cartRepository.findByCustomer(customer).isPresent()) {
            Cart cart = new Cart();
            cart.setCustomer(customer);
            cart.setTotalPrice(0f);
            cartRepository.save(cart);

            Cart cart1 = new Cart();
            cart1.setCustomer(customer1);
            cart1.setTotalPrice(0f);
            cartRepository.save(cart1);
        }


        if (categoryRepository.findAll().isEmpty()) {
            Category category1 = new Category("Fruits And Vegetable");
            Category category2 = new Category("Foodgrains, Oil, Masala");
            Category category3 = new Category("Bakery, Cakes, Dairy");
            Category category4 = new Category("Beverages");
            Category category5 = new Category("Snacks, Branded Foods");
            Category category6 = new Category("Beauty, Hygiene");
            Category category7 = new Category("Cleaning, Household");

            categoryRepository.save(category1);
            categoryRepository.save(category2);
            categoryRepository.save(category3);
            categoryRepository.save(category4);
            categoryRepository.save(category5);
            categoryRepository.save(category6);
            categoryRepository.save(category7);


        }


        Category category1 = categoryRepository.findCategoryByCategoryName("Fruits And Vegetable");
        String path;

        System.out.println(category1.getId());

        if (productRepository.findAllByCategory(category1).isEmpty()) {


            path = contextPath + "/src/main/resources/static/images/FruitsAndVegetables/";
            Product product1 = new Product(category1, "Apple", "eat", 10f);
            Product product2 = new Product(category1, "Banana", "eat", 20f);
            Product product3 = new Product(category1, "Carrot", "eat", 30f);
            Product product4 = new Product(category1, "Cauliflower", "eat", 20f);
            Product product5 = new Product(category1, "Grapes", "eat", 20f);
            Product product6 = new Product(category1, "Lady Finger", "eat", 20f);
            Product product7 = new Product(category1, "Onion", "eat", 20f);
            Product product8 = new Product(category1, "Pomegranate", "eat", 20f);
            Product product9 = new Product(category1, "Potato", "eat", 20f);
            Product product10 = new Product(category1, "Watermelon", "eat", 20f);


            System.out.println(product1.getId());
            productRepository.save(product1);
            System.out.println(product1.getId());
            saveProductImageAndStock(path, "apple.jpg", product1);
            productRepository.save(product2);
            saveProductImageAndStock(path, "banana.jpg", product2);
            productRepository.save(product3);
            saveProductImageAndStock(path, "carrot.jpg", product3);
            productRepository.save(product4);
            saveProductImageAndStock(path, "cauliflower.jpg", product4);
            productRepository.save(product5);
            saveProductImageAndStock(path, "grapes.jpg", product5);
            productRepository.save(product6);
            saveProductImageAndStock(path, "lady-finger.jpg", product6);
            productRepository.save(product7);
            saveProductImageAndStock(path, "onion.jpg", product7);
            productRepository.save(product8);
            saveProductImageAndStock(path, "pomegranate.jpg", product8);
            productRepository.save(product9);
            saveProductImageAndStock(path, "potato.jpg", product9);
            productRepository.save(product10);
            saveProductImageAndStock(path, "watermelon.jpg", product10);
        }


        Category category2 = categoryRepository.findCategoryByCategoryName("Foodgrains, Oil, Masala");
        if (productRepository.findAllByCategory(category2).isEmpty()) {
            path = contextPath + "/src/main/resources/static/images/FoodgrainsAndOilAndMasala/";
            Product product11 = new Product(category2, "Almonds", "eat", 10f);
            Product product12 = new Product(category2, "Atta", "eat", 20f);
            Product product13 = new Product(category2, "Ghee", "eat", 30f);
            Product product14 = new Product(category2, "Jeera", "eat", 20f);
            Product product15 = new Product(category2, "kachi Oil", "eat", 20f);
            Product product16 = new Product(category2, "Oil", "eat", 20f);
            Product product17 = new Product(category2, "Peanuts", "eat", 20f);
            Product product18 = new Product(category2, "Raw Rice", "eat", 20f);
            Product product19 = new Product(category2, "Salt", "eat", 20f);
            Product product20 = new Product(category2, "Sugar", "eat", 20f);


            productRepository.save(product11);
            saveProductImageAndStock(path, "almonds.jpg", product11);
            productRepository.save(product12);
            saveProductImageAndStock(path, "Atta.jpg", product12);
            productRepository.save(product13);
            saveProductImageAndStock(path, "ghee.jpg", product13);
            productRepository.save(product14);
            saveProductImageAndStock(path, "jeera.jpg", product14);
            productRepository.save(product15);
            saveProductImageAndStock(path, "kachi-oil.jpg", product15);
            productRepository.save(product16);
            saveProductImageAndStock(path, "oil.jpg", product16);
            productRepository.save(product17);
            saveProductImageAndStock(path, "peanuts.jpg", product17);
            productRepository.save(product18);
            saveProductImageAndStock(path, "raw-rice.jpg", product18);
            productRepository.save(product19);
            saveProductImageAndStock(path, "salt.jpg", product19);
            productRepository.save(product20);
            saveProductImageAndStock(path, "sugar.jpg", product20);


        }

        Category category3 = categoryRepository.findCategoryByCategoryName("Bakery, Cakes, Dairy");
        if (productRepository.findAllByCategory(category3).isEmpty()) {
            path = contextPath + "/src/main/resources/static/images/BakeryAndCakesAndDairy/";
            Product product21 = new Product(category3, "Almond Milk", "eat", 10f);
            Product product22 = new Product(category3, "Bread", "eat", 20f);
            Product product23 = new Product(category3, "Butter", "eat", 30f);
            Product product24 = new Product(category3, "Butter Milk", "eat", 20f);
            Product product25 = new Product(category3, "Coco Cream", "eat", 20f);
            Product product26 = new Product(category3, "Curd", "eat", 20f);
            Product product27 = new Product(category3, "Milk", "eat", 20f);
            Product product28 = new Product(category3, "Paneer", "eat", 20f);
            Product product29 = new Product(category3, "Soya Vanilla", "eat", 20f);
            Product product30 = new Product(category3, "Tea Cake", "eat", 20f);


            productRepository.save(product21);
            saveProductImageAndStock(path, "almond-milk.jpg", product21);
            productRepository.save(product22);
            saveProductImageAndStock(path, "bread.jpg", product22);
            productRepository.save(product23);
            saveProductImageAndStock(path, "butter.jpg", product23);
            productRepository.save(product24);
            saveProductImageAndStock(path, "butter-milk.jpg", product24);
            productRepository.save(product25);
            saveProductImageAndStock(path, "coco-cream.jpg", product25);
            productRepository.save(product26);
            saveProductImageAndStock(path, "curd.jpg", product26);
            productRepository.save(product27);
            saveProductImageAndStock(path, "milk.jpg", product27);
            productRepository.save(product28);
            saveProductImageAndStock(path, "paneer.jpg", product28);
            productRepository.save(product29);
            saveProductImageAndStock(path, "soya-vanilla.jpg", product29);
            productRepository.save(product30);
            saveProductImageAndStock(path, "tea-cake.jpg", product30);
        }

        if (addressRepository.findAllByCustomer(customer).isEmpty()) {
            Address address1 = new Address(customer, "silk board", "bangalore",
                    "bharat", "1234567890", "123456");
            Address address2 = new Address(customer1, "near silk board", "bangalore",
                    "bharat", "0987654321", "654321");

            addressRepository.save(address1);
            addressRepository.save(address2);
        }
    }

    @Transactional
    Customer createUserIfNotFound(final String email, final String name, final String password, final String phone,
                                  final Collection<Role> roles) {
        Customer user;
        if (customerRepository.findByEmail(email).isPresent()) {
            user = customerRepository.findByEmail(email).get();
            user.setRoles(roles);
            user = customerRepository.save(user);
            return user;
        }
        user = new Customer();
        user.setName(name);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        user.setEnabled(true);
        user.setPhoneNumber(phone);
        user.setRoles(roles);
        user = customerRepository.save(user);
        return user;
    }


    public void saveProductImageAndStock(String path, String fileName, Product product) throws IOException {

        File file = new File(path + fileName);
        StringBuilder fileNames = new StringBuilder();
        FileInputStream input = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile("file",
                file.getName(), "text/plain", IOUtils.toByteArray(input));
        fileNames.append(multipartFile.getOriginalFilename() + " ");
        ProductImage productImage = new ProductImage(fileNames.toString(), multipartFile.getBytes());
        productImage.setImage(multipartFile.getBytes());
        productImage.setImageName(fileNames.toString());
        productImage.setProduct(product);

        productImageRepository.save(productImage);

        product.setStatus(true);
        product.setStock(20);

        Stock stock = new Stock();
        stock.setProduct(product);
        stock.setProductQuantityInStock(20f);
        stock.setEmptyStock(false);
        stockRepository.save(stock);
    }
}
