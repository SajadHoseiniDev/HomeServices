package nycto.homeservices.service;

import lombok.RequiredArgsConstructor;
import nycto.homeservices.dto.orderDto.OrderCreateDto;
import nycto.homeservices.dto.orderDto.OrderResponseDto;
import nycto.homeservices.dto.orderDto.OrderUpdateDto;
import nycto.homeservices.entity.*;
import nycto.homeservices.entity.enums.OrderStatus;
import nycto.homeservices.exceptions.NotFoundException;
import nycto.homeservices.exceptions.NotValidInputException;
import nycto.homeservices.repository.OrderRepository;
import nycto.homeservices.service.serviceInterface.OrderService;
import nycto.homeservices.service.serviceInterface.SpecialistCreditService;
import nycto.homeservices.util.ValidationUtil;
import nycto.homeservices.util.dtoMapper.OrderMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final ValidationUtil validationUtil;
    private final SpecialistCreditService specialistCreditService;

    @Override
    public OrderResponseDto createOrder(OrderCreateDto createDto, Customer customer, SubService subService)
            throws NotValidInputException, NotFoundException {
        if (!validationUtil.validate(createDto)) {
            throw new NotValidInputException("Not valid order data");
        }

        if (customer == null) {
            throw new NotFoundException("Customer not found");
        }
        if (subService == null) {
            throw new NotFoundException("SubService not found");
        }

        Order order = orderMapper.toEntity(createDto);
        order.setCustomer(customer);
        order.setSubService(subService);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.WAITING_PROPOSALS);

        Order savedOrder = orderRepository.save(order);
        return orderMapper.toResponseDto(savedOrder);
    }

    @Override
    public OrderResponseDto getOrderById(Long id) throws NotFoundException {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order with id " + id + " not found"));
        return orderMapper.toResponseDto(order);
    }

    @Override
    public List<OrderResponseDto> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(orderMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public OrderResponseDto updateOrder(Long id, OrderUpdateDto updateDto)
            throws NotFoundException, NotValidInputException {

        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order with id "
                        + id + " not found"));

        if (!validationUtil.validate(updateDto))
            throw new NotValidInputException("Not valid update data");


        Order updatedOrder = orderMapper.toEntity(updateDto, existingOrder);
        updatedOrder.setId(id);

        Order savedOrder = orderRepository.save(updatedOrder);
        return orderMapper.toResponseDto(savedOrder);
    }

    @Override
    public void deleteOrder(Long id) throws NotFoundException {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order with id " + id + " not found"));
        orderRepository.delete(order);
    }

    @Override
    public OrderResponseDto updateOrderStatus(Long orderId, OrderStatus newStatus) throws NotFoundException {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order with id " + orderId + " not found"));

        order.setStatus(newStatus);
        if (newStatus == OrderStatus.STARTED)
            order.setExecutionDate(LocalDateTime.now());

        if (newStatus == OrderStatus.PAID) {
            Proposal selectedProposal = order.getProposals().stream()
                    .filter(proposal -> proposal.getOrder().getStatus() == OrderStatus.DONE)
                    .findFirst()
                    .orElseThrow(() -> new NotFoundException("No proposal found for this order"));
            Specialist specialist = selectedProposal.getSpecialist();
            specialistCreditService.increaseSpecialistCredit(specialist,1000L);
        }

        Order updatedOrder = orderRepository.save(order);
        return orderMapper.toResponseDto(updatedOrder);
    }

}
