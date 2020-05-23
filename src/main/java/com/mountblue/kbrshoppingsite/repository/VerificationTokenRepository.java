package com.mountblue.kbrshoppingsite.repository;

import com.mountblue.kbrshoppingsite.model.Customer;
import com.mountblue.kbrshoppingsite.model.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.stream.Stream;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {

    VerificationToken findByToken(String token);

    VerificationToken findByCustomer(Customer user);

    Stream<VerificationToken> findAllByExpiryDateLessThan(Date now);

    @Transactional
    VerificationToken deleteByCustomer(Customer user);

    void deleteByExpiryDateLessThan(Date now);

    @Modifying
    @Query("delete from VerificationToken t where t.expiryDate <= ?1")
    void deleteAllExpiredSince(Date now);
}

