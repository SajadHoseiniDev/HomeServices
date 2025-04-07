package nycto.homeservices.dto.payment;

import jakarta.validation.constraints.NotNull;
import nycto.homeservices.entity.enums.PaymentMethod;

public record PaymentRequestDto(

        @NotNull(message = "payment method can't be null!")
        PaymentMethod method,

        String captchaToken
) {}
