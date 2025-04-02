package nycto.homeservices.service;

import lombok.RequiredArgsConstructor;
import nycto.homeservices.dto.proposalDto.ProposalCreateDto;
import nycto.homeservices.dto.proposalDto.ProposalResponseDto;
import nycto.homeservices.entity.Order;
import nycto.homeservices.entity.Proposal;
import nycto.homeservices.entity.Specialist;
import nycto.homeservices.entity.enums.OrderStatus;
import nycto.homeservices.exceptions.NotFoundException;
import nycto.homeservices.exceptions.ProposalException;
import nycto.homeservices.repository.OrderRepository;
import nycto.homeservices.repository.ProposalRepository;
import nycto.homeservices.repository.SpecialistRepository;
import nycto.homeservices.dto.dtoMapper.ProposalMapper;
import nycto.homeservices.service.serviceInterface.ProposalService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ProposalServiceImpl implements ProposalService {
    private final ProposalRepository proposalRepository;
    private final ProposalMapper proposalMapper;


    private final SpecialistRepository specialistRepository;
    private final OrderRepository orderRepository;

    @Override
    public ProposalResponseDto createProposal(ProposalCreateDto createDto, Long specialistId)
            throws NotFoundException, ProposalException {


        Specialist specialist = specialistRepository.findById(specialistId)
                .orElseThrow(() -> new NotFoundException("Specialist with id "
                        + specialistId + " not found"));

        Order order = orderRepository.findById(createDto.orderId())
                .orElseThrow(() -> new NotFoundException("Order with id "
                        + createDto.orderId() + " not found"));


        if (order.getStatus() != OrderStatus.WAITING_PROPOSALS) {
            throw new ProposalException("Order is not waiting for a new proposal!");
        }


        if (!specialist.getServices().contains(order.getSubService().getService())) {
            throw new ProposalException
                    ("You can't create a proposal for this service!" + order.getSubService().getService());
        }

        Proposal proposal = proposalMapper.toEntity(createDto);
        proposal.setSpecialist(specialist);
        proposal.setOrder(order);
        proposal.setProposalDate(LocalDateTime.now());

        Proposal savedProposal = proposalRepository.save(proposal);
        return proposalMapper.toResponseDto(savedProposal);
    }

}
