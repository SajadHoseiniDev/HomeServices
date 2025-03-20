package nycto.homeservices.service;

import lombok.RequiredArgsConstructor;
import nycto.homeservices.dto.subService.SubServiceCreateDto;
import nycto.homeservices.dto.subService.SubServiceResponseDto;
import nycto.homeservices.entity.SubService;
import nycto.homeservices.exceptions.NotValidInputException;
import nycto.homeservices.repository.SubServiceRepository;
import nycto.homeservices.util.ValidationUtil;
import nycto.homeservices.util.dtoMapper.SubServiceMapper;
import org.springframework.stereotype.Service;

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

}
