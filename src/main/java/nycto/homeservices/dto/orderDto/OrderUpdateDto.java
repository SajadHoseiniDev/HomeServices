package nycto.homeservices.dto.orderDto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record OrderUpdateDto(
        @Size(max = 255, message = "description can't be longer than 255 characters!")
        String description,

        @Min(value = 0, message = "proposedPrice must be non-negative!")
        Long proposedPrice,

        @NotEmpty(message = "address can't be empty!")
        @Size(max = 255, message = "address can't be longer than 255 characters!")
        String address
) {
}
