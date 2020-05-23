package com.mountblue.kbrshoppingsite.repository;

import com.mountblue.kbrshoppingsite.model.Address;
import com.mountblue.kbrshoppingsite.model.Customer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface AddressRepository extends CrudRepository<Address, Long> {
    List<Address> findAllByCustomer(Customer customer);
    Address findAddressById(Long id);
}
