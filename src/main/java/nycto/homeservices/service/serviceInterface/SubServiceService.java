package nycto.homeservices.service.serviceInterface;

import nycto.homeservices.dto.subService.SubServiceCreateDto;
import nycto.homeservices.dto.subService.SubServiceResponseDto;
import nycto.homeservices.dto.subService.SubServiceUpdateDto;
import nycto.homeservices.entity.Service;
import nycto.homeservices.exceptions.DuplicateDataException;
import nycto.homeservices.exceptions.NotFoundException;
import nycto.homeservices.exceptions.NotValidInputException;

import java.util.List;

public interface SubServiceService {
    SubServiceResponseDto createSubService(SubServiceCreateDto dto, Service existingService)
            throws NotValidInputException, DuplicateDataException;

    SubServiceResponseDto getSubServiceById(Long id)
            throws NotFoundException;

    List<SubServiceResponseDto> getAllSubServices();

    SubServiceResponseDto updateSubService(Long id, SubServiceUpdateDto updateDto, Service existingService)
            throws NotFoundException, NotValidInputException;

    void deleteSubService(Long id) throws NotFoundException;
}
