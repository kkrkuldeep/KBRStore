package com.mountblue.kbrshoppingsite.controller.Customer;

import com.mountblue.kbrshoppingsite.model.Customer;
import com.mountblue.kbrshoppingsite.service.CustomerService.MyUserDetailsService;
import com.mountblue.kbrshoppingsite.service.CustomerService.PasswordResetTokenService;
import javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;

@Controller
@RequestMapping("/auth/")
public class AuthController {

    @Autowired

    private MyUserDetailsService userService;
    @Autowired
    private PasswordResetTokenService passwordResetTokenService;

    Logger logger = LoggerFactory.getLogger(AuthController.class);

    @GetMapping("add")
    public String showAddUser(Customer customer) {
        logger.info("Registration Page shown");
        return "sign-up";
    }

    @PostMapping("register")
    public String addUser(@Valid Customer user, BindingResult result, Model model, HttpServletRequest request) {
        if (result.hasErrors()) {
            logger.error("Error while register user");
            return "sign-up";
        }
        try {
            userService.save(user, request);
        } catch (Exception e) {
            result.rejectValue("email", "email.already", e.getMessage());
            return "sign-up";
        }
        model.addAttribute("emailId", user.getEmail());
        return "successfulRegistration";
    }

    @GetMapping({"resend/{email}"})
    public String resendVerifyEmail(@PathVariable("email") String email, Model model, HttpServletRequest request) {
        try {
            userService.resendVerifyEmail(email, request);
        } catch (NotFoundException | IOException e) {
            model.addAttribute("message", e.getMessage());
            return "error";
        }
        model.addAttribute("emailId", email);
        logger.info("Verification Email Resend");
        return "successfulRegistration";
    }

    @GetMapping({"edit-email/{email}"})
    public String editVerifyEmail(@PathVariable("email") String email, Model model) {
        try {
            userService.checkEmailAndToken(email);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "sign-in";
        }
        model.addAttribute("oldEmail", email);
        logger.info("Edit email page return");
        return "editVerifyEmail";
    }

    @PostMapping("edited-email/{email}")
    public String editedVerifyEmail(@PathVariable("email") String email,
                                    @RequestParam("editedEmail") String editedEmail, Model model,
                                    HttpServletRequest request) {
        try {
            userService.editVerifyEmail(email, editedEmail, request);
        } catch (Exception e) {
            logger.error(e.getMessage());
            model.addAttribute("oldEmail", email);
            model.addAttribute("message", e.getMessage());
            return "editVerifyEmail";
        }
        logger.info(email + " changed to " + editedEmail);
        model.addAttribute("emailId", editedEmail);
        logger.info("Email edited and new verification token send via email" + editedEmail);
        return "successfulRegistration";
    }

    @GetMapping("login-form")
    public String showLoginForm(String error, Model model, Principal principal) {
        if (principal != null) return "redirect:/";
        logger.info("Login page show");
        if (error != null) {
            model.addAttribute("errorMsg", "Your username and password are invalid.");
        }
        return "sign-in";
    }

    @GetMapping("forget")
    public String showForgetPasswordForm() {
        logger.info("Forget password page show");
        return "forgetPassword";
    }

    @PostMapping("reset-password")
    public String sendResetPasswordTokenForm(@RequestParam("resetEmail") String email, Model model,
                                             HttpServletRequest request) {
        try {
            userService.forgetPasswordTokenSend(email, request);
        } catch (NotFoundException | IOException e) {
            logger.error(e.getMessage() + email);
            model.addAttribute("message", e.getMessage());
            e.printStackTrace();
            return "forgetPassword";
        }
        model.addAttribute("emailId", email);
        logger.info("Reset password token generated and sends via email" + email);
        return "successfulRegistration";
    }

    @RequestMapping(value = "/confirmAccount", method = {RequestMethod.GET, RequestMethod.POST})
    public String confirmUserAccount(@RequestParam("token") String confirmationToken, Model model) {
        try {
            userService.confirmAccount(confirmationToken);
        } catch (NotFoundException e) {
            e.printStackTrace();
            logger.error("Invalid email verification token");
            model.addAttribute("message", e.getMessage());
            return "error";
        }
        logger.info("Email verified successfully");
        return "accountVerified";
    }

    @RequestMapping(value = "/passwordReset", method = {RequestMethod.GET, RequestMethod.POST})
    public String resetPasswordToken(@RequestParam("token") String confirmationToken, Model model) {
        try {
            Customer customer = passwordResetTokenService.verifyResetToken(confirmationToken);
            model.addAttribute("email", customer.getEmail());
            logger.info("Password token verify successfully and reset password page shown for user " + customer.getName());
            return "resetPassword";
        } catch (NotFoundException e) {
            e.printStackTrace();
            logger.error("Invalid password reset token");
            model.addAttribute("message", e.getMessage());
            return "error";
        }
    }

    @PostMapping("reset-password/{email}")
    public String resetPassword(@PathVariable("email") String email, @RequestParam("newPassword") String newPassword,
                                @RequestParam("confirmPassword") String confirmPassword,
                                Model model) {
        try {
            userService.resetPassword(email, newPassword, confirmPassword);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("email", email);
            model.addAttribute("message", e.getMessage());
            logger.error("Confirm Password not match");
            return "resetPassword";
        }
        logger.info("Password rest for user " + email);
        model.addAttribute("message", "Password reset Successfully");
        return "sign-in";
    }
}
