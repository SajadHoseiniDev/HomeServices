package nycto.homeservices.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nycto.homeservices.dto.orderDto.OrderCreateDto;
import nycto.homeservices.dto.orderDto.OrderResponseDto;
import nycto.homeservices.dto.payment.PaymentRequestDto;
import nycto.homeservices.dto.payment.PaymentResponseDto;
import nycto.homeservices.entity.Customer;
import nycto.homeservices.entity.SubService;
import nycto.homeservices.entity.User;
import nycto.homeservices.entity.enums.OrderStatus;
import nycto.homeservices.entity.enums.UserType;
import nycto.homeservices.exceptions.NotFoundException;
import nycto.homeservices.exceptions.NotValidInputException;
import nycto.homeservices.repository.AdminRepository;
import nycto.homeservices.repository.CustomerRepository;
import nycto.homeservices.repository.SubServiceRepository;
import nycto.homeservices.service.serviceInterface.OrderService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final CustomerRepository customerRepository;
    private final SubServiceRepository subServiceRepository;
    private final AdminRepository adminRepository;

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

    @GetMapping("/for-specialist")
    public ResponseEntity<List<OrderResponseDto>> getOrdersForSpecialist(@RequestParam Long specialistId) {
        List<OrderResponseDto> orders = orderService.getOrdersForSpecialist(specialistId);
        return ResponseEntity.ok(orders);
    }


    @PutMapping("/{orderId}/select-proposal")
    public ResponseEntity<OrderResponseDto> selectProposal(
            @PathVariable Long orderId,
            @RequestParam Long proposalId) {
        OrderResponseDto responseDto = orderService.selectProposal(orderId, proposalId);
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<OrderResponseDto> changeOrderStatus(
            @PathVariable Long id,
            @RequestParam OrderStatus status) {
        OrderResponseDto responseDto = orderService.changeOrderStatus(id, status);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/available-for-payment")
    public ResponseEntity<List<OrderResponseDto>> getOrdersAvailableForPayment(@RequestParam Long customerId) {
        List<OrderResponseDto> orders = orderService.getAvailableOrdersToPayment(customerId);
        return ResponseEntity.ok(orders);
    }
    @PostMapping("/{orderId}/start-payment")
    public ResponseEntity<String> startPayment(
            @PathVariable Long orderId,
            @RequestParam Long customerId) {
        String paymentToken = orderService.startPayment(orderId, customerId);
        return ResponseEntity.ok(paymentToken);
    }

    @PostMapping("/{orderId}/pay")
    public ResponseEntity<PaymentResponseDto> payOrder(
            @PathVariable Long orderId,
            @RequestParam Long customerId,
            @RequestParam String paymentToken,
            @Valid @RequestBody PaymentRequestDto paymentRequest) {
        PaymentResponseDto response = orderService.payOrder(orderId, customerId, paymentRequest, paymentToken);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/admin-orders")
    public ResponseEntity<List<OrderResponseDto>> getOrdersByFilters(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(required = false) OrderStatus status,
            @RequestParam(required = false) Long serviceId,
            @RequestParam(required = false) Long subServiceId,
            @RequestParam Long adminId) {
        User admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new NotFoundException("Admin with id " + adminId + " not found"));
        if (admin.getUserType() != UserType.ADMIN) {
            throw new NotValidInputException("Only admins can access this endpoint");
        }

        List<OrderResponseDto> orders = orderService.getOrdersByFilters(startDate, endDate, status, serviceId, subServiceId);
        return ResponseEntity.ok(orders);
    }



}
