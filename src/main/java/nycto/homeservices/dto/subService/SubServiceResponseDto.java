package nycto.homeservices.dto.subService;

public record SubServiceResponseDto(
        Long id,
        String name,
        Long basePrice,
        String description,
        Long serviceId
) {
}
