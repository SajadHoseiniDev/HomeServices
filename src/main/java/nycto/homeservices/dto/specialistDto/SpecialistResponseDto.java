package nycto.homeservices.dto.specialistDto;

import nycto.homeservices.entity.enums.UserStatus;

import java.time.LocalDateTime;

public record SpecialistResponseDto(
        Long id,
        String firstName,
        String lastName,
        String email,
        UserStatus status,
        LocalDateTime registrationDate,
        String profilePicUrl,
        Double rating
) {
}
