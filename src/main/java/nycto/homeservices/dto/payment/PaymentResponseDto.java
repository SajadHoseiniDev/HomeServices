package nycto.homeservices.dto.payment;

import java.time.LocalDateTime;

public record PaymentResponseDto(

        Long id,
        Long amount,
        String method,
        String status,
        LocalDateTime paymentTime,
        String transactionId
) {
}
