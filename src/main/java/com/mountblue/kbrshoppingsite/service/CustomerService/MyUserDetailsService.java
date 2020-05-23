package com.mountblue.kbrshoppingsite.service.CustomerService;


import com.mountblue.kbrshoppingsite.model.Cart;
import com.mountblue.kbrshoppingsite.model.Customer;
import com.mountblue.kbrshoppingsite.model.MyUserDetails;
import com.mountblue.kbrshoppingsite.model.VerificationToken;
import com.mountblue.kbrshoppingsite.repository.CartRepository;
import com.mountblue.kbrshoppingsite.repository.CustomerRepository;
import com.mountblue.kbrshoppingsite.repository.RoleRepository;
import javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private VerificationTokenService verificationTokenService;
    @Autowired
    private EmailSenderService senderService;
    @Autowired
    private PasswordResetTokenService resetTokenService;
    @Autowired
    private CartRepository cartRepository;

    Logger logger = LoggerFactory.getLogger(MyUserDetailsService.class);

    public void save(Customer customer, HttpServletRequest request) throws Exception {
        Customer existCustomer = customerRepository.findCustomerByEmail(customer.getEmail());
        if (existCustomer != null) {
            throw new Exception("Email already Registered");
        }
        customer.setRoles(Arrays.asList(roleRepository.findByName("ROLE_CUSTOMER")));
        customer.setPassword(passwordEncoder().encode(customer.getPassword()));
        customerRepository.save(customer);
        verificationTokenService.createAndSave(customer);
        senderService.verifyEmail(customer.getEmail(), verificationTokenService.findTokenByCustomer(customer).getToken(), request);
    }

    public Customer findCustomerByEmail(String email) throws NotFoundException {
        if (customerRepository.findByEmail(email).isPresent()) {
            return customerRepository.findByEmail(email).get();
        }
        throw new NotFoundException("Wrong email or Not Found");
    }

    public void resendVerifyEmail(String email, HttpServletRequest request) throws NotFoundException, IOException {
        Customer customer = findCustomerByEmail(email);
        senderService.resendVerifyEmail(email, verificationTokenService.findTokenByCustomer(customer).getToken(), request);
    }

    public void editVerifyEmail(String email, String editedEmail, HttpServletRequest request) throws Exception {
        Customer customer = findCustomerByEmail(email);
        if (customerRepository.findByEmail(editedEmail).isPresent()) {
            throw new Exception("Email already Registered");
        }
        customer.setEmail(editedEmail);
        customerRepository.save(customer);
        verificationTokenService.editTokenAndSave(customer);
        senderService.verifyEmail(customer.getEmail(), verificationTokenService.findTokenByCustomer(customer).getToken(), request);
    }

    public void confirmAccount(String token) throws NotFoundException {
        VerificationToken verificationToken = verificationTokenService.findByToken(token);
        if (!customerRepository.findById(verificationToken.getCustomer().getId()).isPresent()) {
            throw new NotFoundException("Customer details not found");
        }
        Customer customer = customerRepository.findById(verificationToken.getCustomer().getId()).get();
        customer.setEnabled(true);
        customerRepository.save(customer);
        Cart cart = new Cart();
        cart.setCustomer(customer);
        cart.setTotalPrice(0f);
        cartRepository.save(cart);
        verificationTokenService.delete(verificationToken);
    }

    public void forgetPasswordTokenSend(String email, HttpServletRequest request) throws NotFoundException, IOException {
        Customer customer = findCustomerByEmail(email);
        resetTokenService.createAndSaveToken(customer);
        senderService.sendResetPasswordToken(email, resetTokenService.findByUser(customer).getToken(), request);
    }

    public void resetPassword(String email, String newPassword, String confirmPassword) throws Exception {
        Customer customer = findCustomerByEmail(email);
        if (!newPassword.equals(confirmPassword)) {
            throw new Exception("Confirm password not match");
        }
        customer.setPassword(passwordEncoder().encode(confirmPassword));
        customerRepository.save(customer);
        resetTokenService.deleteByUser(customer);
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Optional<Customer> customer = customerRepository.findByEmail(s);

        customer.orElseThrow(() -> new UsernameNotFoundException("Not found: " + s));
        System.out.println("hello");
        // System.out.println(user.map(MyUserDetails::new).get().getAuthorities());
        return customer.map(MyUserDetails::new).get();
    }

    private PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public void checkEmailAndToken(String email) throws NotFoundException {
        Customer customer = findCustomerByEmail(email);
        verificationTokenService.findTokenByCustomer(customer);
    }
}
