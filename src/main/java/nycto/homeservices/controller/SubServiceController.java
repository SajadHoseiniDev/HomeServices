package nycto.homeservices.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nycto.homeservices.dto.subService.SubServiceCreateDto;
import nycto.homeservices.dto.subService.SubServiceResponseDto;
import nycto.homeservices.entity.Service;
import nycto.homeservices.service.serviceInterface.ServiceService;
import nycto.homeservices.service.serviceInterface.SubServiceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subServices")
@RequiredArgsConstructor
public class SubServiceController {

    private final SubServiceService subServiceService;
    private final ServiceService serviceService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<SubServiceResponseDto> createSubService(
            @RequestParam Long serviceId,
            @Valid @RequestBody SubServiceCreateDto createDto) {

        Service existingService = serviceService.findServiceById(serviceId);

        SubServiceResponseDto responseDto = subServiceService.createSubService(createDto, existingService);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubServiceResponseDto> getSubServiceById(@PathVariable Long id) {
        SubServiceResponseDto responseDto = subServiceService.getSubServiceById(id);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping
    public ResponseEntity<List<SubServiceResponseDto>> getAllSubServices() {
        List<SubServiceResponseDto> subServices = subServiceService.getAllSubServices();
        return ResponseEntity.ok(subServices);
    }

    @GetMapping("/by-service/{serviceId}")
    public ResponseEntity<List<SubServiceResponseDto>> getSubServicesByServiceId(@PathVariable Long serviceId) {
        Service service = serviceService.findServiceById(serviceId);
        List<SubServiceResponseDto> subServices = subServiceService.getAllSubServices().stream()
                .filter(subService -> subService.serviceId().equals(serviceId))
                .toList();
        return ResponseEntity.ok(subServices);
    }

}
