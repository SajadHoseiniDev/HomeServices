package nycto.homeservices.dto.userDto;

import nycto.homeservices.dto.orderDto.OrderResponseDto;

import java.util.List;

public record UserHistoryDto(
        List<OrderResponseDto> orders,
        Long totalCredit
) {
}
