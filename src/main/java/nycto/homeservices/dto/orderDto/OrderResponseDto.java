package nycto.homeservices.dto.orderDto;

import nycto.homeservices.entity.enums.OrderStatus;

import java.time.LocalDateTime;

public record OrderResponseDto(

        Long id,
        Long customerId,
        String customerFirstName,
        String customerLastName,
        Long subServiceId,
        String subServiceName,
        Long specialistId,
        String description,
        Long proposedPrice,
        String address,
        LocalDateTime orderDate,
        LocalDateTime executionDate,
        OrderStatus status
) {
}
