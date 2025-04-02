package nycto.homeservices.dto.dtoMapper;

import nycto.homeservices.dto.subService.SubServiceCreateDto;
import nycto.homeservices.dto.subService.SubServiceResponseDto;
import nycto.homeservices.dto.subService.SubServiceUpdateDto;
import nycto.homeservices.entity.Service;
import nycto.homeservices.entity.SubService;
import org.springframework.stereotype.Component;

@Component
public class SubServiceMapper {

    public SubService toEntity(SubServiceCreateDto createDto, Service service) {
        SubService subService = new SubService();
        subService.setName(createDto.name());
        subService.setBasePrice(createDto.basePrice());
        subService.setDescription(createDto.description());
        subService.setService(service);
        return subService;
    }

    public SubService toEntity(SubServiceUpdateDto updateDto, SubService existingSubService, Service service) {
        existingSubService.setName(updateDto.name());
        existingSubService.setBasePrice(updateDto.basePrice());
        existingSubService.setDescription(updateDto.description());
        existingSubService.setService(service);
        return existingSubService;
    }

    public SubServiceResponseDto toResponseDto(SubService subService) {
        return new SubServiceResponseDto(
                subService.getId(),
                subService.getName(),
                subService.getBasePrice(),
                subService.getDescription(),
                subService.getService().getId()
        );
    }

}
