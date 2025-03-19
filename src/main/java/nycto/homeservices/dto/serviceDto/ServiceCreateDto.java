package nycto.homeservices.dto.serviceDto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record ServiceCreateDto(
        @NotEmpty(message = "name can't be empty!")
        @Size(min = 2, max = 50, message = "name should be between 2 and 50 characters!")
        String name
) {
}
