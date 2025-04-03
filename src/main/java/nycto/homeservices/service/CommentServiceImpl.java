package nycto.homeservices.service;

import lombok.RequiredArgsConstructor;
import nycto.homeservices.dto.commentDto.CommentCreateDto;
import nycto.homeservices.dto.commentDto.CommentResponseDto;
import nycto.homeservices.entity.*;
import nycto.homeservices.entity.enums.OrderStatus;
import nycto.homeservices.exceptions.NotFoundException;
import nycto.homeservices.exceptions.NotValidInputException;
import nycto.homeservices.repository.CommentRepository;
import nycto.homeservices.repository.OrderRepository;
import nycto.homeservices.service.serviceInterface.CommentService;
import nycto.homeservices.service.serviceInterface.SpecialistService;
import nycto.homeservices.dto.dtoMapper.CommentMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final CommentMapper commentMapper;
    private final SpecialistService specialistService;

    private final OrderRepository orderRepository;

    @Override
    public CommentResponseDto createComment(CommentCreateDto createDto, Long customerId)
            throws NotValidInputException, NotFoundException {

        if (createDto.rating() < 1 || createDto.rating() > 5) {
            throw new NotValidInputException("Rating must be between 1 and 5");
        }

        Order order = orderRepository.findById(createDto.orderId())
                .orElseThrow(() ->
                        new NotFoundException("Order with id " + createDto.orderId() + " not found"));

        Customer customer = order.getCustomer();
        if (customer == null)
            throw new NotFoundException("Customer not found for this order");

        if (!customer.getId().equals(customerId))
            throw new NotValidInputException("Only the customer who placed the order can add a comment");


        if (order.getStatus() != OrderStatus.DONE)
            throw new NotValidInputException("Order must be DONE to submit a comment");


        Specialist specialist = order.getProposals().stream()
                .filter(proposal -> proposal.getOrder().getStatus() == OrderStatus.DONE)
                .map(Proposal::getSpecialist)
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Specialist not found for this order"));

        Comment comment = commentMapper.toEntity(createDto);
        comment.setCustomer(customer);
        comment.setOrder(order);

        Comment savedComment = commentRepository.save(comment);

        List<Order> completedOrders = order.getProposals().stream()
                .filter(proposal -> proposal.getSpecialist().getId().equals(specialist.getId()))
                .map(Proposal::getOrder)
                .filter(o -> o.getStatus() == OrderStatus.DONE)
                .toList();
        specialistService.calculateSpecialistScore(specialist, completedOrders);

        return commentMapper.toResponseDto(savedComment);
    }
}
