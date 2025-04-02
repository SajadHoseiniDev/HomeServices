package nycto.homeservices.service.serviceInterface;

import nycto.homeservices.dto.proposalDto.ProposalCreateDto;
import nycto.homeservices.dto.proposalDto.ProposalResponseDto;
import nycto.homeservices.exceptions.NotFoundException;
import nycto.homeservices.exceptions.ProposalException;

public interface ProposalService {
    ProposalResponseDto createProposal(ProposalCreateDto createDto, Long specialistId)
            throws NotFoundException, ProposalException;
}
