package nycto.homeservices.dto.dtoMapper;

import nycto.homeservices.dto.specialistDto.SpecialistCreateDto;
import nycto.homeservices.dto.specialistDto.SpecialistResponseDto;
import nycto.homeservices.dto.specialistDto.SpecialistUpdateDto;
import nycto.homeservices.entity.Specialist;
import nycto.homeservices.entity.enums.UserType;
import org.springframework.stereotype.Component;

@Component
public class SpecialistMapper {

    public Specialist toEntity(SpecialistCreateDto createDto) {
        Specialist specialist = new Specialist();
        specialist.setFirstName(createDto.firstName());
        specialist.setLastName(createDto.lastName());
        specialist.setEmail(createDto.email());
        specialist.setPassword(createDto.password());
        specialist.setProfilePicUrl(createDto.profilePicUrl());
        specialist.setUserType(UserType.SPECIALIST);
        return specialist;
    }

    public Specialist toEntity(SpecialistUpdateDto updateDto, Specialist existingSpecialist) {
        existingSpecialist.setFirstName(updateDto.firstName());
        existingSpecialist.setLastName(updateDto.lastName());
        existingSpecialist.setEmail(updateDto.email());
        existingSpecialist.setStatus(updateDto.status());
        existingSpecialist.setProfilePicUrl(updateDto.profilePicUrl());
        existingSpecialist.setRating(updateDto.rating());
        return existingSpecialist;
    }

    public SpecialistResponseDto toResponseDto(Specialist specialist) {
        return new SpecialistResponseDto(
                specialist.getId(),
                specialist.getFirstName(),
                specialist.getLastName(),
                specialist.getEmail(),
                specialist.getStatus(),
                specialist.getRegistrationDate(),
                specialist.getProfilePicUrl(),
                specialist.getRating()
        );
    }
}

