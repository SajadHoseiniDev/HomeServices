package nycto.homeservices.dto.subService;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SubServiceUpdateDto(
        @NotEmpty(message = "name can't be empty!")
        @Size(min = 2, max = 50, message = "name should be between 2 and 50 characters!")
        String name,

        @NotNull(message = "basePrice can't be null!")
        @Min(value = 0, message = "basePrice must be non-negative!")
        Long basePrice,

        @Size(max = 255, message = "description can't be longer than 255 characters!")
        String description
) {
}
