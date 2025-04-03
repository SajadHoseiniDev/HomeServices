package nycto.homeservices.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nycto.homeservices.dto.orderDto.OrderCreateDto;
import nycto.homeservices.dto.orderDto.OrderResponseDto;
import nycto.homeservices.entity.Customer;
import nycto.homeservices.entity.SubService;
import nycto.homeservices.repository.CustomerRepository;
import nycto.homeservices.repository.SubServiceRepository;
import nycto.homeservices.service.serviceInterface.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final CustomerRepository customerRepository;
    private final SubServiceRepository subServiceRepository;

    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrder(
            @Valid @RequestBody OrderCreateDto createDto,
            @RequestParam Long customerId) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer with id " + customerId + " not found"));

        SubService subService = subServiceRepository.findById(createDto.subServiceId())
                .orElseThrow(() -> new RuntimeException("SubService with id " + createDto.subServiceId() + " not found"));

        OrderResponseDto responseDto = orderService.createOrder(createDto, customer, subService);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDto> getOrderById(@PathVariable Long id) {
        OrderResponseDto responseDto = orderService.getOrderById(id);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping
    public ResponseEntity<List<OrderResponseDto>> getAllOrders() {
        List<OrderResponseDto> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }


}
