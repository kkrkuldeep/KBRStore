package com.mountblue.kbrshoppingsite.model;

import com.mountblue.kbrshoppingsite.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class MyUserDetails implements UserDetails {
    @Autowired
    private CustomerRepository customerRepository;

    private Customer customer;
    private List<GrantedAuthority> authorities;

    public MyUserDetails(Customer customer) {
        this.customer = customer;
    }

    public MyUserDetails() {

    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> list = new ArrayList<>();
        customer.getRoles().forEach(role -> list.add(new SimpleGrantedAuthority(role.getName())));
//        return Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
        return list;
    }

    @Override
    public String getPassword() {
        return customer.getPassword();
    }

    @Override
    public String getUsername() {
        return customer.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return customer.isEnabled();
    }
}
