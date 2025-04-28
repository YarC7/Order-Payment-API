package com.example.demo.controller;

import com.example.demo.dto.PaymentRequest;
import com.example.demo.model.Payment;
import com.example.demo.service.PaymentService;
import com.example.demo.service.VNPayService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;
    private final VNPayService vnPayService;

    @Autowired
    public PaymentController(PaymentService paymentService, VNPayService vnPayService) {
        this.paymentService = paymentService;
        this.vnPayService = vnPayService;
    }

    @PostMapping
    public ResponseEntity<Payment> processPayment(@Valid @RequestBody PaymentRequest paymentRequest) {
        Payment payment = paymentService.processPayment(paymentRequest);
        return new ResponseEntity<>(payment, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Payment> getPayment(@PathVariable Long id) {
        Payment payment = paymentService.getPaymentById(id);
        return ResponseEntity.ok(payment);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<Payment>> getPaymentsByOrderId(@PathVariable Long orderId) {
        List<Payment> payments = paymentService.getPaymentsByOrderId(orderId);
        return ResponseEntity.ok(payments);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Payment> updatePaymentStatus(@PathVariable Long id, @RequestParam String status) {
        Payment payment = paymentService.updateBankPaymentStatus(id, status);
        return ResponseEntity.ok(payment);
    }

    @PostMapping("/create-payment")
    public String createPayment(@RequestBody Map<String, Object> body) throws UnsupportedEncodingException {
        System.out.println("Body: " + body);
        long amount = Long.parseLong(body.get("amount").toString());
        String orderInfo = body.get("orderInfo").toString();
        String url = vnPayService.createPaymentUrl(amount, orderInfo);
        System.out.println("Payment URL: " + url);
        return url;
    }

    @GetMapping("/vnpay-payment")
    public ResponseEntity<String> paymentCallback(@RequestParam Map<String, String> queryParams) {
        String vnp_ResponseCode = queryParams.get("vnp_ResponseCode");
        String vnp_TxnRef = queryParams.get("vnp_TxnRef");
        String vnp_Amount = queryParams.get("vnp_Amount");
    
        String message;
        if ("00".equals(vnp_ResponseCode)) {
            message = "<h1 style='color: #4CAF50;'>Thanh toán thành công!</h1>"
                    + "<p>Mã đơn hàng: <b>" + vnp_TxnRef + "</b></p>"
                    + "<p>Số tiền: <b>" + vnp_Amount + " VND</b></p>";
        } else {
            message = "<h1 style='color: #F44336;'>Thanh toán thất bại!</h1>"
                    + "<p>Mã đơn hàng: <b>" + vnp_TxnRef + "</b></p>";
        }
    
        String html = "<html><head><title>Kết quả thanh toán</title></head>"
                + "<body style='background:#222;color:#fff;font-family:sans-serif;text-align:center;padding-top:50px;'>"
                + message
                + "</body></html>";
    
        return ResponseEntity.ok().header("Content-Type", "text/html; charset=UTF-8").body(html);
    }
}