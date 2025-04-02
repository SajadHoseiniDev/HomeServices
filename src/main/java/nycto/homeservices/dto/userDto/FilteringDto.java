package nycto.homeservices.dto.userDto;

import nycto.homeservices.entity.enums.UserType;

public record FilteringDto(
        String firstName,
        String lastName,
        String email,
        UserType userType,
        String serviceName,
        Double rating
) {
}
