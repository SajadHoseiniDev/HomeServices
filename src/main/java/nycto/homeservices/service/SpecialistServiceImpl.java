package nycto.homeservices.service;

import lombok.RequiredArgsConstructor;
import nycto.homeservices.dto.specialistDto.SpecialistCreateDto;
import nycto.homeservices.dto.specialistDto.SpecialistResponseDto;
import nycto.homeservices.dto.specialistDto.SpecialistUpdateDto;
import nycto.homeservices.entity.Comment;
import nycto.homeservices.entity.Order;
import nycto.homeservices.entity.Specialist;
import nycto.homeservices.entity.enums.UserStatus;
import nycto.homeservices.exceptions.DuplicateDataException;
import nycto.homeservices.exceptions.NotFoundException;
import nycto.homeservices.repository.SpecialistRepository;
import nycto.homeservices.service.serviceInterface.SpecialistService;
import nycto.homeservices.dto.dtoMapper.SpecialistMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SpecialistServiceImpl implements SpecialistService {
    private final SpecialistRepository specialistRepository;

    private final SpecialistMapper specialistMapper;



    @Override
    public SpecialistResponseDto createSpecialist(SpecialistCreateDto createDto)
            throws  DuplicateDataException {

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
            throws NotFoundException{

        Specialist existingSpecialist = specialistRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Specialist with id " + id + " not found"));

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

    @Override
    public List<SpecialistResponseDto> getSpecialistsByService(nycto.homeservices.entity.Service service) throws NotFoundException {
        if (service == null) {
            throw new NotFoundException("Service not found");
        }

        return service.getSpecialists().stream()
                .map(specialistMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public void calculateSpecialistScore(Specialist specialist, List<Order> completedOrders) throws NotFoundException {
        if (specialist == null) {
            throw new NotFoundException("Specialist not found");
        }

        if (completedOrders == null || completedOrders.isEmpty()) {
            specialist.setRating(0.0);
        } else {
            double averageRating = completedOrders.stream()
                    .flatMap(order -> order.getComments().stream())
                    .filter(comment -> comment.getRating()!=null)
                    .mapToDouble(Comment::getRating)
                    .average()
                    .orElse(0.0);

            specialist.setRating(averageRating);
        }

        specialistRepository.save(specialist);
    }




}
