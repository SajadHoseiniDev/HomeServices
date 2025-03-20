package nycto.homeservices.service;

import lombok.RequiredArgsConstructor;
import nycto.homeservices.dto.orderDto.OrderCreateDto;
import nycto.homeservices.dto.orderDto.OrderResponseDto;
import nycto.homeservices.entity.Customer;
import nycto.homeservices.entity.Order;
import nycto.homeservices.entity.SubService;
import nycto.homeservices.entity.enums.OrderStatus;
import nycto.homeservices.exceptions.NotFoundException;
import nycto.homeservices.exceptions.NotValidInputException;
import nycto.homeservices.repository.CustomerRepository;
import nycto.homeservices.repository.OrderRepository;
import nycto.homeservices.repository.SubServiceRepository;
import nycto.homeservices.util.ValidationUtil;
import nycto.homeservices.util.dtoMapper.OrderMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final ValidationUtil validationUtil;
    private final CustomerRepository customerRepository;
    private final SubServiceRepository subServiceRepository;

    public OrderResponseDto createOrder(OrderCreateDto createDto, Long customerId)
            throws NotValidInputException, NotFoundException {
        if (!validationUtil.validate(createDto)) {
            throw new NotValidInputException("Not valid order data");
        }

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new NotFoundException("Customer with id "
                        + customerId + " not found"));

        SubService subService = subServiceRepository.findById(createDto.subServiceId())
                .orElseThrow(() -> new NotFoundException("SubService with id "
                        + createDto.subServiceId() + " not found"));

        Order order = orderMapper.toEntity(createDto);
        order.setCustomer(customer);
        order.setSubService(subService);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.WAITING_PROPOSALS);

        Order savedOrder = orderRepository.save(order);

        return orderMapper.toResponseDto(savedOrder);
    }


}
