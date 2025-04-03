package nycto.homeservices.dto.dtoMapper;

import nycto.homeservices.dto.orderDto.OrderCreateDto;
import nycto.homeservices.dto.orderDto.OrderResponseDto;
import nycto.homeservices.dto.orderDto.OrderUpdateDto;
import nycto.homeservices.entity.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class OrderMapper {

    public Order toEntity(OrderCreateDto createDto) {
        Order order = new Order();
        order.setDescription(createDto.description());
        order.setProposedPrice(createDto.proposedPrice());
        order.setAddress(createDto.address());
        order.setExecutionDate(LocalDateTime.now().plusDays(1));
        return order;
    }

    public Order toEntity(OrderUpdateDto updateDto, Order existingOrder) {
        if (updateDto.description() != null) {
            existingOrder.setDescription(updateDto.description());
        }
        if (updateDto.proposedPrice() != null) {
            existingOrder.setProposedPrice(updateDto.proposedPrice());
        }
        if (updateDto.address() != null) {
            existingOrder.setAddress(updateDto.address());
        }
        return existingOrder;
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