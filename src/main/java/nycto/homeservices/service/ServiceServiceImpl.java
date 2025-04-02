package nycto.homeservices.service;

import lombok.RequiredArgsConstructor;
import nycto.homeservices.dto.serviceDto.ServiceCreateDto;
import nycto.homeservices.dto.serviceDto.ServiceResponseDto;
import nycto.homeservices.dto.serviceDto.ServiceUpdateDto;
import nycto.homeservices.entity.Service;
import nycto.homeservices.exceptions.DuplicateDataException;
import nycto.homeservices.exceptions.NotFoundException;
import nycto.homeservices.repository.ServiceRepository;
import nycto.homeservices.service.serviceInterface.ServiceService;
import nycto.homeservices.dto.dtoMapper.ServiceMapper;

import java.util.List;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class ServiceServiceImpl implements ServiceService {

    private final ServiceRepository serviceRepository;

    private final ServiceMapper serviceMapper;

    @Override
    public ServiceResponseDto createService(ServiceCreateDto createDto)
    {

        if (serviceRepository.findByName(createDto.name()).isPresent())
            throw new DuplicateDataException("Service with name: "
                    + createDto.name() + " already exists");

        Service service = serviceMapper.toEntity(createDto);
        Service savedService = serviceRepository.save(service);
        return serviceMapper.toResponseDto(savedService);
    }

    @Override
    public ServiceResponseDto getServiceById(Long id) {
        Service service = serviceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Service with id "
                        + id + " not found"));

        return serviceMapper.toResponseDto(service);
    }

    @Override
    public List<ServiceResponseDto> getAllServices() {
        return serviceRepository.findAll().stream()
                .map(serviceMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public ServiceResponseDto updateService(Long id, ServiceUpdateDto updateDto) {

        Service existingService = serviceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Service with id "
                        + id + " not found"));

        Service updatedService = serviceMapper
                .toEntity(updateDto, existingService);

        updatedService.setId(id);

        return serviceMapper
                .toResponseDto(serviceRepository.save(updatedService));
    }

    @Override
    public void deleteService(Long id) {
        Service service = serviceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Service with id "
                        + id + " not found"));

        serviceRepository.delete(service);
    }

    @Override
    public Service findServiceById(Long id) {
        return serviceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Service with id " + id + " not found"));
    }


}
