package nycto.homeservices.service.serviceInterface;

import nycto.homeservices.dto.serviceDto.ServiceCreateDto;
import nycto.homeservices.dto.serviceDto.ServiceResponseDto;
import nycto.homeservices.dto.serviceDto.ServiceUpdateDto;
import nycto.homeservices.exceptions.DuplicateDataException;
import nycto.homeservices.exceptions.NotFoundException;
import nycto.homeservices.exceptions.NotValidInputException;

import java.util.List;

public interface ServiceService {
    ServiceResponseDto createService(ServiceCreateDto createDto)
                throws NotValidInputException, DuplicateDataException;

    ServiceResponseDto getServiceById(Long id) throws NotFoundException;

    List<ServiceResponseDto> getAllServices();

    ServiceResponseDto updateService(Long id, ServiceUpdateDto updateDto)
            throws NotFoundException, NotValidInputException;

    void deleteService(Long id) throws NotFoundException;
}
