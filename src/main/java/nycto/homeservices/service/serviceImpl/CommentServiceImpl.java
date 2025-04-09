package nycto.homeservices.service.serviceImpl;

import lombok.RequiredArgsConstructor;
import nycto.homeservices.dto.commentDto.CommentCreateDto;
import nycto.homeservices.dto.commentDto.CommentResponseDto;
import nycto.homeservices.dto.commentDto.CommentUpdateDto;
import nycto.homeservices.dto.dtoMapper.CommentMapper;
import nycto.homeservices.entity.*;
import nycto.homeservices.entity.enums.OrderStatus;
import nycto.homeservices.exceptions.NotFoundException;
import nycto.homeservices.exceptions.NotValidInputException;
import nycto.homeservices.repository.CommentRepository;
import nycto.homeservices.repository.OrderRepository;
import nycto.homeservices.service.serviceInterface.CommentService;
import nycto.homeservices.service.serviceInterface.SpecialistService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private static final Logger logger = LoggerFactory.getLogger(CommentServiceImpl.class);

    private final CommentRepository commentRepository;

    private final CommentMapper commentMapper;
    private final SpecialistService specialistService;

    private final OrderRepository orderRepository;

    @Override
    public CommentResponseDto createComment(CommentCreateDto createDto, Long customerId)
            throws NotValidInputException, NotFoundException {

        if (createDto.rating() < 1 || createDto.rating() > 5) {
            logger.error("Invalid rating: {}. Must be between 1 and 5", createDto.rating());
            throw new NotValidInputException("Rating must be between 1 and 5");
        }

        Order order = orderRepository.findById(createDto.orderId())
                .orElseThrow(() -> {
                    logger.error("Order not found for id: {}", createDto.orderId());
                    return new NotFoundException("Order with id " + createDto.orderId() + " not found");
                });

        Customer customer = order.getCustomer();
        if (customer == null) {
            logger.error("Customer not found for id: {}", createDto.orderId());
            throw new NotFoundException("Customer not found for this order");
        }

        if (!customer.getId().equals(customerId)) {
            logger.error("CustomerId {} does not match id {}", customerId, customer.getId());
            throw new NotValidInputException("Only the customer who placed the order can add a comment");
        }

        if (order.getStatus() != OrderStatus.DONE) {
            logger.error("Order status is not DONE: {}", order.getStatus());
            throw new NotValidInputException("Order must be DONE to submit a comment");
        }

        Specialist specialist = order.getProposals().stream()
                .map(Proposal::getSpecialist)
                .findFirst()
                .orElseThrow(() -> {
                    logger.error("Specialist not found for orderId: {}", createDto.orderId());
                    return new NotFoundException("Specialist not found for this order");
                });

        Comment comment = commentMapper.toEntity(createDto);
        comment.setCustomer(customer);
        comment.setOrder(order);

        Comment savedComment = commentRepository.save(comment);
        logger.info("Comment saved successfully with id: {}", savedComment.getId());

        List<Order> completedOrders = order.getProposals().stream()
                .filter(proposal -> proposal.getSpecialist().getId().equals(specialist.getId()))
                .map(Proposal::getOrder)
                .filter(o -> o.getStatus() == OrderStatus.DONE)
                .toList();
        specialistService.calculateSpecialistScore(specialist, completedOrders);

        return commentMapper.toResponseDto(savedComment);
    }


    @Override
    public CommentResponseDto getCommentById(Long id) throws NotFoundException {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Comment with id " + id + " not found"));
        return commentMapper.toResponseDto(comment);
    }

    @Override
    public List<CommentResponseDto> getAllComments() {
        return commentRepository.findAll().stream()
                .map(commentMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentResponseDto updateComment(Long id, CommentUpdateDto updateDto) throws NotFoundException {
        Comment existingComment = commentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Comment with id " + id + " not found"));

        if (updateDto.content() != null) {
            existingComment.setComment(updateDto.content());
        }
        if (updateDto.rating() != null) {
            if (updateDto.rating() < 1 || updateDto.rating() > 5) {
                throw new NotValidInputException("Rating must be between 1 and 5");
            }
            existingComment.setRating(updateDto.rating());
        }

        Comment updatedComment = commentRepository.save(existingComment);

        Order order = updatedComment.getOrder();
        Specialist specialist = order.getProposals().stream()
                .filter(proposal -> proposal.getOrder().getStatus() == OrderStatus.DONE)
                .map(Proposal::getSpecialist)
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Specialist not found for this order"));

        List<Order> completedOrders = order.getProposals().stream()
                .filter(proposal -> proposal.getSpecialist().getId().equals(specialist.getId()))
                .map(Proposal::getOrder)
                .filter(o -> o.getStatus() == OrderStatus.DONE)
                .toList();
        specialistService.calculateSpecialistScore(specialist, completedOrders);

        return commentMapper.toResponseDto(updatedComment);
    }

    @Override
    public void deleteComment(Long id) throws NotFoundException {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Comment with id " + id + " not found"));

        Order order = comment.getOrder();
        Specialist specialist = order.getProposals().stream()
                .filter(proposal -> proposal.getOrder().getStatus() == OrderStatus.DONE)
                .map(Proposal::getSpecialist)
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Specialist not found for this order"));

        commentRepository.delete(comment);

        List<Order> completedOrders = order.getProposals().stream()
                .filter(proposal -> proposal.getSpecialist().getId().equals(specialist.getId()))
                .map(Proposal::getOrder)
                .filter(o -> o.getStatus() == OrderStatus.DONE)
                .toList();
        specialistService.calculateSpecialistScore(specialist, completedOrders);
    }


}
