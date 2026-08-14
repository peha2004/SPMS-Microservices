package lk.ijse.paymentservice.controller;

import lk.ijse.paymentservice.dto.req.PaymentRequest;
import lk.ijse.paymentservice.dto.resp.PaymentResponse;
import lk.ijse.paymentservice.service.PaymentService;
import lk.ijse.paymentservice.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@CrossOrigin
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<ApiResponse<PaymentResponse>> processPayment(@RequestBody PaymentRequest request) {
        PaymentResponse response = paymentService.processPayment(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(201, "Payment processed", response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PaymentResponse>> getPayment(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponse<>(200, "Payment retrieved successfully", paymentService.getPayment(id)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PaymentResponse>>> getAllPayments() {
        return ResponseEntity.ok(new ApiResponse<>(200, "Payments retrieved successfully", paymentService.getAllPayments()));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<PaymentResponse>>> getPaymentsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(new ApiResponse<>(200, "Payments retrieved successfully", paymentService.getPaymentsByUser(userId)));
    }

    @GetMapping("/receipt/{receiptNumber}")
    public ResponseEntity<ApiResponse<PaymentResponse>> getReceipt(@PathVariable String receiptNumber) {
        return ResponseEntity.ok(new ApiResponse<>(200, "Receipt retrieved successfully", paymentService.getReceipt(receiptNumber)));
    }
}