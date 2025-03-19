package nycto.homeservices.dto.specialistDto;

import jakarta.validation.constraints.*;
import nycto.homeservices.entity.enums.UserStatus;

public record SpecialistUpdateDto(
        @NotEmpty(message = "firstName can't be empty!")
        @Size(min = 3, max = 30, message = "firstName should be between 3 and 30 characters!")
        String firstName,

        @NotEmpty(message = "lastName can't be empty!")
        @Size(min = 3, max = 30, message = "lastName should be between 3 and 30 characters!")
        String lastName,

        @NotEmpty(message = "email can't be empty!")
        @Pattern(regexp = "^[A-Za-z0-9+_.-]+@(.+)$", message = "Invalid email format")
        String email,

        @NotNull(message = "status can't be null!")
        UserStatus status,

        @NotEmpty(message = "profilePicUrl can't be empty!")
        String profilePicUrl,

        @Min(value = 0, message = "rating must be non-negative!")
        @Max(value = 5, message = "rating must be at most 5!")
        Double rating
) {
}
