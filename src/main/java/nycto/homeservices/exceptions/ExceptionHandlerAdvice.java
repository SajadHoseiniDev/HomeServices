package nycto.homeservices.exceptions;

import nycto.homeservices.dto.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler(NotValidInputException.class)
    public ResponseEntity<ErrorResponseDto> handleNotValidInputException(NotValidInputException ex) {
        ErrorResponseDto errorDto = ErrorResponseDto.builder()
                .message(ex.getMessage())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto);
    }

    @ExceptionHandler(DuplicateDataException.class)
    public ResponseEntity<ErrorResponseDto> handleDuplicateDataException(DuplicateDataException ex) {
        ErrorResponseDto errorDto = ErrorResponseDto.builder()
                .message(ex.getMessage())
                .statusCode(HttpStatus.CONFLICT.value())
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorDto);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGenericException(Exception ex) {
        ErrorResponseDto errorDto = ErrorResponseDto.builder()
                .message(ex.getMessage())
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDto);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleNotFoundException(NotFoundException ex) {
        ErrorResponseDto errorDto = ErrorResponseDto.builder()
                .message(ex.getMessage())
                .statusCode(HttpStatus.NOT_FOUND.value())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDto);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(Exception.class)public ResponseEntity<ErrorResponseDto> handleIOException(IOException ex) {
        ErrorResponseDto errorDto = ErrorResponseDto.builder()
                .message("File upload failed: " + ex.getMessage())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto);
    }

    @ExceptionHandler(EmptyFileException.class)
    public ResponseEntity<ErrorResponseDto> handleEmptyFileException(EmptyFileException ex) {
        ErrorResponseDto errorDto = ErrorResponseDto.builder()
                .message(ex.getMessage())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto);
    }
    @ExceptionHandler(InvalidFileFormatException.class)
    public ResponseEntity<ErrorResponseDto> handleInvalidFileFormatException(InvalidFileFormatException ex) {
        ErrorResponseDto errorDto = ErrorResponseDto.builder()
                .message(ex.getMessage())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto);
    }

    @ExceptionHandler(FileSizeLimitException.class)
    public ResponseEntity<ErrorResponseDto> handleFileSizeLimitExceededException(FileSizeLimitException ex) {
        ErrorResponseDto errorDto = ErrorResponseDto.builder()
                .message(ex.getMessage())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto);
    }

}