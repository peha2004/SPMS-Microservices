package lk.ijse.paymentservice.dto.resp;

import lk.ijse.paymentservice.entity.PaymentStatus;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponse {
    private Long id;
    private Long userId;
    private Long parkingId;
    private Long vehicleId;
    private Double amount;
    private String cardHolderName;
    private String maskedCardNumber;
    private PaymentStatus status;
    private String receiptNumber;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}