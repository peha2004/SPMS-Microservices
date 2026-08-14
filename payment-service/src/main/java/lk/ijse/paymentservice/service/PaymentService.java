package lk.ijse.paymentservice.service;

import lk.ijse.paymentservice.dto.req.PaymentRequest;
import lk.ijse.paymentservice.dto.resp.PaymentResponse;

import java.util.List;

public interface PaymentService {
    PaymentResponse processPayment(PaymentRequest request);
    PaymentResponse getPayment(Long id);
    List<PaymentResponse> getAllPayments();
    List<PaymentResponse> getPaymentsByUser(Long userId);
    PaymentResponse getReceipt(String receiptNumber);
}