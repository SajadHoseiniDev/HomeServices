package nycto.homeservices.dto.dtoMapper;

import nycto.homeservices.dto.payment.PaymentResponseDto;
import nycto.homeservices.dto.transactionsDto.TransactionsDto;
import nycto.homeservices.entity.Payment;
import nycto.homeservices.entity.Transactions;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PaymentMapper {
    public PaymentResponseDto toResponseDto(Payment payment, List<TransactionsDto> transactions) {
        return new PaymentResponseDto(
                payment.getId(),
                payment.getAmount(),
                payment.getMethod().name(),
                payment.getStatus().name(),
                payment.getPaymentTime(),
                payment.getTransactionId(),
                transactions

        );
    }
}