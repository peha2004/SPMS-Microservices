package lk.ijse.paymentservice.dto.req;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentRequest {
    private Long userId;
    private Long parkingId;
    private Long vehicleId;
    private Double amount;
    private String cardHolderName;
    private String cardNumber;
    private String expiryDate;
    private String cvv;
}