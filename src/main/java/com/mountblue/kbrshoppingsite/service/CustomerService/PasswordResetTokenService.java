package com.mountblue.kbrshoppingsite.service.CustomerService;

import com.mountblue.kbrshoppingsite.model.Customer;
import com.mountblue.kbrshoppingsite.model.PasswordResetToken;
import com.mountblue.kbrshoppingsite.repository.PasswordResetTokenRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class PasswordResetTokenService {
    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    public void deleteByUser(Customer user) {
        passwordResetTokenRepository.delete(passwordResetTokenRepository.findByCustomer(user));
    }

    public PasswordResetToken findByToken(String token) {
        return passwordResetTokenRepository.findByToken(token);
    }

    public PasswordResetToken findByUser(Customer user) {
        return passwordResetTokenRepository.findByCustomer(user);
    }

    public void save(PasswordResetToken passwordResetToken) {
        passwordResetTokenRepository.save(passwordResetToken);
    }

    public void createAndSaveToken(Customer customer) {
        String token = UUID.randomUUID().toString();
        passwordResetTokenRepository.save(new PasswordResetToken(token, customer));
    }

    public Customer verifyResetToken(String confirmationToken) throws NotFoundException {
        if (passwordResetTokenRepository.findByToken(confirmationToken) != null) {
            return passwordResetTokenRepository.findByToken(confirmationToken).getCustomer();
        }
        throw new NotFoundException("Invalid password reset token");
    }
}
