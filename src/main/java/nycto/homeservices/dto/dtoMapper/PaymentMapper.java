package nycto.homeservices.dto.dtoMapper;

import nycto.homeservices.dto.payment.PaymentResponseDto;
import nycto.homeservices.entity.Payment;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapper {
    public PaymentResponseDto toResponseDto(Payment payment) {
        return new PaymentResponseDto(
                payment.getId(),
                payment.getAmount(),
                payment.getMethod().name(),
                payment.getStatus().name(),
                payment.getPaymentTime(),
                payment.getTransactionId()
        );
    }
}