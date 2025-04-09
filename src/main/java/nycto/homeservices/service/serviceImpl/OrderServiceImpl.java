package nycto.homeservices.service.serviceImpl;

import lombok.RequiredArgsConstructor;
import nycto.homeservices.dto.dtoMapper.OrderMapper;
import nycto.homeservices.dto.orderDto.OrderCreateDto;
import nycto.homeservices.dto.orderDto.OrderResponseDto;
import nycto.homeservices.dto.orderDto.OrderUpdateDto;
import nycto.homeservices.entity.*;
import nycto.homeservices.entity.enums.OrderStatus;
import nycto.homeservices.exceptions.NotFoundException;
import nycto.homeservices.exceptions.NotValidInputException;
import nycto.homeservices.repository.OrderRepository;
import nycto.homeservices.repository.ProposalRepository;
import nycto.homeservices.repository.SpecialistRepository;
import nycto.homeservices.service.serviceInterface.CustomerCreditService;
import nycto.homeservices.service.serviceInterface.OrderService;
import nycto.homeservices.service.serviceInterface.SpecialistCreditService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    private final SpecialistCreditService specialistCreditService;
    private final CustomerCreditService customerCreditService;
    private final SpecialistRepository specialistRepository;
    private final ProposalRepository proposalRepository;


    @Override
    public OrderResponseDto createOrder(OrderCreateDto createDto, Customer customer, SubService subService)
            throws NotFoundException {


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
    public OrderResponseDto getOrderById(Long id) {
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
    public OrderResponseDto updateOrder(Long id, OrderUpdateDto updateDto) {

        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order with id "
                        + id + " not found"));


        Order updatedOrder = orderMapper.toEntity(updateDto, existingOrder);
        updatedOrder.setId(id);


        Order savedOrder = orderRepository.save(updatedOrder);
        return orderMapper.toResponseDto(savedOrder);
    }

    @Override
    public void deleteOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order with id " + id + " not found"));
        orderRepository.delete(order);
    }

    @Override
    public OrderResponseDto changeOrderStatus(Long orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->{
                        logger.info("Changing order status for id: {} to {}", orderId, newStatus);
                        return new NotFoundException("Order with id " + orderId + " not found");
                });


        checkStatusTransition(order, newStatus);

        order.setStatus(newStatus);
        if (newStatus == OrderStatus.STARTED) {
            logger.error("No selected proposal found for orderId: {}", orderId);
            order.setExecutionDate(LocalDateTime.now());
        }
        if (newStatus == OrderStatus.PAID) {
            if (order.getSelectedProposal() == null) {
                logger.error("No selected proposal found for orderId: {}", orderId);
                throw new NotFoundException("No selected proposal found for this order");
            }
            Specialist specialist = order.getSelectedProposal().getSpecialist();

            Long orderAmount = order.getProposedPrice();
            if (orderAmount == null || orderAmount <= 0) {
                logger.error("Invalid order amount: {} for orderId: {}", orderAmount, orderId);
                throw new NotValidInputException("Order amount must be positive");
            }
            customerCreditService.decreaseCustomerCredit(order.getCustomer(), orderAmount);

            specialistCreditService.increaseSpecialistCredit(specialist, 1000L);
        }

        Order updatedOrder = orderRepository.save(order);
        logger.info("Order status updated successfully for orderId: {}", orderId);
        return orderMapper.toResponseDto(updatedOrder);
    }

    @Override
    public void checkStatusTransition(Order order, OrderStatus newStatus) {
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



    @Override
    public List<OrderResponseDto> getOrdersForSpecialist(Long specialistId) {
        Specialist specialist = specialistRepository.findById(specialistId)
                .orElseThrow(() ->
                        new NotFoundException("Specialist with id " + specialistId + " not found"));

        List<nycto.homeservices.entity.Service> specialistServices = specialist.getServices();

        return orderRepository.findAll().stream()
                .filter(order -> specialistServices.contains(order.getSubService().getService()))
                .map(orderMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderResponseDto> getOrdersForCustomer(Long customerId) {
        return orderRepository.findByCustomerId(customerId).stream()
                .map(orderMapper::toResponseDto)
                .collect(Collectors.toList());
    }


    @Override
    public OrderResponseDto selectProposal(Long orderId, Long proposalId) throws NotFoundException {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order with id " + orderId + " not found"));

        Proposal proposal = proposalRepository.findById(proposalId)
                .orElseThrow(() -> new NotFoundException("Proposal with id " + proposalId + " not found"));

        if (!proposal.getOrder().getId().equals(orderId)) {
            throw new NotValidInputException("Proposal does not belong to this order");
        }

        order.setSelectedProposal(proposal);
        checkStatusTransition(order, OrderStatus.WAITING_SPECIALIST);
        order.setStatus(OrderStatus.WAITING_SPECIALIST);

        Order updatedOrder = orderRepository.save(order);
        return orderMapper.toResponseDto(updatedOrder);
    }

    @Override
    public OrderResponseDto confirmProposalBySpecialist(Long orderId, Long specialistId) throws NotFoundException {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order with id " + orderId + " not found"));

        if (order.getStatus() != OrderStatus.WAITING_SPECIALIST) {
            throw new NotValidInputException("Order must be in WAITING_SPECIALIST status to confirm proposal");
        }

        Proposal selectedProposal = order.getSelectedProposal();
        if (selectedProposal == null) {
            throw new NotFoundException("No selected proposal found for this order");
        }

        if (!selectedProposal.getSpecialist().getId().equals(specialistId)) {
            throw new NotValidInputException("Only the selected specialist can confirm this proposal");
        }

        checkStatusTransition(order, OrderStatus.WAITING_ARRIVAL);
        order.setStatus(OrderStatus.WAITING_ARRIVAL);

        Order updatedOrder = orderRepository.save(order);
        return orderMapper.toResponseDto(updatedOrder);
    }


    @Override
    public List<OrderResponseDto> getAvailableOrdersToPayment(Long customerId) {

        if (customerId == null || customerId <= 0) {
            throw new NotValidInputException("customerId must be a positive number");
        }

        return orderRepository.findByCustomerIdAndStatus(customerId, OrderStatus.DONE).stream()
                .map(orderMapper::toResponseDto)
                .collect(Collectors.toList());
    }

}
