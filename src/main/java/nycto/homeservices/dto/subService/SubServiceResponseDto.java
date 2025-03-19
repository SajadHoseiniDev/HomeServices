package nycto.homeservices.dto.subService;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SubServiceResponseDto(
        Long id,
        String name,
        Long basePrice,
        String description,
        Long serviceId
) {
}
