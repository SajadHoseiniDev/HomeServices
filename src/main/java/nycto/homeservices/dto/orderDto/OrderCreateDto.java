package nycto.homeservices.dto.orderDto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

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
        String address
) {
}
