package nycto.homeservices.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nycto.homeservices.dto.specialistDto.SpecialistRegisterDto;
import nycto.homeservices.dto.userDto.FilteringDto;
import nycto.homeservices.dto.userDto.UserCreateDto;
import nycto.homeservices.dto.userDto.UserResponseDto;
import nycto.homeservices.entity.Specialist;
import nycto.homeservices.entity.enums.UserType;
import nycto.homeservices.repository.UserRepository;
import nycto.homeservices.service.FileUploadService;
import nycto.homeservices.service.serviceInterface.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;
    private final FileUploadService fileUploadService;

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


    @PostMapping(value = "/register/specialist", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserResponseDto> registerSpecialist(@Valid @ModelAttribute SpecialistRegisterDto registerDto) throws IOException, IOException {

        String profilePicUrl = fileUploadService.uploadFile(registerDto.getProfilePic());

        UserCreateDto createDto = new UserCreateDto(
                registerDto.getFirstName(),
                registerDto.getLastName(),
                registerDto.getEmail(),
                registerDto.getPassword(),
                UserType.SPECIALIST
        );

        UserResponseDto responseDto = userService.createUser(createDto);

        Specialist specialist = (Specialist) userRepository.findById(responseDto.id()).orElseThrow();
        specialist.setProfilePicUrl(profilePicUrl);
        userRepository.save(specialist);

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

    @GetMapping("/filter")
    public ResponseEntity<List<UserResponseDto>> filterUsers(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) UserType userType,
            @RequestParam(required = false) String serviceName,
            @RequestParam(required = false) Double rating) {
        FilteringDto filterParams = new FilteringDto
                (firstName, lastName, email, userType, serviceName, rating);
        List<UserResponseDto> users = userService.getUsersByFilter(filterParams);
        return ResponseEntity.ok(users);
    }

}
