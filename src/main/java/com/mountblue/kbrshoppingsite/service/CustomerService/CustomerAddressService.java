package com.mountblue.kbrshoppingsite.service.CustomerService;

import com.mountblue.kbrshoppingsite.model.Address;
import com.mountblue.kbrshoppingsite.model.Customer;
import com.mountblue.kbrshoppingsite.repository.AddressRepository;
import com.mountblue.kbrshoppingsite.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
public class CustomerAddressService {
    private AddressRepository addressRepository;
    private CustomerRepository customerRepository;

    @Autowired
    public CustomerAddressService(AddressRepository addressRepository,
                                  CustomerRepository customerRepository) {
        this.addressRepository = addressRepository;
        this.customerRepository = customerRepository;
    }

    public List<Address> getAddressList(Principal principal) {
        Customer customer = customerRepository.findCustomerByEmail(principal.getName());
        List<Address> addressList = addressRepository.findAllByCustomer(customer);
        return addressList;
    }

    public void addAddress(Address address, Principal principal) {
        Customer customer = customerRepository.findCustomerByEmail(principal.getName());
        address.setCustomer(customer);
        addressRepository.save(address);
    }
}
