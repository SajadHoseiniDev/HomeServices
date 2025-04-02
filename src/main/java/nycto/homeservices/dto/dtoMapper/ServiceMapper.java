package nycto.homeservices.dto.dtoMapper;

import nycto.homeservices.dto.serviceDto.ServiceCreateDto;
import nycto.homeservices.dto.serviceDto.ServiceResponseDto;
import nycto.homeservices.dto.serviceDto.ServiceUpdateDto;
import nycto.homeservices.entity.Service;
import org.springframework.stereotype.Component;

@Component
public class ServiceMapper {

    public Service toEntity(ServiceCreateDto createDto) {
        Service service = new Service();
        service.setName(createDto.name());
        return service;
    }

    public Service toEntity(ServiceUpdateDto updateDto, Service existingService) {
        existingService.setName(updateDto.name());
        return existingService;
    }

    public ServiceResponseDto toResponseDto(Service service) {
        return new ServiceResponseDto(
                service.getId(),
                service.getName()
        );
    }
}