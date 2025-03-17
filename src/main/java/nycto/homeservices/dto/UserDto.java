package nycto.homeservices.dto;

import jakarta.validation.constraints.Min;
import nycto.homeservices.entity.enums.UserStatus;

import java.time.LocalDateTime;

public record UserDto(

        Long id
        , String firstName
        , String lastName
        , String email
        , UserStatus status
        , LocalDateTime registrationDate
) {}
