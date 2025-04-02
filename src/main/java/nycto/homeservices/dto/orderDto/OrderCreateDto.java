package nycto.homeservices.dto.orderDto;

import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public record OrderCreateDto(

        @NotNull(message = "subServiceId can't be null!")
        Long subServiceId,

        @Size(max = 255, message = "description can't be longer than 255 characters!")
        String description,

        @NotNull(message = "proposedPrice can't be null!")
        @Min(value = 0, message = "proposedPrice must be non-negative!")
        Long proposedPrice,

        @NotEmpty(message = "address can't be empty!")
        @Size(max = 255, message = "address can't be longer than 255 characters!")
        String address,

        @NotNull(message = "executionDate can't be null!")
        @Future(message = "date must be in the future!")
        LocalDateTime executionDate
) {
}
