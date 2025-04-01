package nycto.homeservices.service;

import lombok.RequiredArgsConstructor;
import nycto.homeservices.dto.userDto.*;
import nycto.homeservices.entity.Admin;
import nycto.homeservices.entity.Customer;
import nycto.homeservices.entity.Specialist;
import nycto.homeservices.entity.User;
import nycto.homeservices.entity.enums.UserStatus;
import nycto.homeservices.entity.enums.UserType;
import nycto.homeservices.exceptions.DuplicateDataException;
import nycto.homeservices.exceptions.NotFoundException;
import nycto.homeservices.exceptions.NotValidInputException;
import nycto.homeservices.repository.UserRepository;
import nycto.homeservices.service.serviceInterface.CustomerCreditService;
import nycto.homeservices.service.serviceInterface.UserService;
import nycto.homeservices.util.ValidationUtil;
import nycto.homeservices.util.dtoMapper.UserMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final CustomerCreditService customerCreditService;
    private final ValidationUtil validationUtil;
    private final UserMapper userMapper;

    @Override
    public UserResponseDto createUser(UserCreateDto createDto)
            throws NotValidInputException, DuplicateDataException, NotFoundException {

        if (!validationUtil.validate(createDto))
            throw new NotValidInputException("Not valid user data");

        if (userRepository.findByEmail(createDto.email()).isPresent())
            throw new DuplicateDataException
                    ("user with email:" + createDto.email()
                            + "is already exists");

        User user;
        switch (createDto.userType()) {
            case CUSTOMER:
                Customer customer = new Customer();
                userMapper.toEntity(createDto);
                user = customer;
                break;
            case SPECIALIST:
                Specialist specialist = new Specialist();
                userMapper.toEntity(createDto);
                specialist.setRating(0.0);
                specialist.setProfilePicUrl("osapfosdf");
                user = specialist;
                break;
            case ADMIN:
                Admin admin = new Admin();
                userMapper.toEntity(createDto);
                user = admin;
                break;
            default:
                throw new NotValidInputException("Invalid user type: " + createDto.userType());
        }

        user.setRegistrationDate(LocalDateTime.now());
        user.setStatus(UserStatus.NEW);

        User savedUser = userRepository.save(user);


        if (createDto.userType() == UserType.CUSTOMER) {
            customerCreditService.increaseCustomerCredit((Customer) savedUser, 0L);
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
            throws NotFoundException, NotValidInputException {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User with id " + id + " not found"));

        if (!validationUtil.validate(updateDto))
            throw new NotValidInputException("Not valid update data");

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

        if (!validationUtil.validate(passwordDto))
            throw new NotValidInputException("Not valid specialist data");

        user.setPassword(passwordDto.password());

        userRepository.save(user);

    }

    @Override
    public List<UserResponseDto> getUsersByFilter(FilteringDto filterParams) {
        List<User> users = userRepository.findUsersByFilters(
                filterParams.firstName(),
                filterParams.lastName(),
                filterParams.email(),
                filterParams.userType(),
                filterParams.serviceName()
        );
        return users.stream()
                .map(userMapper::toResponseDto)
                .collect(Collectors.toList());
    }


}
