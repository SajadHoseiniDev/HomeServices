package nycto.homeservices.dto.dtoMapper;

import nycto.homeservices.dto.proposalDto.ProposalCreateDto;
import nycto.homeservices.dto.proposalDto.ProposalResponseDto;
import nycto.homeservices.entity.Proposal;
import org.springframework.stereotype.Component;

@Component
public class ProposalMapper {
    public Proposal toEntity(ProposalCreateDto createDto) {
        Proposal proposal = new Proposal();
        proposal.setProposedPrice(createDto.proposedPrice());
        proposal.setDuration(createDto.duration());
        proposal.setStartTime(createDto.startTime());
        return proposal;
    }

    public ProposalResponseDto toResponseDto(Proposal proposal) {
        return new ProposalResponseDto(
                proposal.getId(),
                proposal.getSpecialist().getId(),
                proposal.getOrder().getId(),
                proposal.getProposedPrice(),
                proposal.getDuration(),
                proposal.getStartTime()
        );
    }
}
