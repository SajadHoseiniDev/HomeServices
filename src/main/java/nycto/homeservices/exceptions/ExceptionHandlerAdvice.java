package nycto.homeservices.exceptions;

import nycto.homeservices.dto.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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
}