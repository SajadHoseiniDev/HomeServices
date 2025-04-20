package nycto.homeservices.dto.userDto;

import java.time.LocalDateTime;

public record UserReportDto(
        Long userId,
        String firstName,
        String lastName,
        LocalDateTime registrationDate,
        Long orderCount
) {
}
