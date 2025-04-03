package nycto.homeservices.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nycto.homeservices.dto.commentDto.CommentCreateDto;
import nycto.homeservices.dto.commentDto.CommentResponseDto;
import nycto.homeservices.service.serviceInterface.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentResponseDto> createComment(
            @Valid @RequestBody CommentCreateDto createDto,
            @RequestParam Long customerId) {
        CommentResponseDto responseDto = commentService.createComment(createDto, customerId);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

}
