package nycto.homeservices.exceptions;

import nycto.homeservices.dto.ErrorResponseDto;
import org.hibernate.TypeMismatchException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ExceptionHandlerAdvice {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandlerAdvice.class);


    @ExceptionHandler(NotValidInputException.class)
    public ResponseEntity<ErrorResponseDto> handleNotValidInputException(NotValidInputException ex) {
        logger.error("Not valid error: {}", ex.getMessage());
        ErrorResponseDto errorDto = ErrorResponseDto.builder()
                .message(ex.getMessage())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto);
    }

    @ExceptionHandler(DuplicateDataException.class)
    public ResponseEntity<ErrorResponseDto> handleDuplicateDataException(DuplicateDataException ex) {
        logger.error("Duplicate data: {}", ex.getMessage());
        ErrorResponseDto errorDto = ErrorResponseDto.builder()
                .message(ex.getMessage())
                .statusCode(HttpStatus.CONFLICT.value())
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorDto);
    }


    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleNotFoundException(NotFoundException ex) {
        logger.error("Not found error: {}", ex.getMessage());
        ErrorResponseDto errorDto = ErrorResponseDto.builder()
                .message(ex.getMessage())
                .statusCode(HttpStatus.NOT_FOUND.value())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDto);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        logger.error("Dto Validation error: {}", ex.getMessage());
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(Exception.class)public ResponseEntity<ErrorResponseDto> handleIOException(IOException ex) {
            logger.error("An Exception type error: {}", ex.getMessage());
        ErrorResponseDto errorDto = ErrorResponseDto.builder()
                .message("File upload failed: " + ex.getMessage())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto);
    }

    @ExceptionHandler(EmptyFileException.class)
    public ResponseEntity<ErrorResponseDto> handleEmptyFileException(EmptyFileException ex) {
        logger.error("your file is empty: {}", ex.getMessage());
        ErrorResponseDto errorDto = ErrorResponseDto.builder()
                .message(ex.getMessage())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto);
    }
    @ExceptionHandler(InvalidFileFormatException.class)
    public ResponseEntity<ErrorResponseDto> handleInvalidFileFormatException(InvalidFileFormatException ex) {
        logger.error("your file is invalid type: {}", ex.getMessage());
        ErrorResponseDto errorDto = ErrorResponseDto.builder()
                .message(ex.getMessage())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto);
    }

    @ExceptionHandler(FileSizeLimitException.class)
    public ResponseEntity<ErrorResponseDto> handleFileSizeLimitExceededException(FileSizeLimitException ex) {
        logger.error("you reach the size limit: {}", ex.getMessage());
        ErrorResponseDto errorDto = ErrorResponseDto.builder()
                .message(ex.getMessage())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto);
    }

    @ExceptionHandler(TypeMismatchException.class)
    public ResponseEntity<ErrorResponseDto> handleTypeMismatchException(TypeMismatchException ex) {
        ErrorResponseDto errorDto = ErrorResponseDto.builder()
                .message("Invalid parameter type: " + ex.getMessage())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto);
    }
    @ExceptionHandler(InvalidDataAccessResourceUsageException.class)
    public ResponseEntity<ErrorResponseDto> handleInvalidDataAccessResourceUsageException(InvalidDataAccessResourceUsageException ex) {
        ErrorResponseDto errorDto = ErrorResponseDto.builder()
                .message("Database query error: " + ex.getMessage())
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDto);
    }
    @ExceptionHandler(JpaSystemException.class)
    public ResponseEntity<ErrorResponseDto> handleJpaSystemException(JpaSystemException ex) {
        ErrorResponseDto errorDto = ErrorResponseDto.builder()
                .message("Database error: " + ex.getMessage())
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDto);
    }

}