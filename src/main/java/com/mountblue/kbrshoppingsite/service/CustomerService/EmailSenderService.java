package com.mountblue.kbrshoppingsite.service.CustomerService;

import com.mountblue.kbrshoppingsite.properties.SendGridProperties;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Service
public class EmailSenderService {

    @Autowired
    private SendGridProperties properties;

    Logger logger = LoggerFactory.getLogger(EmailSenderService.class);

    private void sendEmail(String email, String subject, String tokenUrl) throws IOException {
        Email from = new Email(properties.getMailFrom());
        Email to = new Email(email);
        Content content = new Content(properties.getType(), tokenUrl);
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid(properties.getKey());
        Request request = new Request();

        request.setMethod(Method.POST);
        request.setEndpoint(properties.getEndpoint());
        request.setBody(mail.build());
        Response response = sg.api(request);
        logger.debug(String.valueOf(response.getStatusCode()));
        logger.debug(response.getBody());
        logger.debug(String.valueOf(response.getHeaders()));
    }

    public void verifyEmail(String email, String token, HttpServletRequest request) throws IOException {
        String tokenUrl = properties.getVerifyMessage() + serverUrl(request) + properties.getVerifyTokenEndpoint() + token;
        sendEmail(email, properties.getVerifySubject(), tokenUrl);
    }

    public void resendVerifyEmail(String email, String token, HttpServletRequest request) throws IOException {
        String tokenUrl = properties.getVerifyMessage() + serverUrl(request) + properties.getVerifyTokenEndpoint() + token;
        sendEmail(email, properties.getResendVerifySubject(), tokenUrl);
    }

    public void sendResetPasswordToken(String email, String token, HttpServletRequest request) throws IOException {
        String tokenUrl = properties.getResetMessage() + serverUrl(request) + properties.getResetPasswordTokenEndpoint() + token;
        sendEmail(email, properties.getResetPassSubject(), tokenUrl);
    }

    private String serverUrl(HttpServletRequest request) {
        return request.getScheme() + "://" + request.getServerName();
    }
}