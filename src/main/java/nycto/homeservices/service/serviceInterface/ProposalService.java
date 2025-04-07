package nycto.homeservices.service.serviceInterface;

import nycto.homeservices.dto.proposalDto.ProposalCreateDto;
import nycto.homeservices.dto.proposalDto.ProposalResponseDto;
import nycto.homeservices.exceptions.NotFoundException;
import nycto.homeservices.exceptions.ProposalException;

import java.util.List;

public interface ProposalService {
    ProposalResponseDto createProposal(ProposalCreateDto createDto, Long specialistId)
            throws NotFoundException, ProposalException;

    List<ProposalResponseDto> getProposalsByOrderId(Long orderId) throws NotFoundException;

}
