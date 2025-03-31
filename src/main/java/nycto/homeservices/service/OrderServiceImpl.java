package nycto.homeservices.service;

import lombok.RequiredArgsConstructor;
import nycto.homeservices.dto.orderDto.OrderCreateDto;
import nycto.homeservices.dto.orderDto.OrderResponseDto;
import nycto.homeservices.dto.orderDto.OrderUpdateDto;
import nycto.homeservices.entity.*;
import nycto.homeservices.entity.enums.OrderStatus;
import nycto.homeservices.exceptions.CreditException;
import nycto.homeservices.exceptions.NotFoundException;
import nycto.homeservices.exceptions.NotValidInputException;
import nycto.homeservices.repository.OrderRepository;
import nycto.homeservices.service.serviceInterface.CustomerCreditService;
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
    private final CustomerCreditService customerCreditService;

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
    public OrderResponseDto changeOrderStatus(Long orderId, OrderStatus newStatus) throws NotFoundException, CreditException, NotValidInputException {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order with id " + orderId + " not found"));

        checkStatusTransition(order, newStatus);

        order.setStatus(newStatus);
        if (newStatus == OrderStatus.STARTED) {
            order.setExecutionDate(LocalDateTime.now());
        }
        if (newStatus == OrderStatus.PAID) {
            if (order.getSelectedProposal() == null) {
                throw new NotFoundException("No selected proposal found for this order");
            }
            Specialist specialist = order.getSelectedProposal().getSpecialist();

            Long orderAmount = order.getProposedPrice();
            if (orderAmount == null || orderAmount <= 0) {
                throw new NotValidInputException("Order amount must be positive");
            }
            customerCreditService.decreaseCustomerCredit(order.getCustomer(), orderAmount);

            specialistCreditService.increaseSpecialistCredit(specialist, 1000L);
        }

        Order updatedOrder = orderRepository.save(order);
        return orderMapper.toResponseDto(updatedOrder);
    }

    @Override
    public void checkStatusTransition(Order order, OrderStatus newStatus) throws NotValidInputException {
        OrderStatus currentStatus = order.getStatus();
        switch (currentStatus) {
            case WAITING_PROPOSALS:
                if (newStatus != OrderStatus.WAITING_SPECIALIST) {
                    throw new NotValidInputException("Order must transition from WAITING_PROPOSALS to WAITING_SPECIALIST");
                }
                if (order.getProposals().isEmpty()) {
                    throw new NotValidInputException("At least one proposal must exist to move to WAITING_SPECIALIST");
                }
                break;
            case WAITING_SPECIALIST:
                if (newStatus != OrderStatus.WAITING_ARRIVAL) {
                    throw new NotValidInputException("Order must transition from WAITING_SPECIALIST to WAITING_ARRIVAL");
                }
                if (order.getSelectedProposal() == null) {
                    throw new NotValidInputException("A proposal must be selected to move to WAITING_ARRIVAL");
                }
                break;
            case WAITING_ARRIVAL:
                if (newStatus != OrderStatus.STARTED) {
                    throw new NotValidInputException("Order must transition from WAITING_ARRIVAL to STARTED");
                }
                break;
            case STARTED:
                if (newStatus != OrderStatus.DONE) {
                    throw new NotValidInputException("Order must transition from STARTED to DONE");
                }
                break;
            case DONE:
                if (newStatus != OrderStatus.PAID) {
                    throw new NotValidInputException("Order must transition from DONE to PAID");
                }
                break;
            case PAID:
                throw new NotValidInputException("Order is already PAID and cannot be updated");
            default:
                throw new NotValidInputException("Invalid current status: " + currentStatus);
        }
    }





}
