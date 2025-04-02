package nycto.homeservices.dto.dtoMapper;

import nycto.homeservices.dto.commentDto.CommentCreateDto;
import nycto.homeservices.dto.commentDto.CommentResponseDto;
import nycto.homeservices.entity.Comment;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {

    public Comment toEntity(CommentCreateDto createDto) {
        Comment comment = new Comment();
        comment.setComment(createDto.content());
        comment.setRating(createDto.rating());
        return comment;
    }

    public CommentResponseDto toResponseDto(Comment comment) {
        return new CommentResponseDto(
                comment.getId(),
                comment.getCustomer().getId(),
                comment.getOrder().getId(),
                comment.getComment(),
                comment.getRating()
        );
    }
}
