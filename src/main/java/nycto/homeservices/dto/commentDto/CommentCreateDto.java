package nycto.homeservices.dto.commentDto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CommentCreateDto(
        @NotNull(message = "orderId can't be null!")
        Long orderId,

        @Size(max = 255, message = "content can't be longer than 255 characters!")
        String content,

        @NotNull(message = "rating can't be null!")
        @Min(value = 1, message = "rating must be between 1 and 5!")
        @Max(value = 5, message = "rating must be between 1 and 5!")
        Double rating
) {
}
