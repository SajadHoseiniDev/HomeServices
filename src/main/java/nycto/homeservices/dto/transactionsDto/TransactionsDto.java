package nycto.homeservices.dto.transactionsDto;

import java.time.LocalDateTime;

public record TransactionsDto(
        Long id,
        Long customerId,
        Long specialistId,
        Long creditDeducted,
        Long creditAdded,
        Long orderId,
        String description,
        LocalDateTime transactionDate
) {
}
