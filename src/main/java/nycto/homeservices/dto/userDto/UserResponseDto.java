package nycto.homeservices.dto.userDto;

import nycto.homeservices.entity.enums.UserStatus;

import java.time.LocalDateTime;

public record UserResponseDto(
        Long id,
        String firstName,
        String lastName,
        String email,
        UserStatus status,
        LocalDateTime registrationDate
) {
}
