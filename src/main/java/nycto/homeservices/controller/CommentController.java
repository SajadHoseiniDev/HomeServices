package nycto.homeservices.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nycto.homeservices.dto.commentDto.CommentCreateDto;
import nycto.homeservices.dto.commentDto.CommentResponseDto;
import nycto.homeservices.dto.commentDto.CommentUpdateDto;
import nycto.homeservices.service.serviceInterface.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/{id}")
    public ResponseEntity<CommentResponseDto> getCommentById(@PathVariable Long id) {
        CommentResponseDto responseDto = commentService.getCommentById(id);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping
    public ResponseEntity<List<CommentResponseDto>> getAllComments() {
        List<CommentResponseDto> comments = commentService.getAllComments();
        return ResponseEntity.ok(comments);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentResponseDto> updateComment(
            @PathVariable Long id,
            @Valid @RequestBody CommentUpdateDto updateDto) {
        CommentResponseDto responseDto = commentService.updateComment(id, updateDto);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }

}
