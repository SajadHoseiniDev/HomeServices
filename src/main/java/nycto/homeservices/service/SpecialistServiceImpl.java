package nycto.homeservices.service;

import lombok.RequiredArgsConstructor;
import nycto.homeservices.dto.specialistDto.SpecialistCreateDto;
import nycto.homeservices.dto.specialistDto.SpecialistResponseDto;
import nycto.homeservices.dto.specialistDto.SpecialistUpdateDto;
import nycto.homeservices.entity.Specialist;
import nycto.homeservices.entity.enums.UserStatus;
import nycto.homeservices.exceptions.DuplicateDataException;
import nycto.homeservices.exceptions.NotFoundException;
import nycto.homeservices.exceptions.NotValidInputException;
import nycto.homeservices.repository.SpecialistRepository;
import nycto.homeservices.service.serviceInterface.SpecialistService;
import nycto.homeservices.util.ValidationUtil;
import nycto.homeservices.util.dtoMapper.SpecialistMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SpecialistServiceImpl implements SpecialistService {
    private final SpecialistRepository specialistRepository;
    private final ValidationUtil validationUtil;
    private final SpecialistMapper specialistMapper;
    private final ServiceService serviceService;


    @Override
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

    @Override
    public SpecialistResponseDto getSpecialistById(Long id) throws NotFoundException {
        Specialist specialist = specialistRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Specialist with id " + id + " not found"));
        return specialistMapper.toResponseDto(specialist);
    }

    @Override
    public List<SpecialistResponseDto> getAllSpecialists() {

        return specialistRepository.findAll().stream()
                .map(specialistMapper::toResponseDto)
                .collect(Collectors.toList());
    }


    @Override
    public SpecialistResponseDto updateSpecialist(Long id, SpecialistUpdateDto updateDto)
            throws NotFoundException, NotValidInputException {

        Specialist existingSpecialist = specialistRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Specialist with id " + id + " not found"));

        if (!validationUtil.validate(updateDto))
            throw new NotValidInputException("Not valid update data");

        Specialist updatedSpecialist = specialistMapper.toEntity(updateDto, existingSpecialist);

        updatedSpecialist.setId(id);

        return specialistMapper.toResponseDto(specialistRepository.save(updatedSpecialist));
    }

    @Override
    public void deleteSpecialist(Long id) throws NotFoundException {
        Specialist specialist = specialistRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Specialist with id " + id + " not found"));
        specialistRepository.delete(specialist);
    }


    @Override
    public void addServiceToSpecialist(Long specialistId, nycto.homeservices.entity.Service service) throws NotFoundException {
        Specialist specialist = specialistRepository.findById(specialistId)
                .orElseThrow(() -> new NotFoundException("Specialist with id " + specialistId + " not found"));

        specialist.getServices().add(service);
        specialistRepository.save(specialist);
    }

    @Override
    public void removeServiceFromSpecialist(Long specialistId, nycto.homeservices.entity.Service service)
            throws NotFoundException {

        Specialist specialist = specialistRepository.findById(specialistId)
                .orElseThrow(() -> new NotFoundException("Specialist with id "
                        + specialistId + " not found"));


        if (!specialist.getServices().contains(service)) {
            throw new NotFoundException("Service not found in specialist's services");
        }


        specialist.getServices().remove(service);
        specialistRepository.save(specialist);
    }


}
