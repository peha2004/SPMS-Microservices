package lk.ijse.paymentservice.service.impl;

import lk.ijse.paymentservice.dto.req.PaymentRequest;
import lk.ijse.paymentservice.dto.resp.PaymentResponse;
import lk.ijse.paymentservice.entity.Payment;
import lk.ijse.paymentservice.entity.PaymentStatus;
import lk.ijse.paymentservice.exception.BadRequestException;
import lk.ijse.paymentservice.exception.PaymentNotFoundException;
import lk.ijse.paymentservice.repository.PaymentRepository;
import lk.ijse.paymentservice.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    @Override
    public PaymentResponse processPayment(PaymentRequest request) {
        validateRequest(request);

        boolean cardValid = mockValidateCard(request.getCardNumber(), request.getExpiryDate(), request.getCvv());

        Payment payment = Payment.builder()
                .userId(request.getUserId())
                .parkingId(request.getParkingId())
                .vehicleId(request.getVehicleId())
                .amount(request.getAmount())
                .cardHolderName(request.getCardHolderName())
                .maskedCardNumber(maskCard(request.getCardNumber()))
                .status(cardValid ? PaymentStatus.SUCCESS : PaymentStatus.FAILED)
                .receiptNumber(cardValid ? generateReceiptNumber() : null)
                .build();

        return mapToResponse(paymentRepository.save(payment));
    }

    @Override
    public PaymentResponse getPayment(Long id) {
        return mapToResponse(findPayment(id));
    }

    @Override
    public List<PaymentResponse> getAllPayments() {
        return paymentRepository.findAll().stream().map(this::mapToResponse).toList();
    }

    @Override
    public List<PaymentResponse> getPaymentsByUser(Long userId) {
        return paymentRepository.findByUserId(userId).stream().map(this::mapToResponse).toList();
    }

    @Override
    public PaymentResponse getReceipt(String receiptNumber) {
        Payment payment = paymentRepository.findByReceiptNumber(receiptNumber)
                .orElseThrow(() -> new PaymentNotFoundException("Receipt not found"));
        if (payment.getStatus() != PaymentStatus.SUCCESS) {
            throw new BadRequestException("No receipt available for a non-successful payment");
        }
        return mapToResponse(payment);
    }

    private void validateRequest(PaymentRequest request) {
        if (request.getUserId() == null) throw new BadRequestException("User ID is required");
        if (request.getParkingId() == null) throw new BadRequestException("Parking ID is required");
        if (request.getAmount() == null || request.getAmount() <= 0)
            throw new BadRequestException("Amount must be greater than zero");
        if (request.getCardHolderName() == null || request.getCardHolderName().trim().isEmpty())
            throw new BadRequestException("Card holder name is required");
        if (request.getCardNumber() == null || request.getCardNumber().replaceAll("\\s", "").length() != 16)
            throw new BadRequestException("Card number must be 16 digits");
        if (request.getExpiryDate() == null || !request.getExpiryDate().matches("(0[1-9]|1[0-2])/\\d{2}"))
            throw new BadRequestException("Expiry date must be in MM/YY format");
        if (request.getCvv() == null || !request.getCvv().matches("\\d{3}"))
            throw new BadRequestException("CVV must be 3 digits");
    }

    private boolean mockValidateCard(String cardNumber, String expiryDate, String cvv) {
        String digitsOnly = cardNumber.replaceAll("\\s", "");
        char lastDigit = digitsOnly.charAt(digitsOnly.length() - 1);
        return (lastDigit - '0') % 2 == 0;
    }

    private String maskCard(String cardNumber) {
        String digitsOnly = cardNumber.replaceAll("\\s", "");
        return "**** **** **** " + digitsOnly.substring(digitsOnly.length() - 4);
    }

    private String generateReceiptNumber() {
        return "RCPT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private Payment findPayment(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new PaymentNotFoundException("Payment not found"));
    }

    private PaymentResponse mapToResponse(Payment payment) {
        return PaymentResponse.builder()
                .id(payment.getId())
                .userId(payment.getUserId())
                .parkingId(payment.getParkingId())
                .vehicleId(payment.getVehicleId())
                .amount(payment.getAmount())
                .cardHolderName(payment.getCardHolderName())
                .maskedCardNumber(payment.getMaskedCardNumber())
                .status(payment.getStatus())
                .receiptNumber(payment.getReceiptNumber())
                .createdAt(payment.getCreatedAt())
                .updatedAt(payment.getUpdatedAt())
                .build();
    }
}