package nycto.homeservices.dto.customerDto;

import nycto.homeservices.entity.enums.UserStatus;

import java.time.LocalDateTime;

public record CustomerResponseDto(
        Long id,
        String firstName,
        String lastName,
        String email,
        UserStatus status,
        LocalDateTime registrationDate
) {
}
