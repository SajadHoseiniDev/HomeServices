package nycto.homeservices.dto.payment;

import nycto.homeservices.dto.transactionsDto.TransactionsDto;

import java.time.LocalDateTime;
import java.util.List;

public record PaymentResponseDto(

        Long id,
        Long amount,
        String method,
        String status,
        LocalDateTime paymentTime,
        String transactionId,
        List<TransactionsDto> transactions
) {
}
