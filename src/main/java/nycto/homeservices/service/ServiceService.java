package nycto.homeservices.service;

import lombok.RequiredArgsConstructor;
import nycto.homeservices.dto.serviceDto.ServiceCreateDto;
import nycto.homeservices.dto.serviceDto.ServiceResponseDto;
import nycto.homeservices.exceptions.DuplicateDataException;
import nycto.homeservices.exceptions.NotFoundException;
import nycto.homeservices.exceptions.NotValidInputException;
import nycto.homeservices.repository.ServiceRepository;
import nycto.homeservices.util.ValidationUtil;
import nycto.homeservices.util.dtoMapper.ServiceMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServiceService {

    private final ServiceRepository serviceRepository;
    private final ValidationUtil validationUtil;
    private final ServiceMapper serviceMapper;

    public ServiceResponseDto createService(ServiceCreateDto createDto)
            throws NotValidInputException, DuplicateDataException {
        if (!validationUtil.validate(createDto))
            throw new NotValidInputException("Not valid service data");

        if (serviceRepository.findByName(createDto.name()).isPresent())
            throw new DuplicateDataException("Service with name: " + createDto.name() + " already exists");

        nycto.homeservices.entity.Service service = serviceMapper.toEntity(createDto);
        nycto.homeservices.entity.Service savedService = serviceRepository.save(service);
        return serviceMapper.toResponseDto(savedService);
    }

    public ServiceResponseDto getServiceById(Long id) throws NotFoundException {
        nycto.homeservices.entity.Service service = serviceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Service with id " + id + " not found"));
        return serviceMapper.toResponseDto(service);
    }

    public List<ServiceResponseDto> getAllServices() {
        return serviceRepository.findAll().stream()
                .map(serviceMapper::toResponseDto)
                .collect(Collectors.toList());
    }




}
