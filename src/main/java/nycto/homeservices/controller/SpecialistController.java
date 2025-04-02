package nycto.homeservices.controller;

import lombok.RequiredArgsConstructor;
import nycto.homeservices.entity.Service;
import nycto.homeservices.service.serviceInterface.ServiceService;
import nycto.homeservices.service.serviceInterface.SpecialistService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/specialists")
@RequiredArgsConstructor
public class SpecialistController {
    private final SpecialistService specialistService;
    private final ServiceService serviceService;

    @PostMapping("/{specialistId}/services/{serviceId}")
    public ResponseEntity<Void> addServiceToSpecialist(
            @PathVariable Long specialistId,
            @PathVariable Long serviceId) {
        Service service = serviceService.findServiceById(serviceId);
        specialistService.addServiceToSpecialist(specialistId, service);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{specialistId}/services/{serviceId}")
    public ResponseEntity<Void> removeServiceFromSpecialist(
            @PathVariable Long specialistId,
            @PathVariable Long serviceId) {
        Service service = serviceService.findServiceById(serviceId);
        specialistService.removeServiceFromSpecialist(specialistId, service);
        return ResponseEntity.ok().build();
    }
}
