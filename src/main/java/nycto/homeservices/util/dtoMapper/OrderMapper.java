package nycto.homeservices.util.dtoMapper;

import nycto.homeservices.dto.orderDto.OrderCreateDto;
import nycto.homeservices.dto.orderDto.OrderResponseDto;
import nycto.homeservices.entity.Order;

public class OrderMapper {

    public Order toEntity(OrderCreateDto createDto) {
        Order order = new Order();
        order.setDescription(createDto.description());
        order.setProposedPrice(createDto.proposedPrice());
        order.setAddress(createDto.address());
        return order;
    }

    public OrderResponseDto toResponseDto(Order order) {
        return new OrderResponseDto(
                order.getId(),
                order.getCustomer().getId(),
                order.getSubService().getId(),
                order.getDescription(),
                order.getProposedPrice(),
                order.getAddress(),
                order.getOrderDate(),
                order.getExecutionDate(),
                order.getStatus()
        );
    }

}
