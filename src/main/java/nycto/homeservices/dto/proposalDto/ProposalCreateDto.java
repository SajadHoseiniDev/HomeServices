package nycto.homeservices.dto.proposalDto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ProposalCreateDto(
        @NotNull(message = "orderId can't be null!")
        Long orderId,

        @NotNull(message = "proposedPrice can't be null!")
        @Min(value = 0, message = "proposedPrice must be non-negative!")
        Long proposedPrice,

        @NotEmpty(message = "duration can't be empty!")
        String duration,

        @NotNull(message = "startTime can't be null!")
        LocalDateTime startTime
) {
}
