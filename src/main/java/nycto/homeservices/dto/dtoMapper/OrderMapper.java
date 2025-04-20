package nycto.homeservices.dto.dtoMapper;

import nycto.homeservices.dto.orderDto.OrderCreateDto;
import nycto.homeservices.dto.orderDto.OrderResponseDto;
import nycto.homeservices.dto.orderDto.OrderUpdateDto;
import nycto.homeservices.entity.Order;
import nycto.homeservices.entity.Proposal;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class OrderMapper {

    public Order toEntity(OrderCreateDto createDto) {
        Order order = new Order();
        order.setDescription(createDto.description());
        order.setProposedPrice(createDto.proposedPrice());
        order.setAddress(createDto.address());
        order.setExecutionDate(createDto.executionDate());
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
        Proposal selectedProposal = order.getSelectedProposal();
        Long specialistId = (selectedProposal != null && selectedProposal.getSpecialist() != null)
                ? selectedProposal.getSpecialist().getId()
                : null;

        Long customerId = (order.getCustomer() != null) ? order.getCustomer().getId() : null;
        String customerFirstName = (order.getCustomer() != null) ? order.getCustomer().getFirstName() : null;
        String customerLastName = (order.getCustomer() != null) ? order.getCustomer().getLastName() : null;
        Long subServiceId = (order.getSubService() != null) ? order.getSubService().getId() : null;
        String subServiceName = (order.getSubService() != null) ? order.getSubService().getName() : null;

        return new OrderResponseDto(
                order.getId(),
                customerId,
                customerFirstName,
                customerLastName,
                subServiceId,
                subServiceName,
                specialistId,
                order.getDescription(),
                order.getProposedPrice(),
                order.getAddress(),
                order.getOrderDate(),
                order.getExecutionDate(),
                order.getStatus()
        );
    }
}