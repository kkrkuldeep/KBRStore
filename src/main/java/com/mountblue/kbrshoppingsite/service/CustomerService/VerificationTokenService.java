package com.mountblue.kbrshoppingsite.service.CustomerService;

import com.mountblue.kbrshoppingsite.model.Customer;
import com.mountblue.kbrshoppingsite.model.VerificationToken;
import com.mountblue.kbrshoppingsite.repository.VerificationTokenRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class VerificationTokenService {
    @Autowired
    private VerificationTokenRepository tokenRepository;

    public void createAndSave(Customer customer) {
        String token = UUID.randomUUID().toString();
        tokenRepository.save(new VerificationToken(token, customer));
    }

    public void editTokenAndSave(Customer customer) throws NotFoundException {
        VerificationToken verificationToken = findTokenByCustomer(customer);
        tokenRepository.delete(verificationToken);
        String token = UUID.randomUUID().toString();
        tokenRepository.save(new VerificationToken(token, customer));
    }

    public VerificationToken findTokenByCustomer(Customer customer) throws NotFoundException {
        VerificationToken verificationToken = tokenRepository.findByCustomer(customer);
        if (verificationToken == null) {
            throw new NotFoundException("Invalid email for verification or the email already verified");
        }
        return verificationToken;
    }

    public VerificationToken findByToken(String token) throws NotFoundException {
        VerificationToken verificationToken = tokenRepository.findByToken(token);
        if (verificationToken != null) {
            return verificationToken;
        }
        throw new NotFoundException("Invalid email verification token");
    }

    public void deleteByCustomer(Customer customer) {
        tokenRepository.deleteByCustomer(customer);
    }

    public void delete(VerificationToken verificationToken) {
        tokenRepository.delete(verificationToken);
    }
}
