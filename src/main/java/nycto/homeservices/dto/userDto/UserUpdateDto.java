package nycto.homeservices.dto.userDto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import nycto.homeservices.entity.enums.UserStatus;

public record UserUpdateDto(
        @NotEmpty(message = "firstName can't be empty!")
        @Size(min = 3, max = 30, message = "firstName should be between 3 and 30 characters!")
        String firstName,

        @NotEmpty(message = "lastName can't be empty!")
        @Size(min = 3, max = 30, message = "lastName should be between 3 and 30 characters!")
        String lastName,

        @NotEmpty(message = "email can't be empty!")
        @Pattern(regexp = "^[A-Za-z0-9+_.-]+@(.+)$", message = "Invalid email format")
        String email,


        UserStatus status
) {
}
