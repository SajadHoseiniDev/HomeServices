package nycto.homeservices.dto.commentDto;

public record CommentResponseDto(
        Long id,
        Long customerId,
        Long orderId,
        String content,
        Integer rating
) {
}
