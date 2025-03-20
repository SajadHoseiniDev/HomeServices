package nycto.homeservices.service;

import lombok.RequiredArgsConstructor;
import nycto.homeservices.dto.subService.SubServiceCreateDto;
import nycto.homeservices.dto.subService.SubServiceResponseDto;
import nycto.homeservices.dto.subService.SubServiceUpdateDto;
import nycto.homeservices.entity.Service;
import nycto.homeservices.entity.SubService;
import nycto.homeservices.exceptions.NotFoundException;
import nycto.homeservices.exceptions.NotValidInputException;
import nycto.homeservices.repository.SubServiceRepository;
import nycto.homeservices.util.ValidationUtil;
import nycto.homeservices.util.dtoMapper.SubServiceMapper;

import java.util.List;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class SubServiceService {
    private final SubServiceRepository subServiceRepository;
    private final SubServiceMapper subServiceMapper;
    private final ValidationUtil validationUtil;

    public SubServiceResponseDto createSubService(SubServiceCreateDto dto, Service existingService)
            throws NotValidInputException {
        if (!validationUtil.validate(dto))
            throw new NotValidInputException("Not valid sub-service data");

        SubService subService = subServiceMapper.toEntity(dto, existingService);

        return subServiceMapper.toResponseDto(subServiceRepository.save(subService));
    }

    public SubServiceResponseDto getSubServiceById(Long id)
            throws NotFoundException {

        SubService subService = subServiceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("SubService with id "
                        + id + " not found"));

        return subServiceMapper.toResponseDto(subService);
    }

    public List<SubServiceResponseDto> getAllSubServices() {
        return subServiceRepository.findAll().stream()
                .map(subServiceMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public SubServiceResponseDto updateSubService(Long id, SubServiceUpdateDto updateDto, Service existingService)
            throws NotFoundException, NotValidInputException {

        SubService existingSubService = subServiceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("SubService with id "
                        + id + " not found"));

        if (!validationUtil.validate(updateDto))
            throw new NotValidInputException("Not valid update data");

        SubService updatedSubService = subServiceMapper
                .toEntity(updateDto, existingSubService, existingService);

        updatedSubService.setId(id);

        return subServiceMapper
                .toResponseDto(subServiceRepository.save(updatedSubService));
    }


    public void deleteSubService(Long id) throws NotFoundException {

        SubService subService = subServiceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("SubService with id "
                        + id + " not found"));

        subServiceRepository.delete(subService);
    }


}
