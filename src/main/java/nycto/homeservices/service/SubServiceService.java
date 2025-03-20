package nycto.homeservices.service;

import lombok.RequiredArgsConstructor;
import nycto.homeservices.dto.subService.SubServiceCreateDto;
import nycto.homeservices.dto.subService.SubServiceResponseDto;
import nycto.homeservices.entity.SubService;
import nycto.homeservices.exceptions.NotFoundException;
import nycto.homeservices.exceptions.NotValidInputException;
import nycto.homeservices.repository.SubServiceRepository;
import nycto.homeservices.util.ValidationUtil;
import nycto.homeservices.util.dtoMapper.SubServiceMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubServiceService {
    private final SubServiceRepository subServiceRepository;
    private final SubServiceMapper subServiceMapper;
    private final ValidationUtil validationUtil;

    public SubServiceResponseDto createSubService(SubServiceCreateDto dto, nycto.homeservices.entity.Service excitingService)
            throws NotValidInputException {
        if (!validationUtil.validate(dto))
            throw new NotValidInputException("Not valid sub-service data");

        SubService subService = subServiceMapper.toEntity(dto, excitingService);

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

}
