package nycto.homeservices.service;

import lombok.RequiredArgsConstructor;
import nycto.homeservices.dto.specialistDto.SpecialistCreateDto;
import nycto.homeservices.dto.specialistDto.SpecialistResponseDto;
import nycto.homeservices.entity.Specialist;
import nycto.homeservices.entity.enums.UserStatus;
import nycto.homeservices.exceptions.DuplicateDataException;
import nycto.homeservices.exceptions.NotValidInputException;
import nycto.homeservices.repository.SpecialistRepository;
import nycto.homeservices.util.ValidationUtil;
import nycto.homeservices.util.dtoMapper.SpecialistMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SpecialistService {
    private final SpecialistRepository specialistRepository;
    private final ValidationUtil validationUtil;
    private final SpecialistMapper specialistMapper;

    public SpecialistResponseDto createSpecialist(SpecialistCreateDto createDto)
            throws NotValidInputException, DuplicateDataException {
        if (!validationUtil.validate(createDto))
            throw new NotValidInputException("Not valid specialist data");

        if (specialistRepository.findByEmail(createDto.email()).isPresent())
            throw new DuplicateDataException("Specialist with email: "
                    + createDto.email() + " already exists");

        Specialist specialist = specialistMapper.toEntity(createDto);
        specialist.setRegistrationDate(LocalDateTime.now());
        specialist.setStatus(UserStatus.NEW);

        Specialist savedSpecialist = specialistRepository.save(specialist);
        return specialistMapper.toResponseDto(savedSpecialist);
    }

}
