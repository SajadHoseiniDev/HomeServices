package nycto.homeservices.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nycto.homeservices.dto.userDto.UserCreateDto;
import nycto.homeservices.dto.userDto.UserResponseDto;
import nycto.homeservices.entity.enums.UserType;
import nycto.homeservices.service.serviceInterface.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register/customer")
    public ResponseEntity<UserResponseDto> registerCustomer(@Valid @RequestBody UserCreateDto createDto) {
        createDto = new UserCreateDto(
                createDto.firstName(),
                createDto.lastName(),
                createDto.email(),
                createDto.password(),
                UserType.CUSTOMER
        );
        UserResponseDto userResponseDto = userService.createUser(createDto);
        return new ResponseEntity<>(userResponseDto, HttpStatus.CREATED);
    }

    @PostMapping("/register/specialist")
    public ResponseEntity<UserResponseDto> registerSpecialist(@Valid @RequestBody UserCreateDto createDto)
             {
        createDto = new UserCreateDto(
                createDto.firstName(),
                createDto.lastName(),
                createDto.email(),
                createDto.password(),
                UserType.SPECIALIST
        );
        UserResponseDto responseDto = userService.createUser(createDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @PostMapping("/register/admin")
    public ResponseEntity<UserResponseDto> registerAdmin(@Valid @RequestBody UserCreateDto createDto) {
        createDto = new UserCreateDto(
                createDto.firstName(),
                createDto.lastName(),
                createDto.email(),
                createDto.password(),
                UserType.ADMIN
        );
        UserResponseDto responseDto = userService.createUser(createDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

}
