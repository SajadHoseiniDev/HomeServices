package nycto.homeservices.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import nycto.homeservices.entity.enums.OrderStatus;

import java.time.LocalDateTime;

public record OrderDto(

        Long id
        ,
        Long proposedPrice

        ,
        String description

        ,

        LocalDateTime orderDate

        ,
        @Future(message = "executionDate must be in the future")
        LocalDateTime executionDate

        ,
        @NotEmpty
        @Size(min = 5, max = 100)
        String address

        ,
        @NotNull(message = "OrderStatus can't ne null!")
        OrderStatus status
) {
}
