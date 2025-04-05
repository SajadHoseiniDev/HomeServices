package nycto.homeservices.service.serviceInterface;

import nycto.homeservices.dto.orderDto.OrderCreateDto;
import nycto.homeservices.dto.orderDto.OrderResponseDto;
import nycto.homeservices.dto.orderDto.OrderUpdateDto;
import nycto.homeservices.entity.Customer;
import nycto.homeservices.entity.Order;
import nycto.homeservices.entity.SubService;
import nycto.homeservices.entity.enums.OrderStatus;
import nycto.homeservices.exceptions.CreditException;
import nycto.homeservices.exceptions.NotFoundException;
import nycto.homeservices.exceptions.NotValidInputException;

import java.util.List;

public interface OrderService {
    OrderResponseDto createOrder(OrderCreateDto createDto, Customer customer, SubService subService)
            throws NotValidInputException, NotFoundException;

    OrderResponseDto getOrderById(Long id) throws NotFoundException;

    List<OrderResponseDto> getAllOrders();


    OrderResponseDto updateOrder(Long id, OrderUpdateDto updateDto)
            throws NotFoundException, NotValidInputException;

    void deleteOrder(Long id) throws NotFoundException;

    OrderResponseDto changeOrderStatus(Long orderId, OrderStatus newStatus) ;

    void checkStatusTransition(Order order, OrderStatus newStatus) ;

    List<OrderResponseDto> getOrdersForSpecialist(Long specialistId) ;

    List<OrderResponseDto> getOrdersForCustomer(Long customerId);
}
