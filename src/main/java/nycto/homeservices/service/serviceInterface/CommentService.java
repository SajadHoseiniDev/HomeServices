package nycto.homeservices.service.serviceInterface;

import nycto.homeservices.dto.commentDto.CommentCreateDto;
import nycto.homeservices.dto.commentDto.CommentResponseDto;
import nycto.homeservices.dto.commentDto.CommentUpdateDto;
import nycto.homeservices.entity.Customer;
import nycto.homeservices.entity.Order;
import nycto.homeservices.entity.Specialist;
import nycto.homeservices.exceptions.NotFoundException;
import nycto.homeservices.exceptions.NotValidInputException;

import java.util.List;

public interface CommentService {


    CommentResponseDto createComment(CommentCreateDto createDto, Long customerId)
            throws NotValidInputException, NotFoundException;

    CommentResponseDto getCommentById(Long id) throws NotFoundException;

    List<CommentResponseDto> getAllComments();

    CommentResponseDto updateComment(Long id, CommentUpdateDto updateDto) throws NotFoundException;

    void deleteComment(Long id) throws NotFoundException;
}
