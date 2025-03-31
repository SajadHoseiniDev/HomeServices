package nycto.homeservices.service.serviceInterface;

import nycto.homeservices.dto.specialistDto.SpecialistCreateDto;
import nycto.homeservices.dto.specialistDto.SpecialistResponseDto;
import nycto.homeservices.dto.specialistDto.SpecialistUpdateDto;
import nycto.homeservices.entity.Order;
import nycto.homeservices.entity.Specialist;
import nycto.homeservices.exceptions.DuplicateDataException;
import nycto.homeservices.exceptions.NotFoundException;
import nycto.homeservices.exceptions.NotValidInputException;
import org.springframework.stereotype.Service;

import java.util.List;

public interface SpecialistService {

    SpecialistResponseDto createSpecialist(SpecialistCreateDto createDto)
            throws NotValidInputException, DuplicateDataException;

    SpecialistResponseDto getSpecialistById(Long id) throws NotFoundException;

    List<SpecialistResponseDto> getAllSpecialists();

    SpecialistResponseDto updateSpecialist(Long id, SpecialistUpdateDto updateDto)
            throws NotFoundException, NotValidInputException;

    void deleteSpecialist(Long id) throws NotFoundException;


    void addServiceToSpecialist(Long specialistId, nycto.homeservices.entity.Service service) throws NotFoundException;

    void removeServiceFromSpecialist(Long specialistId, nycto.homeservices.entity.Service service)
            throws NotFoundException;

    List<SpecialistResponseDto> getSpecialistsByService(nycto.homeservices.entity.Service service) throws NotFoundException;

    void calculateSpecialistScore(Specialist specialist, List<Order> completedOrders) throws NotFoundException;
}
