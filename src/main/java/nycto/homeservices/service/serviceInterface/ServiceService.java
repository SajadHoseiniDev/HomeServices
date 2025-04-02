package nycto.homeservices.service.serviceInterface;

import nycto.homeservices.dto.serviceDto.ServiceCreateDto;
import nycto.homeservices.dto.serviceDto.ServiceResponseDto;
import nycto.homeservices.dto.serviceDto.ServiceUpdateDto;
import nycto.homeservices.entity.Service;
import nycto.homeservices.exceptions.DuplicateDataException;
import nycto.homeservices.exceptions.NotFoundException;
import nycto.homeservices.exceptions.NotValidInputException;

import java.util.List;

public interface ServiceService {
    ServiceResponseDto createService(ServiceCreateDto createDto);

    ServiceResponseDto getServiceById(Long id) ;

    List<ServiceResponseDto> getAllServices();

    ServiceResponseDto updateService(Long id, ServiceUpdateDto updateDto);

    void deleteService(Long id) ;

    Service findServiceById(Long id) ;
}
