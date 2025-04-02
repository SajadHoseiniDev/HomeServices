package nycto.homeservices.service;

import lombok.RequiredArgsConstructor;
import nycto.homeservices.dto.subService.SubServiceCreateDto;
import nycto.homeservices.dto.subService.SubServiceResponseDto;
import nycto.homeservices.dto.subService.SubServiceUpdateDto;
import nycto.homeservices.entity.Service;
import nycto.homeservices.entity.SubService;
import nycto.homeservices.exceptions.DuplicateDataException;
import nycto.homeservices.exceptions.NotFoundException;
import nycto.homeservices.repository.SubServiceRepository;
import nycto.homeservices.service.serviceInterface.SubServiceService;
import nycto.homeservices.dto.dtoMapper.SubServiceMapper;

import java.util.List;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class SubServiceServiceImpl implements SubServiceService {
    private final SubServiceRepository subServiceRepository;
    private final SubServiceMapper subServiceMapper;


    @Override
    public SubServiceResponseDto createSubService(SubServiceCreateDto dto, Service existingService)
            throws DuplicateDataException {

        if (existingService == null)
            throw new NotFoundException("Service not found for creating sub-service");



        if (subServiceRepository.findByName(dto.name()).isPresent())
            throw new DuplicateDataException("Service with name: "
                    + dto.name() + " already exists");



        SubService subService = subServiceMapper.toEntity(dto, existingService);

        return subServiceMapper.toResponseDto(subServiceRepository.save(subService));
    }

    @Override
    public SubServiceResponseDto getSubServiceById(Long id)
            throws NotFoundException {

        SubService subService = subServiceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("SubService with id "
                        + id + " not found"));

        return subServiceMapper.toResponseDto(subService);
    }

    @Override
    public List<SubServiceResponseDto> getAllSubServices() {
        return subServiceRepository.findAll().stream()
                .map(subServiceMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public SubServiceResponseDto updateSubService(Long id, SubServiceUpdateDto updateDto, Service existingService)
            throws NotFoundException {

        SubService existingSubService = subServiceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("SubService with id "
                        + id + " not found"));


        SubService updatedSubService = subServiceMapper
                .toEntity(updateDto, existingSubService, existingService);

        updatedSubService.setId(id);

        return subServiceMapper
                .toResponseDto(subServiceRepository.save(updatedSubService));
    }


    @Override
    public void deleteSubService(Long id) throws NotFoundException {

        SubService subService = subServiceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("SubService with id "
                        + id + " not found"));

        subServiceRepository.delete(subService);
    }


}
