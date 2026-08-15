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
    public ResponseEntity<ApiResponse<PaymentResponse>> processPayment(
            @RequestBody PaymentRequest request,
            @RequestHeader(value = "X-User-Role", required = false) String role,
            @RequestHeader(value = "X-User-Id", required = false) Long userId) {

        if (!"DRIVER".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse<>(403, "Only drivers can make payments", null));
        }

        request.setUserId(userId);
        PaymentResponse response = paymentService.processPayment(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(201, "Payment processed", response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PaymentResponse>> getPayment(
            @PathVariable Long id,
            @RequestHeader(value = "X-User-Role", required = false) String role,
            @RequestHeader(value = "X-User-Id", required = false) Long userId) {

        PaymentResponse payment = paymentService.getPayment(id);
        if (!"OWNER".equals(role) && !payment.getUserId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse<>(403, "You can only view your own payments", null));
        }
        return ResponseEntity.ok(new ApiResponse<>(200, "Payment retrieved successfully", payment));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PaymentResponse>>> getAllPayments(
            @RequestHeader(value = "X-User-Role", required = false) String role) {

        if (!"OWNER".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse<>(403, "Only owners can view all payments", null));
        }
        return ResponseEntity.ok(new ApiResponse<>(200, "Payments retrieved successfully", paymentService.getAllPayments()));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<PaymentResponse>>> getPaymentsByUser(
            @PathVariable Long userId,
            @RequestHeader(value = "X-User-Role", required = false) String role,
            @RequestHeader(value = "X-User-Id", required = false) Long requesterId) {

        if (!"OWNER".equals(role) && !userId.equals(requesterId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse<>(403, "You can only view your own payments", null));
        }
        return ResponseEntity.ok(new ApiResponse<>(200, "Payments retrieved successfully", paymentService.getPaymentsByUser(userId)));
    }

    @GetMapping("/receipt/{receiptNumber}")
    public ResponseEntity<ApiResponse<PaymentResponse>> getReceipt(
            @PathVariable String receiptNumber,
            @RequestHeader(value = "X-User-Role", required = false) String role,
            @RequestHeader(value = "X-User-Id", required = false) Long userId) {

        PaymentResponse payment = paymentService.getReceipt(receiptNumber);
        if (!"OWNER".equals(role) && !payment.getUserId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse<>(403, "You can only view your own receipts", null));
        }
        return ResponseEntity.ok(new ApiResponse<>(200, "Receipt retrieved successfully", payment));
    }
}