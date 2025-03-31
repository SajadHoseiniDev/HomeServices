package nycto.homeservices.service.serviceInterface;

import nycto.homeservices.dto.commentDto.CommentCreateDto;
import nycto.homeservices.dto.commentDto.CommentResponseDto;
import nycto.homeservices.entity.Customer;
import nycto.homeservices.entity.Order;
import nycto.homeservices.entity.Specialist;
import nycto.homeservices.exceptions.NotFoundException;
import nycto.homeservices.exceptions.NotValidInputException;

public interface CommentService {

    CommentResponseDto createComment(CommentCreateDto createDto, Order order)
            throws NotValidInputException, NotFoundException;
}
