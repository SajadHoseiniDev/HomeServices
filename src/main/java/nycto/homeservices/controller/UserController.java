package nycto.homeservices.controller;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nycto.homeservices.dto.specialistDto.SpecialistRegisterDto;
import nycto.homeservices.dto.userDto.*;
import nycto.homeservices.entity.ActivationToken;
import nycto.homeservices.entity.Specialist;
import nycto.homeservices.entity.User;
import nycto.homeservices.entity.enums.UserStatus;
import nycto.homeservices.entity.enums.UserType;
import nycto.homeservices.exceptions.NotFoundException;
import nycto.homeservices.exceptions.NotValidInputException;
import nycto.homeservices.repository.UserRepository;
import nycto.homeservices.service.serviceImpl.FileUploadServiceImpl;
import nycto.homeservices.service.serviceInterface.ActivationTokenService;
import nycto.homeservices.service.serviceInterface.UserService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;
    private final FileUploadServiceImpl fileUploadService;
    private final ActivationTokenService tokenService;

    @PostMapping("/register/customer")
    public ResponseEntity<UserResponseDto> registerCustomer(@Valid @RequestBody UserCreateDto createDto) throws MessagingException {
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
    public ResponseEntity<UserResponseDto> registerSpecialist(@Valid @ModelAttribute SpecialistRegisterDto registerDto) throws IOException, IOException, MessagingException {

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
    public ResponseEntity<UserResponseDto> registerAdmin(@Valid @RequestBody UserCreateDto createDto) throws MessagingException {
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

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {
        UserResponseDto responseDto = userService.getUserById(id);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        List<UserResponseDto> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }


    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserUpdateDto updateDto) {
        UserResponseDto responseDto = userService.updateUser(id, updateDto);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{userId}/history")
    public ResponseEntity<UserHistoryDto> getUserHistory(
            @PathVariable Long userId,
            @RequestParam String userType) {
        UserHistoryDto history = userService.getUserHistory(userId, userType);
        return ResponseEntity.ok(history);
    }

    @PutMapping("/{id}/change-password")
    public ResponseEntity<Void> changePassword(
            @PathVariable Long id,
            @Valid @RequestBody ChangeUserPasswordDto passwordDto) {
        userService.changePassword(id, passwordDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userId}/admin-history")
    public ResponseEntity<UserHistoryDto> getUserHistoryForAdmin(
            @PathVariable Long userId,
            @RequestParam String userType,
            @RequestParam Long adminId) {

        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new NotFoundException("Admin with id " + adminId + " not found"));
        if (admin.getUserType() != UserType.ADMIN) {
            throw new NotValidInputException("Only admins can access user history");
        }

        UserHistoryDto history = userService.getUserHistory(userId, userType);
        return ResponseEntity.ok(history);
    }


    @GetMapping("/admin/customer-report")
    public ResponseEntity<List<UserReportDto>> getCustomerReport(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam Long adminId) {
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new NotFoundException("Admin with id " + adminId + " not found"));
        if (admin.getUserType() != UserType.ADMIN) {
            throw new NotValidInputException("Only admins can access this endpoint");
        }

        List<UserReportDto> report = userService.getCustomerReport(startDate, endDate);
        return ResponseEntity.ok(report);
    }

    @GetMapping("/admin/specialist-report")
    public ResponseEntity<List<UserReportDto>> getSpecialistReport(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam Long adminId) {
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new NotFoundException("Admin with id " + adminId + " not found"));
        if (admin.getUserType() != UserType.ADMIN) {
            throw new NotValidInputException("Only admins can access this endpoint");
        }

        List<UserReportDto> report = userService.getSpecialistReport(startDate, endDate);
        return ResponseEntity.ok(report);
    }

    @GetMapping("/activate")
    public ResponseEntity<String> activateAccount(@RequestParam("token") String token) {

        ActivationToken activationToken = tokenService.validateToken(token);


        User user = activationToken.getUser();
        if (user.getUserType() == UserType.SPECIALIST) {
            user.setStatus(UserStatus.PENDING_APPROVAL);
        } else {
            user.setStatus(UserStatus.APPROVED);
        }
        userRepository.save(user);


        tokenService.markTokenAsUsed(activationToken);


        String message = user.getUserType() == UserType.SPECIALIST
                ? "Your account has been successfully activated. Please wait for the administrator's approval"
                : "your account has been successfully activated!";
        return ResponseEntity.ok(message);
    }



}
