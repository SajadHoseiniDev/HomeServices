package nycto.homeservices.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import nycto.homeservices.entity.enums.UserStatus;

import java.time.LocalDateTime;

public record UserDto(

        Long id

        ,
        @Min(3)
        @Max(30)
        @NotEmpty(message = "firstName can't be empty!")
        String firstName

        ,
        @Min(3)
        @Max(30)
        @NotEmpty(message = "lastName can't be empty!")
        String lastName

        ,
        @NotEmpty(message = "email can't be empty!")
        @Pattern(regexp = "^[A-Za-z0-9+_.-]+@(.+)$", message = "Invalid email format")
        String email
        , UserStatus status
        , LocalDateTime registrationDate
) {}
