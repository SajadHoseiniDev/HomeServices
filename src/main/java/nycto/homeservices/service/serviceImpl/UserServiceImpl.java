package nycto.homeservices.service.serviceImpl;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import nycto.homeservices.dto.orderDto.OrderResponseDto;
import nycto.homeservices.dto.userDto.*;
import nycto.homeservices.entity.*;
import nycto.homeservices.entity.enums.UserStatus;
import nycto.homeservices.entity.enums.UserType;
import nycto.homeservices.exceptions.DuplicateDataException;
import nycto.homeservices.exceptions.NotFoundException;
import nycto.homeservices.exceptions.NotValidInputException;
import nycto.homeservices.repository.UserRepository;
import nycto.homeservices.service.serviceInterface.*;
import nycto.homeservices.dto.dtoMapper.UserMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final CustomerCreditService customerCreditService;
    private final OrderService orderService;
    private final SpecialistCreditService specialistCreditService;
    private final EmailService emailService;
    private final ActivationTokenService tokenService;

    private final PasswordEncoder passwordEncoder;

    private final UserMapper userMapper;

    @Override
    public UserResponseDto createUser(UserCreateDto createDto) throws MessagingException {

        if (userRepository.findByEmail(createDto.email()).isPresent())
            throw new DuplicateDataException
                    ("user with email:" + createDto.email()
                            + "is already exists");

        User user;
        switch (createDto.userType()) {
            case CUSTOMER:
                Customer customer = new Customer();
                userMapper.toEntity(createDto, customer);
                user = customer;
                break;
            case SPECIALIST:
                Specialist specialist = new Specialist();
                userMapper.toEntity(createDto, specialist);
                specialist.setRating(0.0);
                specialist.setProfilePicUrl("osapfosdf");
                user = specialist;
                break;
            case ADMIN:
                Admin admin = new Admin();
                userMapper.toEntity(createDto, admin);
                user = admin;
                break;
            default:
                throw new NotValidInputException("Invalid user type: " + createDto.userType());
        }

        user.setRegistrationDate(LocalDateTime.now());
        user.setStatus(UserStatus.NEW);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User savedUser = userRepository.save(user);

        ActivationToken activationToken = tokenService.createActivationToken(savedUser);
        emailService.sendActivationEmail(savedUser.getEmail(), activationToken.getToken());


        if (createDto.userType() == UserType.CUSTOMER) {
            customerCreditService.increaseCustomerCredit((Customer) savedUser, 10000L);
        }

        return userMapper.toResponseDto(savedUser);
    }

    @Override
    public UserResponseDto getUserById(Long id) throws NotFoundException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User with id " + id + " not found"));
        return userMapper.toResponseDto(user);
    }

    @Override
    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponseDto updateUser(Long id, UserUpdateDto updateDto)
            throws NotFoundException {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User with id " + id + " not found"));


        User updatedUser = userMapper.toEntity(updateDto, existingUser);

        updatedUser.setId(id);

        return userMapper.toResponseDto(userRepository.save(updatedUser));
    }

    @Override
    public void deleteUser(Long id) throws NotFoundException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User with id " + id + " not found"));
        userRepository.delete(user);
    }

    @Override
    public void changePassword(Long id, ChangeUserPasswordDto passwordDto) throws NotFoundException, NotValidInputException {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User with id " + id + " not found"));


        user.setPassword(passwordEncoder.encode(passwordDto.password()));

        userRepository.save(user);

    }

    @Override
    public List<UserResponseDto> getUsersByFilter(FilteringDto filterParams) {
        List<User> users = userRepository.findUsersByFilters(
                filterParams.firstName(),
                filterParams.lastName(),
                filterParams.email(),
                filterParams.userType(),
                filterParams.serviceName(),
                filterParams.rating()
        );
        return users.stream()
                .map(userMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserHistoryDto getUserHistory(Long userId, String userType) {
        List<OrderResponseDto> orders;
        Long totalCredit;


        if ("CUSTOMER".equalsIgnoreCase(userType)) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new NotFoundException("Customer with id " + userId + " not found"));

            if (!(user instanceof Customer))
                throw new NotValidInputException("User with id " + userId + " is not a customer");

            orders = orderService.getOrdersForCustomer(userId);
            totalCredit = customerCreditService.getTotalCredit(userId);

        } else if ("SPECIALIST".equalsIgnoreCase(userType)) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new NotFoundException("Specialist with id " + userId + " not found"));
            if (!(user instanceof Specialist)) {
                throw new NotValidInputException("User with id " + userId + " is not a specialist");
            }
            orders = orderService.getOrdersForSpecialist(userId);
            totalCredit = specialistCreditService.getTotalCredit(userId);
        } else {
            throw new NotValidInputException("Invalid user type: " + userType);
        }

        return new UserHistoryDto(orders, totalCredit);
    }

    @Override
    public List<UserReportDto> getCustomerReport(LocalDateTime startDate, LocalDateTime endDate) {
        return userRepository.findCustomersByRegistrationDate(startDate, endDate).stream()
                .map(user -> new UserReportDto(
                        user.getId(),
                        user.getFirstName(),
                        user.getLastName(),
                        user.getRegistrationDate(),
                        (long) orderService.getOrdersForCustomer(user.getId()).size()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<UserReportDto> getSpecialistReport(LocalDateTime startDate, LocalDateTime endDate) {
        return userRepository.findSpecialistsByRegistrationDate(startDate, endDate).stream()
                .map(user -> new UserReportDto(
                        user.getId(),
                        user.getFirstName(),
                        user.getLastName(),
                        user.getRegistrationDate(),
                        (long) orderService.getOrdersForSpecialist(user.getId()).size()
                ))
                .collect(Collectors.toList());
    }



}
