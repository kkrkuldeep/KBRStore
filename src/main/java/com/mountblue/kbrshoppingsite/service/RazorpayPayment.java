package com.mountblue.kbrshoppingsite.service;

import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.springframework.stereotype.Service;


@Service
public class RazorpayPayment {


    RazorpayClient razorpayPayment = new RazorpayClient("rzp_test_O3AUSxB8L47Bsf",
            "6hqEOkxBYStuXLMkwrZFEnlx");

    public RazorpayPayment() throws RazorpayException {
    }
}
