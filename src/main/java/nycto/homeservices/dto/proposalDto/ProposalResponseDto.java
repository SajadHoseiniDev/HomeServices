package nycto.homeservices.dto.proposalDto;

import java.time.LocalDateTime;

public record ProposalResponseDto(
        Long id,
        Long specialistId,
        Long orderId,
        Long proposedPrice,
        String duration,
        LocalDateTime startTime
) {
}
