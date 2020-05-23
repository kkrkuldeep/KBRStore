package com.mountblue.kbrshoppingsite.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties
@PropertySource("classpath:sendgrid.properties")
public class SendGridProperties {
    private String key;
    private String endpoint;
    private String mailFrom;
    private String type;
    private String verifySubject;
    private String resendVerifySubject;
    private String resetPassSubject;
    private String verifyMessage;
    private String resetMessage;
    private String verifyTokenEndpoint;
    private String resetPasswordTokenEndpoint;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getMailFrom() {
        return mailFrom;
    }

    public void setMailFrom(String mailFrom) {
        this.mailFrom = mailFrom;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVerifySubject() {
        return verifySubject;
    }

    public void setVerifySubject(String verifySubject) {
        this.verifySubject = verifySubject;
    }

    public String getResendVerifySubject() {
        return resendVerifySubject;
    }

    public void setResendVerifySubject(String resendVerifySubject) {
        this.resendVerifySubject = resendVerifySubject;
    }

    public String getResetPassSubject() {
        return resetPassSubject;
    }

    public void setResetPassSubject(String resetPassSubject) {
        this.resetPassSubject = resetPassSubject;
    }

    public String getVerifyMessage() {
        return verifyMessage;
    }

    public void setVerifyMessage(String verifyMessage) {
        this.verifyMessage = verifyMessage;
    }

    public String getResetMessage() {
        return resetMessage;
    }

    public void setResetMessage(String resetMessage) {
        this.resetMessage = resetMessage;
    }

    public String getVerifyTokenEndpoint() {
        return verifyTokenEndpoint;
    }

    public void setVerifyTokenEndpoint(String verifyTokenEndpoint) {
        this.verifyTokenEndpoint = verifyTokenEndpoint;
    }

    public String getResetPasswordTokenEndpoint() {
        return resetPasswordTokenEndpoint;
    }

    public void setResetPasswordTokenEndpoint(String resetPasswordTokenEndpoint) {
        this.resetPasswordTokenEndpoint = resetPasswordTokenEndpoint;
    }
}
