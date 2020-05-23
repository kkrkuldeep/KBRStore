package com.mountblue.kbrshoppingsite.Config;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mountblue.kbrshoppingsite.controller.Customer.AuthController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    Logger logger = LoggerFactory.getLogger(CustomLoginSuccessHandler.class);
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest arg0, HttpServletResponse arg1, Authentication authentication)
            throws IOException, ServletException {

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        authorities.forEach(authority -> {
            if (authority.getAuthority().equals("ROLE_CUSTOMER")) {
                try {
                    logger.info("Customer Login redirect");
                    redirectStrategy.sendRedirect(arg0, arg1, "/");
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    logger.error("Customer Login redirect error");
                }
            } else if (authority.getAuthority().equals("ROLE_ADMIN")) {
                try {
                    logger.info("Admin Login redirect");
                    redirectStrategy.sendRedirect(arg0, arg1, "/user/");
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    logger.error("Admin Login redirect error");
                }
            } else {
                logger.error("Unknown Login redirect error");
                throw new IllegalStateException();
            }
        });

    }

}