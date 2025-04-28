package com.example.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VNPayConfig {
    @Value("${vnpay.tmnCode}")
    private String tmnCode;

    @Value("${vnpay.hashSecret}")
    private String hashSecret;

    @Value("${vnpay.payUrl}")
    private String payUrl;

    @Value("${vnpay.returnUrl}")
    private String returnUrl;

    @Value("${vnpay.apiUrl}")
    private String apiUrl;

    public String getTmnCode() {
        return tmnCode;
    }

    public String getHashSecret() {
        return hashSecret;
    }

    public String getPayUrl() {
        return payUrl;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public String getApiUrl() {
        return apiUrl;
    }
} 